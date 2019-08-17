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


import lombok.extern.slf4j.Slf4j;
import org.sysfoundry.kaoline.tc.TypeConverter;

@Slf4j
class IntegerTypeConverter implements TypeConverter {


    @Override
    public Object convert(Object in) {

        Integer value = null;
        if(in != null) {
            if(in instanceof Integer){
                value = (Integer)in;
            }else if (in instanceof String){
                String integerString = (String)in;
                try {
                    value = new Integer(integerString);
                }catch(Exception e){
                    log.warn("Unable to convert {} to type {}",integerString, Integer.class);
                }
            }else{
                log.warn("Unsupported source type {}. Cannot convert to type {}",in.getClass(), Integer.class);
            }
        }
        return value;
    }
}
