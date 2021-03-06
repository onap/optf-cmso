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

package org.onap.optf.cmso.service.rs.models;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import org.onap.optf.cmso.model.ChangeManagementDetail;
import org.onap.optf.cmso.model.Schedule;

/**
 * The persistent class for the change_management_groups database table.
 * 
 */
@ApiModel
public class CmDetailsMessage extends ChangeManagementDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Schedule scheduleRequest;
    private List<ApprovalMessage> approvals;

    public Schedule getScheduleRequest() {
        return scheduleRequest;
    }

    public void setScheduleRequest(Schedule scheduleRequest) {
        this.scheduleRequest = scheduleRequest;
    }

    public List<ApprovalMessage> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalMessage> approvals) {
        this.approvals = approvals;
    }

}
