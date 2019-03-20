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
	<div class="empty-page-info"></div>

	<section id="hero" class="wow fadeIn">
	<h3 style="text-align: center;">Diet and health restrictions
		available.</h3>
		<div class="container">
			<div class="row">
				<div class="col">
					<c:choose>
						<c:when test="${not empty dietLabels && fn:length(dietLabels)>0}">
							
								<h3 class="ingredient-heading">Following diet labels can be
									used</h3>

								<ul>
									<c:forEach var="dietLabel" items="${dietLabels}"
										varStatus="status">
										<li>${dietLabel.label}(${dietLabel.parameterName})</li>
									</c:forEach>
								</ul>
						</c:when>
						<c:otherwise>
							<h3 class="ingredient-heading">No diet labels found.</h3>

						</c:otherwise>
					</c:choose>
				</div>

				<div class="col">
					<c:choose>
						<c:when
							test="${not empty healthLabels && fn:length(healthLabels)>0}">
							<h3 class="ingredient-heading">Following health labels can
								be used</h3>
								<ul>
									<c:forEach var="healthLabel" items="${healthLabels}"
										varStatus="status">
										<li>${healthLabel.label}(${healthLabel.parameterName})</li>
									</c:forEach>
								</ul>
						</c:when>
						<c:otherwise>
							<h3 class="ingredient-heading">No health labels found.</h3>

						</c:otherwise>
					</c:choose>
				</div>
			</div>

		</div>
	</section>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
