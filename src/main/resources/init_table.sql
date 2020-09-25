# CREATE TABLE IF NOT EXISTS `archive_oddschange` (
#                                       `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
#                                       `bookmakerid` int(11) NOT NULL,
#                                       `guthmatchid` int(11) NOT NULL,
#                                       `marketid` int(11) NOT NULL,
#                                       `typekey` varchar(10) NOT NULL,
#                                       `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
#                                       `object` mediumtext NOT NULL,
#                                       PRIMARY KEY (`id`),
#                                       KEY `market_idx` (`bookmakerid`,`guthmatchid`,`marketid`,`typekey`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELETE FROM archive_oddschange;