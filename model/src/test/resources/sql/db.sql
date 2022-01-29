DROP TABLE IF EXISTS certificate_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS gift_certificates;

    CREATE TABLE gift_certificates
(
    certificate_id   int NOT NULL AUTO_INCREMENT,
    name             varchar(45)    DEFAULT NULL,
    description      varchar(100)   DEFAULT NULL,
    price            decimal(10, 0) DEFAULT '0',
    duration         bigint         DEFAULT '0',
    create_date      datetime       DEFAULT NULL,
    last_update_date datetime       DEFAULT NULL,
    PRIMARY KEY (certificate_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 33
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO gift_certificates
VALUES (3, 'changed', 'first gift certificate', 105, 7, '2022-01-21 20:20:48', '2022-01-24 11:32:52'),
       (4, 'first', 'first gift certificate', 105, 7, '2022-01-21 20:24:28', '2022-01-21 20:24:28'),
       (8, 'third', 'third', 333, 7, '2022-01-22 17:19:17', '2022-01-22 17:19:17'),
       (9, 'Wow it works', 'Yeah, it works!', 333, 123, '2022-01-22 19:19:13', '2022-01-24 20:03:05'),
       (11, 'deded', 'last', 255, 20, '2022-01-22 21:08:57', '2022-01-22 21:09:18'),
       (12, 'deded', 'last certificate on the earth', 200, 20, '2022-01-23 17:20:10', '2022-01-23 17:20:10'),
       (13, 'The best cert', 'Last updated tag', 77, 222, '2022-01-24 11:35:56', '2022-01-24 13:52:58'),
       (14, 'certificate of the year', 'description of the best cert', 876, 27, '2022-01-25 22:06:27',
        '2022-01-25 22:06:27'),
       (15, 'certificate of the year 2021', 'description of the best cert on the earth', 999, 27, '2022-01-25 22:07:10',
        '2022-01-25 22:07:10'),
       (16, 'new name of certificate', 'Description of updated tag', 100, 1, '2022-01-25 22:07:46',
        '2022-01-25 22:21:45');

CREATE TABLE tags
(
    tag_id int          NOT NULL AUTO_INCREMENT,
    name   varchar(100) NOT NULL,
    PRIMARY KEY (tag_id),
    UNIQUE KEY name_UNIQUE (name)
) ENGINE = InnoDB
  AUTO_INCREMENT = 91
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO tags
VALUES (58, 'another-existing'),
       (48, 'another-new-123'),
       (51, 'another-one-tag'),
       (49, 'another-tag'),
       (53, 'best-tag'),
       (40, 'bla-bla-car'),
       (16, 'car'),
       (45, 'inserted-new-tag'),
       (44, 'inserted-tag'),
       (55, 'just-now-tag'),
       (20, 'laptop'),
       (42, 'last-tag'),
       (54, 'new-one-tag'),
       (47, 'new-tag-123'),
       (57, 'new-tags'),
       (56, 'one-tag'),
       (19, 'smartphone'),
       (37, 'supercar'),
       (31, 'surprise'),
       (41, 'uniq-tag'),
       (43, 'updated'),
       (21, 'wowow'),
       (25, 'wowow3');

CREATE TABLE certificate_tags
(
    record_id      int NOT NULL AUTO_INCREMENT,
    certificate_id int NOT NULL,
    tag_id         int NOT NULL,
    PRIMARY KEY (record_id),
    KEY FK_gift_certificates_certificate_tags_idx (certificate_id),
    KEY FK_tags_certificate_tags_idx (tag_id),
    CONSTRAINT FK_gift_certificates_certificate_tags FOREIGN KEY (certificate_id) REFERENCES gift_certificates (certificate_id) ON DELETE CASCADE,
    CONSTRAINT FK_tags_certificate_tags FOREIGN KEY (tag_id) REFERENCES tags (tag_id) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 63
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO certificate_tags
VALUES (9, 11, 16),
       (19, 13, 45),
       (20, 9, 48),
       (21, 9, 47),
       (22, 14, 49),
       (23, 15, 51),
       (24, 15, 49),
       (29, 16, 58),
       (30, 16, 57);