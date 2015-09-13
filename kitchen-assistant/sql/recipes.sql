CREATE TABLE recipe (
  rec_id int NOT NULL,
  url varchar(100) NOT NULL,

  CONSTRAINT recipe_pk PRIMARY KEY (rec_id)

)


CREATE TABLE recipe_ingredient (
  rec_ing_id int NOT NULL,
  rec_id int NOT NULL,
  ing_id int NOT NULL,
  amount real NOT NULL,
  a_type_id int NOT NULL,

   CONSTRAINT recipe_ing_pk PRIMARY KEY (rec_ing_id)


)

CREATE TABLE amount_type (
   a_type_id int NOT NULL,
   nazwa varchar(45) NOT NULL,

   CONSTRAINT amount_type_pk PRIMARY KEY (a_type_id)


)



ALTER TABLE recipe_ingredient ADD CONSTRAINT r_ing_recipe_fk
FOREIGN KEY (rec_id)
REFERENCES recipe (rec_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE recipe_ingredient ADD CONSTRAINT r_ing_ingredient_fk
FOREIGN KEY (ing_id)
REFERENCES produkt (p_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE recipe_ingredient ADD CONSTRAINT r_ing_a_type_fk
FOREIGN KEY (a_type_id)
REFERENCES amount_type (a_type_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;



insert into amount_type (a_type_id,  nazwa)values(1,'ml');
insert into amount_type (a_type_id,  nazwa)values(2,'mg');
insert into amount_type (a_type_id,  nazwa)values(3,'szt');