<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<jsp:include page="bootstrap/includes/constants.jsp" />

</head>
<body>
	<font style="color: red;">${invalidInputInformation}</font>

	<form action="nutrientsForFoodName">
		Paste ndbno (obtainded from United States Department of Agriculture
		National Nutrient Database):<br> <input type="number"
			name="${ndbno}"> 
		<br>
		Or, alternatively, search for food:<br>
		<input type="text" name="${foodName}">
		<br>
		<input type="submit">
	</form>



</body>

</html>