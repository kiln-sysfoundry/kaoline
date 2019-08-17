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

import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kaoline.fn.*;
import org.sysfoundry.kaoline.tc.ss.TypeConversionSubsys;
import org.sysfoundry.kiln.base.evt.EventBus;
import org.sysfoundry.kiln.base.sys.AboutSubsys;
import org.sysfoundry.kiln.base.sys.Key;
import org.sysfoundry.kiln.base.sys.Subsys;

import javax.inject.Singleton;

import static org.sysfoundry.kaoline.fn.Fnbus.*;
import static org.sysfoundry.kaoline.fn.ss.FnSubsys.FN_SUBSYS_CONFIG;
import static org.sysfoundry.kiln.base.Constants.Authors.KILN_TEAM;
import static org.sysfoundry.kiln.base.Constants.KILN_PROVIDER_URL;

@Slf4j
@AboutSubsys(
        doc="The Fn subsystem of kaoline. Provides the abstractions for supporting Fns as well as the Fnbus " +
                "abstraction which allows executing the Fns",
        provider=KILN_PROVIDER_URL,
        authors = KILN_TEAM,
        configPrefix = FN_SUBSYS_CONFIG,
        configType = FnSubsysConfig.class,
        validateConfig = true,
        servers = FnServer.class,
        provisions = {
                @Key(type = Fnbus.class,scope= Singleton.class)
        },
        emits = {FN_BUS_STARTED,FN_BUS_STOPPED,FN_BUS_START_FAILED},
        requirements = {@Key(type= EventBus.class,scope=Singleton.class)}

)
public class FnSubsys extends Subsys {

    public static final String FN_SUBSYS_CONFIG = "/fn-subsys-config";


    @Override
    protected void configure() {
        super.configure();

        install(new TypeConversionSubsys());

        bind(Fnbus.class).to(FnbusImpl.class).in(Singleton.class);
        Multibinder.newSetBinder(binder(),Fn.class, FnSet.class);
    }
}
