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

import com.google.inject.multibindings.MapBinder;
import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kaoline.tc.TypeConversionService;
import org.sysfoundry.kaoline.tc.TypeConverter;
import org.sysfoundry.kaoline.tc.TypeConverterMap;
import org.sysfoundry.kiln.base.Constants;
import org.sysfoundry.kiln.base.sys.AboutSubsys;
import org.sysfoundry.kiln.base.sys.Key;
import org.sysfoundry.kiln.base.sys.Subsys;

import javax.inject.Singleton;
import java.util.Map;

@Slf4j
@AboutSubsys(
        doc = "Datatype conversion subsystem which offers a type conversion service to support " +
                "converting from and to different type of data to different types",
        provider = Constants.KILN_PROVIDER_URL,
        authors = Constants.Authors.KILN_TEAM,
        provisions = {
                @Key(type= TypeConversionService.class,scope= Singleton.class),
                @Key(type= Map.class,keyType = Class.class,valueType = TypeConverter.class,annotation= TypeConverterMap.class)
        }
)
public class TypeConversionSubsys extends Subsys {

    @Override
    protected void configure() {
        super.configure();

        //Bind the TypeConversionService
        bind(TypeConversionService.class).to(TypeConversionServiceImpl.class).in(Singleton.class);

        //Bind the map binder for TypeConverterMap
        MapBinder<Class, TypeConverter> typeConverterMapBinder = MapBinder.newMapBinder(binder(), Class.class, TypeConverter.class, TypeConverterMap.class);

        //bind the default datatype converters to the converter
        typeConverterMapBinder.addBinding(String.class).to(StringTypeConverter.class);
        typeConverterMapBinder.addBinding(Integer.class).to(IntegerTypeConverter.class);
        typeConverterMapBinder.addBinding(Float.class).to(FloatTypeConverter.class);
        typeConverterMapBinder.addBinding(Double.class).to(DoubleTypeConverter.class);
    }
}
