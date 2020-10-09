CREATE TABLE IF NOT EXISTS `user`
(
    id         BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(90) NOT NULL,
    birth_date DATE        NOT NULL,
    cpf        VARCHAR(11) NOT NULL,
    email      VARCHAR(90),
    address    VARCHAR(255)
);