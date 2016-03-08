insert into Basic_Ingredient (bi_id,name) VALUES (1,'energia');
insert into Basic_Ingredient (bi_id,name) VALUES (2,'białko');
insert into Basic_Ingredient (bi_id,name) VALUES (3,'tłuszcz');
insert into Basic_Ingredient (bi_id,name) VALUES (4,'tłuszcz nasycony');
insert into Basic_Ingredient (bi_id,name) VALUES (5,'węglowodany');
insert into Basic_Ingredient (bi_id,name) VALUES (6,'cukry');
insert into Basic_Ingredient (bi_id,name) VALUES (7,'błonnik');
insert into Basic_Ingredient (bi_id,name) VALUES (8,'sód');

insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (1,1,'energia');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (2,2,'białko');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (3,3,'tłuszcz');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (4,4,'kwasy tłuszczowe nasycone');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (5,5,'węglowodany');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (6,6,'cukry');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (7,7,'błonnik');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (8,8,'sód');
insert into Basic_Ingredient_Name (bin_id,bi_id,possible_name) VALUES (10,1,'wartość energetyczna');

insert into Health_Relevant_Ingredient (hr_id,name) VALUES (1,'energia');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (2,'białko');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (3,'tłuszcz');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (4,'tłuszcz nasycony');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (5,'węglowodany');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (6,'cukry');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (7,'błonnik');
insert into Health_Relevant_Ingredient (hr_id,name) VALUES (8,'sód');


insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (1,1,1);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (2,2,2);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (3,3,3);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (4,4,4);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (5,5,5);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (6,6,6);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (7,7,7);
insert into Basic_Ingredient_Health_Relevance (bihr_id,hr_id,bi_id) VALUES (8,8,8);
