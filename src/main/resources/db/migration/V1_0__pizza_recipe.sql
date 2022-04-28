CREATE TABLE IF NOT EXISTS `recipes`
(
    `id` INT GENERATED ALWAYS AS IDENTITY ,
    `recipe_name` varchar(255),
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `ingredients`
(
    `id` INT GENERATED ALWAYS AS IDENTITY ,
    `recipes_id` INT,
    `name` varchar(255),
    `type` varchar(255),
    `weight` int,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_recipes` FOREIGN KEY (`recipes_id`) REFERENCES recipes(`id`)
);
