package mariusz.ambroziak.kassistant.tesco;

import api.extractors.AbstractQuantityEngExtractor;
import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class TescoQExtractor extends AbstractQuantityEngExtractor{
	
	
	
	public static QuantityTranslation getTranslationToBaseType(String amount) {
		String temp=(amount==null?"":amount).toLowerCase().trim();
		QuantityTranslation retValue=translations.get(temp.toLowerCase());
		
		if(retValue==null) {
			ProblemLogger.logProblem(amount+ " wasn't parsed properly");
			retValue=new QuantityTranslation(AmountTypes.szt, 0);
		}
		
		
		return retValue;
		
		
	}
}
