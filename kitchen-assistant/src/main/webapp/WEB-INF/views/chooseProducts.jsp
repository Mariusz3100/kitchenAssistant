<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>

<meta charset="UTF-8">
<jsp:include page="include/constants.jsp" />

<title>Insert title here</title>
</head>
<body>

	<div>
		<h1>

			<br>Dla url <i>
			"<%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name) %>"
			</i> znaleziono następujące składniki:

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
						<b>Dla składnika ${result.key.searchPhraseAnswered } [${result.value}]znaleziono
							następujące produkty:</b>
						<br>
						

						<c:forEach var="produkt" items="${result.key.produkts}"
							varStatus="opcjaCount">
							<input type="radio"
								name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}" value="${radioValuePrefix}${produkt.url}">	
								- [${produkt.cena} zł, ${produkt.recountedPrice}] ${produkt.nazwa} <u>${produkt.url}</u>
						<br />
						</c:forEach>
					</c:when>
					<c:otherwise>
						<b>Dla składnika ${result.key.searchPhraseAnswered } nie znaleziono
							żadnych produktów.</b>

						<br />
					</c:otherwise>


				</c:choose>


				<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
					value="${innaOpcjaName}">
					Propozycja własna:
				<input type="text" name="${skladnikName}${skladnikCount.count}_${innyUrlName}">
				<br />
				<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
					value="${pominOpcjaName}">
					Pomin ten skladnik (na własne ryzyko)
				
				
			</c:forEach>

			<input type="submit" value="Submit">
		</form>



		<br /> <br /> System nie znalazł żadnego produktu? A może znalazł, ale
		to co zostało wybrane twoim zdaniem nie pasuje do przepisu? Na końcu
		każdej listy proponowanych produktów jest opcja na własną propozycję,
		gdzie możesz wpisać url produktu, o jaki twoim zdaniem chodziło.
	</div>

</body>
</html>