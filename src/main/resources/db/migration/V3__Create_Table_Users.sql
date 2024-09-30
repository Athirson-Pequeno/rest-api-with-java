CREATE TABLE IF NOT EXISTS `users`
(
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    `user_name`               varchar(255) NOT NULL,
    `email`                   varchar(255) NOT NULL,
    `password`                varchar(255) NOT NULL,
    `account_non_expired`     bit(1) DEFAULT NULL,
    `account_non_locked`      bit(1) DEFAULT NULL,
    `credentials_non_expired` bit(1) DEFAULT NULL,
    `enabled`                 bit(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE = InnoDB;