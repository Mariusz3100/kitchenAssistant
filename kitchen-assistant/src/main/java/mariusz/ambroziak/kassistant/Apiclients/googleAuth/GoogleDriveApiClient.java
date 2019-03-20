package mariusz.ambroziak.kassistant.Apiclients.googleAuth;



import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
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

public class GoogleDriveApiClient {

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
		System.out.println();
		///		writeToDrive("xxx");
		List<DietLabels> l=new ArrayList<>();
		//		l.add(DietLabels.HIGH_FIBER);
		//		l.add(DietLabels.BALANCED);
		l.add(DietLabels.LOW_CARB);


		writeDietLabelsToDrive(l);

	}

	private static void writeToDrive(String string) throws IOException, GoogleDriveAccessNotAuthorisedException {
		Drive service = getDriveService();

		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.setQ("name='creationTest'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			return;
		OutputStream outputStream = new ByteArrayOutputStream();


		service.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();

		String[] labels=driveContent.split(StringHolder.GOOGLE_DRIVE_LINE_SEPARATOR);
		if(driveContent!=null&&!driveContent.equals(""))
		{
			for(String lab:labels){
				DietLabels retrievedByName = DietLabels.tryRetrieving(lab);


			}
		}


	}

	//	public static void getOrCreateDirectory() throws IOException, GoogleDriveAccessNotAuthorisedException {
	//		Drive driveService = getDriveService();
	//		com.google.api.services.drive.model.File remoteFile;
	//		com.google.api.services.drive.model.File remoteFolder = getOrCreateKitchenAssistantFolder(driveService);
	//
	//		if(remoteFolder==null) {
	//			
	//		}
	//		getOrCreateGoogleDriveFile(driveService, StringHolder.GOOGLE_DRIVE_DIET_FILENAME,remoteFolder);
	//		getOrCreateGoogleDriveFile(driveService, StringHolder.GOOGLE_DRIVE_HEALTH_FILENAME,remoteFolder);
	//		
	//	}

	public static void writeDietLabelsToDrive(List<DietLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		if(labels==null) {
			labels=new ArrayList<>();
		}

		if(!labels.isEmpty()) {
			Drive driveService = getDriveService();
			com.google.api.services.drive.model.File remoteFolder = getOrCreateKitchenAssistantFolder(driveService);
			com.google.api.services.drive.model.File dietFile = getOrCreateGoogleDriveFile(driveService, StringHolder.GOOGLE_DRIVE_DIET_FILENAME,remoteFolder);
			File outFile = createLocalFileWithDietLabelsToUpload(labels);

			com.google.api.services.drive.model.File fileToBeCreated = createLocalVersionOfDriveFile(dietFile);
			FileContent mediaContent = new FileContent(dietFile.getMimeType(), outFile);
			com.google.api.services.drive.model.File updatedFile = driveService.files().update(dietFile.getId(), fileToBeCreated, mediaContent).execute();
			outFile.delete();
			String dietLimitationsAsString = getDietLimitationsAsString();
			System.out.println(dietLimitationsAsString);
		}
	}


	public static void writeHealthLabelsToDrive(List<HealthLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		if(labels==null) {
			labels=new ArrayList<>();
		}

		if(!labels.isEmpty()) {
			Drive driveService = getDriveService();
			com.google.api.services.drive.model.File remoteFolder = getOrCreateKitchenAssistantFolder(driveService);
			com.google.api.services.drive.model.File healthFile = getOrCreateGoogleDriveFile(driveService, StringHolder.GOOGLE_DRIVE_HEALTH_FILENAME,remoteFolder);
			File outFile = createLocalFileWithHealthLabelsToUpload(labels);

			com.google.api.services.drive.model.File fileToBeCreated = createLocalVersionOfDriveFile(healthFile);
			FileContent mediaContent = new FileContent(healthFile.getMimeType(), outFile);
			com.google.api.services.drive.model.File updatedFile = driveService.files().update(healthFile.getId(), fileToBeCreated, mediaContent).execute();
			outFile.delete();
			String dietLimitationsAsString = getDietLimitationsAsString();
			System.out.println(dietLimitationsAsString);
		}
	}

	private static File createLocalFileWithHealthLabelsToUpload(List<HealthLabels> labels) throws IOException {
		String newContent = convertHealthLabelsToString(labels);

		File outFile=new File("tempFile");
		BufferedWriter writer;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
		writer.write(newContent);
		writer.close();


		return outFile;
	}

	private static String convertHealthLabelsToString(List<HealthLabels> labels) {
		String newContent="";

		for(HealthLabels label:labels) {
			newContent+=label.getParameterName()+StringHolder.GOOGLE_DRIVE_LINE_SEPARATOR;
		}
		return newContent;
	}

	private static File createLocalFileWithDietLabelsToUpload(List<DietLabels> labels) throws IOException {
		String newContent = convertDietLabelsToString(labels);

		File outFile=new File("tempFile");
		BufferedWriter writer;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
		writer.write(newContent);
		writer.close();

		return outFile;
	}

	private static com.google.api.services.drive.model.File createLocalVersionOfDriveFile(
			com.google.api.services.drive.model.File dietFile) {
		com.google.api.services.drive.model.File fileToBeCreated=new com.google.api.services.drive.model.File();
		fileToBeCreated.setMimeType(dietFile.getMimeType());
		fileToBeCreated.setName(dietFile.getName());
		fileToBeCreated.setParents(dietFile.getParents());
		return fileToBeCreated;
	}

	private static String convertDietLabelsToString(List<DietLabels> labels) {
		String newContent="";

		for(DietLabels label:labels) {
			newContent+=label.getParameterName()+StringHolder.GOOGLE_DRIVE_LINE_SEPARATOR;
		}
		return newContent;
	}


	private static com.google.api.services.drive.model.File getOrCreateGoogleDriveFile(Drive driveService,String filename,
			com.google.api.services.drive.model.File remoteFolder) throws IOException {
		FileList kitchenAssistantFolder = getKitchenAssistantFolder(driveService);

		com.google.api.services.drive.model.File remoteFile;
		String googleDriveQuery = "name='"+filename+"'"
				+" and '"+kitchenAssistantFolder.getFiles().get(0).getId()+"' in parents"
				+" and trashed = false";
		FileList result = driveService.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.setQ(googleDriveQuery)
				.execute();

		if(result==null||result.isEmpty()||result.getFiles().isEmpty()) {
			com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
			fileMetadata.setName(filename);
			fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
			List<String> asList = Arrays.asList(remoteFolder.getId());
			fileMetadata.setParents(asList);
			remoteFile = driveService.files().create(fileMetadata).setFields("id, name").execute();
		}else {
			remoteFile=result.getFiles().get(0);
		}
		return remoteFile;
	}

	private static com.google.api.services.drive.model.File getOrCreateKitchenAssistantFolder(Drive driveService)
			throws IOException {
		FileList result = getKitchenAssistantFolder(driveService);		

		com.google.api.services.drive.model.File remoteFolder;

		if(result.isEmpty()||result.getFiles().isEmpty())
		{
			com.google.api.services.drive.model.File folderMetadata = new com.google.api.services.drive.model.File();
			folderMetadata.setName(StringHolder.GOOGLE_DRIVE_FOLDER);
			folderMetadata.setMimeType("application/vnd.google-apps.folder");
			remoteFolder = driveService.files().create(folderMetadata).setFields("id, name").execute();
		}else {
			remoteFolder=result.getFiles().get(0);
		}
		return remoteFolder;
	}

	private static FileList getKitchenAssistantFolder(Drive driveService) throws IOException {
		FileList result = driveService.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.setQ("name='"+StringHolder.GOOGLE_DRIVE_FOLDER+"' and trashed = false")

				.execute();
		return result;
	}

	public static void getDirecotry() {

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
		String driveContent=getHealthLimitationsAsString();
		if(driveContent!=null&&!driveContent.equals(""))
		{
			String[] labels=driveContent.split(StringHolder.GOOGLE_DRIVE_LINE_SEPARATOR);

			for(String lab:labels){
				HealthLabels retrievedByName = HealthLabels.tryRetrieving(lab);

				if(retrievedByName!=null) {
					retValue.add(retrievedByName);
				}
			}
		}

		return retValue;
	}


	public static String getHealthLimitationsAsString() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Drive service = getDriveService();
		FileList kitchenAssistantFolder = getKitchenAssistantFolder(service);

		if(kitchenAssistantFolder.isEmpty()||kitchenAssistantFolder.getFiles().isEmpty()) {
			return "";
		}else {
			String googleDriveQuery = "name='"+StringHolder.GOOGLE_DRIVE_HEALTH_FILENAME+"'"
					+" and '"+kitchenAssistantFolder.getFiles().get(0).getId()+"' in parents";

			String driveContent=executeQuery(googleDriveQuery,service);

			return driveContent;
		}
	}

	public static ArrayList<DietLabels> getDietLimitations() throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<DietLabels> retValue=new ArrayList<>();
		String driveContent=getDietLimitationsAsString();
		if(driveContent!=null&&!driveContent.equals(""))
		{
			String[] labels=driveContent.split(StringHolder.GOOGLE_DRIVE_LINE_SEPARATOR);

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
		FileList kitchenAssistantFolder = getKitchenAssistantFolder(service);

		if(kitchenAssistantFolder.isEmpty()||kitchenAssistantFolder.getFiles().isEmpty()) {
			return "";
		}else {
			String googleDriveQuery = "name='"+StringHolder.GOOGLE_DRIVE_DIET_FILENAME+"'"
					+" and '"+kitchenAssistantFolder.getFiles().get(0).getId()+"' in parents";

			String driveContent=executeQuery(googleDriveQuery,service);

			return driveContent;
		}
	}


	private static String executeQuery(String query,Drive service) throws IOException {
		FileList result = service.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				.setQ(query)
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