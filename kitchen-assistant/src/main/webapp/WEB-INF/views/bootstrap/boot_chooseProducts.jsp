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

	<form action="b_productsChosen" target="_blank">
		<input type="hidden" name="${liczbaSkladnikow}"
			value="${fn:length(results)}"> <input type="hidden"
			name="${recipeUrl_name}" value="${recipeUrl}"> <input
			type="hidden" name="${recipeName_name}" value="${recipeName}">
		<div class="empty-page-info"></div>

		<c:choose>
			<c:when test="${fn:length(results) gt 0}">
				<h2>Details for "${recipeName}"</h2>


				<div class="ingredient-list">
					<c:forEach var="result" items="${results}"
						varStatus="skladnikCount">

						<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${quantityName}"
							value="${result.key.quantity}">
						<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${produktPhraseName}"
							value="${result.key.produktPhrase}">
						<input type="hidden"
							name="${skladnikName}${skladnikCount.count}_${searchPhraseName}"
							value="${result.key.searchPhraseAnswered}">

						<div class="section-shortcut"
							id="${skladnikName}${skladnikCount.count}_${skladnikSectionName}"></div>
						<section class="wow fadeIn single-ingredient-section">
						<div class="container with-left-shortcuts">
							<div class="shortcuts-on-the-left">
								<c:forEach var="innerResult" items="${results}"
									varStatus="innerSkladnikCount">
									<a
										href="#${skladnikName}${innerSkladnikCount.count}_${skladnikSectionName}">&#9636</a>
								</c:forEach>
							</div>
							<div class="funkyradio">
								<c:choose>
									<c:when test="${fn:length(result.key.produkts) gt 0}">
										<h3 class="ingredient-heading">For ingredient
											"${result.key.searchPhraseAnswered }" [${result.value}] these
											products were found:</h3>
									</c:when>
									<c:otherwise>
										<h3 class="ingredient-heading">No products were found for
											ingredient "${result.key.searchPhraseAnswered}".</h3>
									</c:otherwise>
								</c:choose>


								<div class="dynamic-size"
									style="overflow-y: auto; max-height: 60vh">
									<c:forEach var="produkt" items="${result.key.produkts}"
										varStatus="opcjaCount">
										<div class="funkyradio-success">
											<input type="radio"
												name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
												id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${opcjaCount.index}"
												value="${radioValuePrefix}${produkt.url}" /> <label
												for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${opcjaCount.index}">[${produkt.cena}
												$, ${produkt.recountedPrice}] ${produkt.nazwa}
													<c:url var="escapedUrl" value="${produkt.url}" />
													<a href="${productByUrlSuffix}?${produktUrl_name}=${escapedUrl}">details</a>
												
												</label>
										</div>


									</c:forEach>

									<div class="funkyradio-success">
										<input type="radio"
											name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
											id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${innaOpcjaName}"
											value="${innaOpcjaName}" class="inny-radio-label">
										<!-- class didn't connect with style from kitchenStyle.css for some reason. Lets fo for inline-->
										<label
											for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${innaOpcjaName}"
											class="inny-radio-label" style="width: 19.6%;">
											Other: </label><input type="text" disabled="disabled"
											name="${skladnikName}${skladnikCount.count}_${innyUrlName}"
											id="${skladnikName}${skladnikCount.count}_${innyUrlName}"
											class="inny-url">

									</div>

									<div class="funkyradio-warning">

										<input type="radio"
											name="${skladnikName}${skladnikCount.count}_${skladnikRadioName}"
											id="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${pominOpcjaName}"
											value="${pominOpcjaName}"> <label
											for="${skladnikName}${skladnikCount.count}_${skladnikRadioName}_${pominOpcjaName}">

											Skip this ingredient (for your own risk) </label>
									</div>
								</div>
							</div>
						</div>


						</section>
					</c:forEach>


				</div>
			</c:when>
			<c:otherwise>
			<section class="wow fadeIn">
				<div class="container">
				<h3 class="empty-page-info">
					Url "<%=pageContext.getRequest()
							.getParameter(mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeApiId)%>"
					was not recognized by any of the data sources
				</h3>
				</div>
			</section>
			</c:otherwise>

		</c:choose>


		<jsp:include page="includes/footerInclude.jsp" />

		<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
		<button type="submit" class="btn btn-success recipe-parsed-submit"
			style="bottom: 45px;">Parse</button>
	</form>
	<jsp:include page="includes/bottomImports.jsp" />


</body>
</html>
