/*
 * Copyright © 2017-2018 AT&T Intellectual Property. Modifications Copyright © 2018 IBM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * Unless otherwise specified, all documentation contained herein is licensed under the Creative
 * Commons License, Attribution 4.0 Intl. (the "License"); you may not use this documentation except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.optf.cmso.optimizer.common;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

/**
 * The Class BasicAuthenticatorFilter.
 */
public class BasicAuthenticatorFilter implements ClientRequestFilter {
    private static EELFLogger log = EELFManager.getInstance().getLogger(BasicAuthenticatorFilter.class);
    private final String user;
    private final String password;

    /**
     * Instantiates a new basic authenticator filter.
     *
     * @param user the user
     * @param password the password
     */
    public BasicAuthenticatorFilter(String user, String password) {
        this.user     = user;
        this.password = password;
        log.info("user: " + user + " pass:" + "****");
    }

    /**
     * Filter.
     *
     * @param requestContext the request context
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication();
        headers.add("Authorization", basicAuthentication);
    }

    private String getBasicAuthentication() {
        String token = this.user + ":" + this.password;
        try {
            return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Cannot encode with UTF-8", ex);
        }
    }

    /**
     * Gets the user.
     *
     * @param request the request
     * @return the user
     */
    public static String getUser(HttpServletRequest request) {
        String user = "";
        String header = request.getHeader("Authorization");
        if (header != null) {
            String[] auth = header.split("Basic ");
            if (auth.length == 2) {
                String token = getToken(auth[1]);
                if (token.contains(":")) {
                    String[] tokens = token.split(":");
                    user = tokens[0];
                }
            }
        }
        return user;
    }

    private static String getToken(String auth) {
        try {
            return new String(DatatypeConverter.parseBase64Binary(auth));
        } catch (Exception e) {
            return auth;
        }
    }
}
