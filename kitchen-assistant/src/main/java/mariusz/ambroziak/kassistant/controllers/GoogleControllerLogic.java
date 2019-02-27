package mariusz.ambroziak.kassistant.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.drive.DriveScopes;

import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiParameters;
import mariusz.ambroziak.kassistant.api.agents.GoogleAccessAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public abstract class GoogleControllerLogic extends AbstractAuthorizationCodeServlet {

	/** Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE,DriveScopes.DRIVE_SCRIPTS,
						DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE);

	public GoogleControllerLogic() {
		super();
	}
	protected ArrayList<String> getHealthLimitations() throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<HealthLabels> healthLimittions;
		healthLimittions = GoogleAuthApiClient.getHealthLimitations();
		ArrayList<String> list=new ArrayList<String>();
		if(healthLimittions!=null&&!healthLimittions.isEmpty())
		{
			for(HealthLabels s:healthLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		return list;
	}

	protected ArrayList<String> getHealthLimitationWithComments() throws IOException, GoogleDriveAccessNotAuthorisedException, AgentSystemNotStartedException {
		ArrayList<HealthLabels> healthLimittions;
		healthLimittions = GoogleAccessAgent.getHealthLimitations();
	
	
		ArrayList<String> list=new ArrayList<String>();
		
	
		if(healthLimittions==null||healthLimittions.isEmpty())
			list.add("Health labels for this user were not found in his google drive.");
		else{
			list.add("Health labels for a user:");
	
			for(HealthLabels s:healthLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		return list;
	}

	protected ArrayList<String> getDietLimitationsWithComments() throws IOException, GoogleDriveAccessNotAuthorisedException, AgentSystemNotStartedException {
		ArrayList<DietLabels> healthLimittions;
		healthLimittions = GoogleAccessAgent.getDietLimitations();
	
	
		ArrayList<String> list=new ArrayList<String>();
	
		if(healthLimittions==null||healthLimittions.isEmpty())
			list.add("Diet labels for a user not found.");
		else{
			list.add("Diet labels for a user:");
	
			for(DietLabels s:healthLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		return list;
	}
	protected ArrayList<String> getDietLimitations() throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<DietLabels> dietLimittions;
		dietLimittions = GoogleAuthApiClient.getDietLimitations();
	
	
		ArrayList<String> list=new ArrayList<String>();
	
		if(dietLimittions!=null&&!dietLimittions.isEmpty())
		{
			for(DietLabels s:dietLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		return list;
	}

	protected void deleteLocalAuthorisationData() {
		GoogleAuthApiClient.deleteCredentials();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(GoogleAuthApiParameters.getJsonFactory(), new StringReader(StringHolder.googleCredentials));
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), clientSecrets, SCOPES)
				.setDataStoreFactory(GoogleAuthApiParameters.getDATA_STORE_FACTORY())
				.setAccessType("offline")
				.build();
	
		return flow;
	
		//		  return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
		//	        HTTP_TRANSPORT,
		//	        new JacksonFactory(),
		//	        new GenericUrl("https://server.example.com/token"),
		//	        new BasicAuthentication("s6BhdRkqt3", "7Fjfp0ZBr1KtDRbnfVdmIw"),
		//	        "s6BhdRkqt3",
		//	        "https://server.example.com/authorize").setDataStoreFactory(
		//	        DATA_STORE_FACTORY)
		//	        .build();
	}

	
}