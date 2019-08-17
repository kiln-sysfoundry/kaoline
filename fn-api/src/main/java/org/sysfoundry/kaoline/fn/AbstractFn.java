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

package org.sysfoundry.kaoline.fn;

import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.Message;

import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractFn implements Fn{

    protected URI uri;
    protected String doc;
    protected List<ParamDeclaration> inParamDeclarations;
    protected List<ParamDeclaration> outParamDeclarations;

    public AbstractFn(){
        init();
    }

    /**
     * Initializes the Fn details by extracting the Annotation from the subclass
     */
    private void init() {
        Class<? extends AbstractFn> fnActualClass = this.getClass();

        AboutFn fnAboutAnnotation = fnActualClass.getAnnotation(AboutFn.class);

        if(fnAboutAnnotation == null){
            String message = String.format("Fn %s has not been annotated with %s. " +
                    "It is advised to annotate for self documentation of Fns.",fnActualClass, AboutFn.class);
            Message msgObj = new Message(message);
            List<Message> messages = new ArrayList<>();
            messages.add(msgObj);
            throw new ConfigurationException(messages);
        }

        String uriString = fnAboutAnnotation.uri();
        String fnDoc = fnAboutAnnotation.doc();
        Parameter[] inParameters = fnAboutAnnotation.in();
        Parameter[] outParameters = fnAboutAnnotation.out();

        List<ParamDeclaration> inParameterDeclList = asParamDeclarations(inParameters);
        List<ParamDeclaration> outParameterDeclList = asParamDeclarations(outParameters);

        try {
            this.uri = new URI(uriString);
            this.doc = fnDoc;
            this.inParamDeclarations = inParameterDeclList;
            this.outParamDeclarations = outParameterDeclList;

        } catch (URISyntaxException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<ParamDeclaration> asParamDeclarations(Parameter[] params) {
        List<Parameter> parameterList = Arrays.asList(params);

        List<ParamDeclaration> paramDeclList = parameterList.stream().map(p -> {
            DefaultParamDeclaration paramDeclaration = new DefaultParamDeclaration(p.key(), p.type(), p.mandatory(),
                    p.defaultValue(), p.category());
            return paramDeclaration;
        }).collect(Collectors.toList());

        return Collections.unmodifiableList(paramDeclList);
    }

    @Override
    public URI getURI() {
        return this.uri;
    }

    @Override
    public List<ParamDeclaration> getOutDeclarations() {
        return this.outParamDeclarations;
    }

    @Override
    public List<ParamDeclaration> getInDeclarations() {
        return this.inParamDeclarations;
    }


    /**
     *
     * Static helper functions for making it easy to work with Guice Injector framework
     *
     */

    /**
     * Helps registering the list of Fn classes in the appropriate set binder of guice using the appropriate
     * kaoline annotation
     * @param fns The list of Fn classes which needs to be registered
     */
    public static void registerFns(Binder binder,Class<? extends Fn>... fns){

        Multibinder<Fn> fnMultibinder = Multibinder.newSetBinder(binder, Fn.class, FnSet.class);

        for (Class<? extends Fn> fn : fns) {
            fnMultibinder.addBinding().to(fn).in(Singleton.class);
        }

    }
}
