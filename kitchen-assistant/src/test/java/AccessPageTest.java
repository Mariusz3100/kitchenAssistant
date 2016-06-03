import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AccessPageTest {

	@Test
	public void test() throws MalformedURLException, Page404Exception {

		String page=getPage("https://www.przepisy.pl/przepis/lasagne-1");
	}

	public static String getPage(String finalUrl) throws MalformedURLException, Page404Exception {
		initCookieHandler();

		URLConnection connection;
		InputStream detResponse = null;
		boolean successfull=false;
		int timeOut=1000;
		InputStreamReader inputStreamReader = null;
		while(!successfull){
			try {
				URL url = new URL(finalUrl);
				connection = url.openConnection();

				connection.setConnectTimeout(1000000);
				connection.setRequestProperty("Accept-Charset", "UTF-8");
				//argh
				connection.connect();
				detResponse = connection.getInputStream();
				successfull=true;
				inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
			} catch (IOException e) {
				if(e instanceof FileNotFoundException){
					throw new Page404Exception(finalUrl);
				}
				ProblemLogger.logProblem(
						"There was a problem with accessing url '"+finalUrl+"' exception: "+e.getMessage());

				//e.printStackTrace();
			}
		}
		BufferedReader detBR=new BufferedReader(inputStreamReader);

		String respLine=null;
		StringBuilder response=new StringBuilder();
		try {
			while((respLine=detBR.readLine())!=null){
				response.append(respLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	public static void initCookieHandler() {
		if(CookieHandler.getDefault()==null)
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}


}
