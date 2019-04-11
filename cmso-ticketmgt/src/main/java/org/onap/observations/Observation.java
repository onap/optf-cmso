/*
 * Copyright © 2019 AT&T Intellectual Property.
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

package org.onap.observations;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import org.apache.log4j.Level;


public class Observation {
    private static EELFLogger log = EELFManager.getInstance().getLogger(Observation.class);
    private static EELFLogger metrics = EELFManager.getInstance().getMetricsLogger();
    private static EELFLogger audit = EELFManager.getInstance().getAuditLogger();
    private static EELFLogger errors = EELFManager.getInstance().getErrorLogger();
    private static EELFLogger debug = EELFManager.getInstance().getDebugLogger();

    /**
     * Report.
     *
     * @param obs the obs
     * @param exc the exc
     * @param arguments the arguments
     */
    // *************************************************************************************************
    public static void report(ObservationInterface obs, Exception exc, String... arguments) {
        Mdc.setCaller(4);
        Mdc.setObservation(obs);
        if (obs.getAudit()) {
            audit.info(obs, exc, arguments);
        }
        if (obs.getMetric()) {
            metrics.info(obs, exc, arguments);
        }
        Level lev = obs.getLevel();
        switch (lev.toInt()) {
            case Level.WARN_INT:
                errors.warn(obs, arguments);
                debug.debug(obs, exc, arguments);
                break;
            case Level.INFO_INT:
                log.info(obs, exc, arguments);
                debug.debug(obs, exc, arguments);
                break;
            case Level.ERROR_INT:
                errors.error(obs, arguments);
                debug.debug(obs, exc, arguments);
                break;
            case Level.TRACE_INT:
                debug.trace(obs, exc, arguments);
                break;
            case Level.DEBUG_INT:
                debug.debug(obs, exc, arguments);
                break;
            default:
                log.info(obs, exc, arguments);
        }
        Mdc.clearCaller();
    }

    /**
     * Report.
     *
     * @param obs the obs
     * @param arguments the arguments
     */
    public static void report(ObservationInterface obs, String... arguments) {
        Mdc.setCaller(4);
        Mdc.setObservation(obs);
        if (obs.getAudit()) {
            audit.info(obs, arguments);
        }
        if (obs.getMetric()) {
            metrics.info(obs, arguments);
        }
        Level lev = obs.getLevel();
        switch (lev.toInt()) {
            case Level.WARN_INT:
                errors.warn(obs, arguments);
                debug.debug(obs, arguments);
                break;
            case Level.INFO_INT:
                log.info(obs, arguments);
                debug.debug(obs, arguments);
                break;
            case Level.ERROR_INT:
                errors.error(obs, arguments);
                debug.debug(obs, arguments);
                break;
            case Level.TRACE_INT:
                debug.debug(obs, arguments);
                break;
            case Level.DEBUG_INT:
                debug.debug(obs, arguments);
                break;
            default:
                log.info(obs, arguments);
        }
        Mdc.clearCaller();
    }

}
