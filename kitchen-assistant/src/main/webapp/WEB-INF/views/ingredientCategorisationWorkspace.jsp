<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
</head>
<body>
	<div align="center">
		<h1>Ingredient categorised List</h1>
		<table border="1">
			<tr>
				<th>Phrase</th>
				<th>marked phrase</th>
				<th>constituency results</th>

				<th>quantity phrase</th>
				<th>produkt phrase</th>
				<th>produkt phrase constituencies</th>


			</tr>
			<c:forEach var="phrase" items="${phrases}" varStatus="status">
				<tr>
					<td>${phrase}</td>
					<td>${markedPhrases[phrase]}</td>

					<td>${constituency[phrase]}</td>
					<td>${quantityPhrases[phrase]}</td>
					<td>${productPhrases[phrase]}</td>
					<td>${nonQuantityConstituencies[phrase]}</td>


				</tr>
			</c:forEach>
		</table>
	</div>

	<div>${sumLine}</div>
</body>
</html>
