CREATE DATABASE  IF NOT EXISTS `cluster_${ldp_lighthouse_cluster_id}_ldp_mysqldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cluster_${ldp_lighthouse_cluster_id}_ldp_mysqldb`;
-- MySQL dump 10.13  Distrib 8.0.30, for macos12 (x86_64)
--
-- Host: 10.206.6.43    Database: cluster_a90dee1a_ldp_mysqldb
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
                                  `title` varchar(50) DEFAULT NULL,
                                  `level` int NOT NULL,
                                  `components_type` int NOT NULL,
                                  `private_flag` int DEFAULT NULL,
                                  `data` mediumtext NOT NULL,
                                  `user_id` int DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_department`
--

DROP TABLE IF EXISTS `ldp_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_department` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `name` varchar(60) NOT NULL,
                                  `level` int NOT NULL,
                                  `pid` int NOT NULL,
                                  `create_time` datetime NOT NULL,
                                  `update_time` datetime NOT NULL,
                                  `fullpath` varchar(60) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `index_pid` (`pid`),
                                  KEY `index_create_time` (`create_time`),
                                  KEY `index_level` (`level`)
) ENGINE=InnoDB AUTO_INCREMENT=10054 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_limited_records`
--

DROP TABLE IF EXISTS `ldp_limited_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_limited_records` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `relation_id` int DEFAULT NULL,
                                       `relation_type` int DEFAULT NULL,
                                       `strategy` varchar(50) DEFAULT NULL,
                                       `start_time` datetime DEFAULT NULL,
                                       `end_time` datetime DEFAULT NULL,
                                       `create_time` datetime DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100118 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_meta_table`
--

DROP TABLE IF EXISTS `ldp_meta_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_meta_table` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `meta_name` varchar(100) DEFAULT NULL,
                                  `type` int DEFAULT NULL,
                                  `state` int DEFAULT NULL,
                                  `template` varchar(3000) DEFAULT NULL,
                                  `record_size` int DEFAULT NULL,
                                  `content_size` bigint NOT NULL,
                                  `desc` varchar(100) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `index_meta_name` (`meta_name`),
                                  KEY `index_state` (`state`),
                                  KEY `index_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=10014 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_order`
--

DROP TABLE IF EXISTS `ldp_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_order` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `user_id` int NOT NULL,
                             `order_type` int NOT NULL,
                             `privilege_kid` int NOT NULL,
                             `privilege_type` int NOT NULL,
                             `approve_user` int DEFAULT NULL,
                             `state` int NOT NULL,
                             `hash` varchar(100) NOT NULL,
                             `params` varchar(300) DEFAULT NULL,
                             `desc` varchar(200) DEFAULT NULL,
                             `create_time` datetime DEFAULT NULL,
                             `process_time` datetime DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `index_hash` (`hash`),
                             KEY `index_privilege_type` (`privilege_type`),
                             KEY `index_state` (`state`)
) ENGINE=InnoDB AUTO_INCREMENT=100150 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_privilege`
--

DROP TABLE IF EXISTS `ldp_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_privilege` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `relation_a` int NOT NULL,
                                 `relation_b` int NOT NULL,
                                 `privilege_type` int DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `unique_privilege` (`relation_a`,`relation_b`,`privilege_type`),
                                 KEY `index_privilege_type` (`privilege_type`),
                                 KEY `index_relationa` (`relation_a`,`privilege_type`),
                                 KEY `index_relationb` (`relation_b`,`privilege_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100320 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_relations`
--

DROP TABLE IF EXISTS `ldp_relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_relations` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `relation_a` int NOT NULL,
                                 `relation_b` int NOT NULL,
                                 `relation_type` int NOT NULL,
                                 `create_time` datetime NOT NULL,
                                 `hash` varchar(100) NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `index_relationa_relationb` (`relation_a`,`relation_b`,`relation_type`),
                                 KEY `index_relationa` (`relation_a`,`relation_type`),
                                 KEY `index_relationb` (`relation_b`,`relation_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100078 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_stat_group`
--

DROP TABLE IF EXISTS `ldp_stat_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stat_group` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `token` varchar(30) NOT NULL,
                                  `project_id` varchar(100) NOT NULL,
                                  `debug_mode` int DEFAULT NULL,
                                  `columns` varchar(3000) DEFAULT NULL,
                                  `remark` varchar(800) DEFAULT NULL,
                                  `stat_type` int NOT NULL,
                                  `secret_key` varchar(60) NOT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  `create_user` varchar(45) DEFAULT NULL,
                                  `state` int NOT NULL,
                                  `limited_threshold` varchar(200) DEFAULT NULL,
                                  `debug_params` varchar(500) DEFAULT NULL,
                                  `refresh_time` datetime NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `index_token` (`token`),
                                  KEY `index_project_id` (`project_id`),
                                  KEY `index_stat_type` (`stat_type`),
                                  KEY `index_create_time` (`create_time`),
                                  KEY `index_create_user` (`create_user`),
                                  KEY `index_refresh_time` (`refresh_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100156 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_stat_item`
--

DROP TABLE IF EXISTS `ldp_stat_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stat_item` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `title` varchar(60) NOT NULL,
                                 `group_id` int NOT NULL,
                                 `project_id` int DEFAULT NULL,
                                 `template` varchar(500) NOT NULL,
                                 `time_param` varchar(50) DEFAULT NULL,
                                 `state` int DEFAULT NULL,
                                 `filter_config` varchar(500) DEFAULT NULL,
                                 `data_volume` int DEFAULT NULL,
                                 `data_expire` bigint NOT NULL,
                                 `res_meta` int DEFAULT NULL,
                                 `sequence_flag` tinyint NOT NULL,
                                 `display_type` int DEFAULT NULL,
                                 `data_version` int DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 `create_user` int NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `index_state` (`state`),
                                 KEY `index_group_id` (`group_id`),
                                 KEY `index_project_id` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1100465 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_stat_project`
--

DROP TABLE IF EXISTS `ldp_stat_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stat_project` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `name` varchar(60) NOT NULL,
                                    `department_id` varchar(100) NOT NULL,
                                    `private_flag` int NOT NULL,
                                    `user_id` int NOT NULL,
                                    `desc` varchar(300) DEFAULT NULL,
                                    `create_time` datetime NOT NULL,
                                    `update_time` datetime NOT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `index_user_id` (`user_id`),
                                    KEY `index_department_id` (`department_id`),
                                    KEY `index_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11037 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_stat_sitebind`
--

DROP TABLE IF EXISTS `ldp_stat_sitebind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stat_sitebind` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `name` varchar(60) DEFAULT NULL,
                                     `node_id` int NOT NULL,
                                     `site_id` int DEFAULT NULL,
                                     `element_id` int DEFAULT NULL,
                                     `element_type` int DEFAULT NULL,
                                     `state` int DEFAULT NULL,
                                     `create_time` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10019 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_stat_sitemap`
--

DROP TABLE IF EXISTS `ldp_stat_sitemap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_stat_sitemap` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `name` varchar(60) DEFAULT NULL,
                                    `config` varchar(3000) DEFAULT NULL,
                                    `star` int NOT NULL,
                                    `user_id` int NOT NULL,
                                    `desc` varchar(100) DEFAULT NULL,
                                    `create_time` datetime DEFAULT NULL,
                                    `update_time` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10019 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_sys_env`
--

DROP TABLE IF EXISTS `ldp_sys_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_sys_env` (
                               `param` varchar(100) NOT NULL,
                               `value` text,
                               PRIMARY KEY (`param`),
                               KEY `index_param` (`param`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_tasks_history`
--

DROP TABLE IF EXISTS `ldp_tasks_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_tasks_history` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `task_type` int NOT NULL,
                                     `state` int NOT NULL,
                                     `batch` varchar(45) DEFAULT NULL,
                                     `task_params` varchar(1000) DEFAULT NULL,
                                     `task_result` varchar(1000) DEFAULT NULL,
                                     `start_time` datetime NOT NULL,
                                     `end_time` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `index_task_type` (`task_type`)
) ENGINE=InnoDB AUTO_INCREMENT=28361 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ldp_user`
--

DROP TABLE IF EXISTS `ldp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ldp_user` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `user_name` varchar(30) NOT NULL,
                            `password` varchar(60) NOT NULL,
                            `phone` varchar(30) DEFAULT NULL,
                            `email` varchar(120) DEFAULT NULL,
                            `create_time` datetime NOT NULL,
                            `last_time` datetime NOT NULL,
                            `department_id` int DEFAULT NULL,
                            `state` int NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `index_user_name` (`user_name`),
                            KEY `index_department_id` (`department_id`),
                            KEY `index_user_password` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=110137 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06 21:02:14
