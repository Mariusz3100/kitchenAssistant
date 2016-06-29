<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
</head>
<body>
	<div align="left">
		

		Z twojego opisu:
		<br>
		${originalPhrase}
		<br>
		<c:if test="${not empty fn:trim(invalidEntriesInfo)}">
			nie udało się wyodrębnić następujących wpisów:
			<br>
			${invalidEntriesInfo}
		
		</c:if>
		<br>
		 Poprawnie wyodrębniono następujące składniki 
		 	(możliwe, że nie zostały odnalezione na listach wartości odżywczych)
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
					<td><b>${amountsSum[nutrient]}</b></td>
				</c:forEach>

			</tr>

		</table>


	</div>






</body>
</html>
