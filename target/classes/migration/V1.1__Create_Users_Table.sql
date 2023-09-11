CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(15) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE UNIQUE_KEY (`email`),
  CONSTRAINT FK_USER_ROLE FOREIGN KEY (role) REFERENCES roles(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
