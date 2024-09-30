CREATE TABLE IF NOT EXISTS `part_types`
(
    `id_part` bigint NOT NULL,
    `id_type` bigint NOT NULL,
    PRIMARY KEY (`id_part`, `id_type`),
    KEY `fk_part_type_type` (`id_type`),
    CONSTRAINT `fk_part_type` FOREIGN KEY (`id_part`) REFERENCES `parts` (`id`),
    CONSTRAINT `fk_part_type_type` FOREIGN KEY (`id_type`) REFERENCES `types` (`id`)
) ENGINE = InnoDB;