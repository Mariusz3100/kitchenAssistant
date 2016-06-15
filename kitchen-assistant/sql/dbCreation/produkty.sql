CREATE TABLE Produkt
(
  p_id serial not null,
  url varchar(200) not null,
  nazwa varchar(200),
  sklad varchar(2000),
  quantity_Phrase varchar(200),
  opis varchar(2700),
  cena real,
  przetworzony boolean not null default false,
    

 CONSTRAINT produkt_pk PRIMARY KEY (p_id)
);


CREATE INDEX produkt_url_index
 ON produkt
 ( url );
