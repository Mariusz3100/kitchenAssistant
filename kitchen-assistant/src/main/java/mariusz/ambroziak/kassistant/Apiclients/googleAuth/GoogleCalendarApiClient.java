package mariusz.ambroziak.kassistant.Apiclients.googleAuth;



import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.drive.model.*;

import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Export;
import com.google.api.services.drive.DriveScopes;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GoogleCalendarApiClient extends GoogleApi{


	public static Calendar getCalendarService(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		
		Credential credential = getCredential(accessToken);//new java.io.File("").getAbsolutePath()


		if(credential==null)
			return null;
		else
			return new Calendar.Builder(GoogleAuthApiParameters.getHTTP_TRANSPORT(),
				 GoogleAuthApiParameters.getJsonFactory(), credential)
	                .setApplicationName(GoogleAuthApiParameters.getApplicationName())
	                .build();
	}

	public static ArrayList<HealthLabels> getHealthLimitations(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		Events events = getCurrentEvents(accessToken);

		ArrayList<HealthLabels> retHealths=new ArrayList<HealthLabels>();
		
		for(Event event:events.getItems()) {
			if(event.getSummary()!=null
					&&event.getSummary().indexOf(StringHolder.GOOGLE_CALENDAR_HEALTH_EVENT)>=0)
			{
				String desc=event.getDescription();
				if(desc!=null) {
					String[] split = desc.split(StringHolder.GOOGLE_CALENDAR_LINE_SEPARATOR);
					
					for(String line:split) {
						
						HealthLabels healthLabel=HealthLabels.tryRetrieving(line);
						if(healthLabel!=null) {
							retHealths.add(healthLabel);
						}
					}
				}
			}
			
		}
		return retHealths;

	}

	private static Events getCurrentEvents(String accessToken)
			throws IOException, GoogleDriveAccessNotAuthorisedException {
		Calendar calendarService = getCalendarService(accessToken);
		DateTime now =new DateTime(System.currentTimeMillis());
		DateTime InASecond =new DateTime(System.currentTimeMillis()+1000);

		Events events = calendarService.events().list("primary")
        .setMaxResults(10)
        .setTimeMin(now)
        .setTimeMax(InASecond)
        .setOrderBy("startTime")
        .setSingleEvents(true)
        .execute();
		return events;
	}
	
	public static ArrayList<DietLabels> getDietLimitations(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		Events events = getCurrentEvents(accessToken);

		ArrayList<DietLabels> retDiets=new ArrayList<DietLabels>();
		
		for(Event event:events.getItems()) {
			if(event.getSummary()!=null
					&&event.getSummary().indexOf(StringHolder.GOOGLE_CALENDAR_DIET_EVENT)>=0)
			{
				String desc=event.getDescription();
				if(desc!=null) {
					String[] split = desc.split(StringHolder.GOOGLE_CALENDAR_LINE_SEPARATOR);
					
					for(String line:split) {
						
						DietLabels dietLabel=DietLabels.tryRetrieving(line);
						if(dietLabel!=null) {
							retDiets.add(dietLabel);
						}
					}
				}
			}
			
		}
		return retDiets;

	}
	
	public static String getHealthLimitationsAsString(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		Events events = getCurrentEvents(accessToken);
		
		for(Event event:events.getItems()) {
			if(event.getSummary()!=null
					&&event.getSummary().indexOf(StringHolder.GOOGLE_CALENDAR_HEALTH_EVENT)>=0)
			{
				String desc=event.getDescription();
				if(desc!=null) {
					return desc;
				}
			}
			
		}
		return "";

	}

	
	public static String getDietLimitationsAsString(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		Events events = getCurrentEvents(accessToken);

		
		for(Event event:events.getItems()) {
			if(event.getSummary()!=null
					&&event.getSummary().indexOf(StringHolder.GOOGLE_CALENDAR_DIET_EVENT)>=0)
			{
				String desc=event.getDescription();
				if(desc!=null) {
					return desc;
				}
			}
			
		}
		return "";

	}
	
	
	public static void main(String[] arg){
		String accessToken="ya29.GlvlBhKPD_JSrmhHHtQ1rqsmBI9rPk3_gkpHo7ttBVG6vHz-0PxQ2s3YsorXqENySYTq8aMo4irVwaEnbfsb76HqJQEnMX72FUYvj8bQQDZiJDSe2_04mFFqMvja";
		try {
			ArrayList<HealthLabels> healthLimitations = getHealthLimitations(accessToken);
			ArrayList<DietLabels> dietLimitations = getDietLimitations(accessToken);

		} catch (IOException | GoogleDriveAccessNotAuthorisedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
	}


//	public static String getDietLimitationsAsString(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
//		Drive service = getDriveService(accessToken);
//		FileList kitchenAssistantFolder = getKitchenAssistantFolder(service);
//
//		if(kitchenAssistantFolder.isEmpty()||kitchenAssistantFolder.getFiles().isEmpty()) {
//			return "";
//		}else {
//			String googleDriveQuery = "name='"+StringHolder.GOOGLE_DRIVE_DIET_FILENAME+"'"
//					+" and '"+kitchenAssistantFolder.getFiles().get(0).getId()+"' in parents";
//
//			String driveContent=executeQuery(googleDriveQuery,service);
//
//			return driveContent;
//		}
//	}


	

}