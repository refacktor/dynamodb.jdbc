-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: TestDatabaseForDynamodbJDBC
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `log_access`
--

DROP TABLE IF EXISTS `log_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_access` (
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `remote_ip` varchar(16) NOT NULL,
  `url` text,
  `query_string` text,
  `http_status` int(11) NOT NULL,
  `bytes_sent` int(11) DEFAULT NULL,
  `time_to_first_byte` int(10) unsigned DEFAULT NULL,
  `time_elapsed` int(10) unsigned NOT NULL,
  `session_id` varchar(99) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `agent_proxy` tinyint(4) NOT NULL DEFAULT '0',
  `agent_id` varchar(45) DEFAULT NULL,
  `server_ts` datetime(3) NOT NULL,
  `local_ip` varchar(16) NOT NULL,
  `method` varchar(10) NOT NULL,
  `protocol` varchar(30) NOT NULL,
  `thread_name` varchar(99) NOT NULL,
  `referer` text,
  `user_agent` text,
  `host` varchar(250) DEFAULT NULL,
  `pagename` varchar(45) DEFAULT NULL,
  `listing_id` varchar(45) DEFAULT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `ts` (`ts`),
  KEY `user_log` (`user_id`,`url`(8),`ts`),
  KEY `session_url` (`session_id`,`url`(10)),
  KEY `user_ts` (`user_id`,`ts`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `log_access_desc`
--

DROP TABLE IF EXISTS `log_access_desc`;
/*!50001 DROP VIEW IF EXISTS `log_access_desc`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `log_access_desc` AS SELECT 
 1 AS `ts`,
 1 AS `remote_ip`,
 1 AS `url`,
 1 AS `query_string`,
 1 AS `http_status`,
 1 AS `bytes_sent`,
 1 AS `time_to_first_byte`,
 1 AS `time_elapsed`,
 1 AS `session_id`,
 1 AS `user_id`,
 1 AS `agent_proxy`,
 1 AS `server_ts`,
 1 AS `local_ip`,
 1 AS `method`,
 1 AS `protocol`,
 1 AS `thread_name`,
 1 AS `referer`,
 1 AS `user_agent`,
 1 AS `id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_mail`
--

DROP TABLE IF EXISTS `log_mail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_mail` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `to` varchar(250) NOT NULL,
  `subject` varchar(250) NOT NULL,
  `headers` mediumtext NOT NULL,
  `body` longtext NOT NULL,
  `status` varchar(8) DEFAULT NULL,
  `statusText` mediumtext,
  PRIMARY KEY (`id`),
  KEY `cts` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_showings`
--

DROP TABLE IF EXISTS `log_showings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_showings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `mls` varchar(50) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `contact` varchar(200) DEFAULT NULL,
  `address` varchar(200) NOT NULL,
  `date` char(11) NOT NULL,
  `time` varchar(10) NOT NULL,
  `comments` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `FirstName` varchar(255) NOT NULL DEFAULT '',
  `LastName` varchar(255) NOT NULL DEFAULT '',
  `Password` varchar(255) NOT NULL DEFAULT '',
  `EmailAddress` varchar(999) NOT NULL DEFAULT '',
  `EmailError` varchar(255) DEFAULT NULL,
  `EmailErrorCount` int(10) unsigned NOT NULL DEFAULT '0',
  `day_phone` varchar(255) NOT NULL DEFAULT '',
  `mob_phone` varchar(255) NOT NULL DEFAULT '',
  `ev_phone` varchar(255) NOT NULL DEFAULT '',
  `fax` varchar(255) NOT NULL DEFAULT '',
  `Addr1` varchar(40) DEFAULT NULL,
  `Addr2` varchar(40) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `RecordCreationDT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastLoginDT` timestamp NULL DEFAULT NULL,
  `LastNotificationDT` timestamp NULL DEFAULT NULL,
  `TotalVisit` int(10) unsigned NOT NULL DEFAULT '0',
  `NotificationCount` int(11) unsigned NOT NULL DEFAULT '0',
  `adminLevel` smallint(1) NOT NULL DEFAULT '0',
  `browser_info` varchar(250) DEFAULT NULL,
  `active` smallint(1) NOT NULL DEFAULT '0',
  `ip_address` varchar(40) DEFAULT NULL,
  `screen_info` varchar(32) DEFAULT NULL,
  `random` bigint(20) NOT NULL DEFAULT '0',
  `interestRate` decimal(2,1) unsigned zerofill NOT NULL DEFAULT '6.5',
  `taxBracket` tinyint(2) unsigned NOT NULL DEFAULT '30',
  `dismiss` tinyint(4) DEFAULT NULL,
  `origin_site` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`),
  UNIQUE KEY `random` (`random`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_favorites`
--

DROP TABLE IF EXISTS `user_favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_favorites` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `mls` varchar(10) DEFAULT NULL,
  `agentsuggested` smallint(1) NOT NULL DEFAULT '0',
  `modtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`mls`),
  KEY `modtime` (`modtime`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_search_hist`
--

DROP TABLE IF EXISTS `user_search_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_search_hist` (
  `id` int(12) NOT NULL DEFAULT '0',
  `lastnotified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `id` (`id`,`lastnotified`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_searches`
--

DROP TABLE IF EXISTS `user_searches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_searches` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `createdTs` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `site` varchar(99) DEFAULT NULL,
  `link` mediumtext NOT NULL,
  `name` varchar(250) NOT NULL DEFAULT '',
  `updated` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=428 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `log_access_desc`
--

/*!50001 DROP VIEW IF EXISTS `log_access_desc`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `log_access_desc` AS select `log_access`.`ts` AS `ts`,`log_access`.`remote_ip` AS `remote_ip`,`log_access`.`url` AS `url`,`log_access`.`query_string` AS `query_string`,`log_access`.`http_status` AS `http_status`,`log_access`.`bytes_sent` AS `bytes_sent`,`log_access`.`time_to_first_byte` AS `time_to_first_byte`,`log_access`.`time_elapsed` AS `time_elapsed`,`log_access`.`session_id` AS `session_id`,`log_access`.`user_id` AS `user_id`,`log_access`.`agent_proxy` AS `agent_proxy`,`log_access`.`server_ts` AS `server_ts`,`log_access`.`local_ip` AS `local_ip`,`log_access`.`method` AS `method`,`log_access`.`protocol` AS `protocol`,`log_access`.`thread_name` AS `thread_name`,`log_access`.`referer` AS `referer`,`log_access`.`user_agent` AS `user_agent`,`log_access`.`id` AS `id` from `log_access` order by `log_access`.`id` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-19 11:46:09
