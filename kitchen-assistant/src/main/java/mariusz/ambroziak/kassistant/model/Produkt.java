package mariusz.ambroziak.kassistant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Produkt {

	   @Id
	   @GeneratedValue(strategy=GenerationType.AUTO)
	   private Long id;
	   
	

	@NotNull
	   @Size(min = 1, max = 200)
	   private String url;
	   
	   
	   @NotNull
	   @Size(min = 1, max = 200)
	   private String nazwa;
	   
	   
	   @NotNull
	   @Size(min = 1, max = 2000)
	   private String sklad;
	   
	   
	   @NotNull
	   @Size(min = 1, max = 2000)
	   private String opis;
	   
	   @NotNull
	   private long cena;
	   
	   @NotNull
	   private boolean przetworzony=false;
	   
	   @NotNull
	   private boolean wstepnie_przetworzony=false;
	   
	   
	   
	public Produkt(String nazwa2, String detailsUrl) {
		this(detailsUrl,nazwa2,"","",-1,false,false);
	}

	public Produkt() {
		super();
	}

	public Produkt(String url, String nazwa, String sklad, String opis,
			long cena, boolean przetworzony, boolean wstepnie_przetworzony) {
		super();
		this.url = url;
		this.nazwa = nazwa;
		this.sklad = sklad;
		this.opis = opis;
		this.cena = cena;
		this.przetworzony = przetworzony;
		this.wstepnie_przetworzony = wstepnie_przetworzony;
	}

	public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getNazwa() {
			return nazwa;
		}

		public void setNazwa(String nazwa) {
			this.nazwa = nazwa;
		}

		public String getSklad() {
			return sklad;
		}

		public void setSklad(String sklad) {
			this.sklad = sklad;
		}

		public String getOpis() {
			return opis;
		}

		public void setOpis(String opis) {
			this.opis = opis;
		}

		public long getCena() {
			return cena;
		}

		public void setCena(long cena) {
			this.cena = cena;
		}

		public boolean isPrzetworzony() {
			return przetworzony;
		}

		public void setPrzetworzony(boolean przetworzony) {
			this.przetworzony = przetworzony;
		}

		public boolean isWstepnie_przetworzony() {
			return wstepnie_przetworzony;
		}

		public void setWstepnie_przetworzony(boolean wstepnie_przetworzony) {
			this.wstepnie_przetworzony = wstepnie_przetworzony;
		}
}


/*
 *  nazwa varchar(200),
  sklad varchar(2000),
  opis varchar(2000),
  price real,
  processed boolean not null default false,
  brieflyProcessed boolean not null default false,
 */