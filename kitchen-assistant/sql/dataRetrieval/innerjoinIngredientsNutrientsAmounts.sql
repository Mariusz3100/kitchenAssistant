select * from basic_ingredient_nutrient_amount
 inner join Nutrient on Nutrient.nu_id=basic_ingredient_nutrient_amount.nu_id 
 inner join basic_ingredient on basic_ingredient.bi_id=basic_ingredient_nutrient_amount.bi_id
 where basic_ingredient.name='a'
