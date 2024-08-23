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

CALL AddColumnsIfNotExists('cluster_d5d83a2b_ldp_cmdb','ldp_orders', 'sub_type INT,pid INT');

DROP PROCEDURE IF EXISTS AddColumnsIfNotExists;
