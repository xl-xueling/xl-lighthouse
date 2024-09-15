DROP PROCEDURE IF EXISTS AddColumnsIfNotExists;

DELIMITER $$

CREATE PROCEDURE AddColumnsIfNotExists(
    IN dbName VARCHAR(64),
    IN tableName VARCHAR(64),
    IN columnsList TEXT
)
BEGIN
    DECLARE columnName VARCHAR(64);
    DECLARE columnDefinition VARCHAR(255);
    DECLARE columnExists INT DEFAULT 0;
    DECLARE position INT DEFAULT 1;
    DECLARE delimiterPos INT;
    DECLARE currentColumn TEXT;

    WHILE position <= LENGTH(columnsList) DO
        SET delimiterPos = LOCATE(',', columnsList, position);
        IF delimiterPos = 0 THEN
            SET delimiterPos = LENGTH(columnsList) + 1;
END IF;

        SET currentColumn = TRIM(SUBSTRING(columnsList, position, delimiterPos - position));
        SET columnName = TRIM(SUBSTRING_INDEX(currentColumn, ' ', 1));
        SET columnDefinition = TRIM(SUBSTRING(currentColumn, LENGTH(columnName) + 1));

SELECT COUNT(*)
INTO columnExists
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = dbName
  AND TABLE_NAME = tableName
  AND COLUMN_NAME = columnName;

IF columnExists = 0 THEN
            SET @alterStmt = CONCAT('ALTER TABLE ', dbName, '.', tableName,
                                    ' ADD COLUMN ', columnName, ' ', columnDefinition);
PREPARE stmt FROM @alterStmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END IF;
        SET position = delimiterPos + 1;
END WHILE;

END$$

DELIMITER ;

-- CALL AddColumnsIfNotExists(DATABASE(),'ldp_orders', 'sub_type INT,pid INT');

DROP PROCEDURE IF EXISTS AddColumnsIfNotExists;


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

