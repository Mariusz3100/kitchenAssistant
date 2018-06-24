<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="include/constants.jsp" />
        	
        <title>Home</title>
    </head>
    <body>
        <div align="left">
	        <h1>Welcome to kitchen assistant!</h1>
	        <h4>This is a list of links that you may find useful:</h4>
	        <br>
	        <br><a href="${appName}/agents/all"> List all available agents</a>
 			<br><a href="${appName}/agents/start"> Start System (if no agents are created)</a>
 			<br><a href="${appName}/recipeApiForm"> Search for available recipes(in edaman database)</a>
 			<br><a href="${appName}/google/health/get"> Get your health preferences from google drive</a>
 			<br><a href="${appName}/google/diet/get"> Get your diet preferences from google drive</a>
 			
        </div>
    </body>
</html>
