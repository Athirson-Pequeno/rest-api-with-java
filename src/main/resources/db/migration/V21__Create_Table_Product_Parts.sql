CREATE TABLE IF NOT EXISTS `product_parts`
(
    `id_product` bigint NOT NULL,
    `id_part`    bigint NOT NULL,
    PRIMARY KEY (`id_product`, `id_part`),
    KEY `fk_product_part_part` (`id_part`),
    CONSTRAINT `fk_product_part_production` FOREIGN KEY (`id_product`) REFERENCES `products` (`id`),
    CONSTRAINT `fk_product_part_part` FOREIGN KEY (`id_part`) REFERENCES `parts` (`id`)
) ENGINE = InnoDB;