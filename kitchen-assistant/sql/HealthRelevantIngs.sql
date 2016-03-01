CREATE TABLE Health_Relevant_Ingredient
(
  hr_id serial not null,
  name varchar(20),

  CONSTRAINT Health_Relevant_Ingredient_pk PRIMARY KEY (hr_id)
);

CREATE TABLE Basic_Ingredient_Health_Relevance
(
  bihr_id serial not null,
  hr_id integer not null,
  bi_id integer not null,
  coeficient real,
  
  CONSTRAINT Basic_Ingredient_Health_Relevance_pk PRIMARY KEY (bihr_id)
);

ALTER TABLE Basic_Ingredient_Health_Relevance ADD CONSTRAINT Basic_Ingredient_Health_Relevance_hr
FOREIGN KEY (hr_id)
REFERENCES Health_Relevant_Ingredient (hr_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Basic_Ingredient_Health_Relevance ADD CONSTRAINT Basic_Ingredient_Health_Relevance_bi
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
