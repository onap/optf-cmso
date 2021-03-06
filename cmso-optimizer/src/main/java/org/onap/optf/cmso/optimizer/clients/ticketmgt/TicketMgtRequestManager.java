/*
 * ============LICENSE_START==============================================
 * Copyright (c) 2019 AT&T Intellectual Property.
 * =======================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * ============LICENSE_END=================================================
 *
 */

package org.onap.optf.cmso.optimizer.clients.ticketmgt;

import java.util.Optional;
import java.util.UUID;
import org.onap.optf.cmso.optimizer.clients.ticketmgt.models.ActiveTicketsResponse;
import org.onap.optf.cmso.optimizer.model.Request;
import org.onap.optf.cmso.optimizer.model.Ticket;
import org.onap.optf.cmso.optimizer.model.dao.RequestDao;
import org.onap.optf.cmso.optimizer.model.dao.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Ticket Mgt request manager.
 *
 * @author jf9860
 *
 */
@Component
public class TicketMgtRequestManager {

    @Autowired
    Environment env;

    @Autowired
    RequestDao requestDao;

    @Autowired
    TicketDao ticketDao;

    @Autowired
    TicketMgtClient ticketmgtClient;

    /**
     * Creates the tickets request.
     *
     * @param requestRow the uuid
     * @return the active tickets response
     */
    public ActiveTicketsResponse createTicketsRequest(Request requestRow) {
        Ticket row = null;
        Optional<Ticket> rowOpt = ticketDao.findById(requestRow.getUuid());
        if (rowOpt.isPresent()) {
            row = rowOpt.get();

        }
        if (row == null) {
            row = new Ticket();
            row.setUuid(requestRow.getUuid());
            row.setTicketsRetries(0);
        }
        ActiveTicketsResponse apiResponse = ticketmgtClient.makeRequest(requestRow, row);
        ticketDao.save(row);
        return apiResponse;
    }

    /**
     * Gets the existing tickets.
     *
     * @param uuid the uuid
     * @return the existing tickets
     */
    public Ticket getExistingTickets(UUID uuid) {
        Optional<Ticket> opt = ticketDao.findById(uuid);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

}
