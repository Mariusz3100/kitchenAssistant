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
		<form action="b_produktByUrl">
			<h4>Parse product by url:</h4>
			<input type="text" name="${produktUrl_name}">
			<button type="submit" class="btn btn-success">Parse</button>
		</form>
	</div>
	

	<c:if test="${not empty produkt }">
		<h3 class="single-ingredient-section">Details for "${produkt.nazwa}"</h3>
		<c:choose>
			<c:when test="${not empty apiDetailsLink}">
				<h4>
					Data source: <a href="${apiDetailsLink}">${produkt.url}</a>
				</h4>
			</c:when>
			<c:otherwise>
				<h4>Data source: ${produkt.url}</h4>
			</c:otherwise>
		</c:choose>



		<h4>Composition: ${produkt.sklad }</h4>
		<h4>Price: ${produkt.cena }$</h4>
		<h4>
			<b>Amount:</b> ${produkt.quantityPhrase }
		</h4>
		<h4>
			<b>Description:</b>
		</h4>
		<div>${produkt.opis }</div>

	</c:if>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
