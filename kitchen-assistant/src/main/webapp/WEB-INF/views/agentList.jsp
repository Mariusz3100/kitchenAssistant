<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista agentów</title>
    </head>
    <body>
        <div>
	        <h1>Registered agents:</h1>
        		
				<c:forEach var="agent" items="${agents}" varStatus="status">
	        		${agent.key}
	        		
	        		${agent.value}
	        		<a href="../agents/info?name=${agent.key}">szczegóły</a>
	        		<br>
	        		<br>
				</c:forEach>	        	
        </div>
    </body>
</html>
