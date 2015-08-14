CREATE TABLE nutr_Value
(
  nutr_Value_id serial not null,
  nazwa varchar(100),
  inneNazwy varchar(400),
  opis varchar(1000),
  
  

 CONSTRAINT nutrValue_pk PRIMARY KEY (nutr_Value_id)
)





CREATE TABLE nutr_val_amount
(
  nva_id serial not null,
  Product_id INTEGER NOT NULL,
  nutr_Value_id INTEGER NOT NULL,
  amountmg real NOT NULL,
  opis varchar(1000),
  
  

 CONSTRAINT nutrValueamount_pk PRIMARY KEY (nva_id )
)



ALTER TABLE nutr_val_amount ADD CONSTRAINT nva_NutrValue
FOREIGN KEY (nutr_Value_id)
REFERENCES nutr_Value (nutr_Value_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE nutr_val_amount ADD CONSTRAINT nva_product
FOREIGN KEY (Product_id)
REFERENCES Product (Product_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


insert into nutr_Value (nutr_Value_id,  nazwa)values(1,'bialko');
insert into nutr_Value (nutr_Value_id,  nazwa)values(2,'kalorie');
insert into nutr_Value (nutr_Value_id,  nazwa)values(3,'weglowodany');
insert into nutr_Value (nutr_Value_id,  nazwa)values(4,'cukry');
insert into nutr_Value (nutr_Value_id,  nazwa)values(5,'tluszcz');
insert into nutr_Value (nutr_Value_id,  nazwa)values(6,'weglowodany');
