-- liquibase formatted sql

-- changeset slyuban:1
create table notification_task(
                                  notification_task_id serial primary key ,
                                  chat_id serial not null ,
                                  text_message text not null ,
                                  schedule_date_time timestamp not null
);