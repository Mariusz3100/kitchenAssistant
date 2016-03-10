<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <div align="center">
	        <h1>Produkt List</h1>
        	<table border="1">
        		<th>No</th>	
	        	<th>url</th>
	        	<th>nazwa</th>
	        	<th>skład</th>
	        	<th>opis</th>
	        	<th>cena</th>
	        	
				<c:forEach var="produkt" items="${produktList}" varStatus="status">
	        	<tr>
	        		<td>${produkt.p_id}</td>
					<td>${produkt.url}</td>
					<td>${produkt.nazwa}</td>
					<td>${produkt.sklad}</td>
					<td>${produkt.opis}</td>
					<td>${produkt.cena}</td>
						
					
	        	</tr>
				</c:forEach>	        	
        	</table>
        </div>
    </body>
</html>
