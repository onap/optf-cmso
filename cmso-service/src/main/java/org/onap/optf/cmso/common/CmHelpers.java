/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2018 IBM.
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

package org.onap.optf.cmso.common;

import java.util.List;
import org.onap.optf.cmso.model.DomainData;
import org.onap.optf.cmso.model.ElementData;
import org.onap.optf.cmso.model.Schedule;
import org.onap.optf.cmso.service.rs.models.CmDomainDataEnum;

/**
 * Collection of commann static helper methods for CHangeManagement.
 *
 * @author jf9860
 *
 */
public class CmHelpers {

    public static String getDomainData(Schedule schedule, CmDomainDataEnum key) {
        return getDomainData(schedule.getDomainData(), key);
    }

    /**
     * Gets the domain data.
     *
     * @param domainData the domain data
     * @param key the key
     * @return the domain data
     */
    public static String getDomainData(List<DomainData> domainData, CmDomainDataEnum key) {
        for (DomainData map : domainData) {
            if (map.getName().equals(key.toString())) {
                return map.getValue();
            }
        }
        return null;
    }

    public static String getEventData(Schedule schedule, CmDomainDataEnum key) {
        return getDomainData(schedule.getDomainData(), key);
    }

    /**
     * Gets the element data.
     *
     * @param eventData the event data
     * @param key the key
     * @return the element data
     */
    public static String getElementData(List<ElementData> eventData, CmDomainDataEnum key) {
        for (ElementData map : eventData) {
            if (map.getName().equals(key.toString())) {
                return map.getValue();
            }
        }
        return null;
    }

}
