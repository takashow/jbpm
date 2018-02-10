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

package org.jbpm.services.ejb.impl.tx;

import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jbpm.shared.services.impl.TransactionalCommandService;

@Stateless
public class TransactionalCommandServiceEJBImpl extends TransactionalCommandService {
	
	@PersistenceUnit(unitName="org.jbpm.domain")
	@Override
	public void setEmf(EntityManagerFactory emf) {
		
		super.setEmf(emf);
	}
	
	
	public TransactionalCommandServiceEJBImpl() {
		super(null);
		// entity manager will be set by setter method
	}

}
