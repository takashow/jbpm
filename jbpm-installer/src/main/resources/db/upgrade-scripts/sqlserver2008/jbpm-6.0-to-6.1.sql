-- update context mapping info table with owner id (deployment id) for per process instance strategies
alter table ContextMappingInfo add OWNER_ID varchar(255);
update ContextMappingInfo set OWNER_ID = (select externalId from ProcessInstanceLog where processInstanceId = cast(CONTEXT_ID as bigint));

create table AuditTaskImpl (
        id bigint identity not null,
        activationTime date,
        actualOwner varchar(255),
        createdBy varchar(255),
        createdOn date,
        deploymentId varchar(255),
        description varchar(255),
        dueDate date,
        name varchar(255),
        parentId bigint not null,
        priority int not null,
        processId varchar(255),
        processInstanceId bigint not null,
        processSessionId int not null,
        status varchar(255),
        taskId bigint,
        primary key (id));