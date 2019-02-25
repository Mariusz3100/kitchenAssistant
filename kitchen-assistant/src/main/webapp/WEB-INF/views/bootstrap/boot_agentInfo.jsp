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
	<section id="hero" class="wow fadeIn">
	<div class="hero-container">


		<h3 class="ingredient-heading">${agent.name}</h3>
		<ul>
			<li><b>state:</b> ${agent.state}</li>
			<li><b>roles:</b> <c:forEach var="role"
					items="${agentRoles[agent]}" varStatus="status">
							${role} 
						</c:forEach></li>
			<li><b>descriprion:</b> ${agent.description}</li>

			<li><b>agent:</b>${agent}</li>

		</ul>
		<h3 class="ingredient-heading">Agent logs:</h3>


		<div>${fn:replace(agent.htmlLog, newLine, '<br />')}</div>
	</div>



	</section>


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
