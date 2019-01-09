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

		<div class="funkyradio">

			<c:forEach var="result" items="${results}" varStatus="skladnikCount">

				<br>
				
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${quantityName}"
							value="${result.key.quantity}">
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${produktPhraseName}"
							value="${result.key.produktPhrase}">
				<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${searchPhraseName}"
							value="${result.key.searchPhraseAnswered}">
											
				<c:choose>
					<c:when test="${fn:length(result.key.produkts) gt 0}">
						<b>For ingredient ${result.key.searchPhraseAnswered } [${result.value}] these products were found:</b>
						<br>
						
   				 <div class="funkyradio">

						<c:forEach var="produkt" items="${result.key.produkts}"
							varStatus="opcjaCount">
							
							        <div class="funkyradio-success">
            							<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
            							 id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
            							 value="${radioValuePrefix}${produkt.url}"
            							  />
            							<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}">[${produkt.cena} $, ${produkt.recountedPrice}] ${produkt.nazwa}</label>
        							</div>
							
						</c:forEach>
					</div>
					</c:when>
					<c:otherwise>
						<b>No products were found for ingredient ${result.key.searchPhraseAnswered }.</b>

						<br />
					</c:otherwise>


				</c:choose>

				<div class="funkyradio-success">
					<input type="radio"
						name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
						id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
						value="${innaOpcjaName}">
					<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}">
						Other:
					</label>
					<input type="text" name="${skladnikName}${skladnikCount.count}_${innyUrlName}">
					
				</div>

				<div class="funkyradio-warning">

					<input type="radio" name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
						value="${pominOpcjaName}">
					<label for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}">
						
						Skip this ingredient (for your own risk)
					</label>
				</div>
				
			</c:forEach>










		</div>


	</div>


	</section>

	<jsp:include page="includes/footerInclude.jsp" />

	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
