CREATE DATABASE plinthosdb DEFAULT CHARSET=utf8;

USE plinthosdb;

DROP TABLE IF EXISTS `report_request`;

CREATE TABLE `report_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `OBJ_VERSION` int(11) NOT NULL,
  `CORRELATION_ID` varchar(512) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `TEMPLATE_PARAMS` longtext,
  `STATUS` varchar(255) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `EXPIRATION` datetime DEFAULT NULL,
  `SUBMISSION_TIME` datetime DEFAULT NULL,
  `COMPLETION_TIME` datetime DEFAULT NULL,
  `USER_EMAIL` varchar(255) DEFAULT NULL,
  `CANCEL_REQUESTED` bit(1) DEFAULT NULL,
  `PROGRESS_MESSAGE` varchar(1024) DEFAULT NULL,
  `REQUEST_RESULTS` longtext,
  `STATUS_MESSAGE` varchar(1024) DEFAULT NULL,
  `REQUEST_TYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK414EF28F4F6EF1A` (`REQUEST_TYPE`)
) ENGINE=InnoDB AUTO_INCREMENT=1;


/*Table structure for table `system_configuration` */

DROP TABLE IF EXISTS `system_configuration`;

CREATE TABLE `system_configuration` (
  `NAME` varchar(128) NOT NULL,
  `VALUE` varchar(512) DEFAULT NULL,
  `LAST_CHANGED` datetime DEFAULT NULL,
  `CREATION_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`NAME`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `task_registry`;

CREATE TABLE `task_registry` (
  `task_type` varchar(255) NOT NULL,
  `OBJ_VERSION` int(11) NOT NULL,
  `executor_class` varchar(1024) DEFAULT NULL,
  `etc_supported` bit(1) DEFAULT NULL,
  PRIMARY KEY (`task_type`)
) ENGINE=InnoDB;
