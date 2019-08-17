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

package org.sysfoundry.kaoline.examples.ss.test1;

import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kiln.base.sys.AboutSubsys;
import org.sysfoundry.kiln.base.sys.Subsys;

import static org.sysfoundry.kaoline.fn.AbstractFn.registerFns;

@Slf4j
@AboutSubsys(
        doc = "Test1 Subsystem"

)
public class TestSubsys1 extends Subsys {

    @Override
    protected void configure() {
        super.configure();

        registerFns(binder(),
                Test1Fn.class,
                Test2Fn.class,
                Test3Fn.class);
    }

}
