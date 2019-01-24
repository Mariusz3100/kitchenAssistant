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
		style="padding-top: 1px; overflow-y: auto; max-height: 90vh">
		<table class="table table-striped">
			<thead>
				<tr class="table-success" style="position: sticky; top: 1px;">
					<th scope="col">Nutrient</th>
					<th scope="col">Amount</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="nutrient" items="${nutrients}"
					varStatus="nutrientCount">
					<tr>
						<th scope="row">${nutrient.key.name}</th>
						<td>${nutrient.value}</td>
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
