CREATE DATABASE sas_db;
GRANT ALL PRIVILEGES ON sas_db.* TO 'sas_app'@'%';

CREATE DATABASE sas_auth_server_db;
GRANT ALL PRIVILEGES ON sas_auth_server_db.* TO 'sas_app'@'%';
