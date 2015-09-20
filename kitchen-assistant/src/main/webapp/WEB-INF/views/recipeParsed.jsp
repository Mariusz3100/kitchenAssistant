<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div>
		<h1>Dla url "${url}" znaleziono następujące składniki:</h1>

		<form action="xxx">

			<c:forEach var="result" items="${results}">

				<br>
				<c:choose>
					<c:when test="${fn:length(result.produkts) gt 0}">
        				<b>Dla składnika ${result.searchPhrase } znaleziono następujące produkty:</b>
        			<br>
						<input type="hidden" name="${result.searchPhrase}"
							value="${result.quantity}">

						<c:forEach var="produkt" items="${result.produkts}">
							<input type="radio" name="${result.searchPhrase}"
								value="${produkt.url}">
		        		
		        				        		-
		        		${produkt.cena}
		        		${produkt.nazwa}
						${produkt.url}
						
						<br />

						</c:forEach>

					</c:when>
					<c:otherwise>
        				<b>Dla składnika ${result.searchPhrase } nie znaleziono żadnych produktów:</b>
        				
        			<br />
					</c:otherwise>


				</c:choose>
			</c:forEach>


		</form>

	</div>

</body>
</html>