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
			Oto składniki znalezione w przepisie pod url <i>" <%=pageContext.getRequest()
					.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name)%>
				"
			</i> wraz z ich wielkościami. Jeśli uważasz, że któraś z wielkości
			została podana błędnie, możesz ją poprawić:
		</h1>

		<form action="quantitiesCorrected">
			<c:forEach begin="0" end="${fn:length(results)-1}" varStatus="loop">
    
				<br>

				<input type="hidden"
					name="${skladnikName}${loop.index}_${searchPhraseName}"
					value="${results[loop.index].searchPhrase}">


				<c:forEach var="produkt" items="${results[loop.index].produkts}"
					varStatus="opcjaCount">
					<!-- Powinien być zawsze tylko jeden element -->
					<b>${results[loop.index].searchPhrase } -> </b> [${produkt.cena} zł] ${produkt.nazwa} ${produkt.url}	<br>
					<input type="hidden"
					name="${skladnikName}${loop.index}_${produktUrl_name}"
					value="${produkt.url}">
				</c:forEach>
				
				${quantities[loop.index]} -->
				<input type="text" name="${skladnikName}${loop.index}_${quantityAmountName}"
				 value="${quantities[loop.index].amount}">
				<select name="${skladnikName}${loop.index}_${quantityTypeName}">
				 	<c:forEach var="typ" items="${amountTypesWithoutCalories}">
				 		<option value="${typ}"  ${typ == quantities[loop.index].type ? 'selected="selected"' : ''}>
				 			${typ}
				 		</option>
				 	</c:forEach>
				 	
				 </select>
				
				<input type="hidden"
					name="${skladnikName}${loop.index}_${produktPhraseName}"
					value="${results[loop.index].produktPhrase}">
				<input type="hidden"
					name="${skladnikName}${loop.index}_${searchPhraseName}"
					value="${results[loop.index].searchPhrase}">
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