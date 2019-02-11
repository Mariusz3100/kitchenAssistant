package mariusz.ambroziak.kassistant.agents;

import java.util.Comparator;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;

public class CompareByName implements Comparator<Basic_Ingredient> {

	@Override
	public int compare(Basic_Ingredient o1, Basic_Ingredient o2) {
		if(o1==o2) {
			return 0;
		}else {
			if(o1.getName()!=null&&o2.getName()==null) {
				return 1;
			}else {
				if(o1.getName()==null&&o2.getName()!=null) {
					return -1;
				}else {
					if(o1.getName().length()==o2.getName().length()) {
						return 0;
					}else {
						if(o1.getName().length()>o2.getName().length()) {
							return 1;
						}else {
							return -1;
						}
					}
				}
			}
		}
	}

}
