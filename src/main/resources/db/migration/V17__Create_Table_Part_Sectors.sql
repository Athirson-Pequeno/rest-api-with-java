CREATE TABLE IF NOT EXISTS `part_sectors`
(
    `id_part`   bigint NOT NULL,
    `id_sector` bigint NOT NULL,
    PRIMARY KEY (`id_part`, `id_sector`),
    KEY `fk_part_sector_sector` (`id_sector`),
    CONSTRAINT `fk_part_sector` FOREIGN KEY (`id_part`) REFERENCES `parts` (`id`),
    CONSTRAINT `fk_part_sector_sector` FOREIGN KEY (`id_sector`) REFERENCES `sectors` (`id`)
) ENGINE = InnoDB;