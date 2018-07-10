package mariusz.ambroziak.kassistant.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
@RequestMapping(value="/google/test1")
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
		getCredential();
		response.getWriter().println("teeeesting");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/oauth2callback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(JSON_FACTORY, new StringReader(StringHolder.googleCredentials));
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline")
				.build();

		return flow;
	}



	private int i=0;
	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return Integer.toString(i);
	}

	//  @Override
	//	  protected AuthorizationCodeFlow initializeFlow() throws IOException {
	//	    return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
	//	        new NetHttpTransport(),
	//	        new JacksonFactory(),
	//	        new GenericUrl("https://server.example.com/token"),
	//	        new BasicAuthentication("s6BhdRkqt3", "7Fjfp0ZBr1KtDRbnfVdmIw"),
	//	        "s6BhdRkqt3",
	//	        "https://server.example.com/authorize").setCredentialStore(
	//	        new JdoCredentialStore(JDOHelper.getPersistenceManagerFactory("transactions-optional")))
	//	        .build();
	//	  }

	//	  @Override
	//	  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
	//	    // return user ID
	//	  }
}