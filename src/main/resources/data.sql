create table if not exists _user (
    id bigint,
    username varchar(50) not null,
    password varchar(100) not null,
    role varchar(10) not null,
    primary key (id),
    unique key username (username)
);

-- Users
insert into _user (id, username, password, role) values (1, 'user1', '$2a$10$UcSltcvIoORWLKcCxo.4quqBWSoD0ZdC86caU..NSzNJdOdqgrKx2', 'USER');
insert into _user (id, username, password, role) values (2, 'user2', '$2a$10$egfNXcVS6VnfxpoJrqNIJO2za/OyfQbPU81YgqH8eqdKFN6F8fcs6', 'USER');
insert into _user (id, username, password, role) values (3, 'admin', '$2a$10$1e2I81sXJW0gJDKyDZxhc.JXXIpK1Y0T4AKp0GwNs32370Wa2bDqK', 'ADMIN');