import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Test;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.NotPreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import webscrappers.auchan.AuchanRecipeParser;

public class SkladParseTest {

	@Test
	public void test() {
		String sklad="pomidory rozdrobnione, MLEKO pe�ne, grysik PSZENNY utwardzany, mi�so wo�owe 12,6%, woda, olej rzepakowy, mi�so wieprzowe 4,4%, �mietana(z MLEKA), ser edamski(z MLEKA) 3,8%, skrobia, m�ka PSZENNA, JAJA, ros� wo�owy(woda, mi�so wo�owe, s�l, koncentrat soku z warzyw(pomidory, marchew, cebula), marchew, cebula), cukier, s�l, cebula sma�ona(cebula, olej s�onecznikowy), pieczarki, marchew, por, cebula, zio�a, skrobia PSZENNA, przyprawy, czosnek";

		ArrayList<ProduktIngredientQuantity> parsedSkladniki = AuchanRecipeParser.parseSkladString(new PreciseQuantity(1000,AmountTypes.mg),sklad);
		
		for(ProduktIngredientQuantity piq:parsedSkladniki){
			System.out.println(piq);
		}
		
		assertProduktIngredientQuantityRecursively(parsedSkladniki);
	}

	
	private void assertProduktIngredientQuantityRecursively(ArrayList<? extends ProduktIngredientQuantity> skladniki) {
		for(ProduktIngredientQuantity piq:skladniki){
			assertTrue(piq.getAmount().isValid());
			if (piq instanceof CompoundIngredientQuantity) {
				CompoundIngredientQuantity ciq = (CompoundIngredientQuantity) piq;
				assertProduktIngredientQuantityRecursively(ciq.getAllBasicIngredients());
				
			}
			
		}
	}

	
	
}
