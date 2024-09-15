USE `cluster_${ldp_lighthouse_cluster_id}_ldp_cmdb`;

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
    ) ENGINE=InnoDB AUTO_INCREMENT=1100023 DEFAULT CHARSET=utf8mb3;

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
    ) ENGINE=InnoDB AUTO_INCREMENT=1100347 DEFAULT CHARSET=utf8mb3;