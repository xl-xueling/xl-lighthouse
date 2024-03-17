CREATE DATABASE  IF NOT EXISTS `cluster_${ldp_lighthouse_cluster_id}_ldp_mysqldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cluster_${ldp_lighthouse_cluster_id}_ldp_mysqldb`;
-- MySQL dump 10.13  Distrib 8.0.30, for macos12 (x86_64)
--
-- Host: 10.206.6.13    Database: cluster_dc00df25_ldp_mysqldb
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ldp_components`
--

DROP TABLE IF EXISTS `ldp_components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_components` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `title` varchar(100) NOT NULL,
                                  `component_type` tinyint(1) NOT NULL,
                                  `private_type` tinyint(1) NOT NULL,
                                  `configuration` mediumtext NOT NULL,
                                  `user_id` int NOT NULL,
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `update_time` timestamp NOT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1046 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_components`
--

LOCK TABLES `ldp_components` WRITE;
/*!40000 ALTER TABLE `ldp_components` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_components` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_departments`
--

DROP TABLE IF EXISTS `ldp_departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_departments` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `name` varchar(60) NOT NULL,
                                   `pid` int NOT NULL,
                                   `third_id` varchar(90) DEFAULT NULL,
                                   `third_pid` varchar(90) DEFAULT NULL,
                                   `create_time` timestamp NOT NULL,
                                   `update_time` timestamp NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `index_pid` (`pid`),
                                   KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=10222 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_departments`
--

LOCK TABLES `ldp_departments` WRITE;
/*!40000 ALTER TABLE `ldp_departments` DISABLE KEYS */;
INSERT INTO `ldp_departments` VALUES (10221,'First Department',0,NULL,NULL,'2024-03-17 09:59:46','2024-03-17 09:59:46');
/*!40000 ALTER TABLE `ldp_departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_domains`
--

DROP TABLE IF EXISTS `ldp_domains`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_domains` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(45) NOT NULL,
                               `default_token_prefix` varchar(10) DEFAULT NULL,
                               `create_time` timestamp NULL DEFAULT NULL,
                               `update_time` timestamp NULL DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_domains`
--

LOCK TABLES `ldp_domains` WRITE;
/*!40000 ALTER TABLE `ldp_domains` DISABLE KEYS */;
INSERT INTO `ldp_domains` VALUES (5,'Default-Domain-1710669586408','RrY','2024-03-17 09:59:46','2024-03-17 09:59:46');
/*!40000 ALTER TABLE `ldp_domains` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_env`
--

DROP TABLE IF EXISTS `ldp_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_env` (
                           `param` varchar(100) NOT NULL,
                           `value` text,
                           PRIMARY KEY (`param`),
                           KEY `index_param` (`param`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_env`
--

LOCK TABLES `ldp_env` WRITE;
/*!40000 ALTER TABLE `ldp_env` DISABLE KEYS */;
INSERT INTO `ldp_env` VALUES ('sign_key','RYr7psO3rxBufbCBnrUUQzNtO5wTQiNfjldT2nv6puy1DuMgx6A924oHZak3OQT9');
/*!40000 ALTER TABLE `ldp_env` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_groups`
--

DROP TABLE IF EXISTS `ldp_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_groups` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `token` varchar(30) NOT NULL,
                              `project_id` int NOT NULL,
                              `debug_mode` int DEFAULT '0',
                              `columns` varchar(3000) NOT NULL,
                              `desc` varchar(800) NOT NULL,
                              `random_id` varchar(45) NOT NULL,
                              `secret_key` varchar(60) NOT NULL,
                              `state` int NOT NULL,
                              `extend_config` varchar(2000) DEFAULT NULL,
                              `refresh_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`),
                              KEY `index_token` (`token`),
                              KEY `index_project_id` (`project_id`),
                              KEY `index_create_time` (`create_time`),
                              KEY `index_refresh_time` (`refresh_time`),
                              KEY `index_state` (`state`)
) ENGINE=InnoDB AUTO_INCREMENT=100289 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_groups`
--

LOCK TABLES `ldp_groups` WRITE;
/*!40000 ALTER TABLE `ldp_groups` DISABLE KEYS */;
INSERT INTO `ldp_groups` VALUES (100288,'test_scene_behavior_stat',11108,0,'[{\"name\":\"behavior_id\",\"type\":\"string\"},{\"name\":\"request_id\",\"type\":\"string\"},{\"name\":\"imei\",\"type\":\"string\"},{\"name\":\"province\",\"type\":\"string\"},{\"name\":\"recall_no\",\"type\":\"string\"},{\"name\":\"abtest_no\",\"type\":\"string\"},{\"name\":\"score\",\"type\":\"number\"},{\"name\":\"city\",\"type\":\"string\"}]','测试统计组！','39Vke68YgJ3N6Y6i6o76Nfsz06Sqr3hM','JmVM5qDhpkizvJSLjgCoXa10k5j4UWJyj3LSJsPp',1,'{\"limitingConfig\":{}}','2024-03-17 12:48:35','2024-03-17 12:48:35','2024-03-17 12:48:35');
/*!40000 ALTER TABLE `ldp_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_metas`
--

DROP TABLE IF EXISTS `ldp_metas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_metas` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `meta_name` varchar(100) DEFAULT NULL,
                             `type` int DEFAULT NULL,
                             `state` int DEFAULT NULL,
                             `template` varchar(3000) DEFAULT NULL,
                             `record_size` int DEFAULT NULL,
                             `content_size` bigint NOT NULL,
                             `desc` varchar(100) DEFAULT NULL,
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`),
                             KEY `index_meta_name` (`meta_name`),
                             KEY `index_state` (`state`),
                             KEY `index_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=10016 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_metas`
--

LOCK TABLES `ldp_metas` WRITE;
/*!40000 ALTER TABLE `ldp_metas` DISABLE KEYS */;
INSERT INTO `ldp_metas` VALUES (10015,'ldp_stat_1710681305546',3,1,NULL,0,0,NULL,'2024-03-17 13:15:08','2024-03-17 13:15:08');
/*!40000 ALTER TABLE `ldp_metas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_metricsets`
--

DROP TABLE IF EXISTS `ldp_metricsets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_metricsets` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `title` varchar(60) DEFAULT NULL,
                                  `private_type` int DEFAULT NULL,
                                  `desc` varchar(500) DEFAULT NULL,
                                  `structure` mediumtext,
                                  `create_time` timestamp NULL DEFAULT NULL,
                                  `update_time` timestamp NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_metricsets`
--

LOCK TABLES `ldp_metricsets` WRITE;
/*!40000 ALTER TABLE `ldp_metricsets` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_metricsets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_order_details`
--

DROP TABLE IF EXISTS `ldp_order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_order_details` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `order_id` int DEFAULT NULL,
                                     `user_id` int DEFAULT NULL,
                                     `role_type` int DEFAULT NULL,
                                     `state` tinyint(1) DEFAULT NULL,
                                     `role_id` int DEFAULT NULL,
                                     `reply` varchar(300) DEFAULT NULL,
                                     `create_time` timestamp NULL DEFAULT NULL,
                                     `process_time` timestamp NULL DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `index_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_order_details`
--

LOCK TABLES `ldp_order_details` WRITE;
/*!40000 ALTER TABLE `ldp_order_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_orders`
--

DROP TABLE IF EXISTS `ldp_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_orders` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `user_id` int NOT NULL,
                              `order_type` tinyint(1) NOT NULL,
                              `state` tinyint(1) NOT NULL,
                              `steps` varchar(300) DEFAULT NULL,
                              `extend_config` varchar(600) DEFAULT NULL,
                              `hash` varchar(100) NOT NULL,
                              `current_node` int DEFAULT NULL,
                              `reason` varchar(600) DEFAULT NULL,
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`),
                              KEY `index_hash` (`hash`),
                              KEY `index_state` (`state`),
                              KEY `index_current_node` (`current_node`),
                              KEY `index_createtime` (`create_time`),
                              KEY `index_order_type` (`order_type`),
                              KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100221 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_orders`
--

LOCK TABLES `ldp_orders` WRITE;
/*!40000 ALTER TABLE `ldp_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_permissions`
--

DROP TABLE IF EXISTS `ldp_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_permissions` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `owner_id` int DEFAULT NULL,
                                   `owner_type` int DEFAULT NULL,
                                   `role_id` int DEFAULT NULL,
                                   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                   `update_time` timestamp NULL DEFAULT NULL,
                                   `expire_time` timestamp NULL DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `index_owner` (`owner_type`,`owner_id`),
                                   KEY `index_role_id` (`role_id`),
                                   KEY `index_createtime` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100540 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_permissions`
--

LOCK TABLES `ldp_permissions` WRITE;
/*!40000 ALTER TABLE `ldp_permissions` DISABLE KEYS */;
INSERT INTO `ldp_permissions` VALUES (100538,110222,1,735,'2024-03-17 09:59:47','2024-03-17 09:59:47',NULL),(100539,110222,1,741,'2024-03-17 10:07:52','2024-03-17 10:07:52',NULL);
/*!40000 ALTER TABLE `ldp_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_projects`
--

DROP TABLE IF EXISTS `ldp_projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_projects` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `title` varchar(100) NOT NULL,
                                `department_id` int NOT NULL,
                                `private_type` tinyint(1) NOT NULL,
                                `desc` varchar(500) DEFAULT NULL,
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                KEY `index_department_id` (`department_id`),
                                KEY `index_title` (`title`),
                                KEY `index_createtime` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11109 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_projects`
--

LOCK TABLES `ldp_projects` WRITE;
/*!40000 ALTER TABLE `ldp_projects` DISABLE KEYS */;
INSERT INTO `ldp_projects` VALUES (1,'sssss',10221,0,'sadgasg','2024-03-17 02:07:51','2024-03-17 02:07:51'),(11108,'我的测试数据工程',10221,0,'sadgasg','2024-03-17 10:07:51','2024-03-17 10:07:51');
/*!40000 ALTER TABLE `ldp_projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_records`
--

DROP TABLE IF EXISTS `ldp_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_records` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `user_id` int DEFAULT '0',
                               `resource_id` int NOT NULL,
                               `resource_type` int NOT NULL,
                               `record_type` int NOT NULL,
                               `extend` varchar(5000) DEFAULT NULL,
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`id`),
                               KEY `index_create_time` (`create_time` DESC),
                               KEY `index_record_type` (`record_type`),
                               KEY `index_resource` (`resource_type`,`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100508 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_records`
--

LOCK TABLES `ldp_records` WRITE;
/*!40000 ALTER TABLE `ldp_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_relations`
--

DROP TABLE IF EXISTS `ldp_relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_relations` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `subject_id` int NOT NULL,
                                 `relation_type` tinyint(1) NOT NULL,
                                 `resource_id` int NOT NULL,
                                 `resource_type` tinyint(1) NOT NULL,
                                 `hash` varchar(45) DEFAULT NULL,
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `update_time` timestamp NULL DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `index_relationa` (`subject_id`,`relation_type`),
                                 KEY `index_relationb` (`resource_id`,`relation_type`),
                                 KEY `index_hash` (`hash`),
                                 KEY `index_updatetime` (`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100364 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_relations`
--

LOCK TABLES `ldp_relations` WRITE;
/*!40000 ALTER TABLE `ldp_relations` DISABLE KEYS */;
/*!40000 ALTER TABLE `ldp_relations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_roles`
--

DROP TABLE IF EXISTS `ldp_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_roles` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `role_type` int DEFAULT NULL,
                             `resource_id` int DEFAULT NULL,
                             `pid` int DEFAULT NULL,
                             `create_time` timestamp NULL DEFAULT NULL,
                             `update_time` timestamp NULL DEFAULT NULL,
                             `desc` varchar(300) DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `index_pid` (`pid`),
                             KEY `index_roletype_resourceId` (`role_type`,`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=753 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_roles`
--

LOCK TABLES `ldp_roles` WRITE;
/*!40000 ALTER TABLE `ldp_roles` DISABLE KEYS */;
INSERT INTO `ldp_roles` VALUES (733,1,0,NULL,'2024-03-17 09:59:46','2024-03-17 09:59:46','FULL_MANAGE_PERMISSION'),(734,2,0,NULL,'2024-03-17 09:59:46','2024-03-17 09:59:46','FULL_ACCESS_PERMISSION'),(735,3,0,733,'2024-03-17 09:59:46','2024-03-17 09:59:46','OPT_MANAGE_PERMISSION'),(736,4,0,734,'2024-03-17 09:59:46','2024-03-17 09:59:46','OPT_ACCESS_PERMISSION'),(737,5,5,733,'2024-03-17 09:59:46','2024-03-17 09:59:46','DOMAIN_MANAGE_PERMISSION(Default-Domain-1710669586408)'),(738,6,5,734,'2024-03-17 09:59:46','2024-03-17 09:59:46','DOMAIN_ACCESS_PERMISSION(Default-Domain-1710669586408)'),(739,7,10221,737,'2024-03-17 09:59:47','2024-03-17 09:59:47','DEPARTMENT_MANAGE_PERMISSION(First Department)'),(740,8,10221,738,'2024-03-17 09:59:47','2024-03-17 09:59:47','DEPARTMENT_ACCESS_PERMISSION(First Department)'),(741,9,11108,739,'2024-03-17 10:07:52','2024-03-17 14:02:35','PROJECT_MANAGE_PERMISSION(sssss)'),(742,10,11108,740,'2024-03-17 10:07:52','2024-03-17 14:02:35','PROJECT_ACCESS_PERMISSION(sssss)'),(745,11,100288,741,'2024-03-17 12:48:35','2024-03-17 12:48:35','GROUP_MANAGE_PERMISSION(RrY:test_scene_behavior_stat)'),(746,12,100288,742,'2024-03-17 12:48:35','2024-03-17 12:48:35','GROUP_ACCESS_PERMISSION(RrY:test_scene_behavior_stat)'),(747,13,1100519,745,'2024-03-17 13:15:08','2024-03-17 13:15:08','STAT_MANAGE_PERMISSION(每分钟_次数统计)'),(748,14,1100519,746,'2024-03-17 13:15:08','2024-03-17 13:15:08','STAT_ACCESS_PERMISSION(每分钟_次数统计)'),(749,13,1100520,745,'2024-03-17 13:15:43','2024-03-17 13:15:43','STAT_MANAGE_PERMISSION(每天_次数统计)'),(750,14,1100520,746,'2024-03-17 13:15:43','2024-03-17 13:15:43','STAT_ACCESS_PERMISSION(每天_次数统计)'),(751,13,1100521,745,'2024-03-17 14:03:22','2024-03-17 14:03:22','STAT_MANAGE_PERMISSION(每天_各省份_总次数)'),(752,14,1100521,746,'2024-03-17 14:03:22','2024-03-17 14:03:22','STAT_ACCESS_PERMISSION(每天_各省份_总次数)');
/*!40000 ALTER TABLE `ldp_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_stats`
--

DROP TABLE IF EXISTS `ldp_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stats` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `title` varchar(60) NOT NULL,
                             `group_id` int NOT NULL,
                             `project_id` int DEFAULT NULL,
                             `template` varchar(500) NOT NULL,
                             `timeparam` varchar(50) DEFAULT NULL,
                             `expired` bigint NOT NULL,
                             `state` int DEFAULT NULL,
                             `render_config` varchar(2000) DEFAULT NULL,
                             `meta_id` int DEFAULT NULL,
                             `data_version` int DEFAULT '0',
                             `desc` varchar(300) DEFAULT NULL,
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `refresh_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `random_id` varchar(45) NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `index_state` (`state`),
                             KEY `index_group_id` (`group_id`),
                             KEY `index_project_id` (`project_id`),
                             KEY `index_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1100522 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_stats`
--

LOCK TABLES `ldp_stats` WRITE;
/*!40000 ALTER TABLE `ldp_stats` DISABLE KEYS */;
INSERT INTO `ldp_stats` VALUES (1100519,'每分钟_次数统计',100288,11108,'<stat-item  title=\"每分钟_次数统计\" stat=\"count()\"  />','1-minute',1209600,1,NULL,10015,0,'测试数据组','2024-03-17 13:15:06','2024-03-17 13:15:07','2024-03-17 13:15:06','iEPMNSBufOC4beMrleFCVD9yOoIMVv3q'),(1100520,'每天_次数统计',100288,11108,'<stat-item  title=\"每天_次数统计\" stat=\"count()\" />','1-day',1209600,1,NULL,10015,0,'每天_次数统计！','2024-03-17 13:15:43','2024-03-17 13:15:42','2024-03-17 13:15:43','Waxd5mC11CK2GFRCxJk4pnmUHLyZHBlp'),(1100521,'每天_各省份_总次数',100288,11108,'<stat-item  title=\"每天_各省份_总次数\" stat=\"count()\" dimens=\"province\"  />','1-day',1209600,1,NULL,10015,0,'sssss','2024-03-17 14:03:22','2024-03-17 14:03:22','2024-03-17 14:03:22','Qml4maqTmIyj8glBt4bfwtttcIXJ0d34');
/*!40000 ALTER TABLE `ldp_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ldp_users`
--

DROP TABLE IF EXISTS `ldp_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_users` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `username` varchar(60) NOT NULL,
                             `password` varchar(100) NOT NULL,
                             `domain_id` varchar(45) DEFAULT NULL,
                             `phone` varchar(60) DEFAULT NULL,
                             `email` varchar(60) DEFAULT NULL,
                             `department_id` int DEFAULT NULL,
                             `third_id` int DEFAULT NULL,
                             `state` tinyint(1) NOT NULL,
                             `last_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`id`),
                             KEY `index_user_name` (`username`),
                             KEY `index_department_id` (`department_id`),
                             KEY `index_state` (`state`),
                             KEY `index_third_id` (`third_id`),
                             KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=110223 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_users`
--

LOCK TABLES `ldp_users` WRITE;
/*!40000 ALTER TABLE `ldp_users` DISABLE KEYS */;
INSERT INTO `ldp_users` VALUES (110222,'admin','$2a$10$1.BtLbZCz39ygkQfoK4Df.uewuUhLA.UBwIiFAb0MwloLe8vUiLtm',NULL,NULL,NULL,10221,NULL,2,'2024-03-17 13:57:53','2024-03-17 09:59:47','2024-03-17 13:57:53');
/*!40000 ALTER TABLE `ldp_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-17 22:04:01
