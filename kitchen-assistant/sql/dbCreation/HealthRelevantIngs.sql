CREATE TABLE Nutrient
(
  nu_id serial not null,
  name varchar(30),

  CONSTRAINT Health_Relevant_Ingredient_pk PRIMARY KEY (nu_id)
);


CREATE TABLE Basic_Ingredient_Nutrient_Data_Source
(
  binds_id serial not null,
  bi_id integer not null,

  id_in_api varchar(200),  
  data_source_url varchar(200),
 
  
  CONSTRAINT Basic_Ingredient_Nutrient_Data_Source_pk PRIMARY KEY (binds_id)
);

ALTER TABLE Basic_Ingredient_Nutrient_Data_Source ADD CONSTRAINT Basic_Ingredient_Nutrient_Data_Source_bi_fk
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE TABLE Basic_Ingredient_Nutrient_amount
(
  binu_id serial not null,
  nu_id integer not null,
  bi_id integer not null,
  binds_id integer not null,
  coefficient real,
  
  CONSTRAINT Basic_Ingredient_Nutrient_amount_pk PRIMARY KEY (binu_id)
);



ALTER TABLE Basic_Ingredient_Nutrient_amount ADD CONSTRAINT Basic_Ingredient_Nutrient_amount_bi
FOREIGN KEY (bi_id)
REFERENCES Basic_Ingredient (bi_id)
ON DELETE cascade
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Basic_Ingredient_Nutrient_amount ADD CONSTRAINT Basic_Ingredient_Nutrient_amount_nu
FOREIGN KEY (nu_id)
REFERENCES Nutrient (nu_id)
ON DELETE cascade
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE Basic_Ingredient_Nutrient_amount ADD CONSTRAINT Basic_Ingredient_Nutrient_amount_binds
FOREIGN KEY (binds_id)
REFERENCES Basic_Ingredient_Nutrient_Data_Source (binds_id)
ON DELETE cascade
ON UPDATE NO ACTION
NOT DEFERRABLE;








CREATE TABLE Nutrient_Name
(
  nn_id serial not null,
  possible_name varchar(100),
  nu_id integer,

  
  CONSTRAINT Nutrient_Name_pk PRIMARY KEY (nn_id)
);

ALTER TABLE Nutrient_Name ADD CONSTRAINT Nutrient_names_fk
FOREIGN KEY (nu_id)
REFERENCES Nutrient (nu_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
