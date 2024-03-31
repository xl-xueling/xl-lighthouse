CREATE DATABASE  IF NOT EXISTS `ldp_mysqldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ldp_mysqldb`;
-- MySQL dump 10.13  Distrib 8.0.30, for macos12 (x86_64)
--
-- Host: 10.206.6.17    Database: ldp_mysqldb
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
) ENGINE=InnoDB AUTO_INCREMENT=1049 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_components`
--

LOCK TABLES `ldp_components` WRITE;
/*!40000 ALTER TABLE `ldp_components` DISABLE KEYS */;
INSERT INTO `ldp_components` VALUES (1046,'省份筛选框',5,0,'[{\"label\":\"北京\",\"value\":\"1\"},{\"label\":\"上海\",\"value\":\"2\"},{\"label\":\"河北\",\"value\":\"3\"},{\"label\":\"山东\",\"value\":\"4\"}]',110223,'2024-03-25 02:31:25','2024-03-30 23:42:48'),(1047,'地区级联选择框',5,0,'[{\"label\":\"北京\",\"value\":\"1\",\"children\":[{\"label\":\"朝阳\",\"value\":\"11\"},{\"label\":\"海淀\",\"value\":\"12\"}]},{\"label\":\"上海\",\"value\":\"2\",\"children\":[{\"label\":\"浦东\",\"value\":\"21\"},{\"label\":\"静安\",\"value\":\"22\"}]}]',110226,'2024-03-27 01:10:07','2024-03-27 03:07:54'),(1048,'省市级联筛选框',5,0,'[{\"label\":\"北京\",\"value\":\"1\",\"children\":[{\"label\":\"东城区\",\"value\":\"11\"},{\"label\":\"西城区\",\"value\":\"12\"}]},{\"label\":\"上海\",\"value\":\"2\",\"children\":[{\"label\":\"徐汇区\",\"value\":\"21\"},{\"label\":\"宝山区\",\"value\":\"22\"}]},{\"label\":\"山东\",\"value\":\"3\",\"children\":[{\"label\":\"青岛\",\"value\":\"31\"},{\"label\":\"济南\",\"value\":\"32\"}]},{\"label\":\"浙江\",\"value\":\"4\",\"children\":[{\"label\":\"杭州\",\"value\":\"41\"},{\"label\":\"宁波\",\"value\":\"42\"}]}]',110223,'2024-03-30 23:40:59','2024-03-30 23:40:59');
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
) ENGINE=InnoDB AUTO_INCREMENT=10244 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_departments`
--

LOCK TABLES `ldp_departments` WRITE;
/*!40000 ALTER TABLE `ldp_departments` DISABLE KEYS */;
INSERT INTO `ldp_departments` VALUES (10222,'技术研发部',0,NULL,NULL,'2024-03-25 01:36:47','2024-03-25 01:36:47'),(10223,'技术研发一部',10222,NULL,NULL,'2024-03-26 09:00:43','2024-03-26 09:00:43'),(10224,'技术研发二部',10222,NULL,NULL,'2024-03-26 09:01:09','2024-03-26 09:01:09'),(10225,'产品体验部',0,NULL,NULL,'2024-03-27 04:12:29','2024-03-27 04:12:29'),(10226,'产品一部',10225,NULL,NULL,'2024-03-27 04:12:54','2024-03-27 04:12:54'),(10227,'产品二部',10225,NULL,NULL,'2024-03-27 04:12:57','2024-03-27 04:12:57'),(10230,'运营推广部',0,NULL,NULL,'2024-03-27 04:15:38','2024-03-27 04:15:38'),(10231,'运营一部',10230,NULL,NULL,'2024-03-27 04:16:01','2024-03-27 04:16:01'),(10232,'运营二部',10230,NULL,NULL,'2024-03-27 04:16:04','2024-03-27 04:16:04'),(10236,'外卖事业部',0,NULL,NULL,'2024-03-27 04:18:14','2024-03-27 04:18:14'),(10240,'外卖营销推广部',10236,NULL,NULL,'2024-03-29 01:59:44','2024-03-29 01:59:44'),(10241,'外卖技术产品部',10236,NULL,NULL,'2024-03-29 02:00:08','2024-03-29 02:00:08'),(10243,'短视频事业部',0,NULL,NULL,'2024-03-29 02:01:29','2024-03-29 02:01:29');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_domains`
--

LOCK TABLES `ldp_domains` WRITE;
/*!40000 ALTER TABLE `ldp_domains` DISABLE KEYS */;
INSERT INTO `ldp_domains` VALUES (6,'Default-Domain-1711330606088','Gjd','2024-03-25 01:36:46','2024-03-25 01:36:46');
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
INSERT INTO `ldp_env` VALUES ('sign_key','UR6E7uAAY2WDB5C1L4fSDkSrTPkCKNyUGfk2jZ5NAkrGkS3kDnWWQEu5lTO2tKrB');
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
  `data_version` int NOT NULL DEFAULT '0',
  `debug_mode` int DEFAULT '0',
  `columns` varchar(3000) NOT NULL,
  `desc` varchar(800) NOT NULL,
  `random_id` varchar(45) NOT NULL,
  `secret_key` varchar(60) NOT NULL,
  `state` int NOT NULL,
  `extend_config` varchar(2000) DEFAULT NULL,
  `limiting_param` varchar(800) DEFAULT NULL,
  `debug_param` varchar(800) DEFAULT NULL,
  `refresh_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_token` (`token`),
  KEY `index_project_id` (`project_id`),
  KEY `index_create_time` (`create_time`),
  KEY `index_refresh_time` (`refresh_time`),
  KEY `index_state` (`state`)
) ENGINE=InnoDB AUTO_INCREMENT=100303 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_groups`
--

LOCK TABLES `ldp_groups` WRITE;
/*!40000 ALTER TABLE `ldp_groups` DISABLE KEYS */;
INSERT INTO `ldp_groups` VALUES (100297,'Gjd:feed_behavior_stat',11112,0,0,'[{\"name\":\"imei\",\"type\":\"string\",\"comment\":\"用户标识\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"用户所在省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"用户所在城市\"},{\"name\":\"recall_no\",\"type\":\"string\",\"comment\":\"召回模型标识\"},{\"name\":\"rank_no\",\"type\":\"string\",\"comment\":\"混排模型标识\"},{\"name\":\"behavior_type\",\"type\":\"string\",\"comment\":\"用户行为类型，[1:曝光,2:点击]\"},{\"name\":\"top_cate\",\"type\":\"string\",\"comment\":\"帖子一级分类\"},{\"name\":\"sec_cate\",\"type\":\"string\",\"comment\":\"帖子二级分类\"},{\"name\":\"item_id\",\"type\":\"string\",\"comment\":\"帖子ID\"}]','包含Feed信息流的用户行为日志，有帖子曝光日志和点击日志。','hAKGuQG5J66ULBFyRif3CsU6KgCGKXXb','9oyIXK9XQEiysrMBCoAvTq2pyPjMlTjznmPVXWCe',1,'{\"limitingConfig\":{}}',NULL,NULL,'2024-03-27 04:38:16','2024-03-27 04:38:16','2024-03-27 04:38:16'),(100298,'Gjd:salespage_monitor',11113,0,0,'[{\"name\":\"imei\",\"type\":\"string\",\"comment\":\"用户标识\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"城市\"}]','促销活动页面打开用户量数据监控','mElWz3tebgY1VcqBGLR1SCHvCFqTOgZd','DgJynahIcG8u35sDpBigDe9s07kd2mjbSW5oIynJ',1,'{\"limitingConfig\":{\"GROUP_MESSAGE_SIZE_LIMITING\":-1,\"STAT_RESULT_SIZE_LIMITING\":-1}}',NULL,'{\"startTime\":1711707963889,\"endTime\":1711709763889}','2024-03-29 10:56:59','2024-03-27 07:13:31','2024-03-29 10:26:04'),(100299,'Gjd:social_message_monitor',11114,0,0,'[{\"name\":\"message_id\",\"type\":\"string\",\"comment\":\"消息ID\"},{\"name\":\"userId\",\"type\":\"string\",\"comment\":\"用户ID\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"发送人所在省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"发送人所在城市\"},{\"name\":\"age\",\"type\":\"string\",\"comment\":\"发送人所处年龄段\"},{\"name\":\"message_type\",\"type\":\"string\",\"comment\":\"消息类型[1:文本消息，2:表情，3：图片，4:文件，5：红包]\"},{\"name\":\"app_version\",\"type\":\"string\",\"comment\":\"app版本\"}]','sss','F9YYWUR2IuYhg8NaHPknYfp8UA5lmBw1','QnYis8Pi4SwsfRwfKGfFbJmsEbxQtNkabT0stHl1',1,'{\"limitingConfig\":{\"GROUP_MESSAGE_SIZE_LIMITING\":30000,\"STAT_RESULT_SIZE_LIMITING\":3000}}',NULL,NULL,'2024-03-27 07:45:26','2024-03-27 07:45:26','2024-03-27 07:47:04'),(100300,'Gjd:house_price_change',11116,0,0,'[{\"name\":\"houseId\",\"type\":\"string\",\"comment\":\"房源ID\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"城市\"},{\"name\":\"district\",\"type\":\"string\",\"comment\":\"商圈ID\"},{\"name\":\"change_type\",\"type\":\"string\",\"comment\":\"类型[1:涨价，2:降价]\"},{\"name\":\"price\",\"type\":\"number\",\"comment\":\"变动金额\"},{\"name\":\"rate\",\"type\":\"number\",\"comment\":\"价格变动比率\"}]','包含二手房价格变动事件的监控数据','SZkA7qCQwa8nLgIHhfWD7gfevmrserTO','hkaPJd7Ckih0EuDnrS839OqEg5ZQX3Ryjoe8X7c7',1,'{\"limitingConfig\":{}}',NULL,NULL,'2024-03-27 08:03:50','2024-03-27 08:03:50','2024-03-27 08:03:50'),(100301,'Gjd:homepage_icon_click_stat',11118,0,0,'[{\"name\":\"user_id\",\"type\":\"string\",\"comment\":\"用户ID\"},{\"name\":\"tab_id\",\"type\":\"string\",\"comment\":\"Tab栏标识\"},{\"name\":\"icon_id\",\"type\":\"string\",\"comment\":\"用户点击ICON的标识\"}]','首页图标区域点击数据统计','588f8cMFXx28oqdsjWs3V0Bx3s6pTnLY','HH5RLkRAsPlmWt2jj5Bbv2wgaLpp3u7BlvNkPb8d',1,'{\"limitingConfig\":{}}',NULL,NULL,'2024-03-30 08:23:28','2024-03-30 08:23:28','2024-03-30 08:23:28'),(100302,'Gjd:order_stat',11115,0,0,'[{\"name\":\"userId\",\"type\":\"string\",\"comment\":\"用户ID\"},{\"name\":\"orderId\",\"type\":\"string\",\"comment\":\"订单ID\"},{\"name\":\"province\",\"type\":\"string\",\"comment\":\"用户所在省份\"},{\"name\":\"city\",\"type\":\"string\",\"comment\":\"用户所在城市\"},{\"name\":\"dealerId\",\"type\":\"string\",\"comment\":\"商户id\"},{\"name\":\"amount\",\"type\":\"number\",\"comment\":\"订单金额\"}]','订单数据统计组','diVpUfbvXo4WOtTsOq59Movoy78OetMd','mz0pWxEeyrRfIqFEhbRNJY0fMrwNClSU7U5vSSWL',1,'{\"limitingConfig\":{\"GROUP_MESSAGE_SIZE_LIMITING\":-1,\"STAT_RESULT_SIZE_LIMITING\":-1}}',NULL,'{\"startTime\":1711806920727,\"endTime\":1711808720727}','2024-03-30 23:41:22','2024-03-30 09:59:12','2024-03-30 13:55:21');
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
) ENGINE=InnoDB AUTO_INCREMENT=10064 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_metas`
--

LOCK TABLES `ldp_metas` WRITE;
/*!40000 ALTER TABLE `ldp_metas` DISABLE KEYS */;
INSERT INTO `ldp_metas` VALUES (10016,'ldp_stat_1711333262830',3,1,NULL,0,0,NULL,'2024-03-25 02:21:05','2024-03-25 02:21:05'),(10017,'ldp_stat_1711333944971',3,1,NULL,0,0,NULL,'2024-03-25 02:32:25','2024-03-25 02:32:25'),(10018,'ldp_stat_1711433414024',3,1,NULL,0,0,NULL,'2024-03-26 06:10:14','2024-03-26 06:10:14'),(10019,'ldp_stat_1711448620163',3,1,NULL,0,0,NULL,'2024-03-26 10:23:40','2024-03-26 10:23:40'),(10020,'ldp_stat_1711501914916',3,1,NULL,0,0,NULL,'2024-03-27 01:11:55','2024-03-27 01:11:55'),(10021,'ldp_stat_1711506321495',3,1,NULL,0,0,NULL,'2024-03-27 02:25:21','2024-03-27 02:25:21'),(10022,'ldp_stat_1711514368395',3,1,NULL,0,0,NULL,'2024-03-27 04:39:28','2024-03-27 04:39:28'),(10023,'ldp_stat_1711514412734',3,1,NULL,0,0,NULL,'2024-03-27 04:40:13','2024-03-27 04:40:13'),(10024,'ldp_stat_1711514553741',3,1,NULL,0,0,NULL,'2024-03-27 04:42:34','2024-03-27 04:42:34'),(10025,'ldp_stat_1711514596432',3,1,NULL,0,0,NULL,'2024-03-27 04:43:16','2024-03-27 04:43:16'),(10026,'ldp_stat_1711514717583',3,1,NULL,0,0,NULL,'2024-03-27 04:45:18','2024-03-27 04:45:18'),(10027,'ldp_stat_1711515979158',3,1,NULL,0,0,NULL,'2024-03-27 05:06:19','2024-03-27 05:06:19'),(10028,'ldp_stat_1711516103743',3,1,NULL,0,0,NULL,'2024-03-27 05:08:24','2024-03-27 05:08:24'),(10029,'ldp_stat_1711516193792',3,1,NULL,0,0,NULL,'2024-03-27 05:09:54','2024-03-27 05:09:54'),(10030,'ldp_stat_1711519963551',3,1,NULL,0,0,NULL,'2024-03-27 06:12:44','2024-03-27 06:12:44'),(10031,'ldp_stat_1711523663496',3,1,NULL,0,0,NULL,'2024-03-27 07:14:23','2024-03-27 07:14:23'),(10032,'ldp_stat_1711523702641',3,1,NULL,0,0,NULL,'2024-03-27 07:15:03','2024-03-27 07:15:03'),(10033,'ldp_stat_1711525570734',3,1,NULL,0,0,NULL,'2024-03-27 07:46:11','2024-03-27 07:46:11'),(10034,'ldp_stat_1711525683772',3,1,NULL,0,0,NULL,'2024-03-27 07:48:04','2024-03-27 07:48:04'),(10035,'ldp_stat_1711525740714',3,1,NULL,0,0,NULL,'2024-03-27 07:49:01','2024-03-27 07:49:01'),(10036,'ldp_stat_1711526768300',3,1,NULL,0,0,NULL,'2024-03-27 08:06:08','2024-03-27 08:06:08'),(10037,'ldp_stat_1711527046345',3,1,NULL,0,0,NULL,'2024-03-27 08:10:46','2024-03-27 08:10:46'),(10038,'ldp_stat_1711527165860',3,1,NULL,0,0,NULL,'2024-03-27 08:12:46','2024-03-27 08:12:46'),(10039,'ldp_stat_1711527246161',3,1,NULL,0,0,NULL,'2024-03-27 08:14:06','2024-03-27 08:14:06'),(10040,'ldp_stat_1711527297870',3,1,NULL,0,0,NULL,'2024-03-27 08:14:58','2024-03-27 08:14:58'),(10041,'ldp_stat_1711787059582',3,1,NULL,0,0,NULL,'2024-03-30 08:24:20','2024-03-30 08:24:20'),(10042,'ldp_stat_1711787104171',3,1,NULL,0,0,NULL,'2024-03-30 08:25:04','2024-03-30 08:25:04'),(10043,'ldp_stat_1711787137382',3,1,NULL,0,0,NULL,'2024-03-30 08:25:37','2024-03-30 08:25:37'),(10044,'ldp_stat_1711787164724',3,1,NULL,0,0,NULL,'2024-03-30 08:26:05','2024-03-30 08:26:05'),(10045,'ldp_stat_1711787230342',3,1,NULL,0,0,NULL,'2024-03-30 08:27:10','2024-03-30 08:27:10'),(10046,'ldp_stat_1711787285083',3,1,NULL,0,0,NULL,'2024-03-30 08:28:05','2024-03-30 08:28:05'),(10047,'ldp_stat_1711787346257',3,1,NULL,0,0,NULL,'2024-03-30 08:29:06','2024-03-30 08:29:06'),(10048,'ldp_stat_1711787380746',3,1,NULL,0,0,NULL,'2024-03-30 08:29:41','2024-03-30 08:29:41'),(10049,'ldp_stat_1711787473454',3,1,NULL,0,0,NULL,'2024-03-30 08:31:13','2024-03-30 08:31:13'),(10050,'ldp_stat_1711787525246',3,1,NULL,0,0,NULL,'2024-03-30 08:32:05','2024-03-30 08:32:05'),(10051,'ldp_stat_1711792824363',3,1,NULL,0,0,NULL,'2024-03-30 10:00:24','2024-03-30 10:00:24'),(10052,'ldp_stat_1711792866284',3,1,NULL,0,0,NULL,'2024-03-30 10:01:06','2024-03-30 10:01:06'),(10053,'ldp_stat_1711792911118',3,1,NULL,0,0,NULL,'2024-03-30 10:01:51','2024-03-30 10:01:51'),(10054,'ldp_stat_1711792948923',3,1,NULL,0,0,NULL,'2024-03-30 10:02:29','2024-03-30 10:02:29'),(10055,'ldp_stat_1711792982076',3,1,NULL,0,0,NULL,'2024-03-30 10:03:02','2024-03-30 10:03:02'),(10056,'ldp_stat_1711793023615',3,1,NULL,0,0,NULL,'2024-03-30 10:03:44','2024-03-30 10:03:44'),(10057,'ldp_stat_1711793186770',3,1,NULL,0,0,NULL,'2024-03-30 10:06:27','2024-03-30 10:06:27'),(10058,'ldp_stat_1711793276029',3,1,NULL,0,0,NULL,'2024-03-30 10:07:56','2024-03-30 10:07:56'),(10059,'ldp_stat_1711793334321',3,1,NULL,0,0,NULL,'2024-03-30 10:08:54','2024-03-30 10:08:54'),(10060,'ldp_stat_1711793477798',3,1,NULL,0,0,NULL,'2024-03-30 10:11:18','2024-03-30 10:11:18'),(10061,'ldp_stat_1711793539558',3,1,NULL,0,0,NULL,'2024-03-30 10:12:20','2024-03-30 10:12:20'),(10062,'ldp_stat_1711793596467',3,1,NULL,0,0,NULL,'2024-03-30 10:13:16','2024-03-30 10:13:16'),(10063,'ldp_stat_1711793672332',3,1,NULL,0,0,NULL,'2024-03-30 10:14:32','2024-03-30 10:14:32');
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
  `create_user` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_metricsets`
--

LOCK TABLES `ldp_metricsets` WRITE;
/*!40000 ALTER TABLE `ldp_metricsets` DISABLE KEYS */;
INSERT INTO `ldp_metricsets` VALUES (41,'促销活动指标集',0,'促销活动指标集',NULL,110223,'2024-03-27 07:50:15','2024-03-27 07:50:15'),(42,'电商订单指标集',0,'包含电商订单相关数据指标。',NULL,110223,'2024-03-30 07:30:45','2024-03-30 07:30:45');
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
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_order_details`
--

LOCK TABLES `ldp_order_details` WRITE;
/*!40000 ALTER TABLE `ldp_order_details` DISABLE KEYS */;
INSERT INTO `ldp_order_details` VALUES (64,100227,110223,3,3,829,NULL,'2024-03-25 07:45:43','2024-03-25 07:57:33'),(65,100228,110223,3,3,829,NULL,'2024-03-25 08:23:13','2024-03-25 08:24:15'),(66,100229,110223,3,2,829,NULL,'2024-03-25 08:48:40','2024-03-25 09:03:32'),(67,100230,110226,9,5,835,NULL,'2024-03-26 06:02:03','2024-03-26 06:05:04'),(68,100231,110223,9,2,835,'已同意！','2024-03-26 06:05:41','2024-03-26 06:06:32'),(69,100232,110223,9,2,863,'同意。。','2024-03-26 10:29:17','2024-03-26 10:30:14'),(70,100233,110227,3,1,829,NULL,'2024-03-27 03:30:02',NULL),(71,100234,110223,3,2,829,NULL,'2024-03-27 03:41:46','2024-03-27 03:43:50'),(72,100235,110223,3,2,829,NULL,'2024-03-27 03:42:44','2024-03-27 03:43:44'),(73,100236,110223,3,2,829,NULL,'2024-03-27 03:43:09','2024-03-27 03:43:37');
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
) ENGINE=InnoDB AUTO_INCREMENT=100237 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_orders`
--

LOCK TABLES `ldp_orders` WRITE;
/*!40000 ALTER TABLE `ldp_orders` DISABLE KEYS */;
INSERT INTO `ldp_orders` VALUES (100227,110224,6,3,'[829]',NULL,'190f33c64601f06f1e9175defdafe181',0,NULL,'2024-03-25 07:45:43','2024-03-25 07:57:33'),(100228,110225,6,3,'[829]',NULL,'190f33c64601f06f1e9175defdafe181',0,NULL,'2024-03-25 08:23:13','2024-03-25 08:24:15'),(100229,110226,6,2,'[829]',NULL,'190f33c64601f06f1e9175defdafe181',0,NULL,'2024-03-25 08:48:40','2024-03-25 09:03:32'),(100230,110226,1,4,'[835]','{\"projectId\":11109}','b11e9a5b6ec84b8a1832e946526d95f1',0,'申请该工程的访问权限！','2024-03-26 06:02:03','2024-03-26 06:05:04'),(100231,110226,1,2,'[835]','{\"projectId\":11109}','b11e9a5b6ec84b8a1832e946526d95f1',0,'申请工程访问权限！','2024-03-26 06:05:41','2024-03-26 06:06:32'),(100232,110226,2,2,'[863]','{\"statId\":1100555}','1d5dab3a8d8560b55c627e114c8867f1',0,'申请统计项权限！','2024-03-26 10:29:17','2024-03-26 10:30:14'),(100233,110227,6,0,'[829]',NULL,'190f33c64601f06f1e9175defdafe181',829,NULL,'2024-03-27 03:30:02','2024-03-27 03:30:02'),(100234,110233,6,2,'[829]',NULL,'16373eda2073ca3dc1caf528145dc392',0,NULL,'2024-03-27 03:41:46','2024-03-27 03:43:50'),(100235,110234,6,2,'[829]',NULL,'ed0ac567aa1ab8f9b7d7c6779aff99fe',0,NULL,'2024-03-27 03:42:44','2024-03-27 03:43:44'),(100236,110235,6,2,'[829]',NULL,'3fc5fd5017326d153d23ab65332e0ec0',0,NULL,'2024-03-27 03:43:09','2024-03-27 03:43:37');
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
) ENGINE=InnoDB AUTO_INCREMENT=100571 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_permissions`
--

LOCK TABLES `ldp_permissions` WRITE;
/*!40000 ALTER TABLE `ldp_permissions` DISABLE KEYS */;
INSERT INTO `ldp_permissions` VALUES (100542,110223,1,829,'2024-03-25 01:36:50','2024-03-25 01:36:50',NULL),(100547,110223,1,851,'2024-03-26 06:47:06','2024-03-26 06:47:06',NULL),(100548,110223,1,853,'2024-03-26 08:10:17','2024-03-26 08:10:17',NULL),(100550,10222,2,854,'2024-03-26 08:25:36','2024-03-26 08:25:36',NULL),(100552,10223,2,860,'2024-03-26 09:03:18','2024-03-26 09:03:18',NULL),(100553,10224,2,860,'2024-03-26 09:24:36','2024-03-26 09:24:36',NULL),(100554,110223,1,861,'2024-03-26 09:37:34','2024-03-26 09:37:34',NULL),(100558,110223,1,899,'2024-03-27 04:33:17','2024-03-27 04:33:17',NULL),(100559,110223,1,921,'2024-03-27 07:08:39','2024-03-27 07:08:39',NULL),(100560,110223,1,929,'2024-03-27 07:20:42','2024-03-27 07:20:42',NULL),(100561,110223,1,931,'2024-03-27 07:23:03','2024-03-27 07:23:03',NULL),(100562,110223,1,943,'2024-03-27 07:50:16','2024-03-27 07:50:16',NULL),(100563,110223,1,945,'2024-03-27 07:56:25','2024-03-27 07:56:25',NULL),(100564,110223,1,969,'2024-03-30 07:30:46','2024-03-30 07:30:46',NULL),(100565,10222,2,970,'2024-03-30 07:30:47','2024-03-30 07:30:47',NULL),(100566,10225,2,970,'2024-03-30 07:30:48','2024-03-30 07:30:48',NULL),(100567,110223,1,971,'2024-03-30 07:32:35','2024-03-30 07:32:35',NULL),(100568,10222,2,900,'2024-03-30 07:34:34','2024-03-30 07:34:34',NULL),(100569,10225,2,900,'2024-03-30 07:34:34','2024-03-30 07:34:34',NULL),(100570,110223,1,973,'2024-03-30 08:21:53','2024-03-30 08:21:53',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=11119 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_projects`
--

LOCK TABLES `ldp_projects` WRITE;
/*!40000 ALTER TABLE `ldp_projects` DISABLE KEYS */;
INSERT INTO `ldp_projects` VALUES (11112,'Feed信息流行为数据统计',10225,0,'包含新闻资讯Feed信息流的用户点击率、点击量、曝光量等数据指标！','2024-03-27 04:33:16','2024-03-27 04:33:16'),(11113,'双11大促营销页面监控',10225,0,'双11大促营销活动监控','2024-03-27 07:08:39','2024-03-27 07:08:39'),(11114,'社交私聊消息量监控',10225,0,'社交私聊消息量监控','2024-03-27 07:20:42','2024-03-27 07:20:42'),(11115,'电商订单数据监控',10236,0,'电商订单数据监控','2024-03-27 07:23:02','2024-03-27 07:23:02'),(11116,'二手房调价事件监控',10236,0,'二手房调价事件监控','2024-03-27 07:56:25','2024-03-27 07:56:25'),(11117,'首页推荐位用户行为数据统计',10225,0,'包含首页推荐栏的用户点击等数据指标！','2024-03-30 07:32:34','2024-03-30 07:32:34'),(11118,'首页ICON用户行为数据统计',10225,0,'首页ICON用户行为数据统计','2024-03-30 08:21:52','2024-03-30 08:21:52');
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
) ENGINE=InnoDB AUTO_INCREMENT=100514 DEFAULT CHARSET=utf8mb3;
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
) ENGINE=InnoDB AUTO_INCREMENT=100376 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_relations`
--

LOCK TABLES `ldp_relations` WRITE;
/*!40000 ALTER TABLE `ldp_relations` DISABLE KEYS */;
INSERT INTO `ldp_relations` VALUES (100367,36,1,11109,4,'2ebcbe6d9fcbc34ade45013559c36f30','2024-03-26 05:07:58',NULL),(100368,37,1,11109,4,'ccacd05d66d34c8e30a81b62ef9bd317','2024-03-26 06:48:03',NULL),(100369,37,1,11110,4,'845ebf30a10227e590a804cc4964a58e','2024-03-26 06:48:07',NULL),(100370,110226,2,40,7,'3a53836f9ce1470307729e95feca8480','2024-03-26 09:53:42','2024-03-26 09:53:42'),(100371,110226,2,37,7,'42f87626c794d7bfe449a837140e3128','2024-03-26 09:53:47','2024-03-26 09:53:47'),(100372,38,1,1100555,6,'5b5c6bf78991dc9c6a2092c04adedc1a','2024-03-26 10:24:16',NULL),(100373,41,1,11113,4,'167f58d886f1f895e3c077efaef5d51a','2024-03-27 07:50:31',NULL),(100374,42,1,11115,4,'371ad8c6458d1d826c2d7e9a40424deb','2024-03-30 13:51:36',NULL),(100375,110223,3,11115,4,'417089acbeed43f4ee2b0f72b04ca39f','2024-03-31 01:25:24','2024-03-31 01:25:24');
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
) ENGINE=InnoDB AUTO_INCREMENT=1025 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_roles`
--

LOCK TABLES `ldp_roles` WRITE;
/*!40000 ALTER TABLE `ldp_roles` DISABLE KEYS */;
INSERT INTO `ldp_roles` VALUES (827,1,0,NULL,'2024-03-25 01:36:44','2024-03-25 01:36:44','FULL_MANAGE_PERMISSION'),(828,2,0,NULL,'2024-03-25 01:36:44','2024-03-25 01:36:44','FULL_ACCESS_PERMISSION'),(829,3,0,827,'2024-03-25 01:36:45','2024-03-25 01:36:45','OPT_MANAGE_PERMISSION'),(830,4,0,828,'2024-03-25 01:36:46','2024-03-25 01:36:46','OPT_ACCESS_PERMISSION'),(831,5,6,827,'2024-03-25 01:36:46','2024-03-25 01:36:46','DOMAIN_MANAGE_PERMISSION(Default-Domain-1711330606088)'),(832,6,6,828,'2024-03-25 01:36:47','2024-03-25 01:36:47','DOMAIN_ACCESS_PERMISSION(Default-Domain-1711330606088)'),(833,7,10222,831,'2024-03-25 01:36:48','2024-03-26 09:00:57','DEPARTMENT_MANAGE_PERMISSION(技术研发部)'),(834,8,10222,832,'2024-03-25 01:36:48','2024-03-26 09:00:57','DEPARTMENT_ACCESS_PERMISSION(技术研发部)'),(843,15,36,831,'2024-03-26 05:07:41','2024-03-26 05:07:41','METRIC_MANAGE_PERMISSION(dwwwww)'),(844,16,36,832,'2024-03-26 05:07:41','2024-03-26 05:07:41','METRIC_ACCESS_PERMISSION(dwwwww)'),(851,15,37,831,'2024-03-26 06:47:06','2024-03-26 06:47:06','METRIC_MANAGE_PERMISSION(测试指标集公开的)'),(852,16,37,832,'2024-03-26 06:47:06','2024-03-26 06:47:06','METRIC_ACCESS_PERMISSION(测试指标集公开的)'),(853,15,38,831,'2024-03-26 08:10:17','2024-03-26 08:10:17','METRIC_MANAGE_PERMISSION(admin私有指标集)'),(854,16,38,832,'2024-03-26 08:10:17','2024-03-26 08:10:17','METRIC_ACCESS_PERMISSION(admin私有指标集)'),(855,7,10223,833,'2024-03-26 09:00:43','2024-03-26 09:01:07','DEPARTMENT_MANAGE_PERMISSION(技术研发一部)'),(856,8,10223,834,'2024-03-26 09:00:44','2024-03-26 09:01:07','DEPARTMENT_ACCESS_PERMISSION(技术研发一部)'),(857,7,10224,833,'2024-03-26 09:01:10','2024-03-26 09:01:19','DEPARTMENT_MANAGE_PERMISSION(技术研发二部)'),(858,8,10224,834,'2024-03-26 09:01:10','2024-03-26 09:01:20','DEPARTMENT_ACCESS_PERMISSION(技术研发二部)'),(859,15,39,831,'2024-03-26 09:03:16','2024-03-26 09:03:16','METRIC_MANAGE_PERMISSION(大宝的私有指标集)'),(860,16,39,832,'2024-03-26 09:03:16','2024-03-26 09:03:16','METRIC_ACCESS_PERMISSION(大宝的私有指标集)'),(861,15,40,831,'2024-03-26 09:37:34','2024-03-26 09:37:34','METRIC_MANAGE_PERMISSION(sssss)'),(862,16,40,832,'2024-03-26 09:37:34','2024-03-26 09:37:34','METRIC_ACCESS_PERMISSION(sssss)'),(873,7,10225,831,'2024-03-27 04:12:30','2024-03-27 04:12:52','DEPARTMENT_MANAGE_PERMISSION(产品体验部)'),(874,8,10225,832,'2024-03-27 04:12:30','2024-03-27 04:12:52','DEPARTMENT_ACCESS_PERMISSION(产品体验部)'),(875,7,10226,873,'2024-03-27 04:12:54','2024-03-27 04:13:06','DEPARTMENT_MANAGE_PERMISSION(产品一部)'),(876,8,10226,874,'2024-03-27 04:12:54','2024-03-27 04:13:07','DEPARTMENT_ACCESS_PERMISSION(产品一部)'),(877,7,10227,873,'2024-03-27 04:12:57','2024-03-27 04:13:14','DEPARTMENT_MANAGE_PERMISSION(产品二部)'),(878,8,10227,874,'2024-03-27 04:12:57','2024-03-27 04:13:14','DEPARTMENT_ACCESS_PERMISSION(产品二部)'),(883,7,10230,831,'2024-03-27 04:15:39','2024-03-27 04:15:58','DEPARTMENT_MANAGE_PERMISSION(运营推广部)'),(884,8,10230,832,'2024-03-27 04:15:39','2024-03-27 04:15:59','DEPARTMENT_ACCESS_PERMISSION(运营推广部)'),(885,7,10231,883,'2024-03-27 04:16:01','2024-03-27 04:16:13','DEPARTMENT_MANAGE_PERMISSION(运营一部)'),(886,8,10231,884,'2024-03-27 04:16:02','2024-03-27 04:16:13','DEPARTMENT_ACCESS_PERMISSION(运营一部)'),(887,7,10232,883,'2024-03-27 04:16:04','2024-03-27 04:16:21','DEPARTMENT_MANAGE_PERMISSION(运营二部)'),(888,8,10232,884,'2024-03-27 04:16:04','2024-03-27 04:16:21','DEPARTMENT_ACCESS_PERMISSION(运营二部)'),(895,7,10236,831,'2024-03-27 04:18:15','2024-03-29 01:59:13','DEPARTMENT_MANAGE_PERMISSION(外卖事业部)'),(896,8,10236,832,'2024-03-27 04:18:15','2024-03-29 01:59:13','DEPARTMENT_ACCESS_PERMISSION(外卖事业部)'),(899,9,11112,873,'2024-03-27 04:33:16','2024-03-29 05:56:09','PROJECT_MANAGE_PERMISSION(Feed信息流行为数据统计)'),(900,10,11112,874,'2024-03-27 04:33:16','2024-03-29 05:56:09','PROJECT_ACCESS_PERMISSION(Feed信息流行为数据统计)'),(901,11,100297,899,'2024-03-27 04:38:17','2024-03-27 04:38:17','GROUP_MANAGE_PERMISSION(Gjd:feed_behavior_stat)'),(902,12,100297,900,'2024-03-27 04:38:17','2024-03-27 04:38:17','GROUP_ACCESS_PERMISSION(Gjd:feed_behavior_stat)'),(903,13,1100558,901,'2024-03-27 04:39:29','2024-03-27 04:39:29','STAT_MANAGE_PERMISSION(每分钟_日志量)'),(904,14,1100558,902,'2024-03-27 04:39:29','2024-03-27 04:39:29','STAT_ACCESS_PERMISSION(每分钟_日志量)'),(905,13,1100559,901,'2024-03-27 04:40:13','2024-03-27 04:40:13','STAT_MANAGE_PERMISSION(每分钟_用户量)'),(906,14,1100559,902,'2024-03-27 04:40:13','2024-03-27 04:40:13','STAT_ACCESS_PERMISSION(每分钟_用户量)'),(907,13,1100560,901,'2024-03-27 04:42:34','2024-03-27 04:42:34','STAT_MANAGE_PERMISSION(每分钟_点击量)'),(908,14,1100560,902,'2024-03-27 04:42:34','2024-03-27 04:42:34','STAT_ACCESS_PERMISSION(每分钟_点击量)'),(909,13,1100561,901,'2024-03-27 04:43:17','2024-03-27 04:43:17','STAT_MANAGE_PERMISSION(每分钟_点击用户量)'),(910,14,1100561,902,'2024-03-27 04:43:17','2024-03-27 04:43:17','STAT_ACCESS_PERMISSION(每分钟_点击用户量)'),(911,13,1100562,901,'2024-03-27 04:45:18','2024-03-27 04:45:18','STAT_MANAGE_PERMISSION(每小时_各一级分类_点击率)'),(912,14,1100562,902,'2024-03-27 04:45:18','2024-03-27 04:45:18','STAT_ACCESS_PERMISSION(每小时_各一级分类_点击率)'),(915,13,1100564,901,'2024-03-27 05:08:24','2024-03-27 05:08:24','STAT_MANAGE_PERMISSION(每天_各省份_用户量)'),(916,14,1100564,902,'2024-03-27 05:08:24','2024-03-27 05:08:24','STAT_ACCESS_PERMISSION(每天_各省份_用户量)'),(917,13,1100565,901,'2024-03-27 05:09:54','2024-03-27 05:09:54','STAT_MANAGE_PERMISSION(每天_各省份_点击率)'),(918,14,1100565,902,'2024-03-27 05:09:55','2024-03-27 05:09:55','STAT_ACCESS_PERMISSION(每天_各省份_点击率)'),(919,13,1100566,901,'2024-03-27 06:12:44','2024-03-27 06:12:44','STAT_MANAGE_PERMISSION(每分钟_各省份_点击量)'),(920,14,1100566,902,'2024-03-27 06:12:44','2024-03-27 06:12:44','STAT_ACCESS_PERMISSION(每分钟_各省份_点击量)'),(921,9,11113,873,'2024-03-27 07:08:39','2024-03-29 05:56:20','PROJECT_MANAGE_PERMISSION(双11大促营销页面监控)'),(922,10,11113,874,'2024-03-27 07:08:39','2024-03-29 05:56:20','PROJECT_ACCESS_PERMISSION(双11大促营销页面监控)'),(923,11,100298,921,'2024-03-27 07:13:32','2024-03-29 10:26:04','GROUP_MANAGE_PERMISSION(Gjd:salespage_monitor)'),(924,12,100298,922,'2024-03-27 07:13:32','2024-03-29 10:26:04','GROUP_ACCESS_PERMISSION(Gjd:salespage_monitor)'),(925,13,1100567,923,'2024-03-27 07:14:24','2024-03-27 07:14:24','STAT_MANAGE_PERMISSION(每分钟_用户量)'),(926,14,1100567,924,'2024-03-27 07:14:24','2024-03-27 07:14:24','STAT_ACCESS_PERMISSION(每分钟_用户量)'),(927,13,1100568,923,'2024-03-27 07:15:03','2024-03-27 07:15:03','STAT_MANAGE_PERMISSION(每分钟_各省份_用户量)'),(928,14,1100568,924,'2024-03-27 07:15:03','2024-03-27 07:15:03','STAT_ACCESS_PERMISSION(每分钟_各省份_用户量)'),(929,9,11114,873,'2024-03-27 07:20:42','2024-03-30 09:55:55','PROJECT_MANAGE_PERMISSION(社交私聊消息量监控)'),(930,10,11114,874,'2024-03-27 07:20:42','2024-03-30 09:55:55','PROJECT_ACCESS_PERMISSION(社交私聊消息量监控)'),(931,9,11115,893,'2024-03-27 07:23:03','2024-03-27 07:23:03','PROJECT_MANAGE_PERMISSION(电商订单数据监控)'),(932,10,11115,894,'2024-03-27 07:23:03','2024-03-27 07:23:03','PROJECT_ACCESS_PERMISSION(电商订单数据监控)'),(935,11,100299,929,'2024-03-27 07:45:26','2024-03-27 07:47:05','GROUP_MANAGE_PERMISSION(Gjd:social_message_monitor)'),(936,12,100299,930,'2024-03-27 07:45:26','2024-03-27 07:47:05','GROUP_ACCESS_PERMISSION(Gjd:social_message_monitor)'),(937,13,1100569,935,'2024-03-27 07:46:11','2024-03-27 07:46:29','STAT_MANAGE_PERMISSION(null)'),(938,14,1100569,936,'2024-03-27 07:46:11','2024-03-27 07:46:30','STAT_ACCESS_PERMISSION(null)'),(939,13,1100570,935,'2024-03-27 07:48:04','2024-03-27 07:48:04','STAT_MANAGE_PERMISSION(每天_各年龄段人群_消息发送量)'),(940,14,1100570,936,'2024-03-27 07:48:04','2024-03-27 07:48:04','STAT_ACCESS_PERMISSION(每天_各年龄段人群_消息发送量)'),(941,13,1100571,935,'2024-03-27 07:49:01','2024-03-27 07:49:01','STAT_MANAGE_PERMISSION(每天_各App版本_消息发送量)'),(942,14,1100571,936,'2024-03-27 07:49:01','2024-03-27 07:49:01','STAT_ACCESS_PERMISSION(每天_各App版本_消息发送量)'),(943,15,41,831,'2024-03-27 07:50:16','2024-03-27 07:50:16','METRIC_MANAGE_PERMISSION(促销活动指标集)'),(944,16,41,832,'2024-03-27 07:50:16','2024-03-27 07:50:16','METRIC_ACCESS_PERMISSION(促销活动指标集)'),(945,9,11116,895,'2024-03-27 07:56:25','2024-03-27 07:56:44','PROJECT_MANAGE_PERMISSION(二手房价格变化事件监控)'),(946,10,11116,896,'2024-03-27 07:56:25','2024-03-27 07:56:44','PROJECT_ACCESS_PERMISSION(二手房价格变化事件监控)'),(947,11,100300,945,'2024-03-27 08:03:50','2024-03-27 08:03:50','GROUP_MANAGE_PERMISSION(Gjd:house_price_change)'),(948,12,100300,946,'2024-03-27 08:03:51','2024-03-27 08:03:51','GROUP_ACCESS_PERMISSION(Gjd:house_price_change)'),(949,13,1100572,947,'2024-03-27 08:06:09','2024-03-27 08:06:09','STAT_MANAGE_PERMISSION(每天_价格变动房源量)'),(950,14,1100572,948,'2024-03-27 08:06:09','2024-03-27 08:06:09','STAT_ACCESS_PERMISSION(每天_价格变动房源量)'),(951,13,1100573,947,'2024-03-27 08:10:47','2024-03-27 08:10:47','STAT_MANAGE_PERMISSION(每天_各变动类型_房源量)'),(952,14,1100573,948,'2024-03-27 08:10:47','2024-03-27 08:10:47','STAT_ACCESS_PERMISSION(每天_各变动类型_房源量)'),(953,13,1100574,947,'2024-03-27 08:12:46','2024-03-27 08:12:46','STAT_MANAGE_PERMISSION(每天_各变动类型_大于5%房源量)'),(954,14,1100574,948,'2024-03-27 08:12:46','2024-03-27 08:12:46','STAT_ACCESS_PERMISSION(每天_各变动类型_大于5%房源量)'),(955,13,1100575,947,'2024-03-27 08:14:07','2024-03-27 08:14:07','STAT_MANAGE_PERMISSION(每天降价超过10%的房源数量)'),(956,14,1100575,948,'2024-03-27 08:14:07','2024-03-27 08:14:07','STAT_ACCESS_PERMISSION(每天降价超过10%的房源数量)'),(957,13,1100576,947,'2024-03-27 08:14:58','2024-03-27 08:14:58','STAT_MANAGE_PERMISSION(每天_各城市_降价超过10%的房源数量)'),(958,14,1100576,948,'2024-03-27 08:14:58','2024-03-27 08:14:58','STAT_ACCESS_PERMISSION(每天_各城市_降价超过10%的房源数量)'),(961,7,10240,895,'2024-03-29 01:59:44','2024-03-29 02:00:04','DEPARTMENT_MANAGE_PERMISSION(外卖营销推广部)'),(962,8,10240,896,'2024-03-29 01:59:45','2024-03-29 02:00:05','DEPARTMENT_ACCESS_PERMISSION(外卖营销推广部)'),(963,7,10241,895,'2024-03-29 02:00:08','2024-03-29 02:00:29','DEPARTMENT_MANAGE_PERMISSION(外卖技术产品部)'),(964,8,10241,896,'2024-03-29 02:00:08','2024-03-29 02:00:29','DEPARTMENT_ACCESS_PERMISSION(外卖技术产品部)'),(967,7,10243,831,'2024-03-29 02:01:30','2024-03-29 02:01:44','DEPARTMENT_MANAGE_PERMISSION(短视频事业部)'),(968,8,10243,832,'2024-03-29 02:01:30','2024-03-29 02:01:44','DEPARTMENT_ACCESS_PERMISSION(短视频事业部)'),(969,15,42,831,'2024-03-30 07:30:46','2024-03-30 07:30:46','METRIC_MANAGE_PERMISSION(电商订单指标集)'),(970,16,42,832,'2024-03-30 07:30:46','2024-03-30 07:30:46','METRIC_ACCESS_PERMISSION(电商订单指标集)'),(971,9,11117,873,'2024-03-30 07:32:34','2024-03-30 07:32:34','PROJECT_MANAGE_PERMISSION(首页推荐位用户行为数据统计)'),(972,10,11117,874,'2024-03-30 07:32:34','2024-03-30 07:32:34','PROJECT_ACCESS_PERMISSION(首页推荐位用户行为数据统计)'),(973,9,11118,873,'2024-03-30 08:21:53','2024-03-30 08:21:53','PROJECT_MANAGE_PERMISSION(首页ICON用户行为数据统计)'),(974,10,11118,874,'2024-03-30 08:21:53','2024-03-30 08:21:53','PROJECT_ACCESS_PERMISSION(首页ICON用户行为数据统计)'),(975,11,100301,973,'2024-03-30 08:23:28','2024-03-30 08:23:28','GROUP_MANAGE_PERMISSION(Gjd:homepage_icon_click_stat)'),(976,12,100301,974,'2024-03-30 08:23:28','2024-03-30 08:23:28','GROUP_ACCESS_PERMISSION(Gjd:homepage_icon_click_stat)'),(977,13,1100577,975,'2024-03-30 08:24:20','2024-03-30 08:24:20','STAT_MANAGE_PERMISSION(每5分钟_点击量)'),(978,14,1100577,976,'2024-03-30 08:24:20','2024-03-30 08:24:20','STAT_ACCESS_PERMISSION(每5分钟_点击量)'),(979,13,1100578,975,'2024-03-30 08:25:05','2024-03-30 08:25:05','STAT_MANAGE_PERMISSION(每5分钟_各icon_点击量)'),(980,14,1100578,976,'2024-03-30 08:25:05','2024-03-30 08:25:05','STAT_ACCESS_PERMISSION(每5分钟_各icon_点击量)'),(981,13,1100579,975,'2024-03-30 08:25:38','2024-03-30 08:25:38','STAT_MANAGE_PERMISSION(每小时_点击量)'),(982,14,1100579,976,'2024-03-30 08:25:38','2024-03-30 08:25:38','STAT_ACCESS_PERMISSION(每小时_点击量)'),(983,13,1100580,975,'2024-03-30 08:26:05','2024-03-30 08:26:05','STAT_MANAGE_PERMISSION(每小时_各icon_点击量)'),(984,14,1100580,976,'2024-03-30 08:26:05','2024-03-30 08:26:05','STAT_ACCESS_PERMISSION(每小时_各icon_点击量)'),(985,13,1100581,975,'2024-03-30 08:27:11','2024-03-30 08:27:11','STAT_MANAGE_PERMISSION(每天_各Tab_总点击量)'),(986,14,1100581,976,'2024-03-30 08:27:11','2024-03-30 08:27:11','STAT_ACCESS_PERMISSION(每天_各Tab_总点击量)'),(987,13,1100582,975,'2024-03-30 08:28:06','2024-03-30 08:28:06','STAT_MANAGE_PERMISSION(每天_各icon_总点击量)'),(988,14,1100582,976,'2024-03-30 08:28:06','2024-03-30 08:28:06','STAT_ACCESS_PERMISSION(每天_各icon_总点击量)'),(989,13,1100583,975,'2024-03-30 08:29:07','2024-03-30 08:29:07','STAT_MANAGE_PERMISSION(每5分钟_点击uv)'),(990,14,1100583,976,'2024-03-30 08:29:07','2024-03-30 08:29:07','STAT_ACCESS_PERMISSION(每5分钟_点击uv)'),(991,13,1100584,975,'2024-03-30 08:29:41','2024-03-30 08:29:41','STAT_MANAGE_PERMISSION(每小时_点击uv)'),(992,14,1100584,976,'2024-03-30 08:29:41','2024-03-30 08:29:41','STAT_ACCESS_PERMISSION(每小时_点击uv)'),(993,13,1100585,975,'2024-03-30 08:31:14','2024-03-30 08:31:14','STAT_MANAGE_PERMISSION(每天_总点击uv)'),(994,14,1100585,976,'2024-03-30 08:31:14','2024-03-30 08:31:14','STAT_ACCESS_PERMISSION(每天_总点击uv)'),(995,13,1100586,975,'2024-03-30 08:32:06','2024-03-30 08:32:06','STAT_MANAGE_PERMISSION(每天_各ICON_点击uv)'),(996,14,1100586,976,'2024-03-30 08:32:06','2024-03-30 08:32:06','STAT_ACCESS_PERMISSION(每天_各ICON_点击uv)'),(997,11,100302,931,'2024-03-30 09:59:12','2024-03-30 13:55:22','GROUP_MANAGE_PERMISSION(Gjd:order_stat)'),(998,12,100302,932,'2024-03-30 09:59:12','2024-03-30 13:55:22','GROUP_ACCESS_PERMISSION(Gjd:order_stat)'),(999,13,1100587,997,'2024-03-30 10:00:25','2024-03-30 10:00:25','STAT_MANAGE_PERMISSION(每10分钟_订单量)'),(1000,14,1100587,998,'2024-03-30 10:00:25','2024-03-30 10:00:25','STAT_ACCESS_PERMISSION(每10分钟_订单量)'),(1001,13,1100588,997,'2024-03-30 10:01:07','2024-03-30 10:01:07','STAT_MANAGE_PERMISSION(每10分钟_各商户_订单量)'),(1002,14,1100588,998,'2024-03-30 10:01:07','2024-03-30 10:01:07','STAT_ACCESS_PERMISSION(每10分钟_各商户_订单量)'),(1003,13,1100589,997,'2024-03-30 10:01:52','2024-03-30 10:01:52','STAT_MANAGE_PERMISSION(每10分钟_各省份_订单量)'),(1004,14,1100589,998,'2024-03-30 10:01:52','2024-03-30 10:01:52','STAT_ACCESS_PERMISSION(每10分钟_各省份_订单量)'),(1005,13,1100590,997,'2024-03-30 10:02:29','2024-03-30 10:02:29','STAT_MANAGE_PERMISSION(每10分钟_各城市_订单量)'),(1006,14,1100590,998,'2024-03-30 10:02:30','2024-03-30 10:02:30','STAT_ACCESS_PERMISSION(每10分钟_各城市_订单量)'),(1007,13,1100591,997,'2024-03-30 10:03:03','2024-03-30 10:03:03','STAT_MANAGE_PERMISSION(每天_订单量)'),(1008,14,1100591,998,'2024-03-30 10:03:03','2024-03-30 10:03:03','STAT_ACCESS_PERMISSION(每天_订单量)'),(1009,13,1100592,997,'2024-03-30 10:03:44','2024-03-30 10:03:44','STAT_MANAGE_PERMISSION(每天_各商户_订单量)'),(1010,14,1100592,998,'2024-03-30 10:03:44','2024-03-30 10:03:44','STAT_ACCESS_PERMISSION(每天_各商户_订单量)'),(1011,13,1100593,997,'2024-03-30 10:06:27','2024-03-30 10:06:27','STAT_MANAGE_PERMISSION(每天_各价格区间_订单量)'),(1012,14,1100593,998,'2024-03-30 10:06:27','2024-03-30 10:06:27','STAT_ACCESS_PERMISSION(每天_各价格区间_订单量)'),(1013,13,1100594,997,'2024-03-30 10:07:56','2024-03-30 10:07:56','STAT_MANAGE_PERMISSION(每10分钟_交易金额)'),(1014,14,1100594,998,'2024-03-30 10:07:57','2024-03-30 10:07:57','STAT_ACCESS_PERMISSION(每10分钟_交易金额)'),(1015,13,1100595,997,'2024-03-30 10:08:55','2024-03-30 10:08:55','STAT_MANAGE_PERMISSION(每天_各城市_交易金额)'),(1016,14,1100595,998,'2024-03-30 10:08:55','2024-03-30 10:08:55','STAT_ACCESS_PERMISSION(每天_各城市_交易金额)'),(1017,13,1100596,997,'2024-03-30 10:11:19','2024-03-30 10:11:19','STAT_MANAGE_PERMISSION(每10分钟_下单用户数)'),(1018,14,1100596,998,'2024-03-30 10:11:19','2024-03-30 10:11:19','STAT_ACCESS_PERMISSION(每10分钟_下单用户数)'),(1019,13,1100597,997,'2024-03-30 10:12:20','2024-03-30 10:12:20','STAT_MANAGE_PERMISSION(每天_下单用户数)'),(1020,14,1100597,998,'2024-03-30 10:12:20','2024-03-30 10:12:20','STAT_ACCESS_PERMISSION(每天_下单用户数)'),(1021,13,1100598,997,'2024-03-30 10:13:18','2024-03-30 10:13:59','STAT_MANAGE_PERMISSION(null)'),(1022,14,1100598,998,'2024-03-30 10:13:18','2024-03-30 10:13:59','STAT_ACCESS_PERMISSION(null)'),(1023,13,1100599,997,'2024-03-30 10:14:33','2024-03-30 10:28:09','STAT_MANAGE_PERMISSION(null)'),(1024,14,1100599,998,'2024-03-30 10:14:33','2024-03-30 10:28:09','STAT_ACCESS_PERMISSION(null)');
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
  `limiting_param` varchar(800) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `refresh_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `random_id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_state` (`state`),
  KEY `index_group_id` (`group_id`),
  KEY `index_project_id` (`project_id`),
  KEY `index_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1100600 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_stats`
--

LOCK TABLES `ldp_stats` WRITE;
/*!40000 ALTER TABLE `ldp_stats` DISABLE KEYS */;
INSERT INTO `ldp_stats` VALUES (1100558,'每分钟_日志量',100297,11112,'<stat-item  title=\"每分钟_日志量\"  stat=\"count()\" />','1-minute',1209600,1,NULL,10022,0,'每分钟_日志量',NULL,'2024-03-27 04:39:28','2024-03-27 04:39:28','2024-03-27 04:39:28','uPCSaLlaQmYYmDEs4IASsEUE47jlZWPO'),(1100559,'每分钟_用户量',100297,11112,'<stat-item  title=\"每分钟_用户量\"  stat=\"bitcount(imei)\" />','1-minute',1209600,1,NULL,10023,0,'每分钟_用户量',NULL,'2024-03-27 04:40:13','2024-03-27 04:40:13','2024-03-27 04:40:13','QCxd59FEhgYsDneFNdUkwskHOLcAmszb'),(1100560,'每分钟_点击量',100297,11112,'<stat-item  title=\"每分钟_点击量\" stat=\"count(behavior_type == \'2\')\"  />','1-minute',1209600,1,NULL,10024,0,'每分钟_点击量',NULL,'2024-03-27 04:42:34','2024-03-27 04:42:34','2024-03-27 04:42:34','cC6TjzZQOfJ8qWHbYqIXGMz9717foRRP'),(1100561,'每分钟_点击用户量',100297,11112,'<stat-item  title=\"每分钟_点击用户量\"  stat=\"bitcount(imei,behavior_type == \'2\')\" />','1-minute',1209600,1,NULL,10025,0,'每分钟_点击用户量',NULL,'2024-03-27 04:43:16','2024-03-27 04:43:16','2024-03-27 04:43:16','50S6wDRKkjjXULUSlvQDMUJSjnlWDWB4'),(1100562,'每小时_各一级分类_点击率',100297,11112,'<stat-item  title=\"每小时_各一级分类_点击率\" stat=\"count(behavior_type == \'2\')/count(behavior_type == \'1\')\" dimens=\"top_cate\"/>','1-minute',1209600,1,NULL,10026,0,'每小时_各一级分类_点击率',NULL,'2024-03-27 04:45:17','2024-03-27 04:45:17','2024-03-27 04:45:17','G47nUefT4mOC49oWo5c9F18N7HLZeoZF'),(1100564,'每天_各省份_用户量',100297,11112,'<stat-item  title=\"每天_各省份_用户量\"  stat=\"bitcount(imei)\" dimens=\"province\"/>','1-day',1209600,1,NULL,10028,0,'每天_各省份_用户量',NULL,'2024-03-27 05:08:23','2024-03-27 05:08:24','2024-03-27 05:08:23','z1aNtzxrgAcZdM6xlAS6bjqHczA5WK9t'),(1100565,'每天_各省份_点击率',100297,11112,'<stat-item  title=\"每天_各省份_点击率\"  stat=\"count(behavior_type == \'2\')/count(behavior_type==1)\" dimens=\"province\"/>','1-day',2592000,1,NULL,10029,0,'每天_各省份_点击率',NULL,'2024-03-27 05:09:54','2024-03-27 05:09:54','2024-03-27 05:09:54','0ZqDtsuIUPgLGCbZXnVz07aaC0JVNWfc'),(1100566,'每分钟_各省份_点击量',100297,11112,'<stat-item  title=\"每分钟_各省份_点击量\"  stat=\"count(behavior_type == \'2\')\" dimens=\"province\"/>','1-minute',1209600,1,NULL,10030,0,'每分钟_各省份_点击量',NULL,'2024-03-27 06:12:43','2024-03-27 06:12:44','2024-03-27 06:12:43','LsdFhyrFH9inhmjmnJimB8DIH3RVw6s8'),(1100567,'每分钟_用户量',100298,11113,'<stat-item  title=\"每分钟_用户量\" stat=\"bitcount(imei)\"  />','1-minute',1209600,1,NULL,10031,0,'每分钟_用户量',NULL,'2024-03-27 07:14:23','2024-03-27 07:14:23','2024-03-27 07:14:23','eXG9pg3vVLTvuD9zNODTulwbAxQX6Yw4'),(1100568,'每分钟_各省份_用户量',100298,11113,'<stat-item  title=\"每分钟_各省份_用户量\" stat=\"bitcount(imei)\" dimens=\"province\" />','1-minute',1209600,1,NULL,10032,0,'每分钟_各省份_用户量',NULL,'2024-03-27 07:15:02','2024-03-27 07:15:03','2024-03-27 07:15:02','7FJqA1urXLovMDFtCAMc02oY6Kuu5PCj'),(1100569,'每分钟_消息发送量',100299,11114,'<stat-item title=\"每分钟_消息发送量\" stat=\"count()\" />','1-minute',1209600,1,'{}',10033,0,'每分钟_消息量',NULL,'2024-03-27 07:46:11','2024-03-27 07:46:11','2024-03-27 07:46:29','kkKikH4pPCQzJPKIQpaqnmC4pgpaPFO2'),(1100570,'每天_各年龄段人群_消息发送量',100299,11114,'<stat-item  title=\"每天_各年龄段人群_消息发送量\" stat=\"count()\" dimens=\"age\" />','1-day',1209600,1,NULL,10034,0,'每天_各年龄段人群_消息发送量',NULL,'2024-03-27 07:48:04','2024-03-27 07:48:04','2024-03-27 07:48:04','XKTTGjqTgeDSS0Gv9tbaUNCPPzNmwDkX'),(1100571,'每天_各App版本_消息发送量',100299,11114,'<stat-item  title=\"每天_各App版本_消息发送量\" stat=\"count()\" dimens=\"app_version\" />','1-day',1209600,1,NULL,10035,0,'每天_各App版本_消息发送量',NULL,'2024-03-27 07:49:01','2024-03-27 07:49:01','2024-03-27 07:49:01','mtBNJydi8JsdZA6LT6I4XMinGVC4ixsk'),(1100572,'每天_价格变动房源量',100300,11116,'<stat-item  title=\"每天_价格变动房源量\" stat=\"bitcount(houseId)\"  />','1-day',1209600,1,NULL,10036,0,'每天_价格变动房源量',NULL,'2024-03-27 08:06:08','2024-03-27 08:06:08','2024-03-27 08:06:08','opUL2qnwtcipPf2ozArqOWZ4dEvcz64y'),(1100573,'每天_各变动类型_房源量',100300,11116,'<stat-item  title=\"每天_各变动类型_房源量\" stat=\"bitcount(houseId)\" dimens=\"change_type\" />','1-day',1209600,1,NULL,10037,0,'每天_各变动类型_房源量',NULL,'2024-03-27 08:10:46','2024-03-27 08:10:46','2024-03-27 08:10:46','OXJB3AdMA3sbekCtrBtgxRvY26ypfNse'),(1100574,'每天_各变动类型_大于5%房源量',100300,11116,'<stat-item  title=\"每天_各变动类型_大于5%房源量\" stat=\"bitcount(houseId,rate > \'0.05\')\" dimens=\"change_type\" />','1-day',1209600,1,NULL,10038,0,'每天_各变动类型_大于5%房源量',NULL,'2024-03-27 08:12:46','2024-03-27 08:12:46','2024-03-27 08:12:46','mtTLNFIkKaGUnLOLdkefNeVlBlE84jd4'),(1100575,'每天降价超过10%的房源数量',100300,11116,'<stat-item  title=\"每天降价超过10%的房源数量\" stat=\"bitcount(houseId,rate > \'0.1\',change_type == \'2\')\"  />','1-day',1209600,1,NULL,10039,0,'每天降价超过10%的房源数量',NULL,'2024-03-27 08:14:06','2024-03-27 08:14:06','2024-03-27 08:14:06','TTItZmfA1ZqCskAKgx6SivfZqtL6fdeK'),(1100576,'每天_各城市_降价超过10%的房源数量',100300,11116,'<stat-item  title=\"每天_各城市_降价超过10%的房源数量\" stat=\"bitcount(houseId,rate > \'0.1\',change_type == \'2\')\"  dimens=\"province;city\"/>','1-day',1209600,1,NULL,10040,0,'每天_各城市_降价超过10%的房源数量',NULL,'2024-03-27 08:14:58','2024-03-27 08:14:58','2024-03-27 08:14:58','gSjjZh8DMpvCsRJPZcJXZQAmWWV8HMXG'),(1100577,'每5分钟_点击量',100301,11118,'<stat-item  title=\"每5分钟_点击量\" stat=\"count()\"  />','5-minute',1209600,1,NULL,10041,0,'每5分钟_点击量',NULL,'2024-03-30 08:24:19','2024-03-30 08:24:20','2024-03-30 08:24:19','u4aHI4RsnUZXc5dtTLCcNGAXalQ6I5Zk'),(1100578,'每5分钟_各icon_点击量',100301,11118,'<stat-item  title=\"每5分钟_各icon_点击量\" stat=\"count()\" dimens=\"icon_id\" />','5-minute',1209600,1,NULL,10042,0,'每5分钟_各icon_点击量',NULL,'2024-03-30 08:25:04','2024-03-30 08:25:04','2024-03-30 08:25:04','HGq67FC2jkF9fjA7X8qcALcLWGMwHL3Y'),(1100579,'每小时_点击量',100301,11118,'<stat-item  title=\"每小时_点击量\" stat=\"count()\" />','1-hour',1209600,1,NULL,10043,0,'每小时_点击量',NULL,'2024-03-30 08:25:37','2024-03-30 08:25:37','2024-03-30 08:25:37','kYNJURmpUvo0O232zn6VsTUSWPiOubnS'),(1100580,'每小时_各icon_点击量',100301,11118,'<stat-item  title=\"每小时_各icon_点击量\" stat=\"count()\" dimens=\"icon_id\" />','1-hour',1209600,1,NULL,10044,0,'每小时_各icon_点击量',NULL,'2024-03-30 08:26:05','2024-03-30 08:26:05','2024-03-30 08:26:05','IieOfDw3J1UW7vOiOHQQy1rbNYg6xokL'),(1100581,'每天_各Tab_总点击量',100301,11118,'<stat-item  title=\"每天_各Tab_总点击量\" stat=\"count()\" dimens=\"tab_id\"  />','1-day',1209600,1,NULL,10045,0,'每天_各Tab_总点击量',NULL,'2024-03-30 08:27:10','2024-03-30 08:27:10','2024-03-30 08:27:10','TQ0p0WYRpmNkzK3vl0TGfazroLfRllz4'),(1100582,'每天_各icon_总点击量',100301,11118,'<stat-item  title=\"每天_各icon_总点击量\" stat=\"count()\" dimens=\"icon_id\" />','1-day',1209600,1,NULL,10046,0,'每天_各icon_总点击量',NULL,'2024-03-30 08:28:05','2024-03-30 08:28:05','2024-03-30 08:28:05','nZlYsDmHKHdLEwjMXZawfyqZynRJLHgk'),(1100583,'每5分钟_点击uv',100301,11118,'<stat-item  title=\"每5分钟_点击uv\" stat=\"bitcount(user_id)\"  />','5-minute',1209600,1,NULL,10047,0,'每5分钟_点击uv',NULL,'2024-03-30 08:29:06','2024-03-30 08:29:06','2024-03-30 08:29:06','7NxcrQVAiugQ2JATzSymOfhY9m6l27OW'),(1100584,'每小时_点击uv',100301,11118,'<stat-item  title=\"每小时_点击uv\" stat=\"bitcount(user_id)\"  />','1-hour',1209600,1,NULL,10048,0,'每小时_点击uv',NULL,'2024-03-30 08:29:41','2024-03-30 08:29:41','2024-03-30 08:29:41','g4fSfu11B7ca9X4MusOFc7bnE1qmLNzC'),(1100585,'每天_总点击uv',100301,11118,'<stat-item  title=\"每天_总点击uv\" stat=\"bitcount(user_id)\"   />','1-day',1209600,1,NULL,10049,0,'每天_总点击uv',NULL,'2024-03-30 08:31:13','2024-03-30 08:31:13','2024-03-30 08:31:13','6aXMqHe3ebykI8GXdGu8w19szzFsXTrM'),(1100586,'每天_各ICON_点击uv',100301,11118,'<stat-item  title=\"每天_各ICON_点击uv\" stat=\"bitcount(user_id)\" dimens=\"icon_id\"  />','1-day',1209600,1,NULL,10050,0,'每天_各ICON_点击uv',NULL,'2024-03-30 08:32:05','2024-03-30 08:32:05','2024-03-30 08:32:05','OsRM02Huk0sFivloaag6Iq3dZeEGi7Lu'),(1100587,'每10分钟_订单量',100302,11115,'<stat-item  title=\"每10分钟_订单量\" stat=\"count()\"  />','10-minute',1209600,1,NULL,10051,0,'每10分钟_订单量',NULL,'2024-03-30 10:00:24','2024-03-30 10:00:24','2024-03-30 10:00:24','o1RhMKZWClC9Vs5vWiCD9dt5tRwWN0mG'),(1100588,'每10分钟_各商户_订单量',100302,11115,'<stat-item  title=\"每10分钟_各商户_订单量\" stat=\"count()\" dimens=\"dealerId\"  />','10-minute',1209600,1,NULL,10052,0,'每10分钟_各商户_订单量',NULL,'2024-03-30 10:01:06','2024-03-30 10:01:06','2024-03-30 10:01:06','Iv7wNrLtXH8za7pd87s7kgWMei8n1ZrY'),(1100589,'每10分钟_各省份_订单量',100302,11115,'<stat-item  title=\"每10分钟_各省份_订单量\" stat=\"count()\" dimens=\"province\"  />','10-minute',1209600,1,'{\"filters\":[{\"componentType\":5,\"label\":\"省份\",\"dimens\":\"province\",\"componentId\":1046}]}',10053,0,'每10分钟_各省份_订单量',NULL,'2024-03-30 10:01:51','2024-03-30 10:01:51','2024-03-30 10:01:51','q4zAmoSgAew7h9GVIv7bkefHquxlxBZv'),(1100590,'每10分钟_各城市_订单量',100302,11115,'<stat-item  title=\"每10分钟_各城市_订单量\" stat=\"count()\" dimens=\"province;city\"  />','10-minute',1209600,1,NULL,10054,0,'每10分钟_各城市_订单量',NULL,'2024-03-30 10:02:29','2024-03-30 10:02:29','2024-03-30 10:02:29','qV581qwVhrLGKaxJ4lHXncWMwmAEkxCy'),(1100591,'每天_订单量',100302,11115,'<stat-item  title=\"每天_订单量\" stat=\"count()\"  />','1-day',1209600,1,NULL,10055,0,'每天_订单量',NULL,'2024-03-30 10:03:02','2024-03-30 10:03:02','2024-03-30 10:03:02','oHoOCpwcqm1qh0FMeGMKrRiaCCkb4vzR'),(1100592,'每天_各商户_订单量',100302,11115,'<stat-item  title=\"每天_各商户_订单量\" stat=\"count()\" dimens=\"dealerId\"  />','1-day',1209600,1,NULL,10056,0,'每天_各商户_订单量',NULL,'2024-03-30 10:03:43','2024-03-30 10:03:44','2024-03-30 10:03:43','dwLfA7B2T7XAFzeMxtfgl1iBrWGvBYyM'),(1100593,'每天_各价格区间_订单量',100302,11115,'<stat-item  title=\"每天_各价格区间_订单量\" stat=\"count()\" dimens=\"section(amount,\'50,100,200,500,1000\')\"  />','1-day',1209600,1,NULL,10057,0,'每天_各价格区间_订单量',NULL,'2024-03-30 10:06:27','2024-03-30 10:06:27','2024-03-30 10:06:27','OvXRrbP4lKjCMyBuvdJNjiP6wYM4Wz5y'),(1100594,'每10分钟_交易金额',100302,11115,'<stat-item  title=\"每10分钟_交易金额\" stat=\"sum(amount)\"  />','10-minute',1209600,1,NULL,10058,0,'每10分钟_交易金额',NULL,'2024-03-30 10:07:56','2024-03-30 10:07:56','2024-03-30 10:07:56','jo1yWjtkBhMfFyf7Hko4Au2BtcLYZ0o2'),(1100595,'每天_各城市_交易金额',100302,11115,'<stat-item  title=\"每天_各城市_交易金额\" stat=\"sum(amount)\" dimens=\"province;city\"  />','1-day',1209600,1,NULL,10059,0,'每天_各城市_交易金额',NULL,'2024-03-30 10:08:54','2024-03-30 10:08:54','2024-03-30 10:08:54','1dPtNzJBrAumPlVrrjl9xARWZum3kqPJ'),(1100596,'每10分钟_下单用户数',100302,11115,'<stat-item  title=\"每10分钟_下单用户数\" stat=\"bitcount(userId)\"  />','10-minute',1209600,1,NULL,10060,0,'每10分钟_下单用户数',NULL,'2024-03-30 10:11:18','2024-03-30 10:11:18','2024-03-30 10:11:18','KBmrOjgO5KCXo6A71rSeEUYlCv4qHHfR'),(1100597,'每天_下单用户数',100302,11115,'<stat-item  title=\"每天_下单用户数\" stat=\"bitcount(userId)\" />','1-day',1209600,1,NULL,10061,0,'每天_下单用户数',NULL,'2024-03-30 10:12:19','2024-03-30 10:12:19','2024-03-30 10:12:19','y5dwesjYKz6OfiLt3awPsjeYN0BmQhmN'),(1100598,'每天_各价格区间_下单用户数',100302,11115,'<stat-item title=\"每天_各价格区间_下单用户数\" stat=\"bitcount(userId)\" dimens=\"section(amount,\'50,100,200,500,1000\')\" />','1-day',1209600,1,'{}',10062,0,'每天_各价格区间_下单用户数',NULL,'2024-03-30 10:13:16','2024-03-30 10:13:17','2024-03-30 10:13:59','iUvf9lCT9HJZFniLUf6zfMUe6VnAyp9k'),(1100599,'每天_各省份_下单用户数',100302,11115,'<stat-item title=\"每天_各省份_下单用户数\" stat=\"bitcount(userId)\" dimens=\"province\" />','1-day',1209600,1,'{}',10063,0,'每天_各省份_下单用户数',NULL,'2024-03-30 10:14:32','2024-03-30 10:14:32','2024-03-30 10:28:09','QoutibF2zEWvlkGRc7lyzPzknEloHIq5');
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
) ENGINE=InnoDB AUTO_INCREMENT=110236 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ldp_users`
--

LOCK TABLES `ldp_users` WRITE;
/*!40000 ALTER TABLE `ldp_users` DISABLE KEYS */;
INSERT INTO `ldp_users` VALUES (110223,'admin','$2a$10$CjSV.RC1yLevtP8QKMAdNOne0TaMTT2tg0FIBcR8KkvUq7VF34OHm',NULL,'15011111111','admin@gmail.com',10225,NULL,2,'2024-03-31 06:13:49','2024-03-25 01:36:49','2024-03-31 06:13:49'),(110233,'test1','$2a$10$NE/FHhSK0pMoOfqGpSv6vu4ax1RCfbry5WBFqWDy/L5CxvVUHXsL2',NULL,NULL,'test1@gmail.com',10223,NULL,2,'2024-03-27 03:41:45','2024-03-27 03:41:45','2024-03-27 03:43:51'),(110234,'test2','$2a$10$sCJla83zWsUmXCd6Jkc/dOZEHhbwlqv0.p8TumI.WaR81WwBchnrK',NULL,NULL,'test2@gmail.com',10224,NULL,2,'2024-03-27 03:42:43','2024-03-27 03:42:43','2024-03-27 03:43:44'),(110235,'test3','$2a$10$QOj5K3di3Q4R9RLocsE4YuO3D5plsDjBYEfo7//BVHibJ6KtVsBcu',NULL,NULL,'test3@gmail.com',10224,NULL,2,'2024-03-27 04:05:56','2024-03-27 03:43:09','2024-03-27 04:05:56');
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

-- Dump completed on 2024-03-31 14:18:55
