CREATE TABLE `appointment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `consultant_id` int NOT NULL,
  `job_seeker_id` int NOT NULL,
  `appointment_time` datetime NOT NULL,
  `is_accepted` tinyint(1) NOT NULL DEFAULT '0',
  `is_completed` tinyint(1) NOT NULL DEFAULT '0',
  `is_declined` tinyint(1) NOT NULL DEFAULT '0',
  `isDeclined` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
    CONSTRAINT FK_PROJECT_UNIT_CONSULTANT FOREIGN KEY (consultant_id) REFERENCES consultant(id),
    CONSTRAINT FK_PROJECT_UNIT_JOB_SEEKER FOREIGN KEY (job_seeker_id) REFERENCES job_seeker(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
