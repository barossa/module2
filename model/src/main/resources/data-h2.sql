INSERT INTO gift_certificates
VALUES (3, 'changed', 'first gift certificate', 105.00, 7, '2022-01-21 20:20:48', '2022-01-24 11:32:52'),
       (4, 'first', 'first gift certificate', 105.00, 7, '2022-01-21 20:24:28', '2022-01-21 20:24:28'),
       (8, 'third', 'third', 333.00, 7, '2022-01-22 17:19:17', '2022-01-22 17:19:17'),
       (9, 'Wow it works', 'Yeah, it works!', 444.00, 123, '2022-01-22 19:19:13', '2022-02-01 21:00:36'),
       (11, 'deded', 'Descriзешщ', 982.48, 337, '2022-01-22 21:08:57', '2022-02-08 18:08:23'),
       (12, 'deded', 'last certificate on the earth', 200.00, 20, '2022-01-23 17:20:10', '2022-02-19 16:34:26'),
       (13, 'another-existing', 'Description asdsss', 75.90, 222, '2022-01-24 11:35:56', '2022-02-08 18:44:01'),
       (14, 'certificate of the year', 'description of the best cert', 876.00, 27, '2022-01-25 22:06:27',
        '2022-01-25 22:06:27'),
       (18, 'Name of tag', 'Description of tag', 45.00, 10, '2022-02-01 20:48:35', '2022-02-01 20:48:35'),
       (19, 'Name of tag', 'Description of tag', 45.00, 10, '2022-02-01 20:51:14', '2022-02-01 20:51:14'),
       (20, 'Name of tag', 'Description of tag', 406.00, 10, '2022-02-01 20:51:40', '2022-02-11 17:36:48'),
       (22, 'Name of tag', 'Description of tag', 75.35, 10, '2022-02-02 13:32:07', '2022-02-11 17:52:12'),
       (25, 'ZZZ-the-first-cert', 'Description of ta', 597.32, 108, '2022-02-08 12:54:41', '2022-02-08 12:54:41'),
       (28, 'Abrakadabra', 'Opisanie sertifikata', 79.97, 97, '2022-02-10 19:07:52', '2022-02-10 19:07:52'),
       (29, 'Abrakadabra', 'Opisanie sertifikata', 79.97, 97, '2022-02-11 17:50:15', '2022-02-11 17:50:15'),
       (30, 'Abrakadabra', 'Opisanie sertifikata', 79.97, 97, '2022-02-11 17:50:47', '2022-02-11 17:50:47');

INSERT INTO tags
VALUES (58, 'another-existing'),
       (106, 'another-existing-9'),
       (95, 'another-existing-99'),
       (48, 'another-new-123'),
       (194, 'another-not-exist'),
       (150, 'another-not-existing'),
       (51, 'another-one-tag'),
       (49, 'another-tag'),
       (53, 'best-tag'),
       (40, 'bla-bla-car'),
       (16, 'car'),
       (45, 'inserted-new-tag'),
       (55, 'just-now-tag'),
       (60, 'just-now-tag-123'),
       (20, 'laptop'),
       (42, 'last-tag'),
       (126, 'new-one-321'),
       (54, 'new-one-tag'),
       (83, 'new-one-tag-фыввфы'),
       (187, 'new-tag-200'),
       (192, 'new-tag-207'),
       (186, 'new-tag-214'),
       (120, 'new-tag-bla-002'),
       (146, 'new-tag-bla-003'),
       (147, 'new-tag-bla-070'),
       (184, 'new-tag-bless'),
       (148, 'new-tag-for-13'),
       (151, 'new-tag-for-1555'),
       (180, 'new-tag-for-test-1'),
       (185, 'new-tag-for-test-245'),
       (87, 'new-tag-qwerty'),
       (104, 'new-tag-qwerty-123'),
       (92, 'new-tag-qwerty-1232'),
       (57, 'new-tags'),
       (56, 'one-tag'),
       (59, 'one-tag-123'),
       (81, 'one-tag-name'),
       (139, 'orm-inserted-tag'),
       (19, 'smartphone'),
       (37, 'supercar'),
       (31, 'surprise'),
       (84, 'tag-name'),
       (153, 'tag-test'),
       (183, 'tag-test-for'),
       (193, 'tag-test-for-for'),
       (122, 'two-tag-name'),
       (41, 'uniq-tag'),
       (43, 'updated'),
       (152, 'wow-wow-wow'),
       (21, 'wowow'),
       (25, 'wowow3');


INSERT INTO certificate_tags
VALUES (22, 14, 49),
       (34, 18, 59),
       (35, 18, 60),
       (36, 19, 59),
       (38, 19, 60),
       (60, 25, 40),
       (61, 25, 81),
       (62, 25, 139),
       (64, 11, 147),
       (85, 28, 152),
       (86, 28, 139),
       (87, 20, 150),
       (88, 20, 153),
       (89, 20, 180),
       (90, 20, 151),
       (92, 29, 152),
       (93, 29, 139),
       (94, 30, 152),
       (95, 30, 139),
       (96, 22, 150),
       (97, 22, 183),
       (98, 22, 151),
       (114, 12, 193),
       (115, 12, 194);

INSERT INTO users
VALUES (2, 'anton'),
       (3, 'victoria');

INSERT INTO orders
VALUES (1, 79.97, '2022-02-12 17:14:52.732000', 28, 3),
       (2, 79.97, '2022-02-12 17:15:48.140000', 28, 3),
       (3, 200.00, '2022-02-12 17:16:15.325000', 12, 2),
       (4, 200.00, '2022-02-16 14:31:40.879000', 12, 3),
       (5, 200.00, '2022-02-16 14:31:42.256000', 12, 3),
       (6, 200.00, '2022-02-16 14:31:43.317000', 12, 3),
       (7, 200.00, '2022-02-16 14:31:44.112000', 12, 3),
       (8, 200.00, '2022-02-16 14:31:44.989000', 12, 3),
       (9, 200.00, '2022-02-16 14:33:37.041000', 12, 2),
       (10, 200.00, '2022-02-16 14:33:38.193000', 12, 2),
       (11, 200.00, '2022-02-16 14:33:39.160000', 12, 2),
       (21, 876.00, '2022-02-16 19:26:08.114000', 14, 2),
       (22, 876.00, '2022-02-16 19:26:14.942000', 14, 3),
       (23, 45.00, '2022-02-16 22:22:24.278000', 18, 2),
       (24, 45.00, '2022-02-16 22:22:28.294000', 18, 2),
       (25, 45.00, '2022-02-16 22:22:29.307000', 18, 2),
       (26, 45.00, '2022-02-16 22:22:30.190000', 18, 2),
       (27, 45.00, '2022-02-16 22:22:31.042000', 18, 2),
       (28, 45.00, '2022-02-16 22:22:32.177000', 18, 2),
       (29, 45.00, '2022-02-16 22:22:33.051000', 18, 2),
       (30, 45.00, '2022-02-16 22:22:33.878000', 18, 2),
       (31, 45.00, '2022-02-16 22:22:34.750000', 18, 2),
       (32, 45.00, '2022-02-16 22:22:35.599000', 18, 2),
       (33, 45.00, '2022-02-16 22:22:36.438000', 18, 2),
       (34, 45.00, '2022-02-16 22:22:37.230000', 18, 2),
       (35, 45.00, '2022-02-16 22:22:38.037000', 18, 2),
       (36, 45.00, '2022-02-16 22:22:38.867000', 18, 2),
       (37, 45.00, '2022-02-16 22:22:39.689000', 18, 2),
       (38, 45.00, '2022-02-16 22:22:40.548000', 18, 2),
       (39, 45.00, '2022-02-16 22:22:41.361000', 18, 2),
       (40, 45.00, '2022-02-16 22:22:42.133000', 18, 2),
       (41, 45.00, '2022-02-16 22:22:42.952000', 18, 2),
       (42, 45.00, '2022-02-16 22:22:43.661000', 18, 2),
       (43, 45.00, '2022-02-16 22:22:44.591000', 18, 2),
       (44, 982.48, '2022-02-16 22:31:10.406000', 11, 3),
       (45, 982.48, '2022-02-16 22:31:11.624000', 11, 3),
       (46, 982.48, '2022-02-16 22:31:12.792000', 11, 3),
       (47, 982.48, '2022-02-16 22:31:14.029000', 11, 3),
       (48, 982.48, '2022-02-16 22:31:15.146000', 11, 3),
       (49, 982.48, '2022-02-16 22:31:15.836000', 11, 3),
       (50, 982.48, '2022-02-16 22:31:16.535000', 11, 3),
       (51, 982.48, '2022-02-16 22:31:17.221000', 11, 3),
       (52, 982.48, '2022-02-16 22:31:17.942000', 11, 3),
       (53, 982.48, '2022-02-16 22:31:18.616000', 11, 3),
       (54, 982.48, '2022-02-16 22:31:19.312000', 11, 3),
       (55, 982.48, '2022-02-16 22:31:20.019000', 11, 3),
       (56, 982.48, '2022-02-16 22:31:20.727000', 11, 3),
       (57, 982.48, '2022-02-16 22:31:21.649000', 11, 3),
       (58, 982.48, '2022-02-16 22:31:22.299000', 11, 3);

INSERT INTO hibernate_sequence
VALUES (28);
