USE CMSO;

-- --------------------------------------------------------------
-- DROP ALL OF THE INTENGER IDs
-- --------------------------------------------------------------
ALTER TABLE SCHEDULES DROP COLUMN id;
ALTER TABLE DOMAIN_DATA DROP COLUMN id;
ALTER TABLE DOMAIN_DATA DROP COLUMN schedules_id;
ALTER TABLE APPROVAL_TYPES DROP COLUMN id;
ALTER TABLE SCHEDULE_APPROVALS DROP COLUMN id;
ALTER TABLE SCHEDULE_APPROVALS DROP COLUMN schedules_id;
ALTER TABLE SCHEDULE_APPROVALS DROP COLUMN approval_type_id;
ALTER TABLE CHANGE_MANAGEMENT_GROUPS DROP COLUMN id;
ALTER TABLE CHANGE_MANAGEMENT_GROUPS DROP COLUMN schedules_id;
ALTER TABLE CHANGE_MANAGEMENT_SCHEDULES DROP COLUMN id;
ALTER TABLE CHANGE_MANAGEMENT_SCHEDULES DROP COLUMN change_management_groups_id;
ALTER TABLE CHANGE_MANAGEMENT_CHANGE_WINDOWS DROP COLUMN id;
ALTER TABLE CHANGE_MANAGEMENT_CHANGE_WINDOWS DROP COLUMN change_management_groups_id;
