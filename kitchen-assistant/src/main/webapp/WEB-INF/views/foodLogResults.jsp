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
	<div align="left">
		<h1>Produkt details:</h1>


		<ul>

			<li>ID: ${produkt.p_id}</li>
			<li>url: ${produkt.url}</li>
			<li>Nazwa: ${produkt.nazwa}</li>
			<li>Opis: ${produkt.opis}</li>
			<li>Cena: ${produkt.cena} zł</li>
		</ul>


		Ingredients hierarchy: <br> ${compundIngredient} <br>

		Nutritients of Ingredients:
		<table border="1">

			<tr>
				<th>-</th>
				<c:forEach var="nutrient" items="${allNutrients}" varStatus="status">
					<th>${nutrient}</th>
				</c:forEach>
			</tr>

			<c:forEach var="ingredient" items="${amountsMap}" varStatus="status">
				<tr>
					<td>${ingredient.key}</td>



					<c:forEach var="nutrient" items="${allNutrients}"
						varStatus="status">
						<td>${ingredient.value[nutrient]}</td>
					</c:forEach>

				</tr>
			</c:forEach>

			<tr>
				<td><b>Sum</b></td>

				<c:forEach var="nutrient" items="${allNutrients}" varStatus="status">
					<td>${amountsSum[nutrient]}</td>
				</c:forEach>

			</tr>

		</table>


	</div>






</body>
</html>
