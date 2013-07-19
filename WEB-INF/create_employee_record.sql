CREATE TABLE `employee_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text,
  `document_date` date DEFAULT NULL,
  `estate_id` int(11) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

