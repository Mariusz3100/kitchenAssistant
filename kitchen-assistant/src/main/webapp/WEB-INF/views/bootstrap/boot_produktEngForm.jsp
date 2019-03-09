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

	<div class="hero-container">
		<form action="b_produktByName">
			<h3>Search for products in the system:</h3>
			<input type="text" name="${produktPhraseName}">
			<button type="submit" class="btn btn-success">Search</button>

		</form>

	</div>
	<div style="min-height: 70vh">
	
		<c:if test="${not empty produktList }">
			<h3>Results found:</h3>
	
			<table class="table table-striped">
				<thead>
					<tr class="table-success" style="position: sticky; top: 1px;">
						<th scope="col">Product</th>
						<th scope="col">Price</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${produktList}"
						varStatus="productCount">
						<tr>
							<c:url var="escapedUrl" value = "${product.url}"/>
	
							<td><a href="${productByUrlSuffix}?${produktUrl_name}=${escapedUrl}">${escapedUrl} ${product.nazwa}</a></td>
							<td>${product.cena}$</td>
						</tr>
					</c:forEach>
	
	
				</tbody>
			</table>
		</c:if>
	</div>
	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
