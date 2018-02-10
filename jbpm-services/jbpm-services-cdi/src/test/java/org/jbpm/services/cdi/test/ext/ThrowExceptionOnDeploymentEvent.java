/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.services.cdi.test.ext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jbpm.services.api.DeploymentEvent;
import org.jbpm.services.cdi.Deploy;

@ApplicationScoped
public class ThrowExceptionOnDeploymentEvent {

	public void checkAndThrow(@Observes @Deploy DeploymentEvent event) {
		if (event.getDeploymentId().endsWith("ksession-test")) {
			throw new IllegalStateException("Thrown on purpose to rollback deployment");
		}
	}
}
