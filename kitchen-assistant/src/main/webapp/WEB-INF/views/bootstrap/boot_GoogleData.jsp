<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>
<meta charset="utf-8">
<title>Kitchen Assistant Page</title>

<jsp:include page="includes/headInclude.jsp" />
<jsp:include page="includes/constants.jsp" />
<link rel="stylesheet" href="css/radio.css">

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />

	<!--==========================
  Hero Section
============================-->
	<section id="hero" class="wow fadeIn">
	<div class="hero-container">

		<c:choose>
			<c:when test="${deleted}">
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty dietLabels && fn:length(dietLabels)>0}">
						<div>
							<h3 class="ingredient-heading">Following diet labels were found</h3>

							<ul>
								<c:forEach var="dietLabel" items="${dietLabels}"
									varStatus="status">
									<li>${dietLabel}</li>
								</c:forEach>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<h3 class="ingredient-heading">No diet labels found in your google drive</h3>

					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when
						test="${not empty healthLabels && fn:length(healthLabels)>0}">
						<h3 class="ingredient-heading">Following health labels were found</h3>
						<div>
							<ul>
								<c:forEach var="healthLabel" items="${healthLabels}"
									varStatus="status">
									<li>${healthLabel}</li>
								</c:forEach>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<h3 class="ingredient-heading">No health labels found in your google drive</h3>

					</c:otherwise>
				</c:choose>
			</c:otherwise>

		</c:choose>



	</div>
	</section>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
