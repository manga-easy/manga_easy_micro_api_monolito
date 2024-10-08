create table profile (id varchar(255) not null, achievements_highlight json, biography varchar(255), created_at bigint not null, mangas_highlight json, name varchar(255), picture varchar(255), role varchar(255), total_achievements bigint not null, total_manga_read bigint not null, total_xp bigint not null, updated_at bigint not null, user_id varchar(255) not null, visible_achievements bit not null, visible_mangas bit not null, visible_statics bit not null, primary key (id)) engine=InnoDB;
alter table if exists profile add constraint UK_c1dkiawnlj6uoe6fnlwd6j83j unique (user_id);
create table `catalog-like` (id bigint not null auto_increment, catalog_id varchar(255) not null, created_at bigint, user_id varchar(255) not null, primary key (id)) engine=InnoDB;
create table `catalog-view` (id bigint not null auto_increment, catalog_id varchar(255) not null, created_at bigint, user_id varchar(255) not null, primary key (id)) engine=InnoDB;
create table review (id varchar(255) not null, catalog_id varchar(255) not null, commentary varchar(255), created_at bigint, is_spoiler bit not null, is_updated bit not null, rating float(53) not null, total_likes bigint not null, updated_at bigint, user_id varchar(255) not null, primary key (id)) engine=InnoDB;
create table `review-like` (id bigint not null auto_increment, created_at bigint, review_id varchar(255) not null, user_id varchar(255) not null, primary key (id)) engine=InnoDB;
ALTER TABLE appwrite.`catalog` DROP COLUMN id;
ALTER TABLE appwrite.`catalog` CHANGE uid id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.`catalog` MODIFY COLUMN id varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;
ALTER TABLE appwrite.`catalog` ADD CONSTRAINT catalog_pk PRIMARY KEY (id);
ALTER TABLE appwrite.`catalog` ADD CONSTRAINT catalog_unique UNIQUE KEY (id);