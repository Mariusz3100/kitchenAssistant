CREATE TABLE Nutrient
(
  nu_id serial not null,
  name varchar(30),

  CONSTRAINT Health_Relevant_Ingredient_pk PRIMARY KEY (nu_id)
);

CREATE TABLE Basic_Ingredient_Nutrient_amount
(
  binu_id serial not null,
  nu_id integer not null,
  bi_id integer not null,
  coefficient real,
  
  CONSTRAINT Basic_Ingredient_Nutrient_amount_pk PRIMARY KEY (binu_id)
);

ALTER TABLE Basic_Ingredient_Nutrient_amount ADD CONSTRAINT Basic_Ingredient_Nutrient_amount_nu_fk
FOREIGN KEY (nu_id)
REFERENCES Nutrient (nu_id)
ON DELETE cascade
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Basic_Ingredient_Nutrient_amount ADD CONSTRAINT Basic_Ingredient_Health_Relevance_bi
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE cascade
ON UPDATE NO ACTION
NOT DEFERRABLE;
