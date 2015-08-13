<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

        <div align="center">
	        <h1>Dla url "${url}" znaleziono następujące składniki:</h1>
        	<table border="1">
        		<tr>
        		<th>nazwa</th>
	        	<th>url</th>
	        	<th>cena</th>
	        	</tr>
				<c:forEach var="produkt" items="${ingredients}">
	        	<tr>
	        		<td>${produkt.nazwa}</td>
					<td>${produkt.url}</td>
					<td>${produkt.cena}</td>
						
	        	</tr>
				</c:forEach>	        	
        	</table>
        </div>

</body>
</html>