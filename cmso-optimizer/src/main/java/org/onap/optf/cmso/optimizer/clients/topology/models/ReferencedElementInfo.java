/*
 *
 * Copyright © 2019 AT&T Intellectual Property.
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
 ******************************************************************************/

package org.onap.optf.cmso.optimizer.clients.topology.models;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.onap.optf.cmso.optimizer.service.rs.models.NameValue;

@ApiModel(value = "Topology Related Element", description = "Element Information returned from TopologyRequuest.")
public class ReferencedElementInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static EELFLogger log = EELFManager.getInstance().getLogger(ReferencedElementInfo.class);

    @ApiModelProperty(value = "Element identifier")
    private String elementId;

    @ApiModelProperty(value = "Location information for the element.")
    private ElementLocation elementLocation;

    @ApiModelProperty(value = "Related elements only. Element ids of the element(s) ")
    private List<String> referencingElements;

    @ApiModelProperty(value = "Implementation specific element data.")
    public List<NameValue> elementData = new ArrayList<>();

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public ElementLocation getElementLocation() {
        return elementLocation;
    }

    public void setElementLocation(ElementLocation elementLocation) {
        this.elementLocation = elementLocation;
    }


    public List<String> getRelatedElements() {
        return referencingElements;
    }

    public void setRelatedElements(List<String> relatedElements) {
        this.referencingElements = relatedElements;
    }

    public List<NameValue> getElementData() {
        return elementData;
    }

    public void setElementData(List<NameValue> elementData) {
        this.elementData = elementData;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.debug("Error in toString()", e);
        }
        return "";
    }
}
