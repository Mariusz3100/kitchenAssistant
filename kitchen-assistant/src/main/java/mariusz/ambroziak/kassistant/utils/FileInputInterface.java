package mariusz.ambroziak.kassistant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.naming.directory.InvalidAttributesException;


public class FileInputInterface {
	private int counter;
	private File inputFile;
	String lineBuffer="";

	public FileInputInterface(String filename){
		inputFile=new File(StringHolder.workingDirectory+filename);
		counter=0;

	}


	public String getLine(){
		String retValue=null;
		BufferedReader br=null;
		
		while(lineBuffer.length()<1){

			try {
				
				if(br==null)br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
				
				String temp=br.readLine();
				if(temp==null)System.out.println("\n\nOutOfFile\n\n");
				for(int i=0;(lineBuffer.length()<1)||(i<10&&temp!=null);i++){
					
					if(temp.contains("\t"))
					{
						int count=Integer.parseInt(temp.split("\t")[0]);
						if(count<=counter){
							temp=br.readLine();
							continue;
						}else{
							counter=count;
							String url=temp.split("\t")[1];
							lineBuffer+=url+"\n";
							temp=br.readLine();
						}
					}else{
						throw new InvalidAttributesException("input without counter");
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAttributesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}

		retValue=lineBuffer.substring(0,lineBuffer.indexOf('\n'));
		String rest=lineBuffer.substring(lineBuffer.indexOf('\n')+1);
		lineBuffer=rest;
		return retValue;

	}
	
	public static void main(String arg[]) throws InterruptedException{
		FileInputInterface fii=new FileInputInterface("\\entries\\auchanEntriesCounted.txt");
		while(true){
			System.out.println(fii.getLine());
			System.out.println(fii.getLine());
			System.out.println(fii.getLine());
			Thread.sleep(700);
		}
	
	}
	
	
}
