-- liquibase formatted sql
-- changeset arillon:db.changelog-1.2-add-spendgrid-email-table.sql
create table spendgrid_emails (
    spendgrid_UUID uuid primary key,
    spendgrid_id varchar,
    sender varchar,
    recipient varchar,
    subject varchar,
    email_body varchar,
    created timestamp with time zone default now() not null,
    last_updated timestamp with time zone default now() not null
);

-- create trigger function for last updated
create or replace function spendgrid_email_last_updated() returns trigger
    language plpgsql
as
'
    begin
        NEW.last_updated :=now();
        return NEW;
    end;
';

-- trigger when there is a change to any field
create trigger trigger_spendgrid_email_last_updated
    before update
    on spendgrid_emails
    for each row
execute procedure spendgrid_email_last_updated();
