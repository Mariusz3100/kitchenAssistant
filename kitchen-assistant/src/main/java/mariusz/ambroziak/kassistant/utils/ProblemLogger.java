package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProblemDAO;
import mariusz.ambroziak.kassistant.model.Problem;

public class ProblemLogger {
	public static final int singleMessageLength = 90;
	
	public static void logProblem(String message){
		System.err.println(message);
		DaoProvider provider = DaoProvider.getInstance();
		if(provider==null)
			return;
		if(message.length()<singleMessageLength){
			Problem p=new Problem(0l,true, message,false);
			provider.getProblemDao().addProblem(p);
		}else{
			Long nextId=0l;
			while(message.length()>0){
				String smallMessage;
				boolean firstMessage=false;
				if(message.length()>singleMessageLength){
					smallMessage=message.substring(message.length()-singleMessageLength,message.length());
					message=message.substring(0, message.length()-singleMessageLength);
				}else{
					firstMessage=true;
					smallMessage=message;
					message="";
				}
				
				

				Problem p=new Problem(nextId,firstMessage,smallMessage,false);
				
				provider.getProblemDao().addProblem(p);
				nextId=p.getP_id();
			}
			
			
		}
			
		
		
	}
	
	
	
	public static void logStackTrace( StackTraceElement[] stackTrace){
		if(stackTrace.length==0||stackTrace==null){
			Problem p=new Problem(0l,true,"Empty StackTrace recorded",false);
			DaoProvider.getInstance().getProblemDao().addProblem(p);
		}else{
			String stackTraceMessage="";

			for(int i=0;i<stackTrace.length;i++){
				stackTraceMessage+="<p style=\"text-indent:"+i*10+"px\">"+stackTrace[i].toString();
			}
			logProblem(stackTraceMessage);
		}
	}
	
	
	public static void main(String[] args){
		ProblemLogger.logProblem("test problem....."
				+ "................................ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd................................................................................................................................................................");
		
	}
}
