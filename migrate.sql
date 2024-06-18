RENAME TABLE appwrite.`_1_database_1_collection_9` TO appwrite.banner;

ALTER TABLE appwrite.banner CHANGE `_uid` id char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN id char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner DROP COLUMN `_id`;
ALTER TABLE appwrite.banner ADD CONSTRAINT banner_unique UNIQUE KEY (id);
ALTER TABLE appwrite.banner ADD CONSTRAINT banner_pk PRIMARY KEY (id);
ALTER TABLE appwrite.banner DROP COLUMN `type`;
ALTER TABLE appwrite.banner CHANGE `_createdAt` createdAt bigint(20) NULL;
ALTER TABLE appwrite.banner CHANGE `_updatedAt` updatedAt bigint(20) NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN createdAt bigint(20) NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN updatedAt bigint(20) NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN link varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.banner MODIFY COLUMN image text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;