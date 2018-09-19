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

package org.onap.optf.cmso.model.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.onap.optf.cmso.model.ChangeManagementDetail;
import org.springframework.stereotype.Repository;

@Repository
public class ChangeManagementDetailDAOImpl implements ChangeManagementDetailDAO {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ChangeManagementDetail> searchScheduleDetails(String where, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct" + " s.id as id," + " s.vnf_name as vnf_name," + " s.vnf_id as vnf_id,"
                + " s.status as status," + " s.tm_change_id as tm_change_Id," + " s.start_time as start_time,"
                + " s.finish_time as finish_time," + " s.mso_request_id as mso_request_id,"
                + " s.mso_status as mso_status," + " s.mso_message as mso_message," + " s.mso_time as mso_time,"
                + " s.dispatch_time as dispatch_time," + " s.execution_completed_time as execution_completed_time,"
                + " s.status_message as status_message," + " s.tm_approval_status as tm_approval_status,"
                + " s.tm_status as tm_status," + " g.group_id as group_id,"
                + " g.last_instance_start_time as last_instance_start_time," + " g.policy_id as policy_id,"
                + " g.schedules_id as schedules_id"
                // + " ss.schedule_id as scheduleId,"
                // + " dd.name"
                + " from" + " CHANGE_MANAGEMENT_SCHEDULES s"
                + " inner join CHANGE_MANAGEMENT_GROUPS g on s.change_management_groups_id = g.id"
                + " inner join SCHEDULES ss on g.schedules_id = ss.id "
                + " left outer join DOMAIN_DATA dd on ss.id = dd.schedules_id"
                + " left outer join SCHEDULE_APPROVALS sa on ss.id = sa.schedules_id"
                + " left outer join APPROVAL_TYPES at on sa.approval_type_id = at.id ");
        sql.append(where);
        sql.append(" order by id ");
        if (limit > 0)
            sql.append("LIMIT " + limit);

        Query q = manager.createNativeQuery(sql.toString(), ChangeManagementDetail.class);
        List<ChangeManagementDetail> list = q.getResultList();
        return list;
    }
}
