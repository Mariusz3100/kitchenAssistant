package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Problem;

public class ProblemLogger {
	public static final int singleMessageLength = 200;
	
	public static void logProblem(String message){
		
		if(message.length()<singleMessageLength){
			Problem p=new Problem(0l,message,false);
			DaoProvider.getInstance().getProblemDao().addProblem(p);
		}else{
			Long nextId=0l;
			while(message.length()>singleMessageLength){
				String smallMessage=message.substring(message.length()-200,message.length());
				message=message.substring(0, message.length()-200);
				
				Problem p=new Problem(nextId,smallMessage,false);
				
				DaoProvider.getInstance().getProblemDao().addProblem(p);
				nextId=p.getP_id();
			}
			
			
		}
			
		
		
	}
}
