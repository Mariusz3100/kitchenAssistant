package mariusz.ambroziak.kassistant.Apiclients.googleAuth;



import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GoogleAuthApiClient {

	/**
	 * Build and return an authorized Drive client service.
	 * @return an authorized Drive client service
	 * @throws IOException
	 * @throws GoogleDriveAccessNotAuthorisedException 
	 */
	public static Drive getDriveService() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Credential credential = getCredential();//new java.io.File("").getAbsolutePath()
		if(credential==null)
			return null;
		else
			return new Drive.Builder(
					GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), credential)
				.setApplicationName(GoogleAuthApiParameters.getApplicationName())
				.build();
	}

	private static Credential getCredential() throws GoogleDriveAccessNotAuthorisedException {
		Map<String, Credential> authorisationMap = GoogleAuthApiClientCallbackController.authorisationMap;
		
		if(authorisationMap.isEmpty())
			throw new GoogleDriveAccessNotAuthorisedException();
		else
			return authorisationMap.values().iterator().next();
	}

	public static void main(String[] args) throws IOException, GoogleDriveAccessNotAuthorisedException {
		System.out.println(getDietLimitationsAsString());

	}

	public static void deleteCredentials(){
		try {
		if(GoogleAuthApiParameters.getDataStoreDir().isDirectory()){
			File[] listFiles = GoogleAuthApiParameters.getDataStoreDir().listFiles();
			for(java.io.File f:listFiles){
				f.delete();
			}
		}else{
			GoogleAuthApiParameters.getDataStoreDir().delete();
		}
		}catch (Exception e) {
			ProblemLogger.logProblem("Some (probably runtime) exception during deleting the google authorisation data");
			ProblemLogger.logStackTrace(e.getStackTrace());
		}
	}
	
	
	public static ArrayList<HealthLabels> getHealthLimitations() throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<HealthLabels> retValue=new ArrayList<>();
		// Build a new authorized API client service.
		Drive service = getDriveService();

		if(service==null)
			return retValue;
		// Print the names and IDs for up to 10 files.
		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				//          .setQ("mimeType='application/vnd.google-apps.folder'")
				.setQ("name='health'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			return retValue;
		OutputStream outputStream = new ByteArrayOutputStream();
		
		
		service.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();
		
		if(driveContent!=null&&!driveContent.equals(""))
		{
			String[] labels=driveContent.split("\r\n");
			for(String lab:labels){
				HealthLabels retrievedByName = HealthLabels.tryRetrieving(lab);
	
				if(retrievedByName!=null&&!retrievedByName.equals("")) {
					retValue.add(retrievedByName);
				}
			}
		}

		return retValue;
	}

	
	public static String getHealthLimitationsAsString() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Drive service = getDriveService();

		// Print the names and IDs for up to 10 files.
		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				//          .setQ("mimeType='application/vnd.google-apps.folder'")
				.setQ("name='health'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			return "";
		OutputStream outputStream = new ByteArrayOutputStream();
		
		
		service.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();
		
		return driveContent;
	}
	
	public static ArrayList<DietLabels> getDietLimitations() throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<DietLabels> retValue=new ArrayList<>();
		// Build a new authorized API client service.
		Drive service = getDriveService();

		// Print the names and IDs for up to 10 files.
		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				//          .setQ("mimeType='application/vnd.google-apps.folder'")
				.setQ("name='diet'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			return retValue;
		OutputStream outputStream = new ByteArrayOutputStream();
		
		
		service.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();
		
		String[] labels=driveContent.split("\r\n");
		if(driveContent!=null&&!driveContent.equals(""))
		{
			for(String lab:labels){
				DietLabels retrievedByName = DietLabels.tryRetrieving(lab);
	
				if(retrievedByName!=null) {
					retValue.add(retrievedByName);
				}
			}
		}

		return retValue;
	}

	
	
	public static String getDietLimitationsAsString() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Drive service = getDriveService();

		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				//          .setQ("mimeType='application/vnd.google-apps.folder'")
				.setQ("name='diet'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			return "";
		OutputStream outputStream = new ByteArrayOutputStream();
		
		
		service.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();
		
		return driveContent;
	}

}