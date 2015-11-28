CREATE TABLE Produkt
(
  p_id serial not null,
  urlId varchar(100) not null,
  shopId varchar(5),
  nazwa varchar(200),
  sklad varchar(2000),
  opis varchar(2000),
  cena real,
  przetworzony boolean not null default false,
  wstepnie_przetworzony boolean not null default false,

  

 CONSTRAINT produkt_pk PRIMARY KEY (p_id)
)


CREATE INDEX produkt_urlId_index
 ON produkt
 ( urlId );

CREATE INDEX produkt_shopId_index
 ON produkt
 ( shopId );