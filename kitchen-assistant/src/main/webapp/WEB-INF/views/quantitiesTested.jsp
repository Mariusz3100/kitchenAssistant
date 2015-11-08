<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta charset="UTF-8"><title>Insert title here</title>
</head>
<body>

	<div>
		<h1>Dla url "${url}" znaleziono następujące składniki:</h1>

		<c:forEach var="result" items="${results}">
			<b>${result }</b>
			
		</c:forEach>
		

	
	</div>

</body>
</html>