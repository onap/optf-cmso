/*
 * Copyright © 2017-2019 AT&T Intellectual Property.
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

package org.onap.optf.cmso.sostatus;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.observations.Mdc;
import org.onap.observations.Observation;
import org.onap.optf.cmso.common.BasicAuthenticatorFilter;
import org.onap.optf.cmso.common.DomainsEnum;
import org.onap.optf.cmso.common.LogMessages;
import org.onap.optf.cmso.common.PropertiesManagement;
import org.onap.optf.cmso.filters.CmsoClientFilters;
import org.onap.optf.cmso.model.ChangeManagementSchedule;
import org.onap.optf.cmso.model.Schedule;
import org.onap.optf.cmso.model.dao.ChangeManagementScheduleDao;
import org.onap.optf.cmso.model.dao.ScheduleDao;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * This is the Quartz Job that is run to send the workflow to VID for execution.
 *
 *
 */
@Component
@DisallowConcurrentExecution
public class ScheduleStatusJob implements Job {
    private static EELFLogger debug = EELFManager.getInstance().getDebugLogger();

    @Autowired
    ScheduleDao scheduleDao;

    @Autowired
    ChangeManagementScheduleDao cmScheduleDao;

    @Autowired
    PropertiesManagement pm;

    @Autowired
    Environment env;

    /**
     * Execute.
     *
     * @param context the context
     * @throws JobExecutionException the job execution exception
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        debug.debug(LogMessages.SCHEDULE_STATUS_JOB, "Entered");
        try {
            // First poll SO for WF status
            List<ChangeManagementSchedule> list = cmScheduleDao.findAllTriggered();
            for (ChangeManagementSchedule s : list) {
                debug.debug("Dispathcing to check status of CM schedule Id=" + s.getUuid());
                dispatchMso(s.getUuid());
            }
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
        }
        try {

            //
            // Update overall status of in flight schedules including closing tickets
            List<Schedule> list = scheduleDao.findAllInProgress(DomainsEnum.ChangeManagement.toString());
            for (Schedule s : list) {
                debug.debug("Dispatching to check status of scheduleId=" + s.getScheduleId());
                dispatchScheduleStatusChecker(s.getUuid());
            }
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
        }
        debug.debug(LogMessages.SCHEDULE_STATUS_JOB, "Exited");
    }

    /**
     * Dispatch schedule status checker.
     *
     * @param uuid the uuid
     */
    public void dispatchScheduleStatusChecker(UUID uuid) {
        Map<String, String> mdcSave = Mdc.save();
        try {
            String url = env.getProperty("cmso.dispatch.url", "http://localhost:8089");
            String path = env.getProperty("cmso.dispatch.status.path", "/cmso/dispatch/schedulestatus/");
            url = url + path + uuid;
            String user = env.getProperty("mechid.user", "");
            String pass = pm.getProperty("mechid.pass", "");
            Client client = ClientBuilder.newClient();
            client.register(new BasicAuthenticatorFilter(user, pass));
            client.register(CmsoClientFilters.class);
            WebTarget target = client.target(url);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = null;
            try {
                response = invocationBuilder.get();
                switch (response.getStatus()) {
                    case 200:
                        debug.debug("Returned from dispatch call");
                        break;
                    case 400: // Bad request
                    default: {

                        throw new SchedulerException(
                                "Invalid return from dispach service: " + url + " : " + response.toString());
                    }
                }
            } catch (Exception e) {
                Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
            }
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
        } finally {
            Mdc.restore(mdcSave);
        }

    }

    /**
     * Dispatch mso.
     *
     * @param uuid the uuid
     */
    public void dispatchMso(UUID uuid) {
        Map<String, String> mdcSave = Mdc.save();
        try {
            String url = env.getProperty("cmso.dispatch.url", "http://localhost:8089");
            String path = env.getProperty("cmso.dispatch.sostatus.path", "/cmso/dispatch/sostatus/");
            url = url + path + uuid;
            String user = env.getProperty("mechid.user", "");
            String pass = pm.getProperty("mechid.pass", "");
            Client client = ClientBuilder.newClient();
            client.register(new BasicAuthenticatorFilter(user, pass));
            client.register(CmsoClientFilters.class);
            WebTarget target = client.target(url);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = null;
            try {
                response = invocationBuilder.get();
                switch (response.getStatus()) {
                    case 200:
                        debug.debug("Returned from dispatch call");
                        break;
                    case 400: // Bad request
                    default: {

                        throw new SchedulerException(
                                "Invalid return from dispach service: " + url + " : " + response.toString());
                    }
                }
            } catch (Exception e) {
                Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
            }
        } catch (Exception e) {
            Observation.report(LogMessages.UNEXPECTED_EXCEPTION, e, e.getMessage());
        } finally {
            Mdc.restore(mdcSave);
        }
    }

}
