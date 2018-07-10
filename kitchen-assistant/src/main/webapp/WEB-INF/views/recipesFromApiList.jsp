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

		<h1>

			<br>We see you are hungry for <i> "<%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.searchPhrase_name) %>"
			</i> Would you like any of those recipes?

		</h1>


		<table border="0">

			<c:forEach var="recipe" items="${recipeList}" varStatus="">
				<tr>
					<td><img alt="" src="${recipe.imageUrl}"></td>
					<td><b>${recipe.label}</b> <br> <a href="${recipe.url}">${recipe.url}</a>
						<br> <a href="${recipe.recipeDetailsInApi}">Details on
							Edaman api</a></td>
					<td><a href="${recipe.parseUrl}">Parse</a></td>

				</tr>
			</c:forEach>
		</table>

	</div>
</body>
</html>
