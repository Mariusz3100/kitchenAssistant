<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
	<title>Kitchen Assistant Page</title>

<jsp:include page="includes/headInclude.jsp" />
<jsp:include page="includes/constants.jsp" />

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />


	<section id="screenshots" class="padd-section text-center wow fadeInUp">

	<div class="container">


		<div class="section-title text-center">
			
			<h3>Recipes found for 
			"<%=pageContext.getRequest().getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeSearchPhrase_name)%>":</h3>
		</div>
	</div>

	<div class="container">
		<div class="owl-carousel owl-theme no-autoplay">

			<div>
				<img src="static/img/svg/cloud.svg" alt="img" class="img-fluid">
				<h4>introducing whatsapp</h4>
				<p>Lorem Ipsum is simply dummy text of the printing and
					typesetting industry</p>
				<a href="#">read more</a>
			</div>
			<div>
				<img src="static/img/svg/planet.svg" alt="img" class="img-fluid">
				<h4>user friendly interface</h4>
				<p>Lorem Ipsum is simply dummy text of the printing and
					typesetting industry</p>
				<a href="#">read more</a>

			</div>
			<div>

				<img src="static/img/svg/asteroid.svg" alt="img" class="img-fluid">
				<h4>build the app everyone love</h4>
				<p>Lorem Ipsum is simply dummy text of the printing and
					typesetting industry</p>
				<a href="#">read more</a>

			</div>
			<div>
				<img src="static/img/screen/4.jpg" alt="img">
			</div>
			<div>
				<img src="static/img/screen/5.jpg" alt="img">
			</div>


		</div>
	</div>

	</section>





	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>

	<jsp:include page="includes/bottomImports.jsp"/>
</body>
</html>
