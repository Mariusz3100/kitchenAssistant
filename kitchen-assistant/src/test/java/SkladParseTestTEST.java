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
//		String sklad="pomidory rozdrobnione, MLEKO pe³ne, grysik PSZENNY utwardzany, miêso wo³owe 12,6%, woda, olej rzepakowy, miêso wieprzowe 4,4%, œmietana(z MLEKA), ser edamski(z MLEKA) 3,8%, skrobia, m¹ka PSZENNA, JAJA, rosó³ wo³owy(woda, miêso wo³owe, sól, koncentrat soku z warzyw(pomidory, marchew, cebula), marchew, cebula), cukier, sól, cebula sma¿ona(cebula, olej s³onecznikowy), pieczarki, marchew, por, cebula, zio³a, skrobia PSZENNA, przyprawy, czosnek";
//
//		HashMap<String,QuantityWithName> mapaSkladnikow= new HashMap<String, QuantityWithName>();
//		
//		mapaSkladnikow.put("ser edamski(z MLEKA) 3,8%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("miêso wo³owe 12,6%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("miêso wieprzowe 4,4%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));
//		mapaSkladnikow.put("œmietana(z MLEKA) 18,5%", new QuantityWithName("ser edamski(z MLEKA) 3,8%", new PreciseQuantity(38, AmountTypes.mg)));

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
