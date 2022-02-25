CREATE TABLE tags
(
    tag_id int NOT NULL AUTO_INCREMENT,
    name   varchar(255) DEFAULT NULL,
    PRIMARY KEY (tag_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 930
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE gift_certificates
(
    certificate_id   int            NOT NULL AUTO_INCREMENT,
    name             varchar(45)    NOT NULL,
    description      varchar(100)   NOT NULL,
    price            decimal(10, 2) NOT NULL DEFAULT '0.00',
    duration         bigint         NOT NULL DEFAULT '0',
    create_date      datetime       NOT NULL,
    last_update_date datetime       NOT NULL,
    PRIMARY KEY (certificate_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8408
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE certificate_tags
(
    tag_id         int NOT NULL,
    certificate_id int NOT NULL,
    PRIMARY KEY (certificate_id, tag_id),
    KEY FKp0o3qdgvfhh3vlj4wmk91kiqj (tag_id),
    CONSTRAINT FK94knmyggc83ig35ds4o7hv61m FOREIGN KEY (certificate_id) REFERENCES gift_certificates (certificate_id),
    CONSTRAINT FKp0o3qdgvfhh3vlj4wmk91kiqj FOREIGN KEY (tag_id) REFERENCES tags (tag_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE orders
(
    order_id       int            NOT NULL AUTO_INCREMENT,
    cost           decimal(19, 2) NOT NULL,
    order_date     datetime(6)    NOT NULL,
    certificate_id int DEFAULT NULL,
    user_id        int DEFAULT NULL,
    PRIMARY KEY (order_id),
    KEY FK32ql8ubntj5uh44ph9659tiih (user_id),
    KEY FKsanyd7rxq59po4jket49y2kwn (certificate_id),
    CONSTRAINT FK32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT FKsanyd7rxq59po4jket49y2kwn FOREIGN KEY (certificate_id) REFERENCES gift_certificates (certificate_id) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 28611
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE users
(
    user_id  int          NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY UK_r43af9ap4edm43mmtq01oddj6 (username)
) ENGINE = InnoDB
  AUTO_INCREMENT = 783
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE hibernate_sequence
(
    next_val bigint DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dump completed on 2022-02-25 14:53:55
