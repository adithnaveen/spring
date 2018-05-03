<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix='form' uri='http://www.springframework.org/tags/form'%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form:form method='post' action='myForm' commandName='myUser'>
		<table>
			<tr>
				<td>Name: <font color='red'><form:errors path='name' /></font></td>
			</tr>
			<tr>
				<td><form:input path='name' /></td>
			</tr>
			<tr>
				<td>Age: <font color='red'><form:errors path='age' /></font></td>
			</tr>
			<tr>
				<td><form:input path='age' /></td>
			</tr>
			<tr>
				<td><input type='submit' value='Submit' /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>