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

import java.util.Map;

public class FnInvalidParametersException extends FnExecutionException{

    public FnInvalidParametersException(String fnURI, Map<String, Object> exceptionData, String message) {
        super(fnURI, exceptionData, message);
    }

    public FnInvalidParametersException(String fnURI, Map<String, Object> execeptionData, String message, Throwable cause) {
        super(fnURI, execeptionData, message, cause);
    }

    public FnInvalidParametersException(String message) {
        super(message);
    }

    public FnInvalidParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public FnInvalidParametersException(Throwable cause) {
        super(cause);
    }

    public FnInvalidParametersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
