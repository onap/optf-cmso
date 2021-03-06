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

package org.onap.optf.cmso;

import java.util.concurrent.atomic.AtomicBoolean;
import org.onap.optf.cmso.model.ApprovalType;
import org.onap.optf.cmso.model.Domain;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * The Class JpaInit.
 */
public class JpaInit {

    private static AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * Inits the.
     *
     * @param entityManager the entity manager
     */
    public static void init(TestEntityManager entityManager) {
        if (initialized.compareAndSet(true, true)) {
            return;
        }
        Domain dom = new Domain();
        dom.setDomain("ChangeManagement");
        entityManager.persist(dom);
        ApprovalType at = new ApprovalType();
        at.setApprovalCount(1);
        at.setDomain("ChangeManagement");
        at.setApprovalType("Tier 2");
        entityManager.persist(at);
        entityManager.flush();

    }
}
