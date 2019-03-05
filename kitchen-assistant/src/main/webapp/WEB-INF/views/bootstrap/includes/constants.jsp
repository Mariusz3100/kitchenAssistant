   
<%@page import="mariusz.ambroziak.kassistant.model.quantity.AmountTypes"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    

<c:set var="opcjaName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.opcja_name %>" scope="session"/>
<c:set var="skladnikName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.skladnik_name %>" scope="session"/>
<c:set var="searchPhraseName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.searchPhrase_name %>" scope="session"/>
<c:set var="produktPhraseName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.produktPhrase_name %>" scope="session"/>
<c:set var="produktPhrasePominiety" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.produktPhrasePominiety %>" scope="session"/>
<c:set var="quantityName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.quantity_name %>" scope="session"/>
<c:set var="quantityAmountName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.quantity_amount %>" scope="session"/>
<c:set var="quantityTypeName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.quantity_type %>" scope="session"/>
<c:set var="innaOpcjaName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.innaOpcja_name %>" scope="session"/>
<c:set var="pominOpcjaName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.pominOpcja_name %>" scope="session"/>
<c:set var="innyUrlName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.innyUrl_name %>" scope="session"/>
<c:set var="skladnikRadioName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.skladnikRadio_name %>" scope="session"/>
<c:set var="skladnikRadioIdPrefix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.skladnikRadioIdPrefix %>" scope="session"/>
<c:set var="liczbaSkladnikow" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.liczbaSkladnikow %>" scope="session"/>
<c:set var="radioValuePrefix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.radioValuePrefix %>" scope="session"/>
<c:set var="produktUrl_name" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.produktUrl_name%>" scope="session"/>
<c:set var="recipeSearchPhrase_name" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeSearchPhrase_name%>" scope="session"/>
<c:set var="recipeUrl" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name%>" scope="session"/>
<c:set var="recipeApiId" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeApiId%>" scope="session"/>
<c:set var="recipeName_name" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.recipeUrl_name%>" scope="session"/>
<c:set var="ndbno" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.ndbno%>" scope="session"/>
<c:set var="foodName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.foodName%>" scope="session"/>
<c:set var="skipOptionText" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.SKIP_OPTION_TEXT%>" scope="session"/>
<c:set var="skladnikSectionName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.skladnikSection_name %>" scope="session"/>
<c:set var="agentName" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.agentName %>" scope="session"/>

<c:set var="recipeByUrlSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.RECIPE_BY_URL_SUFFIX%>" scope="session"/>
<c:set var="recipeByNameSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.RECIPE_BY_NAME_SUFFIX%>" scope="session"/>


<c:set var="googleAuthorisationSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.GOOGLE_AUTHORISATION_SUFFIX%>" scope="session"/>
<c:set var="googleDeleteSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.GOOGLE_DELETION_SUFFIX%>" scope="session"/>
<c:set var="googleGetDataSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.GOOGLE_GET_DATA_SUFFIX%>" scope="session"/>



<c:set var="startAgentSystemSuffix" value="<%= mariusz.ambroziak.kassistant.utils.JspStringHolder.START_AGENT_SYSTEM_SUFFIX%>" scope="session"/>


<c:set var="amountTypesWithoutCalories" value="<%= AmountTypes.valuesWithoutCalories()%>" scope="session"/>