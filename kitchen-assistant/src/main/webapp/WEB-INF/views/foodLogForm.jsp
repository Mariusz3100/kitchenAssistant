<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>

</head>
<body>
	<font style="color: red;">${invalidPhraseInformation}</font>
	
	<form action="correctQuantities">
		Ok, what have you eaten? Write everything here, in form:<br>
		<textarea name="${foodPhrase}">
		quantity:produkt, quantity:produkt...
		</textarea>
	</form>
	
</body>
<jsp:include page="include/constants.jsp" />

</html>