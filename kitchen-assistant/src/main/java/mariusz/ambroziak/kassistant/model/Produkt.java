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

import mariusz.ambroziak.kassistant.utils.ProblemLogger;



@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Produkt implements Comparable<Produkt>{
	public static final int opis_length=5000;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long p_id;



	@NotNull
	@Size(min = 1, max = 200)
	private String url;

	@Size(min = 1, max = 200)
	private String quantity_phrase;


	@NotNull
	@Size(min = 1, max = 200)
	private String nazwa;


	@NotNull
	@Size(min = 1, max = 2700)
	private String sklad;


	@NotNull
	@Size(min = 1, max = opis_length)
	private String opis;

	@NotNull
	private float cena;

	@NotNull
	private boolean przetworzony=false;

	@NotNull
	private String metadata;




	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public Produkt(String nazwa2, String detailsUrl) {
		this(detailsUrl,"",nazwa2,"","", -1,false);
	}

	public Produkt() {
		super();
	}

//	public Produkt(String url, String nazwa, String sklad, String opis,
//			float cena, boolean przetworzony) {
//		super();
//		this.url = url;
//		this.nazwa = nazwa;
//		this.sklad = sklad;
//		this.opis = opis;
//		this.cena = cena;
//		this.przetworzony = przetworzony;
//
//	}




	public Long getP_id() {
		return p_id;
	}

	public Produkt(String url, String quantity_phrase, String nazwa, String sklad, String opis, float cena,
			boolean przetworzony) {
		super();
		this.url = url;
		this.quantity_phrase = quantity_phrase;
		this.nazwa = nazwa;
		this.sklad = sklad;
		this.opis = opis;
		this.cena = cena;
		this.przetworzony = przetworzony;
	}

	public Produkt(String url, String quantity_phrase, String nazwa, String sklad, String opis, float cena,
			boolean przetworzony, String metadata) {
		super();
		this.url = url;
		this.quantity_phrase = quantity_phrase;
		this.nazwa = nazwa;
		this.sklad = sklad;
		this.opis = opis;
		this.cena = cena;
		this.przetworzony = przetworzony;
		this.metadata = metadata;
	}

	public void setP_id(Long p_id) {
		this.p_id = p_id;
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
		if(opis!=null){
			if(opis.length()>opis_length){
				ProblemLogger.logProblem("opis produktu \""
						+opis+"\" jest za długi. Maksymalna długość to "+opis_length);
				opis=opis.substring(0,opis_length);
			}
		}
		this.opis = opis;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	public boolean isPrzetworzony() {
		return przetworzony;
	}

	public void setPrzetworzony(boolean przetworzony) {
		this.przetworzony = przetworzony;
	}


	public String getQuantityPhrase() {
		return quantity_phrase;
	}

	public void setQuantityPhrase(String quantityPhrase) {
		this.quantity_phrase = quantityPhrase;
	}


	@Override
	public String toString() {
		return "Produkt [id=" + p_id + ", url=" + url + ", nazwa=" + nazwa
				+ ", sklad=" + sklad + ", opis=" + opis + ", cena=" + cena
				+ ", przetworzony=" + przetworzony+"]";
	}

	@Override
	public int compareTo(Produkt o2) {
		if(this==o2)
			return 0;
		else if(o2==null)
			return 1;
		else if(this.getNazwa()==o2.getNazwa())
			return 0;
		else if(o2.getNazwa()==null)
			return 1;
		else if(this.getNazwa()==null)		
			return -1;
		else return this.getNazwa().compareTo(o2.getNazwa());
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
