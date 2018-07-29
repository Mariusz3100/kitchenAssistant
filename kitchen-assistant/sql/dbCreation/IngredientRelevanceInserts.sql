insert into Basic_Ingredient (bi_id,name) VALUES (1,'energy');
insert into Basic_Ingredient (bi_id,name) VALUES (2,'protein');
insert into Basic_Ingredient (bi_id,name) VALUES (3,'fat');
insert into Basic_Ingredient (bi_id,name) VALUES (4,'saturated fat');
insert into Basic_Ingredient (bi_id,name) VALUES (5,'carbohydrates');
insert into Basic_Ingredient (bi_id,name) VALUES (6,'sugars');
insert into Basic_Ingredient (bi_id,name) VALUES (7,'fibre');
insert into Basic_Ingredient (bi_id,name) VALUES (8,'sodium');

select setval('basic_ingredient_bi_id_seq',10);

insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (1,1,'energy');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (2,2,'protein');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (3,3,'fat');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (4,4,'saturated fat');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (5,5,'carbohydrates');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (6,6,'sugars');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (7,7,'fibre');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (8,8,'sodium');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (10,1,'calories');

select setval('basic_ingredient_name_bin_id_seq',15);

insert into Nutrient (nu_id,name) VALUES (1,'energy');
insert into Nutrient (nu_id,name) VALUES (2,'protein');
insert into Nutrient (nu_id,name) VALUES (3,'fat');
insert into Nutrient (nu_id,name) VALUES (4,'saturated fat');
insert into Nutrient (nu_id,name) VALUES (5,'carbohydrates');
insert into Nutrient (nu_id,name) VALUES (6,'sugars');
insert into Nutrient (nu_id,name) VALUES (7,'fiber');
insert into Nutrient (nu_id,name) VALUES (8,'sodium');

insert into Nutrient (nu_id,name) VALUES (9,'water');
insert into Nutrient (nu_id,name) VALUES (10,'fibre');
insert into Nutrient (nu_id,name) VALUES (11,'Calcium');
insert into Nutrient (nu_id,name) VALUES (12,'Iron');
insert into Nutrient (nu_id,name) VALUES (13,'Magnesium');
insert into Nutrient (nu_id,name) VALUES (14,'Phosphorus');
insert into Nutrient (nu_id,name) VALUES (15,'Potasium');
insert into Nutrient (nu_id,name) VALUES (16,'Potas');
insert into Nutrient (nu_id,name) VALUES (17,'Zinc');
insert into Nutrient (nu_id,name) VALUES (18,'Manganese');
insert into Nutrient (nu_id,name) VALUES (19,'Selenium');
insert into Nutrient (nu_id,name) VALUES (20,'Vitamin A');
insert into Nutrient (nu_id,name) VALUES (21,'Vitamin E');
insert into Nutrient (nu_id,name) VALUES (22,'Vitamin C');
insert into Nutrient (nu_id,name) VALUES (23,'Vitamin B1');
insert into Nutrient (nu_id,name) VALUES (24,'Vitamin B2');
insert into Nutrient (nu_id,name) VALUES (25,'Vitamin B3');
insert into Nutrient (nu_id,name) VALUES (26,'Vitamin B5');
insert into Nutrient (nu_id,name) VALUES (27,'Vitamin B6');
insert into Nutrient (nu_id,name) VALUES (28,'Tryptophan');
insert into Nutrient (nu_id,name) VALUES (29,'Threonine');
insert into Nutrient (nu_id,name) VALUES (30,'Isoleucine');
insert into Nutrient (nu_id,name) VALUES (31,'Leucine');
insert into Nutrient (nu_id,name) VALUES (32,'Lizine');
insert into Nutrient (nu_id,name) VALUES (33,'Methionine');
insert into Nutrient (nu_id,name) VALUES (34,'Phenylalanine');
insert into Nutrient (nu_id,name) VALUES (35,'Valine');
insert into Nutrient (nu_id,name) VALUES (36,'Saturated fattty acid');
insert into Nutrient (nu_id,name) VALUES (37,'Monounsaturated fat');
insert into Nutrient (nu_id,name) VALUES (38,'folic acid');
insert into Nutrient (nu_id,name) VALUES (39,'Vitamin A (RAE)');

select setval('nutrient_nu_id_seq',40);

insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (1,1,1,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (2,2,2,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (3,3,3,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (4,4,4,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (5,5,5,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (6,6,6,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (7,7,7,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,coefficient) VALUES (8,8,8,1);

select setval('basic_ingredient_nutrient_amount_binu_id_seq',10);
