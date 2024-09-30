CREATE TABLE IF NOT EXISTS `products`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) NOT NULL,
    `product_code` varchar(255) NOT NULL,
    `color_code`   varchar(255) NOT NULL,
    `color_name`   varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;