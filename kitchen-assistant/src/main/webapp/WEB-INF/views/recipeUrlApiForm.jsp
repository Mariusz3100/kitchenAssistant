<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>

</head>
<body>
	<font style="color: red;">${invalidUrlInformation}</font>
	
	<form action="apiRecipeParsed">
		paste recipe id:<br>
		<input type="text" name="recipeId">
	</form>
	
</body>
<jsp:include page="include/constants.jsp" />

</html>