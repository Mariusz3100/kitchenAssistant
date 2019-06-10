package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.core.io.Resource;

import com.google.api.services.drive.model.File;

import api.extractors.AbstractQuantityEngExtractor;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;

public class EdamanCategorisationTeacher {

	
	public static Map<String,IngredientParsed> checkCorectness_inEdaman(){
		Map<String, String> teachingExcercise = getTeachingExcercise();
		Map<String,IngredientParsed> retValue=new HashMap<String, IngredientParsed>();
		
		for(String phrase:teachingExcercise.keySet()) {
			IngredientParsed parsed = EdamanCategoriser.getSingleton().parseIngredient(phrase);
			retValue.put(phrase, parsed);
		}
		
		return retValue;
		
			
	}

	public static Map<String,String> getTeachingExcercise() {
		String teachingEdamanContents = getTeachingEdamanContents();
		String[] split = teachingEdamanContents.split("<");
		Map<String,String> ingredientWithTargetCats=new HashMap<String, String>();
		for(String line:split) {
			String[] lineSplitted = line.split("->");
			
			String ingredientPhrase=lineSplitted[0];
			String catPhrase=lineSplitted[1].trim();
			
			ingredientWithTargetCats.put(ingredientPhrase, catPhrase);
		}
		
		return ingredientWithTargetCats;
	}
	
		private static String getTeachingEdamanContents() {
			Resource teachingExpectationsFile = FilesProvider.getInstance().getTeachingEdamanFile();
			StringBuilder content=new StringBuilder();

			InputStream inputStream;
			try {
				inputStream = teachingExpectationsFile.getInputStream();

				BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
				String temp=br.readLine();
				while(temp!=null) {
					content.append(temp);
					temp=br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return content.toString();
		}
//
//		
//	public static List<String> checkCorectness_inShopCom(){
//		String testFileContents = getTeachingShopComFileContents();
//		ArrayList<String> retValue=new ArrayList<String>();
//		String[] lines = testFileContents.split("<");
//		int greens=0,reds=0,yellows=0;
//
//		for(String line1:lines) {
//			String line=line1.replaceAll("\n", "");
//			String[] lineSplitted = line.split("#");
//
//			String name=lineSplitted[0];
//			String id=lineSplitted[1];
//
//			String catPhrase=lineSplitted[2].replace("->", "");
//			Produkt produktByShopId = ShopComApiClientParticularProduct.getProduktByShopId(id);
//			if(produktByShopId!=null) {
//				Category assignCategory = Categoriser.assignCategory(produktByShopId);
//
//				String htmlLine=name+" "+id+" :: ";
//				if(catPhrase.startsWith(assignCategory.getName())) {
//					greens++;
//					htmlLine+="<span style=\"background-color:green\">"+assignCategory+"(("+catPhrase+"))</span>";
//				}else if(catPhrase.endsWith("??")) {
//					yellows++;
//					htmlLine+="<span style=\"background-color:yellow\">"+assignCategory+"(("+catPhrase+"))</span>";
//				}else {
//					reds++;
//					htmlLine+="<span style=\"background-color:red\">"+assignCategory+"(("+catPhrase+"))</span>";
//				}
//				retValue.add(htmlLine);
//				System.out.println(htmlLine);
//			}
//
//		}
//
//		retValue.add("<b>Reds:"+reds+"   yellows:"+yellows+"    greens:"+greens+"    All:"+(reds+yellows+greens)+"</b>");
//
//
//		return retValue;
//	}
//
//	public static List<String> TescoCheckCorectness(){
//		String testFileContents = getTeachingTescoContents();
//		ArrayList<String> retValue=new ArrayList<String>();
//		String[] lines = testFileContents.split("<");
//		int greens=0,reds=0,yellows=0;
//
//		for(String line1:lines) {
//			String line=line1.replaceAll("\n", "");
//			String[] lineSplitted = line.split("#");
//
//			String name=lineSplitted[0];
//			String url=lineSplitted[1];
//
//			String catPhrase=lineSplitted[2].replace("->", "");
//			Produkt produktByShopId = TescoApiClientParticularProduct_notUsed.getProduktByUrlWithExtensiveMetadata(url);
//			Category assignCategory = Categoriser.assignCategory(produktByShopId);
//
//			String htmlLine=name+" "+url+" :: ";
//			if(catPhrase.startsWith(assignCategory.getName())) {
//				greens++;
//				htmlLine+="<span style=\"background-color:green\">"+assignCategory+"(("+catPhrase+"))</span>";
//			}else if(catPhrase.endsWith("??")) {
//				yellows++;
//				htmlLine+="<span style=\"background-color:yellow\">"+assignCategory+"(("+catPhrase+"))</span>";
//			}else {
//				reds++;
//				htmlLine+="<span style=\"background-color:red\">"+assignCategory+"(("+catPhrase+"))</span>";
//			}
//			retValue.add(htmlLine);
//			System.out.println(htmlLine);
//
//		}
//
//		retValue.add("<b>Reds:"+reds+"   yellows:"+yellows+"    greens:"+greens+"    All:"+(reds+yellows+greens)+"</b>");
//
//
//		return retValue;
//	}
//
//
//
//	private static String getTeachingTescoContents() {
//		Resource teachingExpectationsFile = FilesProvider.getInstance().getTeachingTescoFile();
//		StringBuilder content=new StringBuilder();
//
//		InputStream inputStream;
//		try {
//			inputStream = teachingExpectationsFile.getInputStream();
//
//			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
//			String temp=br.readLine();
//			while(temp!=null) {
//				content.append(temp);
//				temp=br.readLine();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return content.toString();
//	}
//
//	private static String getTeachingShopComFileContents() {
//		Resource teachingExpectationsFile = FilesProvider.getInstance().getTeachingShopComFile();
//		StringBuilder content=new StringBuilder();
//
//		InputStream inputStream;
//		try {
//			inputStream = teachingExpectationsFile.getInputStream();
//
//			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
//			String temp=br.readLine();
//			while(temp!=null) {
//				content.append(temp);
//				temp=br.readLine();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return content.toString();
//	}
//
//	public static boolean checkCorrectness(Produkt p,Category c) {
//		return false;
//
//
//	}
}
