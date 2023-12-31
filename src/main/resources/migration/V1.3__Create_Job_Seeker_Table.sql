CREATE TABLE `job_seeker` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(15) NOT NULL,
  `contact_number` varchar(15) NOT NULL,
  `postal_address` varchar(250) NOT NULL,
  `highest_qualification` varchar(250) NOT NULL,
  `job` varchar(250) DEFAULT NULL,
  `job_experience` int DEFAULT NULL,
  `interested_countries` varchar(250) DEFAULT NULL,
  `interested_jobs` varchar(500) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE UNIQUE_KEY (`email`),
  CONSTRAINT FK_USER_ROLE_JOB_SEEKER FOREIGN KEY (role) REFERENCES roles(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
