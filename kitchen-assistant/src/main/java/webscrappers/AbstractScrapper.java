package webscrappers;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.StringHolder;

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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.Certificate;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;



public abstract class AbstractScrapper {
	
	
	public static HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	public static String getPage(String finalUrl) throws MalformedURLException, Page404Exception {
			initCookieHandler();
	
			HttpClient hc=getNewHttpClient();
			hc.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
//			URLConnection connection;
			InputStream detResponse = null;
			boolean successfull=false;
			int timeOut=1000;
			InputStreamReader inputStreamReader = null;
			
			HttpGet request = new HttpGet(finalUrl);

			while(!successfull){
				try {
					HttpResponse httpResponse=hc.execute(request);

					
					successfull=true;
				
			
					inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(),StringHolder.ENCODING);
					
				} catch (IOException e) {
					if(e instanceof FileNotFoundException){
						throw new Page404Exception(finalUrl);
					}
					
					System.out.println("There was a problem with accessing url '"+finalUrl+"' exception: "+e.getMessage());
					
					//e.printStackTrace();
				}
			}
			
			
			BufferedReader detBR=new BufferedReader(inputStreamReader);
			
			String respLine=null;
	//			detResponse.
			StringBuilder response=new StringBuilder();
			try {
				while((respLine=detBR.readLine())!=null){
					response.append(respLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response.toString();
		}

	public static void initCookieHandler() {
		if(CookieHandler.getDefault()==null){
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		}
	}
	
	
	public static void main(String arg[]) throws MalformedURLException, NoSuchAlgorithmException, KeyStoreException, Page404Exception{
//		System.setProperty("javax.net.ssl.trustStore","C:\\Program Files\\Java\\jre1.8.0_77\\lib\\security\\cacerts");
//		TrustManagerFactory trustManagerFactory =
//				   TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//				
//				trustManagerFactory.init((KeyStore)null);
//				int count=0;
//				
//				System.out.println(trustManagerFactory.getProvider());
//				TrustManager[] tmList=trustManagerFactory.getTrustManagers(); 
//				for(int i=0;i<tmList.length;i++){
//					
//					X509TrustManager tman=(X509TrustManager)tmList[i];
//					for(X509Certificate cert: tman.getAcceptedIssuers()){
//						count+=1;
//						System.out.println("NEXT:\n"+cert.getIssuerDN()+"\n\n\n\n\n");
//					}
//				}
//				System.out.println(count);
//				).stream().forEach(t -> {
//				                    x509Certificates.addAll(Arrays.asList(((X509TrustManager)t).getAcceptedIssuers()));
//				                });
		
		String x="https://przepisy.pl/przepis/crespelle-czyli-zapiekane-nalesniki-z-gorgonzola-i-cukinia";
		
		String z=getPage(x);
		System.out.println(z);
	}

}

