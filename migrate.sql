RENAME TABLE appwrite.`_1_database_1_collection_9` TO appwrite.banner;
ALTER TABLE appwrite.banner CHANGE `_uid` id char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN id char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner DROP COLUMN `_id`;
ALTER TABLE appwrite.banner ADD CONSTRAINT banner_unique UNIQUE KEY (id);
ALTER TABLE appwrite.banner ADD CONSTRAINT banner_pk PRIMARY KEY (id);
ALTER TABLE appwrite.banner DROP COLUMN `type`;
ALTER TABLE appwrite.banner CHANGE `_createdAt` created_at bigint(20) NULL;
ALTER TABLE appwrite.banner CHANGE `_updatedAt` update_at bigint(20) NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN createdAt bigint(20) NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN updatedAt bigint(20) NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN link varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN image text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;

RENAME TABLE appwrite.`_1_database_1_collection_3` TO appwrite.host;
ALTER TABLE appwrite.host DROP COLUMN `_id`;
ALTER TABLE appwrite.host CHANGE `_createdat` created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN host varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.host DROP COLUMN interstitialadunitid;
ALTER TABLE appwrite.host CHANGE idhost host_id int(11) NOT NULL;
ALTER TABLE appwrite.host CHANGE `_uid` id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.host ADD CONSTRAINT host_pk PRIMARY KEY (uid);
ALTER TABLE appwrite.host ADD CONSTRAINT host_unique UNIQUE KEY (uid);
ALTER TABLE appwrite.host CHANGE `_updatedat` updated_at bigint(20) NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN updated_at bigint(20) NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN status varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN `order` int(11) NOT NULL;
ALTER TABLE appwrite.host MODIFY COLUMN name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.host CHANGE host url_api varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;

RENAME TABLE appwrite.`_1_database_1_collection_10` TO appwrite.notification;
ALTER TABLE appwrite.notification DROP COLUMN `_id`;
ALTER TABLE appwrite.notification CHANGE `_createdat` created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.notification MODIFY COLUMN created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.notification DROP COLUMN datemade;
ALTER TABLE appwrite.notification DROP COLUMN `_updatedat`;
ALTER TABLE appwrite.notification CHANGE titulo title varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.notification MODIFY COLUMN title varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.notification CHANGE `_uid` id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.notification MODIFY COLUMN id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.notification ADD CONSTRAINT notification_pk PRIMARY KEY (id);
ALTER TABLE appwrite.notification ADD CONSTRAINT notification_unique UNIQUE KEY (id);
ALTER TABLE appwrite.notification CHANGE menssege message varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL NULL;
ALTER TABLE appwrite.notification ADD status smallint NOT NULL;

RENAME TABLE appwrite.`_1_database_1_collection_14` TO appwrite.recommendation;

ALTER TABLE appwrite.recommendation DROP COLUMN `_id`;

UPDATE recommendation
SET datacria = 0
where
	datacria is null

UPDATE recommendation
SET `_createdat` = datacria

where
	1=1

ALTER TABLE appwrite.recommendation CHANGE `_uid` id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.recommendation MODIFY COLUMN uniqueid varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.recommendation MODIFY COLUMN title varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.recommendation MODIFY COLUMN link varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.recommendation DROP COLUMN dataCria;
ALTER TABLE appwrite.recommendation CHANGE `_createdAt` created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.recommendation CHANGE `_updatedAt` updated_at bigint(20) NOT NULL;
ALTER TABLE appwrite.recommendation CHANGE artistid artist_id varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL NULL;
ALTER TABLE appwrite.recommendation CHANGE artistname artist_name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL NULL;
ALTER TABLE appwrite.recommendation ADD CONSTRAINT recommendation_pk PRIMARY KEY (id);
ALTER TABLE appwrite.recommendation ADD CONSTRAINT recommendation_unique UNIQUE KEY (id);


RENAME TABLE appwrite.`_1_database_1_collection_11` TO appwrite.permission;

ALTER TABLE appwrite.permission DROP COLUMN `_id`;
ALTER TABLE appwrite.permission CHANGE `_uid` id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.permission ADD CONSTRAINT permission_pk PRIMARY KEY (id);
ALTER TABLE appwrite.permission ADD CONSTRAINT permission_unique UNIQUE KEY (id);
ALTER TABLE appwrite.permission CHANGE `_createdat` created_at bigint(20) NOT NULL;
ALTER TABLE appwrite.permission CHANGE `_updatedat` updated_at bigint(20) NOT NULL;
ALTER TABLE appwrite.permission CHANGE userid user_id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.permission CHANGE value `level` int(11) NOT NULL;