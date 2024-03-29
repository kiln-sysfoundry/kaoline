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

import lombok.Value;
import org.sysfoundry.kiln.base.Constants;

import java.lang.annotation.Annotation;

/**
 * The Default implementation of {@link ParamDeclaration}
 */
@Value
public class DefaultParamDeclaration implements ParamDeclaration{

    private String key;
    private Class type;
    private boolean mandatory;
    private Object defaultValue;
    private Class<? extends Annotation> category;


    @Override
    public Class<? extends Annotation> getCategory() {
        return category;
    }

    @Override
    public boolean hasDefaultValue(){
        return (defaultValue != null && !defaultValue.equals(Constants.EMPTY));
    }
}
