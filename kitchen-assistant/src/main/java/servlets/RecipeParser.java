package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import agent.RecipeAgent;
import results.Produkt;
import webscrappers.AuchanWebScrapper;
import database.holders.DatabaseInterface;
import database.holders.StringHolder;

/**
 * Servlet implementation class RecipeParser
 */
@WebServlet("/RecipeParser")
public class RecipeParser extends HttpServlet {


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(StringHolder.SERVLET_RESPONSE_ENCODING);
		
		response.getWriter().write("<html charset=\""+StringHolder.SERVLET_RESPONSE_ENCODING+"\"><head><meta charset=\"\"><title>PArsing a recipe</title>\r\n</head><body>"); 
//		response.getWriter().write("test: ęąśźółń");
		String url=request.getParameter("recipeurl");
		
		
		if(url==null||url.equals("")){
		
			String form="<form>\r\n" + 
			"Paste url:<br>\r\n" + 
			"<input type=\"text\" name=\"recipeurl\">"
			+ "</form>";
			
			
			response.getWriter().write(form);
		}else{
			StringBuilder outPage=new StringBuilder();
			outPage.append("<form action=\"RecipeParserResults\">\n");
			outPage.append("Dla url "+url+" znalazłem składniki:<br>");
			Map<String,ArrayList<Produkt>> result=RecipeAgent.parse(url);
			
			for(String key:result.keySet()){
				ArrayList<Produkt> value=result.get(key);
				outPage.append("<br>\n <b>"+key+"</b>:<br>\n");

				if(value.size()==0)
				{
					outPage.append("Nothing found\n<br>\n");

				}
				
				for(Produkt p:value){
					outPage.append(
							"<input type=\"checkbox\""
							+ " name=\""+URLEncoder.encode(key,StringHolder.SERVLET_RESPONSE_ENCODING)+"\""
							+ " value=\""+p.getUrl()+"\">"+

									
							
							p.getNazwa()+
							"  <a href=\""+p.getUrl()+"\">"+p.getUrl()+"</a><br>\n");
				}
				
				outPage.append(
						"<input type=\"checkbox\""
						+ " name=\""+URLEncoder.encode(key,StringHolder.SERVLET_RESPONSE_ENCODING)+"\""
						+ " value=\""+StringHolder.user_name_value_suffix+"\">");
				outPage.append("Nie ma nic akceptowalnego? Podaj nam url do składnika, który cię interesuje");
				outPage.append("<input type=\"text\" name=\""+URLEncoder.encode(key,StringHolder.SERVLET_RESPONSE_ENCODING)+StringHolder.user_name_value_suffix+"\">");
				
			}
			
//			outPage.append(result);
			outPage.append("<input type=\"submit\" value=\"Submit\">\n");

			outPage.append("</form>\n");

			response.getWriter().write(outPage.toString());

		}
		
		response.getWriter().write("</body></html>");

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
