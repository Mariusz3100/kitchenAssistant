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
	<div align="center">

		<c:choose>
			<c:when test="${fn:length(recipeList)>0}">
				<h1>

					<br>We see you are hungry for <i> "<%=pageContext.getRequest()
							.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.searchPhrase_name)%>"
					</i> Would you like any of those recipes?

				</h1>
				<br />
				<table border="0">

					<c:forEach var="recipe" items="${recipeList}" varStatus="">
						<tr>
							<td><img alt="" src="${recipe.imageUrl}"></td>
							<td><b>${recipe.label}</b>
							 <br> 
							 	<!-- <a href="${recipe.url}"> -->
									${recipe.url}
									 <!-- </a>  -->
									 <br> <a href="${recipe.recipeDetailsInApi}">Details
									on Edaman api</a></td>
							<td><a href="${recipe.parseUrl}">Parse</a></td>

						</tr>
					</c:forEach>
				</table>

			</c:when>
			<c:otherwise>
				<h2>
					I am sorry, I didn't find anything for <i> "<%=pageContext.getRequest()
							.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.searchPhrase_name)%>"

					</i>.
				</h2> Would You like to try <a href="/recipeApiForm">again</a>?

			</c:otherwise>
		</c:choose>



	</div>
</body>
</html>
