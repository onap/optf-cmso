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

package org.onap.optf.cmso.optimizer.bean;

public class CMSchedulingInfo {
    private String scheduleId;
    private String startTime;
    private String endTime;
    private int normalDurationInSecs;
    private int additionalDurationInSecs;
    private int concurrencyLimit;
    private String[] policyId;
    private CMVnfDetails[] vnfDetails;

    public CMSchedulingInfo() {}

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNormalDurationInSecs() {
        return normalDurationInSecs;
    }

    public void setNormalDurationInSecs(int normalDurationInSecs) {
        this.normalDurationInSecs = normalDurationInSecs;
    }

    public int getAdditionalDurationInSecs() {
        return additionalDurationInSecs;
    }

    public void setAdditionalDurationInSecs(int additionalDurationInSecs) {
        this.additionalDurationInSecs = additionalDurationInSecs;
    }

    public int getConcurrencyLimit() {
        return concurrencyLimit;
    }

    public void setConcurrencyLimit(int concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }

    public String[] getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String[] policyId) {
        this.policyId = policyId;
    }

    public CMVnfDetails[] getVnfDetails() {
        return vnfDetails;
    }

    public void setVnfDetails(CMVnfDetails[] vnfDetails) {
        this.vnfDetails = vnfDetails;
    }

}
