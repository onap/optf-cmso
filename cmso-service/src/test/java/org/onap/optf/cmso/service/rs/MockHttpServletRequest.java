/*
 * ============LICENSE_START=======================================================================================
 * Copyright (c) 2019 AT&T Intellectual Property.
 * ===================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================================================
 * 
 */

package org.onap.optf.cmso.service.rs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

public class MockHttpServletRequest {
    public StringBuffer url = new StringBuffer("http://localhost:8089/cmso/v1/ChangeManagement/schedules/");
    public HttpServletRequest request = mock(HttpServletRequest.class);

    MockHttpServletRequest() {

        when(request.getRequestURL()).thenReturn(url);
        when(request.getHeader("Authorization")).thenReturn("BasicbTEzODc3OnNjaGVkdWxlci1SMTgwMiE=");

    }

}
