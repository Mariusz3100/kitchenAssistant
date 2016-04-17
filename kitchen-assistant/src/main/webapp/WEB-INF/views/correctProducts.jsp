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

		<form action="chooseQuantities">
			<c:set var="skladnikiCount" value="1" scope="page" />
			
			<c:forEach var="result" items="${correctResults}">
				<c:set var="skladnikiOuterCount" value="${skladnikiOuterCount + 1}" scope="page"/>

				<br>

				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
					value="${result.quantity}">
				<input type="hidden"
					name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
					value="${result.searchPhrase}">


				<c:forEach var="produkt" items="${result.produkts}"
					varStatus="opcjaCount">
					<!-- Powinien być zawsze tylko jeden element -->
					<b>${result.searchPhrase } -> </b> [${produkt.cena} zł] ${produkt.nazwa} ${produkt.url}	
						<br>
					<input type="radio"
						name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
						checked="checked" value="${produkt.url}">
				</c:forEach>



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
					value="${result.searchPhrase}">
				<b>Składnik ${result.searchPhrase } nie został poprawnie
					wybrany: ${result.invalidityReason} </b>
				<c:choose>
					<c:when test="${fn:length(result.produkts) gt 0}">
						<b>Dla składnika ${result.searchPhrase } znaleziono
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
						<b>Dla składnika ${result.searchPhrase } nie znaleziono
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