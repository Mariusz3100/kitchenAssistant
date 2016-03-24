CREATE TABLE Health_Relevant_Ingredient
(
  hr_id serial not null,
  name varchar(25),

  CONSTRAINT Health_Relevant_Ingredient_pk PRIMARY KEY (hr_id)
);

CREATE TABLE Basic_Ingredient_Health_Relevance_Amount
(
  bihr_id serial not null,
  hr_id integer not null,
  bi_id integer not null,
  coeficient real,
  
  CONSTRAINT Basic_Ingredient_Health_Relevance_Amount_pk PRIMARY KEY (bihr_id)
);

ALTER TABLE Basic_Ingredient_Health_Relevance_Amount ADD CONSTRAINT Basic_Ingredient_Health_Relevance_Amount_hr
FOREIGN KEY (hr_id)
REFERENCES Health_Relevant_Ingredient (hr_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Basic_Ingredient_Health_Relevance_Amount ADD CONSTRAINT Basic_Ingredient_Health_Relevance_Amount_bi
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
