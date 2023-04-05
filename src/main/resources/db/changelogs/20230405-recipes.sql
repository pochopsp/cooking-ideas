CREATE SEQUENCE recipe_id_seq;

CREATE TABLE recipe
(
    id          BIGINT      NOT NULL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT        NOT NULL
);

CREATE TABLE ingredients
(
    id           BIGSERIAL   NOT NULL PRIMARY KEY,
    recipe_id    BIGINT,
    name         VARCHAR(50) NOT NULL,
    quantity     INTEGER     NOT NULL,
    measure_unit VARCHAR(25) NOT NULL,
    CONSTRAINT fk_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id)
);

