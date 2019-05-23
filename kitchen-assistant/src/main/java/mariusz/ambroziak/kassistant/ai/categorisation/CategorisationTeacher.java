package mariusz.ambroziak.kassistant.ai.categorisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.springframework.core.io.Resource;

import com.google.api.services.drive.model.File;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;

public class CategorisationTeacher{

	
	
	
	public static List<String> checkCorectness(){

		
		String testFileContents = getTestFileContents();
		ArrayList<String> retValue=new ArrayList<String>();
		String[] lines = testFileContents.split("<");
		int greens=0,reds=0,yellows=0;

		for(String line1:lines) {
			String line=line1.replaceAll("\n", "");
			String[] lineSplitted = line.split("#");
			
			String name=lineSplitted[0];
			String url=lineSplitted[1];
			
			String catPhrase=lineSplitted[2].replace("->", "");
			
			String x = name+" "+url+"     "+catPhrase;
			
			
			Produkt produktByShopId = TescoApiClientParticularProduct_notUsed.getProduktByUrlWithExtensiveMetadata(url);
//			produktByShopId = TescoApiClientParticularProduct_notUsed.updateParticularProduct(produktByShopId);

			Category assignCategory = Categoriser.assignCategory(produktByShopId);
			
			String htmlLine=name+" "+url+" :: ";
			if(catPhrase.startsWith(assignCategory.getName())) {
				greens++;
				htmlLine+="<span style=\"background-color:green\">"+assignCategory+"(("+catPhrase+"))</span>";
			}else if(catPhrase.endsWith("??")) {
				yellows++;
				htmlLine+="<span style=\"background-color:yellow\">"+assignCategory+"(("+catPhrase+"))</span>";
			}else {
				reds++;
				htmlLine+="<span style=\"background-color:red\">"+assignCategory+"(("+catPhrase+"))</span>";
			}
			
				
			
			
			retValue.add(htmlLine);
			System.out.println(htmlLine);
			
		}
		
		retValue.add("<b>Reds:"+reds+"   yellows:"+yellows+"    greens:"+greens+"    All:"+reds+yellows+greens+"</b>");

		
		return retValue;
	}

	private static String getTestFileContents() {
		Resource teachingExpectationsFile = FilesProvider.getInstance().getTeachingExpectationsFile();
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
	
	public static boolean checkCorrectness(Produkt p,Category c) {
		return false;
		
		
	}
}
