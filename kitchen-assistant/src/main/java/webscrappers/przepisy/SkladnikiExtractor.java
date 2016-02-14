package webscrappers.przepisy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.AbstractQuantityExtracter.QuantityTranslation;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class SkladnikiExtractor extends AbstractQuantityExtracter {

	public static void main(String[] arg){
		File file = new File("C:\\agentsWorkspaceMay\\skladniki.txt");
		System.out.println(file.exists());
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			inputStreamReader.getEncoding();
			
			BufferedReader br=new BufferedReader(inputStreamReader);
			
			String line=null;
			
			while((line=br.readLine())!=null){
				if(line.equals("")){
					continue;
				}
				String[] elems=line.split("\t");
				
				QuantityProdukt extract = extract(elems[0], elems[1]);
				
				String extractDesc=extract==null?"null!!!":(extract.getProduktPhrase()+" : "
						+extract.getAmount()+" "+extract.getAmountType());
				
				System.out.println(line.replaceAll("\t", " ")+" -> "+extractDesc);
				
				
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static QuantityProdukt extract(String searchPhrase, String quantityPhrase){
		boolean retValueQOk=false;
		boolean retValuePOk=false;
		QuantityProdukt retValue =null;
		String ingredient=searchPhrase;
		
		if(ingredient.indexOf('(')>0&&ingredient.indexOf(')')>0){
			String attemptedQ=
					ingredient.substring(ingredient.indexOf('(')+1,ingredient.indexOf(')'));
				
			try{
				Quantity extractedQuantity = extractQuantity(attemptedQ);
				
				if(extractedQuantity!=null||
						(retValue.getAmount()==-1&&AmountTypes.szt.equals(retValue.getAmountType()))){
					retValue=new QuantityProdukt();
					retValue.setAmountType(extractedQuantity.getType());
					retValue.setAmount(extractedQuantity.getAmount());
					retValue.setProduktPhrase(searchPhrase);
				
				
					ingredient=ingredient.replaceAll(Pattern.quote(attemptedQ), "")
						.replaceAll("\\(", "")
						.replaceAll("\\)", "").trim();
				}
			}catch(IllegalArgumentException ex){
				retValue=null;
			}
			
			
			
		}
		
		
		if(retValue==null){
			Quantity extractedQuantity = extractQuantity(quantityPhrase);
			
			retValue=new QuantityProdukt();
			
			
			retValue.setAmountType(extractedQuantity.getType());
			retValue.setAmount(extractedQuantity.getAmount());
			
			retValue.setProduktPhrase(searchPhrase);
			
		}
		
		if(retValue.getAmount()==-1&&AmountTypes.szt.equals(retValue.getAmountType())){
			ProblemLogger.logProblem("Nie uda³o siê wyekstrachowaæ miary z "+searchPhrase+"|"+quantityPhrase);
			retValue.setAmount(1);
			//TODO zostawiæ -1, by obs³uzyæ gdzie indziej?
		}
		
		return retValue;
	}
	

	
	public static Quantity extractQuantity(String text){
		Quantity retValue=new Quantity();
		
		if(text==null||text.equals(""))
		{
			retValue.setAmount(-1);
			retValue.setType(AmountTypes.szt);
			return retValue;
		}
		
		for(String x:ommissions)
			text=text.replaceAll(x, "");
			
		text=text.trim();
		
		String[] elems=text.split(" ");
		
		retValue.setAmount(Float.parseFloat(elems[0].replaceAll(",", ".")));
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(elems[1])){
				retValue.setType(at);
			}
		}
		
		if(retValue.getType()==null){
			QuantityTranslation quantityTranslation = translations.get(elems[1]);
			
			if(quantityTranslation==null){
				Base_Word base_Name = 
					DaoProvider.getInstance().getVariantWordDao().getBase_Name(elems[1]);
				
				String new_word=null;
				
				
				
				if(base_Name!=null)
					new_word = 	base_Name.getB_word();		
				
				if(new_word==null)
					new_word=SJPWebScrapper.getAndSaveNewRelation(elems[1]);
					
				if(new_word!=null)
					quantityTranslation = translations.get(new_word);
				
			}
			
			

			
			if(quantityTranslation!=null)
			{
				retValue.setType(quantityTranslation.getTargetAmountType());
				retValue.setAmount(retValue.getAmount()*quantityTranslation.getMultiplier());
				
				
			}else{
				retValue.setAmount(-1);
				retValue.setType(AmountTypes.szt);
				
				ProblemLogger.logProblem("Nieznana miara "+elems[1]);
				return retValue;
			}
		}
		
		return retValue;
		
		
	}
	
	static class Quantity{
		private AmountTypes type;
		private float amount;
		
		public AmountTypes getType() {
			return type;
		}
		public void setType(AmountTypes type) {
			this.type = type;
		}
		public float getAmount() {
			return amount;
		}
		public void setAmount(float amount) {
			this.amount = amount;
		}
	}

}
