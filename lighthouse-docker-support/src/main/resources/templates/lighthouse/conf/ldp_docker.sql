ALTER USER 'root'@'localhost' IDENTIFIED BY '${ldp_mysql_root_passwd}';

CREATE USER IF NOT EXISTS '${ldp_mysql_operate_user}'@'%' IDENTIFIED WITH mysql_native_password BY '${ldp_mysql_operate_user_passwd}';

GRANT ALL PRIVILEGES ON *.* TO '${ldp_mysql_operate_user}'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;

CREATE DATABASE  IF NOT EXISTS `cluster_${ldp_lighthouse_cluster_id}_ldp_warehouse`;


