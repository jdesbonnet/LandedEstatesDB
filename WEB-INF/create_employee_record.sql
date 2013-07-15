CREATE TABLE `employee_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `house_id` int(11) DEFAULT NULL,
  `document_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
