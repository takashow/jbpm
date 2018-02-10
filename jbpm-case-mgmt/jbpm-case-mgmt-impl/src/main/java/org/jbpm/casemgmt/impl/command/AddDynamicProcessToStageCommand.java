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

package org.jbpm.casemgmt.impl.command;

import java.util.Map;

import org.drools.core.command.impl.RegistryContext;
import org.jbpm.casemgmt.api.StageNotFoundException;
import org.jbpm.casemgmt.api.model.instance.CaseFileInstance;
import org.jbpm.casemgmt.impl.event.CaseEventSupport;
import org.jbpm.services.api.ProcessDefinitionNotFoundException;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.jbpm.workflow.instance.node.DynamicNodeInstance;
import org.jbpm.workflow.instance.node.DynamicUtils;
import org.kie.api.runtime.Context;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.identity.IdentityProvider;

/**
 * Adds subprocess (identified by processId) to selected ad hoc process instance with given parameters
 */
public class AddDynamicProcessToStageCommand extends CaseCommand<Long> {

    private static final long serialVersionUID = 6345222909719335953L;

    private String caseId;
    private String processId;
    private String stageId;
    private long processInstanceId;
    private Map<String, Object> parameters;

    public AddDynamicProcessToStageCommand(IdentityProvider identityProvider,
                                           String caseId,
                                           Long processInstanceId,
                                           String stageId,
                                           String processId,
                                           Map<String, Object> parameters) {
        super(identityProvider);
        this.caseId = caseId;
        this.processInstanceId = processInstanceId;
        this.stageId = stageId;
        this.processId = processId;
        this.parameters = parameters;

        if (processInstanceId == null || processId == null || stageId == null) {
            throw new IllegalArgumentException("Mandatory parameters are missing - process instance id / process id / stage id");
        }
    }

    @Override
    public Long execute(Context context) {
        KieSession ksession = ((RegistryContext) context).lookup(KieSession.class);

        ProcessInstance processInstance = ksession.getProcessInstance(processInstanceId);
        if (processInstance == null) {
            throw new ProcessInstanceNotFoundException("No process instance found with id " + processInstanceId);
        }

        DynamicNodeInstance dynamicContext = (DynamicNodeInstance) ((WorkflowProcessInstanceImpl) processInstance).getNodeInstances(true).stream()
                .filter(ni -> (ni instanceof DynamicNodeInstance) && stageId.equals(ni.getNode().getMetaData().get("UniqueId")))
                .findFirst()
                .orElse(null);

        if (dynamicContext == null) {
            throw new StageNotFoundException("No stage found with id " + stageId);
        }

        try {
            
            CaseFileInstance caseFile = getCaseFile(ksession, caseId);  
            
            CaseEventSupport caseEventSupport = getCaseEventSupport(context);
            caseEventSupport.fireBeforeDynamicProcessAdded(caseId,
                                                           caseFile,
                                                           processInstanceId,
                                                           processId,
                                                           parameters);

            long subProcessInstanceId = DynamicUtils.addDynamicSubProcess(dynamicContext,
                                                                          ksession,
                                                                          processId,
                                                                          parameters);

            if (subProcessInstanceId < 0) {
                throw new ProcessDefinitionNotFoundException("No process definition found with id: " + processId);
            }

            caseEventSupport.fireAfterDynamicProcessAdded(caseId,
                                                          caseFile,
                                                          processInstanceId,
                                                          processId,
                                                          parameters,
                                                          subProcessInstanceId);
            return subProcessInstanceId;
        } catch (IllegalArgumentException e) {
            throw new ProcessDefinitionNotFoundException(e.getMessage());
        }
    }
}
