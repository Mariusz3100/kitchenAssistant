package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;

public class UsdaApiParameters {

	public static String getApp_key() {
		return app_key;
	}
	private static String app_key="cUtBi6FvIBwHWgDGNfnnHB18zuI2K6LSj9NsNe0c";
	
	private static String particularProduktBaseUrl="https://api.edamam.com/api/nutrition-details";
	
	public static String getBaseUrl() {
		return particularProduktBaseUrl;
	}


	private String phrase;

	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	
}
