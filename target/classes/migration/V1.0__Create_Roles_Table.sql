CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE UNIQUE_KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
