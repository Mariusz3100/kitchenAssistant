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

	<form action="b_productsChosen">
		<input type="hidden" name="${liczbaSkladnikow}"
			value="${fn:length(badResults)+fn:length(correctResults)+fn:length(skippedResults)}">

		Amount:${fn:length(badResults)}+${fn:length(correctResults)}+${fn:length(skippedResults)}
		<input type="hidden" name="${liczbaNiepoprawnychSkladnikow}"
			value="${fn:length(badResults)}"> <input type="hidden"
			name="${recipeUrl_name}" value="${recipeUrl}"> <input
			type="hidden" name="${recipeName_name}" value="${recipeName}">
		<c:set var="skladnikiOuterCount" value="0" scope="page" />


		<div class="empty-page-info"></div>

		<h2>Details for "${recipeName}"</h2>
		<div class="ingredient-list">


			<c:if test="${fn:length(correctResults) gt 0}">
				<div class="section-shortcut" id="${properResultsSection_name}"></div>

				<h3 class="ingredient-heading">Ingredients correctly chosen:</h3>
				<section class="wow fadeIn single-ingredient-section">
				<div class="container with-left-shortcuts">
					<div class="shortcuts-on-the-left">
						<a href="#${properResultsSection_name}">&#9636</a>
						<a href="#${skippedResultsSection_name}">&#9636</a>
						<c:set var="shortcutCount" value="${fn:length(correctResults) + fn:length(skippedResults)}"/>
						<c:forEach var="shortcut" items="${badResults}">
						<c:set var="shortcutCount" value="${shortcutCount+1}"/>
						
							<a
								href="#${skladnikName}${shortcutCount}_${skladnikSectionName}">&#9636</a>
						</c:forEach>
					</div>
					<div class="dynamic-size"
						style="overflow-y: auto; max-height: 63vh">


						<c:forEach var="result" items="${correctResults}"
							varStatus="skladnikCount">
							<c:set var="skladnikiOuterCount"
								value="${skladnikiOuterCount + 1}" scope="page" />

							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
								value="${result.quantity}">
							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${produktPhraseName}"
								value="${result.produktPhrase}">
							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
								value="${result.searchPhraseAnswered}">

							<div class="funkyradio">
								<h6>${result.searchPhraseAnswered }[${result.quantity}]</h6>

								<c:set var="produkt" value="${result.produkt}" />
								<div class="funkyradio-success">
									<input type="radio" checked="checked"
										name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
										id="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${opcjaCount.index}"
										value="${radioValuePrefix}${produkt.url}" /> <label
										for="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${opcjaCount.index}">[${produkt.cena}
										$, ${produkt.recountedPrice}] ${produkt.nazwa}</label>
								</div>

							</div>


						</c:forEach>
					</div>
				</div>
				</section>



			</c:if>








			<c:if test="${fn:length(skippedResults) gt 0}">
				<div class="section-shortcut" id="${skippedResultsSection_name}"></div>

				<h3 class="ingredient-heading">Ingredients to be skipped:</h3>
				<section class="wow fadeIn single-ingredient-section">
				<div class="container with-left-shortcuts">
					<div class="shortcuts-on-the-left">
						<a href="#${properResultsSection_name}">&#9636</a>
						<a href="#${skippedResultsSection_name}">&#9636</a>


						<c:set var="shortcutCount" value="${fn:length(correctResults) + fn:length(skippedResults)}"/>
						<c:forEach var="shortcut" items="${badResults}">
						<c:set var="shortcutCount" value="${shortcutCount+1}"/>
							<a
								href="#${skladnikName}${shortcutCount}_${skladnikSectionName}">&#9636</a>
						</c:forEach>
					</div>
					<div class="dynamic-size"
						style="overflow-y: auto; max-height: 63vh">


						<c:forEach var="result" items="${skippedResults}"
							varStatus="skladnikCount">
							<c:set var="skladnikiOuterCount"
								value="${skladnikiOuterCount + 1}" scope="page" />

							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
								value="Ingredient will be skipped">
							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${produktPhraseName}"
								value="to be skipped">
							<input type="hidden"
								name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
								value="${result.phraseAnswered}">

							<div class="funkyradio">
								<h6>${result.phraseAnswered }</h6>

								<div class="funkyradio-warning">
									<input type="radio" checked="checked"
										name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
										id="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${pominOpcjaName}"
										value="${pominOpcjaName}"> <label
										for="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${pominOpcjaName}">

										Skip this ingredient (for your own risk) </label>
								</div>

							</div>


						</c:forEach>
					</div>
				</div>
				</section>



			</c:if>







			<h3 class="ingredient-heading">Following products you specified
				were not found. Please try again.</h3>


			<c:choose>
				<c:when test="${fn:length(badResults) gt 0}">
					<c:forEach var="result" items="${badResults}"
						varStatus="skladnikCount">
						<c:set var="skladnikiOuterCount"
							value="${skladnikiOuterCount + 1}" scope="page" />
						<input type="hidden"
							name="${skladnikName}${skladnikiOuterCount}_${quantityName}"
							value="${result.quantity}">
						<input type="hidden"
							name="${skladnikName}${skladnikiOuterCount}_${produktPhraseName}"
							value="${result.produktPhrase}">
						<input type="hidden"
							name="${skladnikName}${skladnikiOuterCount}_${searchPhraseName}"
							value="${result.searchPhraseAnswered}">

						<div class="section-shortcut"
							id="${skladnikName}${skladnikiOuterCount}_${skladnikSectionName}"></div>
						<section class="wow fadeIn single-ingredient-section">
						<div class="container with-left-shortcuts">
							<div class="shortcuts-on-the-left">
								<a href="#${properResultsSection_name}">&#9636</a>
								<a href="#${skippedResultsSection_name}">&#9636</a>
						<c:set var="shortcutCount" value="${fn:length(correctResults) + fn:length(skippedResults)}"/>
						<c:forEach var="shortcut" items="${badResults}">
						<c:set var="shortcutCount" value="${shortcutCount+1}"/>
						
							<a
								href="#${skladnikName}${shortcutCount}_${skladnikSectionName}">&#9636</a>
						</c:forEach>
							</div>

							<c:forEach var="innerResult" items="${results}"
								varStatus="innerSkladnikCount">
								<a
									href="#${skladnikName}${innerSkladnikCount.count}_${skladnikSectionName}">&#9636</a>
							</c:forEach>

							<h3 class="ingredient-heading" style="color: brown">
								Ingredient ${result.searchPhraseAnswered } was not properly
								chosen:<br> ${result.invalidityReason}
							</h3>
							<div class="funkyradio">
								<c:choose>
									<c:when test="${fn:length(result.produkts) gt 0}">
										<h6 class="ingredient-heading">For ingredient
											"${result.searchPhraseAnswered }" [${result.quantityNeeded}] these
											products were found:</h6>
									</c:when>
									<c:otherwise>
										<h6 class="ingredient-heading">No products were found for
											ingredient "${result.searchPhraseAnswered}".</h6>
									</c:otherwise>
								</c:choose>


								<div class="dynamic-size"
									style="overflow-y: auto; max-height: 63vh">
									<c:forEach var="produkt" items="${result.produkts}"
										varStatus="opcjaCount">
										<div class="funkyradio-success">
											<input type="radio"
												name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
												id="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${opcjaCount.index}"
												value="${radioValuePrefix}${produkt.url}" /> <label
												for="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${opcjaCount.index}">[${produkt.cena}
												$, ${produkt.recountedPrice}] ${produkt.nazwa}
												

												<c:url var="escapedUrl" value="${product.url}" />
												<a href="${productByUrlSuffix}?${produktUrl_name}=${escapedUrl}">details</a>
												</label>
										</div>


									</c:forEach>

									<div class="funkyradio-success">
										<input type="radio"
											name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
											id="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${innaOpcjaName}"
											value="${innaOpcjaName}" disabled="disabled" class="inny-radio-label">
										<!-- class didn't connect with style from kitchenStyle.css for some reason. Lets fo for inline-->
										<label
											for="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${innaOpcjaName}"
											class="inny-radio-label" style="width: 19.6%;">
											Other: </label> <input type="text"
											name="${skladnikName}${skladnikiOuterCount}_${innyUrlName}"
											id="${skladnikName}${skladnikiOuterCount}_${innyUrlName}"
											class="inny-url">

									</div>

									<div class="funkyradio-warning">

										<input type="radio"
											name="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}"
											id="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${pominOpcjaName}"
											value="${pominOpcjaName}"> <label
											for="${skladnikName}${skladnikiOuterCount}_${skladnikRadioName}_${pominOpcjaName}">

											Skip this ingredient (for your own risk) </label>
									</div>
								</div>
							</div>

						</div>

						</section>
					</c:forEach>


				</c:when>
				<c:otherwise>
					<h3 class="empty-page-info">
						Url "<%=pageContext.getRequest()
							.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeApiId)%>"
						was not recognized by any of the data sources
					</h3>
				</c:otherwise>

			</c:choose>
		</div>


		<jsp:include page="includes/footerInclude.jsp" />

		<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
		<button type="submit" class="btn btn-success recipe-parsed-submit"
			style="bottom: 45px;">Parse</button>
	</form>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
