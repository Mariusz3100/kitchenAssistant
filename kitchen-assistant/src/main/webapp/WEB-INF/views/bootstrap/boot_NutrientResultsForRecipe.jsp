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
	<section id="hero" class="wow fadeIn">
	<div class="container"
		style="padding-top: 1px; overflow-y: auto; max-height: 90vh; max-width: 95vw">

		<h3 style="margin-top: 2px">
			Results for <u>${productIdObject.name}</u> (ndbno
			${productIdObject.ndbno}):
		</h3>
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
						class="nutrition-table-header nutrition-header-cell">Name of
						the produkt chosen</th>
					<c:forEach var="nutrient" items="${allNutrients}">

						<th
							class="nutrition-table-header  nutrition-header-cell width:${80/fn:length(allNutrients)}vw"
							scope="col">
							<div class="outer-vertical-text">
								<div class="vertical-text">
									<pre style="width: 160px;">${nutrient}</pre>
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
						<td scope="row">Key: ${ingredientToNutrients.key}</td>

						<c:forEach var="nutrient" items="${allNutrients}">

							<td>${ingredientToNutrients.value[nutrient]}</td>
						</c:forEach>
					</tr>
				</c:forEach>


			</tbody>
		</table>
	</div>
	</section>


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
