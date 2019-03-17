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

	<!--==========================
  Hero Section
============================-->
	<div class="empty-page-info"></div>
			<div class="container"
				style="padding-top: 1px; max-width: 95vw">

				<c:choose>
					<c:when test="${empty nutrientsMap or fn:length(nutrientsMap) <1}">
						<h3 class="ingredient-heading">Nutrient details for
							"${recipe_name_value}" were not found.</h3>
					</c:when>
					<c:otherwise>
						<h2 style="padding-top: 1px">Details for
							"${recipe_name_value}"</h2>

						<table class="table table-striped table-bordered nutrition-table">
							<thead>
								<tr class="table-success">
									<th scope="col"
										class="nutrition-table-header nutrition-table-ingredient-column">Ingredient</th>
									<th scope="col" class="nutrition-table-header"
										colspan="${fn:length(allNutrients)}">Nutrients</th>
								</tr>
								<tr class="table-success">
									<th scope="col"
										class="nutrition-table-header nutrition-header-cell">Name
										of the produkt chosen</th>
									<c:forEach var="nutrient" items="${allNutrients}">

										<th class="nutrition-table-header  nutrition-header-cell "
											style="width:${80/fn:length(allNutrients)}vw" scope="col">
											<div class="outer-vertical-text">
												<div class="vertical-text">
													<pre style="width: 175px;">${nutrient}</pre>
												</div>
											</div>
										</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ingredientToNutrients" items="${nutrientsMap}"
									varStatus="nutrientCount">
									<tr>


										<td class="td-nutrient" scope="row"><c:url
												var="escapedUrl" value="${ingredientToNutrients.key.url}" />
											<a
											href="${productByUrlSuffix}?${produktUrl_name}=${escapedUrl}">
												${ingredientToNutrients.key.nazwa} </a> <b>x${ingredientToNutrients.key.multiplier}</b>
										</td>


										<c:choose>
											<c:when test="${empty ingredientToNutrients.value or fn:length(ingredientToNutrients.value)<1}">
											<td colspan="${fn:length(allNutrients)}" style="background-color: #fff9ab;">No data found</td>
											</c:when>
											<c:otherwise>

												<c:forEach var="nutrient" items="${allNutrients}">
													<c:choose>
														<c:when
															test="${not empty ingredientToNutrients.value[nutrient].type and ingredientToNutrients.value[nutrient].type != mgType}">
															<td class="td-nutrient" style="background-color: red">
																${ingredientToNutrients.value[nutrient].amountRepresentation}</td>
														</c:when>
														<c:otherwise>
															<td class="td-nutrient">
																${ingredientToNutrients.value[nutrient].amountRepresentation}</td>
														</c:otherwise>
													</c:choose>

												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tr>
								</c:forEach>

								<tr class="table-success">
									<td scope="row"><b>Sum of all nutrients</b></td>

									<c:forEach var="nutrient" items="${allNutrients}">
										<c:choose>
											<c:when
												test="${not empty sumOfNutrients[nutrient].type and sumOfNutrients[nutrient].type != mgType}">
												<td style="background-color: red" class=td-nutrient>
													${sumOfNutrients[nutrient].amountRepresentation}</td>
											</c:when>
											<c:otherwise>
												<td class="td-nutrient">
													${sumOfNutrients[nutrient].amountRepresentation}</td>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</tr>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
