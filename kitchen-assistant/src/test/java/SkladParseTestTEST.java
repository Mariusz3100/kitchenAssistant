import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.Test;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.NotPreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import webscrappers.auchan.AuchanRecipeParser;

public class SkladParseTestTEST {

	@Test
	public void test() {
//		String sklad="pomidory rozdrobnione, MLEKO pe�ne, grysik PSZENNY utwardzany, mi�so wo�owe 12,6%, woda, olej rzepakowy, mi�so wieprzowe 4,4%, �mietana(z MLEKA), ser edamski(z MLEKA) 3,8%, skrobia, m�ka PSZENNA, JAJA, ros� wo�owy(woda, mi�so wo�owe, s�l, koncentrat soku z warzyw(pomidory, marchew, cebula), marchew, cebula), cukier, s�l, cebula sma�ona(cebula, olej s�onecznikowy), pieczarki, marchew, por, cebula, zio�a, skrobia PSZENNA, przyprawy, czosnek";
//
//		HashMap<String,QuantityWithName> mapaSkladnikow= new HashMap<String, QuantityWithName>();
//		
//		mapaSkladnikow.put("ser edamski(z MLEKA) 3,8%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("mi�so wo�owe 12,6%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("mi�so wieprzowe 4,4%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("�mietana(z MLEKA) 18,5%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));

		HashMap<String, String> x=new HashMap<String, String>();
		
		x.put("", "1");
		x.put(null, "2");
		x.put("", "3");
		x.put(null, "4");
		x.put("null", "5");
		Iterator<Entry<String, String>> iterator = x.entrySet().iterator();
		
		
		
		System.out.println();
		
	}

	
	
}
