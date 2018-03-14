package mariusz.ambroziak.kassistant.Apiclients.google;



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

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Export;
import com.google.api.services.drive.DriveScopes;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveApiClient {
	/** Application name. */
	private static final String APPLICATION_NAME =
			"Kitchen Assistant";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".credentials/drive-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JacksonFactory JSON_FACTORY =
			JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES =
			//        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
			Arrays.asList(DriveScopes.DRIVE_FILE,DriveScopes.DRIVE_SCRIPTS,
					DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE);


	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in =new FileInputStream("WEB-INF/resources/client_secret.json");
		//            Quickstart.class.getResourceAsStream("/client_secret.json");
//		new java.io.File("").getAbsolutePath();new java.io.File("").getAbsolutePath();
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(
				flow, new LocalServerReceiver()).authorize("user");
		//new java.io.File("resources/client_secret.json").exists()
		System.out.println(
				"Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getDietLimitationsAsString());

	}

	public static void deleteCredentials(){
		
		if(DATA_STORE_DIR.isDirectory()){
			for(java.io.File f:DATA_STORE_DIR.listFiles()){
				f.delete();
			}
		}else{
			DATA_STORE_DIR.delete();
		}
	}
	
	
	public static ArrayList<HealthLabels> getHealthLimitations() throws IOException {
		ArrayList<HealthLabels> retValue=new ArrayList<>();
		// Build a new authorized API client service.
		Drive service = getDriveService();

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
		
		String[] labels=driveContent.split("\r\n");
		for(String lab:labels){
			retValue.add(HealthLabels.retrieveByName(lab));
		}

		return retValue;
	}

	
	public static String getHealthLimitationsAsString() throws IOException {
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
	
	public static ArrayList<DietLabels> getDietLimitations() throws IOException {
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
		for(String lab:labels){
			retValue.add(DietLabels.retrieveByName(lab));
		}

		return retValue;
	}

	
	
	public static String getDietLimitationsAsString() throws IOException {
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