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
		<table border="1">productPhrases
			<tr>
				<th>Phrase</th>
				<th>categorisation results</th>
				<th>produkt phrase</th>
				<th>quantity phrase</th>


			</tr>
			<c:forEach var="ingredient" items="${results}" varStatus="status">
				<tr>
					<td>${ingredient}</td>
					<td>${categorisationResults[ingredient]}</td>
					<td>${productPhrases[ingredient]}</td>
					<td>${quantityPhrases[ingredient]}</td>


				</tr>
			</c:forEach>
		</table>
	</div>

	<div>${sumLine}</div>
</body>
</html>
