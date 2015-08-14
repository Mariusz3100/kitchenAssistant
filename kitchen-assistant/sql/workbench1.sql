Select * from variant_word 
	inner join base_word on base_word.base_word_id=variant_word.base_word_id
		where variant_word.v_word='sałatę'



select * from base_word;


insert into base_word (base_word_id,b_word) VALUES (nextval('base_word_base_word_id_seq'),'yyy');

select nextval('base_word_base_word_id_seq') as nextVal;


select * from base_word  where b_word  ='sałata'


select * from variant_word   


select * from product where nazwa ilike '%pomidor%'


select * from base_word 
inner join variant_word on variant_word.base_word_id=base_word.base_word_id 
inner join product on product.nazwa ilike '%'||b_word||'%'



where v_word='sałatę'

select * from base_word 
inner join variant_word on variant_word.base_word_id=base_word.base_word_id 
inner join product on product.nazwa =b_word

where v_word='sałatę'

select * from product where nazwa ilike '%sałata%'

select * from product where nazwa ilike '%sałata lodowa%'
