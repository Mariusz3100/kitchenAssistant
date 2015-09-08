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

        <div>
	        <h1>Dla url "${url}" znaleziono następujące składniki:</h1>
        	
        	<c:forEach var="result" items="${results}">
        	
        	<br>
        	Dla składnika ${result.searchPhrase } znaleziono następujące produkty:
        	
	        	
					<c:forEach var="produkt" items="${result.produkts}">
		        		-
		        		${produkt.cena}
		        		${produkt.nazwa}
						${produkt.url}
						
						<br/>	
		        	
					</c:forEach>	        	
	        	</table>
        	</c:forEach>
        </div>

</body>
</html>