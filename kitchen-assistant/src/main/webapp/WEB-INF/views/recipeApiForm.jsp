<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="bootstrap/includes/constants.jsp" />

<title>Insert title here</title>

</head>
<body>
	<font style="color: red;">${invalidUrlInformation}</font>
	
	<form action="apirecipes">
		Search for:<br>
		<input type="text" name="${recipeSearchPhrase_name}">
	</form>
	
	
	
</body>

</html>