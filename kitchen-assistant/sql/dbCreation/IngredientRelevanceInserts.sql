﻿BEGIN;
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
insert into Nutrient (nu_id,name) VALUES (5,'Saturated fatty acid');
insert into Nutrient (nu_id,name) VALUES (6,'Monounsaturated fat');
insert into Nutrient (nu_id,name) VALUES (7,'Poliunsaturated fat');
insert into Nutrient (nu_id,name) VALUES (8,'carbohydrates');
insert into Nutrient (nu_id,name) VALUES (9,'sugars');
insert into Nutrient (nu_id,name) VALUES (10,'fiber');
insert into Nutrient (nu_id,name) VALUES (11,'sodium');

insert into Nutrient (nu_id,name) VALUES (12,'water');
--insert into Nutrient (nu_id,name) VALUES (13,'fibre');
insert into Nutrient (nu_id,name) VALUES (14,'Calcium');
insert into Nutrient (nu_id,name) VALUES (15,'Iron');
insert into Nutrient (nu_id,name) VALUES (16,'Magnesium');
insert into Nutrient (nu_id,name) VALUES (17,'Phosphorus');
insert into Nutrient (nu_id,name) VALUES (18,'Potassium');
--insert into Nutrient (nu_id,name) VALUES (16,'Potas');
insert into Nutrient (nu_id,name) VALUES (19,'Zinc');
insert into Nutrient (nu_id,name) VALUES (20,'Manganese');
insert into Nutrient (nu_id,name) VALUES (21,'Selenium');
insert into Nutrient (nu_id,name) VALUES (22,'Vitamin A');
insert into Nutrient (nu_id,name) VALUES (23,'Vitamin E');
insert into Nutrient (nu_id,name) VALUES (24,'Vitamin C');
insert into Nutrient (nu_id,name) VALUES (25,'Vitamin B1');
insert into Nutrient (nu_id,name) VALUES (26,'Vitamin B2');
insert into Nutrient (nu_id,name) VALUES (27,'Vitamin B3');
insert into Nutrient (nu_id,name) VALUES (28,'Vitamin B5');
insert into Nutrient (nu_id,name) VALUES (29,'Vitamin B6');
insert into Nutrient (nu_id,name) VALUES (30,'Tryptophan');
insert into Nutrient (nu_id,name) VALUES (31,'Threonine');
insert into Nutrient (nu_id,name) VALUES (32,'Isoleucine');
insert into Nutrient (nu_id,name) VALUES (33,'Leucine');
insert into Nutrient (nu_id,name) VALUES (34,'Lizine');
insert into Nutrient (nu_id,name) VALUES (35,'Methionine');
insert into Nutrient (nu_id,name) VALUES (36,'Phenylalanine');
insert into Nutrient (nu_id,name) VALUES (37,'Valine');
insert into Nutrient (nu_id,name) VALUES (38,'Vitamin B9');
insert into Nutrient (nu_id,name) VALUES (39,'Vitamin A (RAE)');
insert into Nutrient (nu_id,name) VALUES (40,'Vitamin B12');
insert into Nutrient (nu_id,name) VALUES (41,'Vitamin D');
insert into Nutrient (nu_id,name) VALUES (42,'Vitamin K');
insert into Nutrient (nu_id,name) VALUES (43,'Vitamin K3');
insert into Nutrient (nu_id,name) VALUES (44,'Caffeine');
insert into Nutrient (nu_id,name) VALUES (45,'Cholesterol');

insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (1,1,'energy');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (2,2,'protein');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (3,3,'fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (4,4,'saturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (5,5,'Saturated fatty acid');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (6,6,'Monounsaturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (7,7,'Poliunsaturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (8,8,'carbohydrates');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (9,9,'sugars');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (10,10,'fiber');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (11,11,'sodium');

insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (12,12,'water');
--insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (13,'fibre');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (14,14,'Calcium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (15,15,'Iron');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (16,16,'Magnesium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (17,17,'Phosphorus');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (18,18,'Potassium');
--insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (16,'Potas');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (19,19,'Zinc');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (20,20,'Manganese');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (21,21,'Selenium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (22,22,'Vitamin A');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (23,23,'Vitamin E');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (24,24,'Vitamin C');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (25,25,'Vitamin B1');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (26,26,'Vitamin B2');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (27,27,'Vitamin B3');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (28,28,'Vitamin B5');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (29,29,'Vitamin B6');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (30,30,'Tryptophan');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (31,31,'Threonine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (32,32,'Isoleucine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (33,33,'Leucine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (34,34,'Lizine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (35,35,'Methionine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (36,36,'Phenylalanine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (37,37,'Valine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (38,38,'Vitamin B9');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (39,39,'Vitamin A (RAE)');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (40,40,'Vitamin B12');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (41,41,'Vitamin D');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (42,42,'Vitamin K');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (43,43,'Vitamin K3');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (44,44,'Caffeine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (45,45,'Cholesterol');


select setval('nutrient_nu_id_seq',100);
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (51, 3, 'Total lipid (fat)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (52, 6, 'Carbohydrate, by difference');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (53, 10, 'Fiber, total dietary');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (54, 9, 'Sugars, total');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (55, 14, 'Calcium, Ca');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (56, 15, 'Iron, Fe');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (57, 16, 'Magnesium, Mg');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (58, 17, 'Phosphorus, P');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (59, 18, 'Potassium, K');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (60, 11, 'Sodium, Na');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (61, 19, 'Zinc, Zn');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (62, 24, 'Vitamin C, total ascorbic acid');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (63, 26, 'Riboflavin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (64, 25, 'Thiamin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (65, 27, 'Niacin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (66, 29, 'Vitamin B-6');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (67, 38, 'Folate, DFE');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (68, 38, 'Folate');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (69, 38, 'Vitamin B-9');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (70, 38, 'folic acid');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (71, 40, 'Vitamin B-12');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (72, 40, 'Cobalamin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (73, 22, 'Vitamin A, RAE');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (74, 23, 'Vitamin E (alpha-tocopherol)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (75, 41, 'Vitamin D (D2 + D3)');
--Vitamin A, IU intentionally ommitted as old
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (76, 42, 'Vitamin K (phylloquinone)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (77, 43, 'Vitamin K3');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (78, 43, 'Vitamin K-3');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (79, 4, 'Fatty acids, total saturated');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (80, 6, 'Fatty acids, total monounsaturated');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (81, 7, 'Fatty acids, total polyunsaturated');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (82, 10, 'fibre');

insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (1,1,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (2,2,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (3,3,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (4,4,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (5,5,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (6,6,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (7,7,'-','-');
insert into Basic_Ingredient_Nutrient_Data_Source (binds_id,bi_id,data_source_url,id_in_api) VALUES (8,8,'-','-');

select setval('basic_ingredient_nutrient_data_source_binds_id_seq',10);


insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (1,1,1,1,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (2,2,2,2,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (3,3,3,3,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (4,4,4,4,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (5,5,5,5,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (6,6,6,6,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (7,7,7,7,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id,binds_id,coefficient) VALUES (8,8,8,8,1);

select setval('basic_ingredient_nutrient_amount_binu_id_seq',10);

select setval('nutrient_name_nn_id_seq',200);
COMMIT;
