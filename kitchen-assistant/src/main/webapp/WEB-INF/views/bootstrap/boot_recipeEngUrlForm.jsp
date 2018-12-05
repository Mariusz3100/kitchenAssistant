<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
   
<html lang="en">
<head>
<meta charset="utf-8">

<jsp:include page="includes/headInclude.jsp" />
<jsp:include page="includes/constants.jsp" />

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />

	<!--==========================
  Hero Section
============================-->
	<section id="hero" class="wow fadeIn">
	<div class="hero-container">
		<form action="b_correctQuantities">
			<h3>Paste id of a recipe from edaman api:</h3>
			<input type="text"	name="${recipeUrl_name}">
			<button type="submit" class="btn btn-success">Parse Recipe</button>
		</form>
	</div>
	</section>
	
	<section>
			<h1>

			<br>For url <i>
			"<%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeId_name) %>"
			</i> these products were found for ingredients:

		</h1>

		<form action="correctProdukts">
			<input type="hidden" name="${liczbaSkladnikow}" value="${fn:length(results)}">
			<input type="hidden" name="${recipeUrl_name}" value="${url}">
			
			<c:forEach var="result" items="${results}" varStatus="skladnikCount">

				<br>
				
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${quantityName}"
							value="${result.key.quantity}">
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${produktPhraseName}"
							value="${result.key.produktPhrase}">
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${searchPhraseName}"
							value="${result.key.searchPhraseAnswered}">
											
				<c:choose>
					<c:when test="${fn:length(result.key.produkts) gt 0}">
						<b>For ingredient ${result.key.searchPhraseAnswered } [${result.value}] these products were found:</b>
						<br>
						

						<c:forEach var="produkt" items="${result.key.produkts}"
							varStatus="opcjaCount">
							<input type="radio"
								name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}" value="${radioValuePrefix}${produkt.url}">	
								- [${produkt.cena} $, ${produkt.recountedPrice}] ${produkt.nazwa} <u>								<a href="${produkt.url}<%=mariusz.ambroziak.kassistant.Apiclients.shopcom.ShopComApiParameters.getQuestionMarkApikeyAndPerms()%>">${produkt.url}</a></u>
						<br />
						</c:forEach>
					</c:when>
					<c:otherwise>
						<b>No products were found for ingredient ${result.key.searchPhraseAnswered }.</b>

						<br />
					</c:otherwise>


				</c:choose>


				<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
					value="${innaOpcjaName}">
					Other:
				<input type="text" name="${skladnikName}${skladnikCount.count}_${innyUrlName}">
				<br />
				<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
					value="${pominOpcjaName}">
					Skip this ingredient (for your own risk)
				
				
			</c:forEach>

			<input type="submit" value="Submit">
		</form>
	
	</section>
	

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp"/>


</body>
</html>