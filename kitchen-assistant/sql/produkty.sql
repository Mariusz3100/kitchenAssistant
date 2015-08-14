CREATE TABLE Produkt
(
  id serial not null,
  url varchar(200) not null,
  nazwa varchar(200),
  sklad varchar(2000),
  opis varchar(2000),
  cena real,
  przetworzony boolean not null default false,
  wstepnie_przetworzony boolean not null default false,

  

 CONSTRAINT product_pk PRIMARY KEY (id)
)


CREATE INDEX product_url_index
 ON product
 ( url );

insert into product (p_id,  url,nazwa,sklad,opis)values(__id__,'__url__','__nazwa__','__sklad__','__opis__')

insert into product (p_id,  url,nazwa,sklad,opis)values(1,'http://www.auchandirect.pl/sklep/artykuly/1165_1173/92701424/Warzywa-i-Owoce/Warzywa-$015bwie$017ce/Pomidor-podluzny-Roma-500-g','Pomidor podĹ‚uĹĽny Roma','SkĹ‚adniki i wartoĹ›ci odĹĽywcze Pomidor podĹ‚uĹĽny tacka','Opis produktu NOWALIJKA Pomidor podĹ‚uĹĽny tacka Typ handlowy: podĹ‚uĹĽny, kaliber: 47-57 mm, klasa: 1. Tacka 500 g Wyprodukowano w Hiszpanii');

insert into product (p_id,  url,nazwa,sklad,opis)values(1,'http://www.auchandirect.pl/sklep/artykuly/1165_1173/92700857/Warzywa-i-Owoce/Warzywa-$015bwie$017ce/Pomidor-sztuka-$0028okolo-6-7-sztuk-na-kilogram$0029-1-szt','Pomidor sztuka (okoĹ‚o 6-7 sztuk na kilogram)','SkĹ‚adniki i wartoĹ›ci odĹĽywcze pomidor','Opis produktu Pomidor sztuka Smaczne, soczyste warzywo, ktĂłre mozna spoĹĽywac na surowo na kanapkach lub jako jeden ze skladnikĂłw zup, sosĂłw i saĹ‚atek. Pomidory sÄ… maĹ‚o kaloryczne, dziaĹ‚ajÄ… odmĹ‚adzajÄ…co i uspokajajÄ…co. Kraj pochodzenia: Polska');

drop table product
truncate table product  
select * from product;

SELECT currentval('product_id_seq');

truncate table 	product
insert into product (p_id,  url,nazwa,sklad,opis)values( nextval('product_id_seq') ,'http://www.auchandirect.pl/sklep/artykuly/1165_1173/92700268/Warzywa-i-Owoce/Warzywa-$015bwie$017ce/Buraczki-obiadowe-500-g','Buraczki obiadowe','Składniki i wartości odżywcze buraczki czerwone, ocet, sól, cukier, przyprawy roślinne Wartości odżywcze na 100 g Wartosc energetyczna kJ 192 kJ Wartosc energetyczna kcal 45 kcal Bialko g 0,7 g Weglowodany g 9,9 g W tym cukry g 9,6 g Tluszcz g 0,1 g W tym kwasy tluszczowe nasycone g 0,1 g Blonnik g 1,5 g Sod g 0,26 g','Opis produktu Kuchnia polska Buraczki obiadowe Torebka plastikowa 500 g Wyprodukowano w Polsce');