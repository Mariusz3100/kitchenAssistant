package mariusz.ambroziak.kassistant.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.OutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class ServletSample extends AbstractAuthorizationCodeServlet {


	private static final String APPLICATION_NAME =
			"Kitchen Assistant";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			".credentials/drive-java-quickstart");

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
	

	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException {
		Credential credential = getCredential();
		String retValue="";
		Drive drive=new Drive.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
		
		FileList result = drive.files().list()
				.setPageSize(10)
				.setFields("nextPageToken, files(id, name)")
				//          .setQ("mimeType='application/vnd.google-apps.folder'")
				.setQ("name='health'")

				.execute();

		if(result.isEmpty()||result.getFiles().isEmpty())
			retValue="Sorry";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		
		drive.files()
		.export(result.getFiles().get(0).getId(),"text/csv")
		.executeMediaAndDownloadTo(outputStream);
		String driveContent=outputStream.toString();
		
		String[] labels=driveContent.split("\r\n");
		for(String lab:labels){
			retValue+="\n\n<BR>"+(HealthLabels.retrieveByName(lab));
		}

		
	    response.getWriter().print("Hello World"+retValue);
	  }

//		public static ArrayList<HealthLabels> getHealthLimitations() throws IOException {
//			ArrayList<HealthLabels> retValue=new ArrayList<>();
////			// Build a new authorized API client service.
//	
//			// Print the names and IDs for up to 10 files.
//			FileList result = service.files().list()
//					.setPageSize(10)
//					.setFields("nextPageToken, files(id, name)")
//					//          .setQ("mimeType='application/vnd.google-apps.folder'")
//					.setQ("name='health'")
//	
//					.execute();
//	
//			if(result.isEmpty()||result.getFiles().isEmpty())
//				return retValue;
//			OutputStream outputStream = new ByteArrayOutputStream();
//			
//			
//			service.files()
//			.export(result.getFiles().get(0).getId(),"text/csv")
//			.executeMediaAndDownloadTo(outputStream);
//			String driveContent=outputStream.toString();
//			
//			String[] labels=driveContent.split("\r\n");
//			for(String lab:labels){
//				retValue.add(HealthLabels.retrieveByName(lab));
//			}
//
//			return retValue;
//		}

	  @Override
	  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
	    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
	    url.setRawPath("/kitchen-assistant/oauth2callback");
	    return url.build();
	  }

		protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
			GoogleClientSecrets clientSecrets =
					GoogleClientSecrets.load(JSON_FACTORY, new StringReader(StringHolder.googleIds));
			
			return new GoogleAuthorizationCodeFlow.Builder(
					HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
			.setDataStoreFactory(DATA_STORE_FACTORY)
			.setAccessType("offline")
			.build();
		}

	  @Override
	  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
	    return "0";
	  }
	}