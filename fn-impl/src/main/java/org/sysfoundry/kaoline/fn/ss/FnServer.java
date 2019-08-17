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

import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kaoline.fn.Fnbus;
import org.sysfoundry.kiln.base.LifecycleException;
import org.sysfoundry.kiln.base.srv.AboutServer;
import org.sysfoundry.kiln.base.srv.AbstractServer;

import javax.inject.Inject;

@Slf4j
@AboutServer(
        name = FnServer.FN_BUS,
    provides = FnServer.FN_BUS,
        doc = "The FnServer is responsible for the lifecycle of the Fnbus in Kaoline."
)
class FnServer extends AbstractServer{

    public static final String FN_BUS = "fnbus";

    private Fnbus fnbus;

    @Inject
    FnServer(Fnbus fnbus){
        this.fnbus = fnbus;
    }

    @Override
    public void start(String[] strings) throws LifecycleException {
        log.debug("About to start the {}",FN_BUS);
        fnbus.start();
    }


    @Override
    public void stop() throws LifecycleException {
        log.debug("About to stop the {}",FN_BUS);
        fnbus.stop();
    }

}
