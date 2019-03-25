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

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />

	<!--==========================
  Hero Section
============================-->

			<h3 class="ingredient-heading">List of all agents registered in
				the system</h3>


			<c:forEach var="agent" items="${agents}" varStatus="status">
				<div class="section-shortcut" id="${properResultsSection_name}"></div>
				<div class="single-ingredient-section">
	

				<h3 class="ingredient-heading">${agent.value.name}</h3>
				<ul>
					<li><b>state:</b> ${agent.value.state}</li>
					<li><b>busy:</b> ${agent.value.busy}</li>
					
					<li><b>roles:</b> <c:forEach var="role"
							items="${agentRoles[agent.value]}" varStatus="status">
							${role} 
						</c:forEach></li>
					<li><b>descriprion:</b> ${agent.value.description}</li>

					<li>${agent.value}</li>
					<li><a href="../boot_agentInfo?${agentName}=${agent.key}">Details</a></li>

				</ul>
				</div>
			</c:forEach>
	


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
