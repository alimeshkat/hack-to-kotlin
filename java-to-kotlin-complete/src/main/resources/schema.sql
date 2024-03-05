DROP TABLE IF EXISTS `ingredients`;
DROP TABLE IF EXISTS `recipes`;


CREATE TABLE IF NOT EXISTS `recipes`
(
    `recipe_id`   INT GENERATED ALWAYS AS IDENTITY,
    `recipe_name` varchar(255),
    PRIMARY KEY (`recipe_id`)
);

CREATE TABLE IF NOT EXISTS `ingredients`
(
    `ingredient_id`     INT GENERATED ALWAYS AS IDENTITY,
    `recipe_id` INT,
    `name`              varchar(255),
    `type`              varchar(255),
    `weight`            int,
    PRIMARY KEY (`ingredient_id`)
);

ALTER TABLE `ingredients`
    ADD CONSTRAINT `fk_recipes` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;
