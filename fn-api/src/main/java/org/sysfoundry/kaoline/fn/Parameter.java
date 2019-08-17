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

import org.sysfoundry.kiln.base.sys.None;

import java.lang.annotation.Annotation;
import org.sysfoundry.kiln.base.Constants;

public @interface Parameter {

    /**
     * Represents the Key of the parameter
     * @return The Key of the parameter
     */
    String key();

    /**
     * The Type of the parameter expected.
     * @return The Type of the parameter expected.
     */
    Class type() default Object.class;

    /**
     * The Category of the parameter.
     * @return The category of the parameter indicated by the annotation. Default value is None
     */
    Class<? extends Annotation> category() default None.class;

    /**
     * Identifies whether the Parameter is default or optional
     * @return 'True' if mandatory or 'False' if optional. Default value is 'True'
     */
    boolean mandatory() default true;

    /**
     * The default value of the parameter
     * @return The default value in String form.
     */
    String defaultValue() default Constants.EMPTY;

}
