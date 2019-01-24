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
	<div class="container">
		<c:choose>
			<c:when test="${fn:length(usdaProducts) gt 0}">
				<h1>Possible products for <u>${foodName}</u></h1>
				<div style="overflow-y: auto; max-height: 90vh">
					<ul>
						<c:forEach var="usdaProdukt" items="${usdaProducts}"
							varStatus="usdaProduktCount">
							<li><h3><a href="${usdaProdukt.parseLink }">${usdaProdukt.name}</a></h3></li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:otherwise>
				<h4>No products found for <u>${foodName}</u></h4>

			</c:otherwise>
		</c:choose>
	</div>
	</section>


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
