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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.drive.model.*;

import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

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

	/**
	 * Build and return an authorized Drive client service.
	 * @return an authorized Drive client service
	 * @throws IOException
	 * @throws GoogleDriveAccessNotAuthorisedException 
	 */
//	public static Drive getDriveService(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
//		
//		Credential credential = getCredential(accessToken);//new java.io.File("").getAbsolutePath()
//		
//		 Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//	                .setApplicationName(APPLICATION_NAME)
//	                .build();
//
//		if(credential==null)
//			return null;
//		else
//			return new Drive.Builder(
//					GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), credential)
//					.setApplicationName(GoogleAuthApiParameters.getApplicationName())
//					.build();
//	}



	public static void main(String[] args) throws IOException, GoogleDriveAccessNotAuthorisedException {
		

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