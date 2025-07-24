USE `cluster_${ldp_lighthouse_cluster_id}_ldp_cmdb`;

ALTER TABLE ldp_groups MODIFY COLUMN columns TEXT NOT NULL;

CREATE TABLE IF NOT EXISTS `ldp_alarms` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `title` varchar(60) NOT NULL,
                              `unique_code` varchar(60) NOT NULL,
                              `divide` tinyint DEFAULT NULL,
                              `resource_id` int NOT NULL,
                              `resource_type` tinyint DEFAULT NULL,
                              `state` tinyint NOT NULL,
                              `match` tinyint NOT NULL,
                              `conditions` mediumtext NOT NULL,
                              `template_id` int NOT NULL,
                              `recover` tinyint NOT NULL,
                              `silent` int NOT NULL,
                              `delay` int NOT NULL,
                              `desc` varchar(500) NOT NULL,
                              `dimens` mediumtext,
                              `extend` varchar(3000) DEFAULT NULL,
                              `create_time` timestamp NOT NULL,
                              `update_time` timestamp NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `index_title` (`title`),
                              KEY `index_uniqueCode` (`unique_code`),
                              KEY `index_relationa` (`resource_id`,`resource_type`),
                              KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `ldp_alarm_templates` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `title` varchar(80) NOT NULL,
                                       `config` mediumtext NOT NULL,
                                       `create_user` int NOT NULL,
                                       `desc` varchar(500) DEFAULT NULL,
                                       `user_ids` json DEFAULT NULL,
                                       `department_ids` json DEFAULT NULL,
                                       `create_time` timestamp NOT NULL,
                                       `update_time` timestamp NOT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `index_title` (`title`),
                                       KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `ldp_notifications` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `resource_id` int DEFAULT NULL,
                                     `resource_type` tinyint DEFAULT NULL,
                                     `content` varchar(1000) NOT NULL,
                                     `state` tinyint NOT NULL,
                                     `user_ids` json DEFAULT NULL,
                                     `department_ids` json DEFAULT NULL,
                                     `notification_type` tinyint NOT NULL,
                                     `p1` varchar(45) DEFAULT NULL,
                                     `p2` varchar(45) DEFAULT NULL,
                                     `p3` varchar(45) DEFAULT NULL,
                                     `create_time` timestamp NOT NULL,
                                     `update_time` timestamp NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `index_relationa` (`resource_id`,`resource_type`),
                                     KEY `index_user_ids` ((CAST(user_ids AS UNSIGNED ARRAY))),
                                     KEY `index_department_ids` ((CAST(department_ids AS UNSIGNED ARRAY))),
                                     KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1001011 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `ldp_views` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `title` varchar(100) NOT NULL,
                                `user_id` int NOT NULL,
                                `state` tinyint(1) NOT NULL,
                                `private_type` tinyint(1) NOT NULL,
                                `version` int NOT NULL,
                                `config` mediumtext,
                                `desc` varchar(500) NOT NULL,
                                `create_time` timestamp NOT NULL,
                                `update_time` timestamp NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `index_state` (`state`),
                                KEY `index_title` (`title`),
                                KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1100023 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `ldp_rollbacks` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `user_id` int DEFAULT NULL,
                               `resource_id` int NOT NULL,
                               `data_type` int NOT NULL,
                               `config` mediumtext NOT NULL,
                               `version` int DEFAULT NULL,
                               `create_time` datetime NOT NULL,
                               `desc` varchar(500) DEFAULT NULL,
                                `state` tinyint NOT NULL,
                                `extend` varchar(500) DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `index_resource` (`resource_id`,`data_type`),
                                KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1100347 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ldp_callers` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(45) NOT NULL,
                               `desc` varchar(500) DEFAULT NULL,
                               `state` tinyint(1) NOT NULL,
                               `department_id` int NOT NULL DEFAULT '0',
                               `secret_key` varchar(80) NOT NULL,
                               `create_time` timestamp NOT NULL,
                               `update_time` timestamp NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `index_name` (`name`),
                               KEY `index_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=101001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `ldp_links` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `short_code` varchar(32) NOT NULL,
                             `full_url` varchar(500) DEFAULT NULL,
                             `resource_id` int NOT NULL,
                             `resource_type` tinyint NOT NULL,
                             `link_type` tinyint NOT NULL,
                             `params` text,
                             `state` tinyint NOT NULL DEFAULT '0',
                             `caller_id` int NOT NULL,
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `expire_time` timestamp NULL DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `short_code` (`short_code`),
                             KEY `idx_resource` (`resource_id`,`resource_type`),
                             KEY `idx_expire` (`expire_time`)
) ENGINE=InnoDB AUTO_INCREMENT=10009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;