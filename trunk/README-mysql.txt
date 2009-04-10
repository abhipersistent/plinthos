Running PlinthOS with MySQL Database

1 To change database type you can modify ./plinthos/scripts/run.bat 
to use appropriate database type: hsqldb or mysql

2 You need to create empty database. Default name used in hibernate configuration file is
'plinthosdb' 

Script to create database with tables: ./plinthos/scripts/mysql/plinthosDB.sql

3 There is no need to load any data. PlinthOS will load all necessary configuration on first
run and will add any new values on subsequent restarts.

4 You may want to modify default config/hibernate/hibernate-mysql.cfg.xml based on your needs. 
By default it will drop and recreate all tables in the database. 


