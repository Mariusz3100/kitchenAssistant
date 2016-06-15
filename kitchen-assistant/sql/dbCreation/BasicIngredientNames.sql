CREATE TABLE Basic_Ingredient
(
  bi_id serial not null,
  name varchar(100),

  CONSTRAINT Basic_Ingredient_pk PRIMARY KEY (bi_id)
);


CREATE TABLE Basic_Ingredient_Name
(
  bin_id serial not null,
  possible_name varchar(100),
  bi_id integer,

  
  CONSTRAINT Basic_Ingredient_Name_pk PRIMARY KEY (bin_id)
);


ALTER TABLE Basic_Ingredient_Name ADD CONSTRAINT Basic_Ingredient_names_fk
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE UNIQUE INDEX Basic_Ingredient_Name_name_index
 ON Basic_Ingredient_Name
 ( possible_name );


CREATE UNIQUE INDEX Basic_Ingredient_name_index
 ON Basic_Ingredient
 ( name );


