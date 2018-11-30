<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">

<jsp:include page="includes/headInclude.jsp" />
</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />

	<!--==========================
  Hero Section
============================-->
	<section id="hero" class="wow fadeIn">
	<div class="hero-container">
		<h1>recipe eng url form</h1>

		<form action="correctQuantities">
			<h1>Paste url of a recipe from edaman api:</h1>
			<br> ${recipeUrl_name} <input type="text"
				name="${recipeUrl_name}">
			<button type="button" class="btn btn-success">Parse Recipe</button>
		</form>
	</div>
	</section>


	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>


</body>
</html>
