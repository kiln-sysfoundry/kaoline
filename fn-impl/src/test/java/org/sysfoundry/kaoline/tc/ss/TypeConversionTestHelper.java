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

package org.sysfoundry.kaoline.tc.ss;

import org.sysfoundry.kaoline.tc.TypeConversionService;
import org.sysfoundry.kaoline.tc.TypeConverter;

import java.util.HashMap;
import java.util.Map;

public class TypeConversionTestHelper {

    public static TypeConversionService provideDefaultTypeConversionService(){
        Map<Class, TypeConverter> typeConverterMap = getDefaultTypeConverterMap();
        return new TypeConversionServiceImpl(typeConverterMap);
    }

    private static Map<Class, TypeConverter> getDefaultTypeConverterMap() {
        Map<Class,TypeConverter> typeConverterMap = new HashMap<>();
        typeConverterMap.put(String.class,new StringTypeConverter());
        typeConverterMap.put(Float.class,new FloatTypeConverter());
        typeConverterMap.put(Double.class,new DoubleTypeConverter());
        typeConverterMap.put(Integer.class,new IntegerTypeConverter());
        return typeConverterMap;
    }
}
