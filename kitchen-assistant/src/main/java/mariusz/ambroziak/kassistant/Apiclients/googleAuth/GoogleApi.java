package mariusz.ambroziak.kassistant.Apiclients.googleAuth;

import java.io.File;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class GoogleApi {

	
	
	
	
	protected static Credential getCredential(String accessToken) throws GoogleDriveAccessNotAuthorisedException {
//		Map<String, Credential> authorisationMap = GoogleAuthApiClientCallbackController.authorisationMap;
		
		if(accessToken==null||accessToken.equals(""))
			throw new GoogleDriveAccessNotAuthorisedException();
		else{
			GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
			return credential;
		}
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
}
