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

import org.sysfoundry.kiln.base.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AboutFn {

    /**
     * The URI of this Fn.
     * @return The URI of the Fn.
     */
    String uri();

    /**
     * A brief documentation about this Fn. Optionally this can also be a URL pointing to an external documentation.
     * @return The Brief documentation about this Fn.
     */
    String doc() default Constants.TO_BE_DONE;


    /**
     * The list of in parameters which is expected by this Fn
     * @return The Parameter[] which is the list of parameters expected by this Fn
     */
    Parameter[] in() default {};


    /**
     * The list of out parameters which is the output provided this Fn.
     * @return The Parameter[] which is the list of parameters output by this Fn.
     */
    Parameter[] out() default {};

}
