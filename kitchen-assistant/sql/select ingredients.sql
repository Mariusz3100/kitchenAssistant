select basic_ingredient.* from basic_ingredient_name inner join basic_ingredient on basic_ingredient_name.bi_id=basic_ingredient.bi_id
 where possible_name  ='energia' or name='energia' limit 1