package mariusz.ambroziak.kassistant.Apiclients.shopcom;

public class ShopComApiParameters {
	public static String allperms="false";
	public static String apikey="l7xxeb4363ce0bcc441eb94134734dec9aed";
	public static String getAllperms() {
		return allperms;
	}

	public static String getApikey() {
		return apikey;
	}

	
	public static String getQuestionMarkApikeyAndPerms() {
		return "?apikey="+apikey+"&allperms="+allperms;
	}
	
	public static String getUrlWithKeys(String url) {
		return url+"?apikey="+apikey+"&allperms="+allperms;
	}
	
	
}
