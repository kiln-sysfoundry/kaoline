/*
 * Copyright 2019 Sysfoundry (www.sysfoundry.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sysfoundry.kaoline.fn.ss;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kaoline.fn.*;
import org.sysfoundry.kaoline.tc.TypeConversionException;
import org.sysfoundry.kaoline.tc.TypeConversionService;
import org.sysfoundry.kiln.base.LifecycleException;

import javax.inject.Inject;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.sysfoundry.kaoline.fn.Constants.FUNCTION_TO_INVOKE;
import static org.sysfoundry.kaoline.fn.Constants.OUT_PARAMS;
import static org.sysfoundry.kiln.base.util.CollectionUtils.KV;
import static org.sysfoundry.kiln.base.util.CollectionUtils.MAP;

@Slf4j
@AboutFn(
        uri = Constants.FN_BUS_URI,
        doc = "The fnbus of the kaoline system",
        in = {
                @Parameter(key= FUNCTION_TO_INVOKE,type = String.class)
        },
        out = {
                @Parameter(key=Constants.OUT_PARAMS,type = Map.class)
        }

)
class FnbusImpl extends AbstractFn implements Fnbus {

    private Set<Fn> fns;
    private Map<URI,Fn> fnMap;
    private Object lockObj = new Object();
    private FnSubsysConfig config;
    private TypeConversionService typeConversionService;

    @Inject
    FnbusImpl(@NonNull @FnSet Set<Fn> fns,
              @NonNull TypeConversionService typeConversionService,
              @NonNull FnSubsysConfig fnSubsysConfig){
        this.fns = fns;
        this.config = fnSubsysConfig;
        this.typeConversionService = typeConversionService;
    }

    @Override
    public void forEach(@NonNull Consumer<Fn> fnConsumer) {
        if(!isInitialized()){
            initialize();
        }
        Collection<Fn> fnCollection = fnMap.values();
        fnCollection.forEach(fnConsumer);
    }

    @Override
    public boolean hasFnForURI(@NonNull URI uri) {
        if(!isInitialized()){
            initialize();
        }

        return fnMap.containsKey(uri);
    }

    @Override
    public void start() throws LifecycleException {

        log.info("{} starting... with Configuration {}",Constants.FN_BUS_URI,config);
        if(!isInitialized()){
            initialize();
        }

    }


    private boolean isInitialized(){
        return fnMap != null;
    }

    private void initialize(){
        synchronized(lockObj) {
            if(!isInitialized()) {
                Map<URI, Fn> tempFnMap = new HashMap<>();
                //initialize the internal function map here
                for (Fn fn : fns) {
                    if(tempFnMap.containsKey(fn.getURI())){
                        Fn originalFn = tempFnMap.get(fn.getURI());
                        if(config.isAllowFnOverriding()){
                          log.warn("URI {} is being overridden to point to Fn {} from existing Fn {}",fn.getURI(),fn,originalFn);
                          tempFnMap.put(fn.getURI(), fn);
                        }else{
                          log.warn("URI {} is already pointing to Fn {}. It cannot be overridden to point to the new Fn {}"
                                  ,fn.getURI(),originalFn,fn);
                        }
                    }else{
                        tempFnMap.put(fn.getURI(),fn);
                    }
                }

                //finally initialize the class variable
                this.fnMap = tempFnMap;

                log.trace("Fn Map {}",this.fnMap);
            }
        }
    }

    @Override
    public void stop() {
        log.info("{} stopping...",Constants.FN_BUS_URI);
        if(isInitialized()){
            fnMap.clear();
            fnMap = null;
        }
    }

    @Override
    public Param apply(@NonNull Param param) {
        if(!isInitialized()){
            initialize();
        }

        if(param.containsKey(FUNCTION_TO_INVOKE)){
            URI fnURI = (URI)param.get(FUNCTION_TO_INVOKE);

            if(fnMap.containsKey(fnURI)){
                Fn fnToExecute = fnMap.get(fnURI);
                Param outParam = null;
                try {
                    outParam = executeFn(fnToExecute, param);
                    return outParam;

                } catch (FnInvalidParametersException e) {
                    String functionURIStr = e.getFnURI();
                    Map<String, Object> exceptionData = e.getData();
                    if(exceptionData.containsKey(Constants.VALIDATION_RESULTS)){
                        String paramDirection = (String)exceptionData.get(Constants.PARAM_DIRECTION);
                        List<ParamValidationResult> paramValidationResults = (List<ParamValidationResult>)exceptionData.get(Constants.VALIDATION_RESULTS);
                        if(!paramValidationResults.isEmpty()){
                            log.info("The exection call to fn {} is invalid since the below expected mandatory {} are missing!",functionURIStr,paramDirection);
                            for (ParamValidationResult paramValidationResult : paramValidationResults) {
                                ParamDeclaration declaration = paramValidationResult.getDeclaration();
                                log.info("Key : {} from category {}",declaration.getKey(),declaration.getCategory());
                            }

                        }
                    }
                    throw new RuntimeException(e);
                }

            }else{
                throw new RuntimeException(String.format("%s is not pointing to a valid Fn in the system",fnURI));
            }
        }else{
            throw new RuntimeException(String.format("The Parameter %s is missing from the input map",FUNCTION_TO_INVOKE));
        }
    }


    private Param executeFn(Fn fnToExecute,Param inOutParam) throws FnInvalidParametersException {
        List<ParamDeclaration> inDeclarations = fnToExecute.getInDeclarations();
        List<ParamDeclaration> outDeclarations = fnToExecute.getOutDeclarations();


        //steps of execution
        //1. Verify whether the incoming params have all the expected parameters by the fn
        List<ParamValidationResult> validationResults = validateParamsForFn(inDeclarations,inOutParam);

        List<ParamValidationResult> mandatoryParamValidations = validationResults.stream().filter(result->{
            ParamDeclaration declaration = result.getDeclaration();
            return declaration.isMandatory() &&     //is mandatory
                    !declaration.hasDefaultValue() && // and does not have a valid default value
                    !result.isAvailable(); //and not available
        }).collect(Collectors.toList());

        if(!mandatoryParamValidations.isEmpty()){
            String message = String.format("Some mandatory params required by the fn %s is missing",fnToExecute.getURI());
            Map<String, Object> data = MAP(KV(Constants.VALIDATION_RESULTS, mandatoryParamValidations),KV(Constants.PARAM_DIRECTION,Constants.IN_PARAMS));

            throw new FnInvalidParametersException(fnToExecute.getURI().toString(),data,message);
        }

        List<ParamValidationResult> nonMandatoryParamValidations = validationResults.stream().filter(result->{
            ParamDeclaration declaration = result.getDeclaration();
            return !declaration.isMandatory() && !declaration.hasDefaultValue() && !(result.isAvailable() || result.isValidType());
        }).collect(Collectors.toList());

        if(!nonMandatoryParamValidations.isEmpty()){
            log.info("The following parameters are optional and they are not found in the Param input to the function.");
            nonMandatoryParamValidations.forEach(paramValidationResult -> {
                log.info(paramValidationResult.toString());
            });
        }

        //pick the type invalid ones and try to convert them
        List<ParamValidationResult> invalidTypeValidationResults = validationResults.stream().filter(
                result->{
                    return !result.isValidType();
                }
        ).collect(Collectors.toList());

        if(!invalidTypeValidationResults.isEmpty()){
            try {
                //now try to convert these datatypes
                inOutParam = convertInParamTypes(invalidTypeValidationResults, inOutParam);
            }catch(TypeConversionException e){
                throw new FnInvalidParametersException(e);
            }
        }

        //2. If incoming params are existing just execute the fn
        Param outParam = fnToExecute.apply(inOutParam);

        //3. Verify whether the outcoming params have all the expected out params declared by the fn
        List<ParamValidationResult> outputValidationResults = validateParamsForFn(outDeclarations,outParam);

        //check if there are any mandatory outparams which is a must
        List<ParamValidationResult> mandatoryOutputValidationResults = outputValidationResults.stream().filter(
                paramValidationResult -> {
                    ParamDeclaration declaration = paramValidationResult.getDeclaration();
                    return declaration.isMandatory() &&
                            !declaration.hasDefaultValue() &&
                            !(paramValidationResult.isAvailable() || paramValidationResult.isValidType());
                }
        ).collect(Collectors.toList());

        if(!mandatoryOutputValidationResults.isEmpty()){
            String message = String.format("Some mandatory out params declared to be returned by the fn {} is missing",fnToExecute.getURI());
            Map<String, Object> data = MAP(KV(Constants.VALIDATION_RESULTS, mandatoryOutputValidationResults),KV(Constants.PARAM_DIRECTION,OUT_PARAMS));
            throw new FnInvalidParametersException(fnToExecute.getURI().toString(),data,message);
        }

        return outParam;
    }

    private List<ParamValidationResult> validateParamsForFn(List<ParamDeclaration> declarations, Param param) {
        List<ParamValidationResult> validationResults = new ArrayList<>();
        for (ParamDeclaration declaration : declarations) {
            boolean available = param.containsKey(declaration.getKey());
            if(available){
                Object availableValue = param.get(declaration.getKey());
                Class paramType = declaration.getType();
                boolean validType = paramType.isAssignableFrom(availableValue.getClass());
                if(!validType){
                    ParamValidationResult result = new ParamValidationResult(declaration,available,false);
                    validationResults.add(result);
                }
            }else{
                //add to the validation result
                ParamValidationResult result = new ParamValidationResult(declaration,false,false);
                validationResults.add(result);
            }
        }

        return validationResults;
    }

    private Param convertInParamTypes(List<ParamValidationResult> invalidTypeValidationResults, Param inOutParam) throws TypeConversionException {

        Param outParam = inOutParam;

        for (ParamValidationResult invalidTypeValidationResult : invalidTypeValidationResults) {
            ParamDeclaration declaration = invalidTypeValidationResult.getDeclaration();

            Object value = outParam.get(declaration.getKey());

            Object convertedValue = typeConversionService.convert(value, declaration.getType());

            log.trace("Converted {} of type {} to {} of type {}",value,value.getClass(),convertedValue,declaration.getType());

            outParam = outParam.plus(declaration.getKey(), convertedValue);
        }


        return outParam;
    }


}
