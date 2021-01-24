## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

# connect to mysql and run as root user
#Create Databases
CREATE DATABASE cookmaster_dev;
CREATE DATABASE cookmaster_prod;

#Create database service accounts
CREATE USER 'cookmaster_dev_user'@'localhost' IDENTIFIED BY 'guru';
CREATE USER 'cookmaster_prod_user'@'localhost' IDENTIFIED BY 'guru';
CREATE USER 'cookmaster_dev_user'@'%' IDENTIFIED BY 'guru';
CREATE USER 'cookmaster_prod_user'@'%' IDENTIFIED BY 'guru';

#Database grants
GRANT SELECT ON cookmaster_dev.* to 'cookmaster_dev_user'@'localhost';
GRANT INSERT ON cookmaster_dev.* to 'cookmaster_dev_user'@'localhost';
GRANT DELETE ON cookmaster_dev.* to 'cookmaster_dev_user'@'localhost';
GRANT UPDATE ON cookmaster_dev.* to 'cookmaster_dev_user'@'localhost';
GRANT SELECT ON cookmaster_prod.* to 'cookmaster_prod_user'@'localhost';
GRANT INSERT ON cookmaster_prod.* to 'cookmaster_prod_user'@'localhost';
GRANT DELETE ON cookmaster_prod.* to 'cookmaster_prod_user'@'localhost';
GRANT UPDATE ON cookmaster_prod.* to 'cookmaster_prod_user'@'localhost';
GRANT SELECT ON cookmaster_dev.* to 'cookmaster_dev_user'@'%';
GRANT INSERT ON cookmaster_dev.* to 'cookmaster_dev_user'@'%';
GRANT DELETE ON cookmaster_dev.* to 'cookmaster_dev_user'@'%';
GRANT UPDATE ON cookmaster_dev.* to 'cookmaster_dev_user'@'%';
GRANT SELECT ON cookmaster_prod.* to 'cookmaster_prod_user'@'%';
GRANT INSERT ON cookmaster_prod.* to 'cookmaster_prod_user'@'%';
GRANT DELETE ON cookmaster_prod.* to 'cookmaster_prod_user'@'%';
GRANT UPDATE ON cookmaster_prod.* to 'cookmaster_prod_user'@'%';