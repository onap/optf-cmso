/*
 * Copyright � 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright � 2018 IBM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.onap.optf.cmso.common.exceptions;

import com.att.eelf.i18n.EELFResourceManager;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import org.onap.optf.cmso.common.CmsoRequestError;
import org.onap.optf.cmso.common.LogMessages;

public class CmsoException extends Exception {
    private static final long serialVersionUID = 1L;

    protected CmsoRequestError requestError = null;
    private List<String> variables = new ArrayList<String>();
    protected LogMessages messageCode;
    protected Status status;

    /**
     * Instantiates a new CMS exception.
     *
     * @param status the status
     * @param messageCode the message code
     * @param args the args
     */
    public CmsoException(Status status, LogMessages messageCode, String... args) {
        super(EELFResourceManager.format(messageCode, args));
        this.status = status;
        this.messageCode = messageCode;
        for (String arg : args) {
            variables.add(arg);
        }
        requestError = new CmsoRequestError(messageCode.name(), getMessage(), variables);
    }

    public Status getStatus() {
        return status;
    }

    public LogMessages getMessageCode() {
        return messageCode;
    }

    public String[] getVariables() {
        return variables.toArray(new String[variables.size()]);
    }

    public CmsoRequestError getRequestError() {
        return requestError;
    }
}
