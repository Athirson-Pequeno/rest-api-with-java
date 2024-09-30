CREATE TABLE IF NOT EXISTS `orders`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `quantity` bigint NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;