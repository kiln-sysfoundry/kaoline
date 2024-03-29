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

import org.sysfoundry.kaoline.fn.AboutFn;
import org.sysfoundry.kaoline.fn.AbstractFn;
import org.sysfoundry.kaoline.fn.Param;

@AboutFn(
        uri = "fn://testsys/test1fn",
        doc = "Test2 fn doc"
)
public class Test3Fn extends AbstractFn {
    @Override
    public Param apply(Param param) {
        return null;
    }
}
