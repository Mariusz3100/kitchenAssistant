<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>




<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Agent Info</title>
    </head>
    <body>
        <div>
	        <h1>Nazwa Agenta: ${name}</h1>
        	<div>
        		${fn:replace(agent, newLine, '<br>')}
        	</div>
        	<br>
        	<b>Agent logs:</b>
        	<div>
        		${fn:replace(agent.htmlLog, newLine, '<br />')}
        	</div>

					        	
        </div>
    </body>
</html>
