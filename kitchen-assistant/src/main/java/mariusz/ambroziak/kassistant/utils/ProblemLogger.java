package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Problem;

public class ProblemLogger {
	public static final int singleMessageLength = 90;
	
	public static void logProblem(String message){
		
		if(message.length()<singleMessageLength){
			Problem p=new Problem(0l,message,false);
			DaoProvider.getInstance().getProblemDao().addProblem(p);
		}else{
			Long nextId=0l;
			while(message.length()>0){
				String smallMessage;
				if(message.length()>singleMessageLength){
					smallMessage=message.substring(message.length()-singleMessageLength,message.length());
					message=message.substring(0, message.length()-singleMessageLength);
				}else{
					smallMessage=message;
					message="";
				}
				
				

				Problem p=new Problem(nextId,smallMessage,false);
				
				DaoProvider.getInstance().getProblemDao().addProblem(p);
				nextId=p.getP_id();
			}
			
			
		}
			
		
		
	}
	
	
	public static void main(String[] args){
		ProblemLogger.logProblem("test problem....."
				+ "................................ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd................................................................................................................................................................");
		
	}
}