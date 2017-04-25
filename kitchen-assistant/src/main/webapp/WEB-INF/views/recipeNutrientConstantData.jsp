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
		<h1>
			Przepis pod adresem url <i>" <%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name) %>
				"
			</i> został sparsowany, a produkty wybrane, oto wyniki:
		</h1>

			List of nutrients:
		<table border="1">
			<tr>
				<th>-</th>
				<c:forEach var="nutrient" items="${allLastIngs}" varStatus="status">
					<th>${nutrient}</th>
				</c:forEach>
			</tr>
			<c:forEach var="ingredient" items="${lastMap}" varStatus="status">
				<tr>
					<td>${ingredient.key}</td>
					<c:forEach var="nutrient" items="${allLastIngs}"
						varStatus="status">
						<td>${ingredient.value[nutrient]}</td>
					</c:forEach>
				</tr>
			</c:forEach>
				<tr>
					<td><b>Sum of ingredients:</b></td>
					<c:forEach var="nutrient" items="${allLastIngs}"
						varStatus="status">
						<td><b>${lastSum[nutrient]}</b></td>
					</c:forEach>
				</tr>
		</table>


	</div>




</body>
</html>
