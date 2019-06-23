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

				<th>Phrase</th>
				<th>categorisation results</th>
				<th>produkt name</th>
				<th>url</th>


			</tr>
			<c:forEach var="produkt" items="${results}" varStatus="status">
				<tr>
					<td>${produkt}</td>
					<td>${categorisationResults[produkt]}</td>
					<td>${produkt.nazwa}</td>
					<td><a href="${produkt.url}">${produkt.url}</a></td>


				</tr>
			</c:forEach>
		</table>
	</div>

	<div>${sumLine}</div>
</body>
</html>
