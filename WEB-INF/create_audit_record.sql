CREATE TABLE `audit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `record_timestamp` timestamp NOT NULL,
  `entity_id` int(11) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `entity_xml` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

