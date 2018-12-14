<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
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


	<section id="hero" class="wow fadeIn">
	<div class="hero-container">

		<div class="funkyradio">
			<div class="funkyradio-warning">
				<input type="radio" name="radio" value="${pominOpcjaName}" id="${skladnikRadioIdPrefix}${pominOpcjaName}" />
				<label for="${skladnikRadioIdPrefix}${pominOpcjaName}">${skipOptionText}</label>
			</div>
			<div class="funkyradio-primary">
				<input type="radio" name="radio" id="${skladnikRadioIdPrefix}one" />
				<label for="${skladnikRadioIdPrefix}one">First Option p</label>
			</div>
			<div class="funkyradio-primary">
				<input type="radio" name="radio" id="${skladnikRadioIdPrefix}two" />
				<label for="${skladnikRadioIdPrefix}two">Fifth Option warning</label>
			</div>


		</div>
	</div>


	</section>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
