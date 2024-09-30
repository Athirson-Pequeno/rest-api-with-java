CREATE TABLE IF NOT EXISTS `order_products`
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT,
    `id_order`        BIGINT NOT NULL,
    `id_product`      BIGINT NOT NULL,
    `status_product`  BIGINT NOT NULL,
    `amount_products` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`id_order`, `id_product`),
    KEY `fk_order_product_product` (`id_product`),
    CONSTRAINT `fk_order_product` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id`),
    CONSTRAINT `fk_order_product_product` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`)
) ENGINE = InnoDB;
