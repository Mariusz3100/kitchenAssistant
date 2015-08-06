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
@WebServlet("/RecipeParserResults")
public class RecipeParserResult extends HttpServlet {


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(StringHolder.SERVLET_RESPONSE_ENCODING);
		
		response.getWriter().write("<html charset=\""+StringHolder.SERVLET_RESPONSE_ENCODING+"\"><head><meta charset=\"\"><title>Parsing a recipe</title>\r\n</head><body>"); 

//		request.getParameterNames()

		

		
		
		response.getWriter().write("</body></html>");

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
