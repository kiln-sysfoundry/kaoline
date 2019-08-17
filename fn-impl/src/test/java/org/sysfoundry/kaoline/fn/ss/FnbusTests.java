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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sysfoundry.kaoline.fn.*;
import org.sysfoundry.kaoline.tc.TypeConversionService;
import org.sysfoundry.kaoline.tc.ss.TypeConversionTestHelper;
import org.sysfoundry.kiln.base.LifecycleException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.sysfoundry.kiln.base.util.CollectionUtils.KV;
import static org.sysfoundry.kiln.base.util.CollectionUtils.MAP;

public class FnbusTests {

    @DisplayName("invalid.fn.uri.test")
    @Test
    public void testFnbusSimpleFn() throws LifecycleException,URISyntaxException {
        //FnSubsys subsys = new FnSubsys();
        Set<Fn> fnSet = getFnSet();

        FnSubsysConfig config = new FnSubsysConfig();

        TypeConversionService typeConversionService = TypeConversionTestHelper.provideDefaultTypeConversionService();
        Fnbus fnbus = new FnbusImpl(fnSet,typeConversionService,config);

        fnbus.start();

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            invokeFn("fn://sys/tests/testfn2",fnbus,MAP(KV("name","john"),KV("age",35),KV("sex","Male")));
        });

        assertEquals("fn://sys/tests/testfn2 is not pointing to a valid Fn in the system", exception.getMessage());


        fnbus.stop();

    }

    @DisplayName("valid.fn.uri.test")
    @Test
    public void testFnbusSimpleFn2() throws LifecycleException,URISyntaxException {
        //FnSubsys subsys = new FnSubsys();
        Set<Fn> fnSet = getFnSet();

        FnSubsysConfig config = new FnSubsysConfig();

        TypeConversionService typeConversionService = TypeConversionTestHelper.provideDefaultTypeConversionService();
        Fnbus fnbus = new FnbusImpl(fnSet,typeConversionService,config);

        fnbus.start();


        Param outParam = invokeFn("fn://sys/tests/testfn1",fnbus,MAP(KV("name","jane"),KV("age","30"),KV("sex","Female")));

        assertTrue(outParam.containsKey("greeting"));

        System.out.println("Greeting -- "+outParam.get("greeting"));
        fnbus.stop();

    }

    private Set<Fn> getFnSet() {
        HashSet<Fn> fnSet = new HashSet<>();
        fnSet.add(new TestFn1());
        return fnSet;
    }

    public static Param invokeFn(String fnURIString, Fnbus fnbus, Map<String,Object> paramInputs) throws URISyntaxException {
        Param inOutParam = new InOutParam();
        Set<String> keySet = paramInputs.keySet();
        for (String key : keySet) {
            inOutParam = inOutParam.plus(key,paramInputs.get(key));
        }

        inOutParam = inOutParam.plus(Constants.FUNCTION_TO_INVOKE,new URI(fnURIString));
        return fnbus.apply(inOutParam);
    }
}

@AboutFn(
        uri = "fn://sys/tests/testfn1",
        in = {
                @Parameter(key="name",type=String.class),
                @Parameter(key="age",type=Integer.class),
                @Parameter(key="sex",type=String.class)
        },
        out = {
                @Parameter(key="greeting",type=String.class)
        }
)
class TestFn1 extends AbstractFn{

    @Override
    public Param apply(Param param) {

        String name = (String)param.get("name");
        Integer age = (Integer)param.get("age");
        String sex = (String)param.get("sex");

        String greeting = String.format("Hello %s.%s welcome to the jungle!. You are %d yrs old!",(sex.equals("Male") ? "Mr" : "Ms"),name,age);

        param = param.plus("greeting",greeting);

        return param;
    }
}
