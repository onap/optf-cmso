/*
 * Copyright © 2017-2019 AT&T Intellectual Property. Modifications Copyright © 2018 IBM.
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

package org.onap.optf.cmso.sostatus;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.observations.Mdc;
import org.onap.observations.Observation;
import org.onap.optf.cmso.common.BasicAuthenticatorFilter;
import org.onap.optf.cmso.common.CmsoStatusEnum;
import org.onap.optf.cmso.common.LogMessages;
import org.onap.optf.cmso.common.PropertiesManagement;
import org.onap.optf.cmso.filters.CmsoClientFilters;
import org.onap.optf.cmso.model.ChangeManagementSchedule;
import org.onap.optf.cmso.model.dao.ChangeManagementGroupDao;
import org.onap.optf.cmso.model.dao.ChangeManagementScheduleDao;
import org.onap.optf.cmso.model.dao.ScheduleDao;
import org.onap.optf.cmso.service.rs.models.HealthCheckComponent;
import org.onap.optf.cmso.so.bean.MsoOrchestrationQueryResponse;
import org.onap.optf.cmso.so.bean.MsoOrchestrationQueryResponse.MsoStatus;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MsoStatusClient {
    private static EELFLogger debug = EELFManager.getInstance().getDebugLogger();

    @Autowired
    ChangeManagementScheduleDao cmScheduleDao;

    @Autowired
    ChangeManagementGroupDao cmGroupdao;

    @Autowired
    ScheduleDao scheduleDao;

    @Autowired
    Environment env;

    @Autowired
    PropertiesManagement pm;

    /**
     * Execute.
     *
     * @param id the id
     * @throws JobExecutionException the job execution exception
     */
    public void execute(String id) throws JobExecutionException {
        debug.debug(LogMessages.MSO_STATUS_JOB, "Entered", id.toString());
        try {
            UUID uuid = UUID.fromString(id);
            ChangeManagementSchedule cmSchedule = cmScheduleDao.lockOne(uuid);
            if (cmSchedule == null) {
                Observation.report(LogMessages.MSO_POLLING_MISSING_SCHEDULE, id.toString());
                return;
            }
            poll(cmSchedule);
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
        }
        debug.debug(LogMessages.MSO_STATUS_JOB, "Exited", id.toString());
    }

    /**
     * Poll.
     *
     * @param cmSchedule the cm schedule
     */
    @Transactional
    public void poll(ChangeManagementSchedule cmSchedule) {
        Map<String, String> mdcSave = Mdc.save();
        try {
            // Re-fetch schedule inside transaction??
            String requestId = cmSchedule.getMsoRequestId();
            String url = env.getProperty("so.url");
            String user = env.getProperty("so.user");
            String pass = pm.getProperty("so.pass", "");
            if (!url.endsWith("/")) {
                url = url + "/";
            }
            url = url + requestId;
            Client client = ClientBuilder.newClient();
            client.register(new BasicAuthenticatorFilter(user, pass));
            client.register(new CmsoClientFilters());
            WebTarget target = client.target(url);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = null;
            cmSchedule.setMsoTimeMillis(System.currentTimeMillis());
            response = invocationBuilder.get();
            switch (response.getStatus()) {
                case 200: {
                    String respString = response.readEntity(String.class);
                    debug.debug("MSO response {}", respString);
                    MsoOrchestrationQueryResponse resp = getResponseStatus(respString);
                    if (resp != null) {
                        cmSchedule.setMsoStatus(resp.getRequestState());
                        cmSchedule.setMsoMessage(resp.getStatusMessage());
                        MsoStatus msoStatus = MsoStatus.UNKNOWN;
                        try {
                            msoStatus = MsoStatus.valueOf(resp.getRequestState());
                        } catch (Exception e) {
                            Observation.report(LogMessages.UNRECOGNIZED_MSO_STATUS, resp.getRequestState());
                        }
                        long finishTime = getFinishTime(resp);
                        switch (msoStatus) {
                            case COMPLETE:
                                cmSchedule.setExecutionCompletedTimeMillis(finishTime);
                                cmSchedule.setStatus(CmsoStatusEnum.Completed.toString());
                                break;
                            case FAILED:
                                cmSchedule.setExecutionCompletedTimeMillis(finishTime);
                                cmSchedule.setStatus(CmsoStatusEnum.Failed.toString());
                                break;
                            case UNKNOWN:
                            default:
                        }
                    } else {
                        // Do not keep polling...
                        cmSchedule.setStatus(CmsoStatusEnum.Error.toString());
                        cmSchedule.setMsoStatus("Bad Response");
                        cmSchedule.setMsoMessage("Unable to parse :" + respString);

                    }

                }
                    break;
                case 404: { // Not found
                    // Do not keep polling...
                    cmSchedule.setStatus(CmsoStatusEnum.Failed.toString());
                    cmSchedule.setMsoStatus("Not found");
                    cmSchedule.setMsoMessage("Call to MSO Failed :" + response.toString());
                }
                    break;
                case 400: { // Bad request
                    // Do not keep polling...
                    cmSchedule.setStatus(CmsoStatusEnum.Error.toString());
                    cmSchedule.setMsoStatus("Bad Request");
                    cmSchedule.setMsoMessage("Call to MSO Failed :" + response.toString());
                }
                    break;
                case 500:
                default: {
                    cmSchedule.setStatus(CmsoStatusEnum.Error.toString());
                    cmSchedule.setMsoStatus("Failed");
                    cmSchedule.setMsoMessage("Call to MSO Failed :" + response.toString());
                }
            }
        } catch (ProcessingException e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
            // Probably a transient error... Keep polling
            cmSchedule.setMsoTimeMillis(System.currentTimeMillis());
            cmSchedule.setMsoStatus("ConnectionException");
            cmSchedule.setMsoMessage("Could not call MSO:" + e.getMessage());
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
            // Probably a transient error... Keep polling
            cmSchedule.setMsoTimeMillis(System.currentTimeMillis());
            cmSchedule.setMsoStatus("Exception");
            cmSchedule.setMsoMessage("Could not call MSO:" + e.getMessage());
        } finally {
            Mdc.restore(mdcSave);
        }
        // Propagate final MSO status to top level
        cmScheduleDao.save(cmSchedule);
        propagateStatus(cmSchedule);

    }

    private long getFinishTime(MsoOrchestrationQueryResponse resp) {
        // Just in case we cannot parse the time returned by MSO as a UTC time.
        long finishTime = System.currentTimeMillis();
        String timestr = resp.getFinishTime();
        if (timestr != null) {
            try {
                Date dateTime = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").parse(timestr);
                finishTime = dateTime.getTime();
            } catch (Exception e) {
                Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e,
                                "Unable to parse MSO finish timestamp: " + timestr);
            }
        }
        return finishTime;
    }

    private void propagateStatus(ChangeManagementSchedule cmSchedule) {
        // TODO Auto-generated method stub

    }

    private MsoOrchestrationQueryResponse getResponseStatus(String resp) {
        try {
            ObjectMapper om = new ObjectMapper();
            ObjectNode json = (ObjectNode) om.readTree(resp);
            ObjectNode request = (ObjectNode) json.get("request");
            ObjectNode requestStatus = (ObjectNode) request.get("requestStatus");
            MsoOrchestrationQueryResponse msoResponse =
                            om.treeToValue(requestStatus, MsoOrchestrationQueryResponse.class);
            return msoResponse;
        } catch (Exception e) {
            Observation.report(LogMessages.UNABLE_TO_PARSE_MSO_RESPONSE, e, e.getMessage(), resp);
        }
        return null;
    }

    /**
     * Health check.
     *
     * @return the health check component
     */
    public HealthCheckComponent healthCheck() {
        Map<String, String> mdcSave = Mdc.save();
        String requestId = "healthCheck";
        String url = env.getProperty("so.url", "");
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        url = url + "healthcheck";

        HealthCheckComponent hcc = new HealthCheckComponent();
        hcc.setName("MSO Interface");
        hcc.setUrl(url);

        Client client = ClientBuilder.newClient();
        String user = env.getProperty("so.user", "");
        String pass = pm.getProperty("so.pass", "");

        client.register(new BasicAuthenticatorFilter(user, pass));
        client.register(new CmsoClientFilters());

        WebTarget target = client.target(url);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = null;
        try {
            response = invocationBuilder.get();
            switch (response.getStatus()) {
                case 200:
                case 204:
                    hcc.setHealthy(true);
                    hcc.setStatus("OK");
                    break;
                default: {
                    String respString = response.readEntity(String.class);
                    hcc.setStatus(respString);
                }
            }
        } catch (Exception e) {
            hcc.setStatus(e.getMessage());
        } finally {
            Mdc.restore(mdcSave);
        }
        return hcc;
    }
}
