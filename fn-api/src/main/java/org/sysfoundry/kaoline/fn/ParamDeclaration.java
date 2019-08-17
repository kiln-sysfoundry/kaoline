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

import java.lang.annotation.Annotation;

/**
 * The ParamDeclaration represents the Declaration of a Param. Whether input or ouput.
 * The ParamDeclaration abstraction is used by the Fn instance to declare its input and output parameters
 * along with details about its datatype and whether it is a mandatory or optional parameter.
 */
public interface ParamDeclaration {

    /**
     * The Parameter key as a string
     * @return The parameter key
     */
    public String getKey();

    /**
     * The Parameter data type as a {@link Class}
     * @return The data type of the Parameter
     */
    public Class getType();


    /**
     * The Parameter category annotation
     * @return
     */
    public Class<? extends Annotation> getCategory();

    /**
     * Declares whether the Parameter is mandatory or optional
     * @return Specifies whether the Parameter is mandatory or optional
     */
    public boolean isMandatory();


    /**
     * Retrieves the default value if present
     * @return The default value specified
     */
    public Object getDefaultValue();


    /**
     * Declares whether the Parameter has a default value
     * @return Boolean value indicating whether the value has a default or not
     */
    public boolean hasDefaultValue();
}
