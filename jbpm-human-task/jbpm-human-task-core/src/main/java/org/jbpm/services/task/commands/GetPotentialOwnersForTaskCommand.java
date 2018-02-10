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

package org.jbpm.services.task.commands;

import org.kie.api.runtime.Context;
import org.kie.api.task.model.OrganizationalEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@XmlRootElement(name="get-potential-owners-for-task-command")
@XmlAccessorType(XmlAccessType.NONE)
public class GetPotentialOwnersForTaskCommand extends TaskCommand<Map<Long, List<OrganizationalEntity>>> {

	private static final long serialVersionUID = 6296898155907765061L;

    @XmlElement(name="task-id")
	private List<Long> taskIds;
	
	public GetPotentialOwnersForTaskCommand() {
	}
	
	public GetPotentialOwnersForTaskCommand(List<Long> taskIds) {
		this.taskIds = taskIds;
    }
	
    public List<Long> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<Long> taskIds) {
		this.taskIds = taskIds;
	}

	public Map<Long, List<OrganizationalEntity>> execute(Context cntxt ) {
        TaskContext context = (TaskContext) cntxt;

        return context.getTaskQueryService().getPotentialOwnersForTaskIds(taskIds);
    }

}
