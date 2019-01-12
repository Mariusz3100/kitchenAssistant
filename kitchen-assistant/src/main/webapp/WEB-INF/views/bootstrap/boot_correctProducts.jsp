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
<link rel="stylesheet" href="css/radio.css">

</head>

<body>

	<jsp:include page="includes/menuInclude.jsp" />


	<section id="hero" class="wow fadeIn">
	<div class="hero-container">


			<c:forEach var="result" items="${results}" varStatus="skladnikCount">

				<div class="funkyradio">
										
				<c:choose>
					<c:when test="${fn:length(result.key.produkts) gt 0}">
						<b>For ingredient ${result.key.searchPhraseAnswered } [${result.value}] these products were found:</b>
						<br>
						

						<c:forEach var="produkt" items="${result.key.produkts}"
							varStatus="opcjaCount">
							<c:if test="${opcjaCount.index<2}">
							        <div class="funkyradio-success">
            							<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
            							 id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${opcjaCount.index}"
            							 value="${radioValuePrefix}${produkt.url}"
            							  />
            							<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${opcjaCount.index}">[${produkt.cena} $, ${produkt.recountedPrice}] ${produkt.nazwa}</label>
        							</div>
        							
        							</c:if>
							
						</c:forEach>
					</c:when>
					<c:otherwise>
						<b>No products were found for ingredient ${result.key.searchPhraseAnswered }.</b>
					</c:otherwise>


				</c:choose>

				<div class="funkyradio-success">
					<input type="radio"
						name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
						id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${innaOpcjaName}"
						value="${innaOpcjaName}"  class="inny-radio-label">
					<!-- class didn't connect with style from kitchenStyle.css for some reason. Lets fo for inline-->
					<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${innaOpcjaName}" 
						class="inny-radio-label" style="width: 19%;"
						
					>
						Other:
					</label>
					<input type="text"
					name="${skladnikName}${skladnikCount.count}_${innyUrlName}"
					id="${skladnikName}${skladnikCount.count}_${innyUrlName}"
					class="inny-url"
					>
					
				</div>

				<div class="funkyradio-warning">

					<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
						id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${pominOpcjaName}"
						value="${pominOpcjaName}">
					<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${pominOpcjaName}">
						
						Skip this ingredient (for your own risk)
					</label>
				</div>
									</div>
				
			</c:forEach>



	</div>


	</section>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
