<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>

<meta charset="UTF-8">

<title>Insert title here</title>
</head>
<body>

	<div>
		<h1>
			Oto składniki znalezione w przepisie pod url <i>" <%=pageContext.getRequest()
					.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name)%>
				"
			</i> wraz z ich wielkościami. Jeśli uważasz, że któraś z wielkości
			została podana błędnie, możesz ją poprawić!
		</h1>
		
		
		<form action="chooseProdukts">
			<input type="hidden" name="${liczbaSkladnikow}"
				value="${fn:length(results)}">
			<input type="hidden"
				name="${recipeUrl_name}" value="
				<%=pageContext.getRequest()
					.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name)%>"
			>


			<c:if test="${fn:length(results)-1>0}">
			<c:forEach begin="0" end="${fn:length(results)-1}" varStatus="loop">

				<br>
				<br>


				<input type="hidden"
					name="${skladnikName}${loop.index}_${searchPhraseName}"
					value="${results[loop.index].searchPhraseAnswered}">

				<input type="hidden"
					name="${skladnikName}${loop.index}_${produktPhraseName}"
					value="${results[loop.index].produktPhrase}">
			
				<b>${results[loop.index].searchPhraseAnswered}</b>:
				<br>

				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${quantityName}"
							value="${results[loop.index].quantity}">
				${quantities[loop.index]}-->
				<input type="text"
					name="${skladnikName}${loop.index}_${quantityAmountName}"
					value="${quantities[loop.index].amount}">
				<select name="${skladnikName}${loop.index}_${quantityTypeName}">
					<c:forEach var="typ" items="${amountTypesWithoutCalories}">
						<option value="${typ}"
							${typ == quantities[loop.index].type ? 'selected="selected"' : ''}>
							${typ}</option>
					</c:forEach>

				</select>

			</c:forEach>

			</c:if>

			<input type="submit" value="Submit">
		</form>



		<br /> <br /> System nie znalazł żadnego produktu? A może znalazł,
		ale to co zostało wybrane twoim zdaniem nie pasuje do przepisu? Na
		końcu każdej listy proponowanych produktów jest opcja na własną
		propozycję, gdzie możesz wpisać url produktu, o jaki twoim zdaniem
		chodziło.
	</div>

</body>
<jsp:include page="include/constants.jsp" />

</html>