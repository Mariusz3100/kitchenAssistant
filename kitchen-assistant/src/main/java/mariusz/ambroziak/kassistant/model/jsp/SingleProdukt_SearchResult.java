package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

public class SingleProdukt_SearchResult extends SearchResult{
	private Produkt produkt;
	private Float iloscSztuk=1f;
	public SingleProdukt_SearchResult(String searchPhrase, String produktPhrase,
			String quantity, Produkt produkt) {
		super(searchPhrase,produktPhrase,quantity);
		this.produktPhrase = produktPhrase;
		if(quantity!=null&&quantity.contains(JspStringHolder.QUANTITY_MULTIPLY_PREFIX_BORDER)){
			String[] splitted=quantity.split(JspStringHolder.QUANTITY_MULTIPLY_PREFIX_BORDER);
			iloscSztuk=Float.parseFloat(splitted[0]);
			quantityPhrase=splitted[1];
		}else{
			this.quantityPhrase = quantity;
		}
		this.produkt=produkt;
	}

	public Produkt getProdukt() {
		return produkt;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}
	
	public String getProduktNameAndSearchPhrase(){
		return getProdukt().getNazwa()+" ("+this.searchPhraseAnswered.getSearchPhrase()+")";
	}
	
	public Float getIloscSztuk() {
		return iloscSztuk;
	}
}
