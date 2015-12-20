package webscrappers.przepisy;

import java.util.ArrayList;

public class WordsBank {
	protected static ArrayList<String> lacznikiOR;
	protected static ArrayList<String> lacznikiAND;

	
	static{
		
		lacznikiAND=new ArrayList<String>();

		lacznikiAND.add("i");
		lacznikiAND.add("oraz");
		lacznikiAND.add("+");
//		lacznikiOR.add("i");
//		lacznikiOR.add("i");
//		lacznikiOR.add("i");
		
		lacznikiOR=new ArrayList<String>();
		
		lacznikiOR.add("lub");
		lacznikiOR.add("albo");
		
		
	}
	
	
}
