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
import org.kie.api.task.model.Attachment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "get-all-attachments-command")
@XmlAccessorType(XmlAccessType.NONE)
public class GetAllAttachmentsCommand extends TaskCommand<List<Attachment>> {

    private static final long serialVersionUID = -4566088487597623910L;

    public GetAllAttachmentsCommand() {
    }

    public GetAllAttachmentsCommand(Long taskId) {
        this.taskId = taskId;
    }

    public List<Attachment> execute(Context cntxt) {
        TaskContext context = (TaskContext) cntxt;
        return context.getTaskAttachmentService().getAllAttachmentsByTaskId(taskId);
    }

}
