CREATE TABLE IF NOT EXISTS `orders`
(
    `id`     bigint       NOT NULL AUTO_INCREMENT,
    `client` varchar(255) NOT NULL,
    `date`   datetime,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;