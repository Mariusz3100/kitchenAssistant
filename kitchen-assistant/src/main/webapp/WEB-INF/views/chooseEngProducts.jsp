<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>

<meta charset="UTF-8">
<jsp:include page="include/constants.jsp" />

<title>Choose Products</title>
</head>
<body>

	<div>
		<h1>

			<br>For url <i>
			"<%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name) %>"
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
								- [${produkt.cena} $, ${produkt.recountedPrice}] ${produkt.nazwa} <u>${produkt.url}</u>
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



		<br /> <br /> 
		</div>

</body>
</html>