package mariusz.ambroziak.kassistant;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.dao.Amount_TypeDAO;
import mariusz.ambroziak.kassistant.dao.Base_WordDAO;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.dao.RecipeDAO;
import mariusz.ambroziak.kassistant.dao.UserDAO;
import mariusz.ambroziak.kassistant.dao.Variant_WordDAO;
import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.utils.AmountTypes;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private ProduktDAO produktDao;

	@Autowired
	private RecipeDAO recipeDao;

	@Autowired
	private Variant_WordDAO variant_wordDao;

	@Autowired
	private Base_WordDAO base_wordDao;

	@Autowired
	private Amount_TypeDAO amountTypeDao;

	@RequestMapping(value="/users")
	public ModelAndView users() {
		List<User> listUsers = userDao.list();
		ModelAndView model = new ModelAndView("usersList");
		model.addObject("userList", listUsers);
		return model;
	}

	@RequestMapping(value="/vwords")
	public ModelAndView words() {
		List<Variant_Word> vWordList = variant_wordDao.list();
		ModelAndView model = new ModelAndView("List");
		
		ArrayList<String> strings=new ArrayList<String>();
		
		for(Variant_Word v:vWordList){
			strings.add(v.getId()+":"+v.getV_word()
					+" -> "+v.getBase_word().getId()+":"+v.getBase_word().getB_word());
		}
		
		model.addObject("list", strings);
		return model;
	}
	@RequestMapping(value="/bwords")
	public ModelAndView bwords() {
		List<Base_Word> vWordList = base_wordDao.list();
		ModelAndView model = new ModelAndView("List");
		
		ArrayList<String> strings=new ArrayList<String>();
		
		for(Base_Word b:vWordList){
			String temp = b.getId()+":"+b.getB_word()
					+" -> ";
			Hibernate.initialize(b);
			for(Variant_Word v:b.getVariantWords()){
				temp+=v.getV_word()+",";
			}
			
			strings.add(temp);
		}
		
		model.addObject("list", strings);
		return model;
	}
	
	
	@RequestMapping(value="/bwords1")
	public ModelAndView bwords1() {
		Base_Word word = base_wordDao.getBase_NameInitializedVar("jajko");
		ModelAndView model = new ModelAndView("List");
		
		ArrayList<String> strings=new ArrayList<String>();
		
//		for(Base_Word b:vWordList){
			String temp = word.getId()+":"+word.getB_word()
					+" -> ";
			
//			base_wordDao.initializeVariantWords(b);
			for(Variant_Word v:word.getVariantWords()){
				temp+=v.getV_word()+",";
			}
			
			strings.add(temp);
//		}
		
		model.addObject("list", strings);
		return model;
	}
	
	@RequestMapping(value="/produkts")
	public ModelAndView produkts() {
		List<Produkt> listProdukts = produktDao.list();
		ModelAndView model = new ModelAndView("produktsList");
		model.addObject("produktList", listProdukts);
		return model;
	}

	@RequestMapping(value="/recipes")
	public ModelAndView recipes() {
		List<Recipe> listProdukts = recipeDao.list();
		ModelAndView model = new ModelAndView("recipeList");
		model.addObject("produktList", listProdukts);
		return model;
	}

	@ResponseBody
	@RequestMapping(value="/integrity")
	public String databaseIntegrity(){
		String response="<h3>System amount types:</h3><BR>";

		AmountTypes[] values = AmountTypes.values();
		if(values==null||values.length==0)
		{
			response+="nothing found";
		}else{
			for(AmountTypes at:values){
				response+=at+" ";
			}
		}
		response+="<BR><h3>Database amount types:</h3><BR>";

		List<Amount_Type> listTypes = amountTypeDao.list();
		if(listTypes==null||listTypes.size()==0)
		{
			response+="nothing found";
		}else{
			for(Amount_Type at:listTypes){
				response+=at.getNazwa()+" ";
			}
		}

		return response;
	}


	//	@RequestMapping(value="/produkts1")
	//	public ModelAndView produkts1() {
	//		List<Produkt> x= produktDao.list();
	//		
	//		ModelAndView model = new ModelAndView("produktsList");
	//		model.addObject("produktList", x);
	//		return model;
	//	}
	//	
	//	
	//	@RequestMapping(value="/produkts2")
	//	public ModelAndView produkts2() {
	//		List<Produkt> x= produktDao.getProduktsByNazwa("Cykoria tacka");
	//		
	//		ModelAndView model = new ModelAndView("produktsList");
	//		model.addObject("produktList", x);
	//		return model;
	//	}
	//	
	//	
	//	@RequestMapping(value="/bwords")
	//	@ResponseBody
	//	public String bwords() {
	//		List<Base_Word> x= base_wordDao.list();
	//		
	//
	//		return x.get(0).getB_word();
	//	}
	//	
	//	@RequestMapping(value="/vwords")
	//	@ResponseBody
	//	public String vwords() {
	//		List<Variant_Word> x= variant_wordDao.list();
	//		
	//
	//		return x.get(0).getV_word();
	//	}
	//	
	//	@RequestMapping(value="/vwords1")
	//	@ResponseBody
	//	public String vwords1() {
	//		List<Variant_Word> x= variant_wordDao.getVariant_Name("sa³atê");
	//		
	//
	//		return x.get(0).getV_word();
	//	}
	//	
	//	
	//	@RequestMapping(value="/vwords2")
	//	@ResponseBody
	//	public String vwords2() {
	//		List<Variant_Word> x= variant_wordDao.getVariant_Name("sa³atê");
	//
	//		Base_Word y= variant_wordDao.getBase_Name("sa³atê");
	//		
	//
	//		return x.get(0).getV_word()+"->"+y.getB_word();
	//	}
	//	
	//	@RequestMapping(value="/vwords3")
	//	@ResponseBody
	//	public String vwords3() {
	//		ArrayList<String>temp=new ArrayList<String>();
	//		temp.add("sa³atê");
	//		temp.add("czosnku");
	//		List<Base_Word> x= variant_wordDao.getBase_Names(temp);
	//
	//		
	//
	//		return x.get(0).getB_word()+" , "+x.get(1).getB_word();
	//	}
	//	
	//	@RequestMapping(value="/wordsProdukts")
	//	@ResponseBody
	//	public String vwords4() {
	//		ArrayList<String>temp=new ArrayList<String>();
	//		temp.add("ogórkiem");
	//		temp.add("sztuki");
	//		List<Produkt> x= produktDao.getProduktsByVariantNames(temp);
	//
	//		
	//
	//		return x.get(0).getNazwa()+" "+x.get(0).getUrl();
	//	}
}
