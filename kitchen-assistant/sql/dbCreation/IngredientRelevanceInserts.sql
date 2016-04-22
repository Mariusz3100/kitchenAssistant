insert into Basic_Ingredient (bi_id,name) VALUES (1,'energia');
insert into Basic_Ingredient (bi_id,name) VALUES (2,'białko');
insert into Basic_Ingredient (bi_id,name) VALUES (3,'tłuszcz');
insert into Basic_Ingredient (bi_id,name) VALUES (4,'tłuszcz nasycony');
insert into Basic_Ingredient (bi_id,name) VALUES (5,'węglowodany');
insert into Basic_Ingredient (bi_id,name) VALUES (6,'cukry');
insert into Basic_Ingredient (bi_id,name) VALUES (7,'błonnik');
insert into Basic_Ingredient (bi_id,name) VALUES (8,'sód');

select setval('basic_ingredient_bi_id_seq',10)

insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (1,1,'energia');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (2,2,'białko');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (3,3,'tłuszcz');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (4,4,'kwasy tłuszczowe nasycone');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (5,5,'węglowodany');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (6,6,'cukry');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (7,7,'błonnik');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (8,8,'sód');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (10,1,'wartość energetyczna');

select setval('basic_ingredient_name_bin_id_seq',10)

insert into Nutrient (nu_id,name) VALUES (1,'energia');
insert into Nutrient (nu_id,name) VALUES (2,'białko');
insert into Nutrient (nu_id,name) VALUES (3,'tłuszcz');
insert into Nutrient (nu_id,name) VALUES (4,'tłuszcz nasycony');
insert into Nutrient (nu_id,name) VALUES (5,'węglowodany');
insert into Nutrient (nu_id,name) VALUES (6,'cukry');
insert into Nutrient (nu_id,name) VALUES (7,'błonnik');
insert into Nutrient (nu_id,name) VALUES (8,'sód');


insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (1,1,1);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (2,2,2);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (3,3,3);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (4,4,4);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (5,5,5);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (6,6,6);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (7,7,7);
insert into Basic_Ingredient_Nutrient_amount (binu_id,nu_id,bi_id) VALUES (8,8,8);

select setval('basic_ingredient_nutrient_amount_binu_id_seq',10)

insert into Nutrient (nu_id,name) VALUES (9,'Woda');
insert into Nutrient (nu_id,name) VALUES (10,'Błonnik');
insert into Nutrient (nu_id,name) VALUES (11,'Wapń');
insert into Nutrient (nu_id,name) VALUES (12,'Żelazo');
insert into Nutrient (nu_id,name) VALUES (13,'Magnez');
insert into Nutrient (nu_id,name) VALUES (14,'Fosfor');
insert into Nutrient (nu_id,name) VALUES (15,'Potas');
insert into Nutrient (nu_id,name) VALUES (16,'Potas');
insert into Nutrient (nu_id,name) VALUES (17,'Cynk');
insert into Nutrient (nu_id,name) VALUES (18,'Mangan');
insert into Nutrient (nu_id,name) VALUES (19,'Selen');
insert into Nutrient (nu_id,name) VALUES (20,'Witamina A');
insert into Nutrient (nu_id,name) VALUES (21,'Witamina E');
insert into Nutrient (nu_id,name) VALUES (22,'Witamina C');
insert into Nutrient (nu_id,name) VALUES (23,'Witamina B1');
insert into Nutrient (nu_id,name) VALUES (24,'Witamina B2');
insert into Nutrient (nu_id,name) VALUES (25,'Witamina B3');
insert into Nutrient (nu_id,name) VALUES (26,'Witamina B5');
insert into Nutrient (nu_id,name) VALUES (27,'Witamina B6');
insert into Nutrient (nu_id,name) VALUES (28,'Tryptofan');
insert into Nutrient (nu_id,name) VALUES (29,'Treonina');
insert into Nutrient (nu_id,name) VALUES (30,'Izoleucyna');
insert into Nutrient (nu_id,name) VALUES (31,'Leucyna');
insert into Nutrient (nu_id,name) VALUES (32,'Lizyna');
insert into Nutrient (nu_id,name) VALUES (33,'Metionina');
insert into Nutrient (nu_id,name) VALUES (34,'Fenyloalanina');
insert into Nutrient (nu_id,name) VALUES (35,'Walina');
insert into Nutrient (nu_id,name) VALUES (36,'Kw. tł. nasycone');
insert into Nutrient (nu_id,name) VALUES (37,'Kw. tł. jednonienasycone');
insert into Nutrient (nu_id,name) VALUES (38,'Kwas Foliowy');
insert into Nutrient (nu_id,name) VALUES (39,'Witamina A (RAE)');

select setval('nutrient_nu_id_seq',40)