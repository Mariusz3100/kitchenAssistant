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
			Dla url <i>"
			<%= pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name) %>
			"</i> znaleziono następujące składniki:
		</h1>



		<form action="correctProdukts">
			<input type="hidden" name="${liczbaSkladnikow}" value="${fn:length(skippedResults)+fn:length(correctResults)+fn:length(badResults)}">
			<input type="hidden" name="${recipeUrl_name}" value="${url}">
			<c:set var="skladnikiCount" value="1" scope="page" />
			
			<c:forEach var="result" items="${correctResults}">
				<c:set var="skladnikiOuterCount" value="${skladnikiOuterCount + 1}" scope="page"/>

				<br>

				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
					value="${result.quantity}">
				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
					value="${result.searchPhraseAnswered}">
				
					<!-- Powinien być zawsze tylko jeden element -->
					<b>${result.searchPhraseAnswered } -> </b> [${result.produkt.cena} zł] ${result.produkt.nazwa} ${result.produkt.url}	
						<br>
					<input type="radio"
						name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
						checked="checked" value="${radioValuePrefix}${result.produkt.url}">
				<br />
			</c:forEach>


			<c:choose>
				<c:when test="${fn:length(skippedResults) gt 0}">
					<c:forEach var="result" items="${skippedResults}">
						<c:set var="skladnikiOuterCount" value="${skladnikiOuterCount + 1}" scope="page"/>

						<br>
						<br>
						<input type="hidden"
							name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
							value="Składnik oznaczony do pominięcia">
						<input type="hidden"
							name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
							value="${result}">
						<input type="radio"
							name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
							checked="checked" value="${pominOpcjaName}">
						<b>Składnik ${result} oznaczono jako pomijalny</b>
					</c:forEach>

				</c:when>
				<c:otherwise>

				</c:otherwise>


			</c:choose>
			<br>
			<br>
			 <b>Niestety, podane przez ciebie produkty dla
				następujących składników nie mogły zostać przetworzone. Proszę,
				podaj jakieś poprawne propozycje:</b>

			<br>
			<br>
			<c:forEach var="result" items="${badResults}">
				<c:set var="skladnikiOuterCount" value="${skladnikiOuterCount + 1}" scope="page"/>

				<br>

				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
					value="${result.quantity}">
				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
					value="${result.searchPhraseAnswered}">
				<b>Składnik ${result.searchPhraseAnswered } nie został poprawnie
					wybrany: ${result.invalidityReason} </b>
				<c:choose>
					<c:when test="${fn:length(result.produkts) gt 0}">
						<b>Dla składnika ${result.searchPhraseAnswered } znaleziono
							następujące produkty:</b>
						<br>
						<c:forEach var="produkt" items="${result.produkts}"
							varStatus="skladnikCount">
							<input type="radio"
								name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
								value="${radioValuePrefix}${produkt.url}">	
								- [${produkt.cena} zł] ${produkt.nazwa} ${produkt.url}
						<br />
						</c:forEach>

					</c:when>
					<c:otherwise>
						<b>Dla składnika ${result.searchPhraseAnswered } nie znaleziono
							żadnych produktów.</b>

						<br />
					</c:otherwise>


				</c:choose>


				<input type="radio"
					name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
					value="${innaOpcjaName}">
					Propozycja własna:
				<input type="text"
					name="${skladnikName}${skladnikiOuterCount}_${innyUrlName}">
				
				<br>
					<input type="radio" name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
					value="${pominOpcjaName}">
					Pomin ten skladnik (na własne ryzyko):
				
				<br />
			</c:forEach>

			<input type="submit" value="Submit">
		</form>



		<br /> <br /> System nie znalazł żadnego produktu? A może znalazł,
		ale to co zostało wybrane twoim zdaniem nie pasuje do przepisu? Na
		końcu każdej listy proponowanych produktów jest opcja na własną
		propozycję, gdzie możesz wpisać url produktu, o jaki twoim zdaniem
		chodziło.
	</div>

</body>
</html>