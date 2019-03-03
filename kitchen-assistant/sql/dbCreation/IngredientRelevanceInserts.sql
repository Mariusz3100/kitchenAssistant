BEGIN;
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
insert into Nutrient (nu_id,name) VALUES (5,'saturated fatty acid');
insert into Nutrient (nu_id,name) VALUES (6,'monounsaturated fat');
insert into Nutrient (nu_id,name) VALUES (7,'poliunsaturated fat');
insert into Nutrient (nu_id,name) VALUES (8,'carbohydrates');
insert into Nutrient (nu_id,name) VALUES (9,'sugars');
insert into Nutrient (nu_id,name) VALUES (10,'fiber');
insert into Nutrient (nu_id,name) VALUES (11,'sodium');

insert into Nutrient (nu_id,name) VALUES (12,'water');
--insert into Nutrient (nu_id,name) VALUES (13,'fibre');
insert into Nutrient (nu_id,name) VALUES (14,'calcium');
insert into Nutrient (nu_id,name) VALUES (15,'iron');
insert into Nutrient (nu_id,name) VALUES (16,'magnesium');
insert into Nutrient (nu_id,name) VALUES (17,'phosphorus');
insert into Nutrient (nu_id,name) VALUES (18,'potassium');
--insert into Nutrient (nu_id,name) VALUES (16,'Potas');
insert into Nutrient (nu_id,name) VALUES (19,'zinc');
insert into Nutrient (nu_id,name) VALUES (20,'manganese');
insert into Nutrient (nu_id,name) VALUES (21,'selenium');
insert into Nutrient (nu_id,name) VALUES (22,'vitamin A');
insert into Nutrient (nu_id,name) VALUES (23,'vitamin E');
insert into Nutrient (nu_id,name) VALUES (24,'vitamin C');
insert into Nutrient (nu_id,name) VALUES (25,'vitamin B1');
insert into Nutrient (nu_id,name) VALUES (26,'vitamin B2');
insert into Nutrient (nu_id,name) VALUES (27,'vitamin B3');
insert into Nutrient (nu_id,name) VALUES (28,'vitamin B5');
insert into Nutrient (nu_id,name) VALUES (29,'vitamin B6');
insert into Nutrient (nu_id,name) VALUES (30,'tryptophan');
insert into Nutrient (nu_id,name) VALUES (31,'threonine');
insert into Nutrient (nu_id,name) VALUES (32,'isoleucine');
insert into Nutrient (nu_id,name) VALUES (33,'leucine');
insert into Nutrient (nu_id,name) VALUES (34,'lizine');
insert into Nutrient (nu_id,name) VALUES (35,'methionine');
insert into Nutrient (nu_id,name) VALUES (36,'phenylalanine');
insert into Nutrient (nu_id,name) VALUES (37,'valine');
insert into Nutrient (nu_id,name) VALUES (38,'vitamin B9');
insert into Nutrient (nu_id,name) VALUES (39,'vitamin A (RAE)');
insert into Nutrient (nu_id,name) VALUES (40,'vitamin B12');
insert into Nutrient (nu_id,name) VALUES (41,'vitamin D');
insert into Nutrient (nu_id,name) VALUES (42,'vitamin K');
insert into Nutrient (nu_id,name) VALUES (43,'vitamin K3');
insert into Nutrient (nu_id,name) VALUES (44,'caffeine');
insert into Nutrient (nu_id,name) VALUES (45,'cholesterol');

insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (1,1,'energy');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (2,2,'protein');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (3,3,'fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (4,4,'saturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (5,5,'saturated fatty acid');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (6,6,'monounsaturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (7,7,'poliunsaturated fat');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (8,8,'carbohydrates');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (9,9,'sugars');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (10,10,'fiber');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (11,11,'sodium');

insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (12,12,'water');
--insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (13,'fibre');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (14,14,'calcium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (15,15,'iron');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (16,16,'magnesium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (17,17,'phosphorus');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (18,18,'potassium');
--insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (16,'potas');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (19,19,'zinc');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (20,20,'manganese');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (21,21,'selenium');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (22,22,'vitamin A');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (23,23,'vitamin E');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (24,24,'vitamin C');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (25,25,'vitamin B1');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (26,26,'vitamin B2');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (27,27,'vitamin B3');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (28,28,'vitamin B5');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (29,29,'vitamin B6');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (30,30,'tryptophan');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (31,31,'threonine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (32,32,'isoleucine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (33,33,'leucine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (34,34,'lizine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (35,35,'Mmethionine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (36,36,'Pphenylalanine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (37,37,'valine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (38,38,'vitamin B9');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (39,39,'vitamin A (RAE)');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (40,40,'vitamin B12');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (41,41,'vitamin D');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (42,42,'vitamin K');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (43,43,'vitamin K3');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (44,44,'caffeine');
insert into public.nutrient_name (nn_id,nu_id, possible_name) VALUES (45,45,'cholesterol');


select setval('nutrient_nu_id_seq',100);
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (51, 3, 'total lipid (fat)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (52, 6, 'carbohydrate, by difference');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (53, 10, 'fiber, total dietary');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (54, 9, 'sugars, total');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (55, 14, 'calcium, Ca');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (56, 15, 'iron, Fe');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (57, 16, 'magnesium, Mg');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (58, 17, 'phosphorus, P');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (59, 18, 'potassium, K');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (60, 11, 'sodium, Na');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (61, 19, 'zinc, Zn');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (62, 24, 'vitamin C, total ascorbic acid');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (63, 26, 'riboflavin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (64, 25, 'thiamin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (65, 27, 'niacin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (66, 29, 'vitamin B-6');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (67, 38, 'folate, DFE');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (68, 38, 'folate');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (69, 38, 'vitamin B-9');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (70, 38, 'folic acid');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (71, 40, 'vitamin B-12');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (72, 40, 'cobalamin');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (73, 22, 'vitamin A, RAE');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (74, 23, 'vitamin E (alpha-tocopherol)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (75, 41, 'vitamin D (D2 + D3)');
--Vitamin A, IU intentionally ommitted as old
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (76, 42, 'vitamin K (phylloquinone)');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (77, 43, 'vitamin K3');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (78, 43, 'vitamin K-3');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (79, 4, 'fatty acids, total saturated');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (80, 6, 'fatty acids, total monounsaturated');
INSERT INTO public.nutrient_name(nn_id,nu_id, possible_name) VALUES (81, 7, 'fatty acids, total polyunsaturated');
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
