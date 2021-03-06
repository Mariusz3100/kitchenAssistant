<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Kitchen Assistant Page</title>

<jsp:include page="includes/headInclude.jsp" />
<jsp:include page="includes/constants.jsp" />

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />


	<section id="screenshots" class="padd-section text-center wow fadeInUp">

	<div class="container">


		<div class="section-title text-center">

			<h3>
				<c:choose>
					<c:when test="${fn:length(recipeList)>0}">
						Recipes found for "<%=pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeSearchPhrase_name)%>":
					</c:when>
					<c:otherwise>
						No recipes found for phrase "<%=pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeSearchPhrase_name)%>":
					</c:otherwise>
				</c:choose>
			</h3>
		</div>
	</div>

	<div class="container">
		<div class="owl-carousel owl-theme no-autoplay">
			<c:forEach var="recipe" items="${recipeList}" varStatus="">
				
				<div class="recipe-outline">
					<img src="${recipe.imageUrl}" alt="img" class="img-fluid recipe-img">
					<div class="recipe-label"><h4>${recipe.label}</h4></div>
					<p class="recipe-link"><a href="${recipe.url}">Details on Edaman page</a></p>
					<p class="recipe-link"><a href="${recipe.recipeDetailsInApi}">Details on Edaman api</a></p>
					<a class="btn btn-success" target="_blank" href="${recipe.bootstrap_parseUrl}">Parse</a>
					
				</div>
			
			
			</c:forEach>
		</div>
	</div>

	</section>





	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>

	<jsp:include page="includes/bottomImports.jsp" />
</body>
</html>
