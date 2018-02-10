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

package org.jbpm.kie.services.impl.admin.commands;

import org.drools.core.command.impl.ExecutableCommand;
import org.drools.core.command.impl.RegistryContext;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.services.api.NodeInstanceNotFoundException;
import org.jbpm.services.api.ProcessInstanceNotFoundException;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.Context;
import org.kie.internal.command.ProcessInstanceIdCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelNodeInstanceCommand implements ExecutableCommand<Void>, ProcessInstanceIdCommand {

    private static final long serialVersionUID = -8252686458877022331L;
    
    private static final Logger logger = LoggerFactory.getLogger(CancelNodeInstanceCommand.class);

    private long processInstanceId;
    private long nodeInstanceId;

    public CancelNodeInstanceCommand(long processInstanceId, long nodeInstanceId) {
        this.processInstanceId = processInstanceId;
        this.nodeInstanceId = nodeInstanceId;
    }

    public Void execute(Context context) {
        logger.debug("About to cancel node instance with id {} on process instance {}", nodeInstanceId, processInstanceId);
    	KieSession kieSession = ((RegistryContext) context).lookup( KieSession.class );

        RuleFlowProcessInstance wfp = (RuleFlowProcessInstance) kieSession.getProcessInstance(processInstanceId, false);
        if (wfp == null) {
            throw new ProcessInstanceNotFoundException("Process instance with id " + processInstanceId + " not found");
        }
        
        NodeInstance nodeInstance = wfp.getNodeInstances(true).stream().filter(ni -> ni.getId() == nodeInstanceId).findFirst().orElse(null);
        if (nodeInstance == null) {
            throw new NodeInstanceNotFoundException("Node instance with id " + nodeInstanceId + " not found");
        }
        logger.debug("Found node instance {} to be canceled", nodeInstance);
        ((NodeInstanceImpl)nodeInstance).cancel();
        logger.debug("Node instance {} canceled successfully", nodeInstance);
        
        return null;
    }

    @Override
    public void setProcessInstanceId(Long procInstId) {
        this.processInstanceId = procInstId;
        
    }

    @Override
    public Long getProcessInstanceId() {
        return this.processInstanceId;
    }

}
