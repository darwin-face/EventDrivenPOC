-- liquibase formatted sql
-- changeset arillon:db.changelog-1.1-add--snailgun-email-table.sql

create type status as enum ('queued', 'sent', 'failed');

create table snailgun_emails (
    snailgun_UUID uuid primary key,
    snailgun_id varchar,
    to_email varchar,
    to_name varchar,
    from_email varchar,
    from_name varchar,
    subject varchar,
    email_body varchar,
    status status,
    snailgun_created timestamp with time zone,
    created timestamp with time zone default now() not null,
    last_updated timestamp with time zone default now() not null
);

-- create trigger function for last updated
create or replace function snailgun_email_last_updated() returns trigger
    language plpgsql
as
'
    begin
        NEW.last_updated :=now();
        return NEW;
    end;
';

-- trigger when there is a change to any field
create trigger trigger_snailgun_email_last_updated
    before update
    on snailgun_emails
    for each row
execute procedure snailgun_email_last_updated();

