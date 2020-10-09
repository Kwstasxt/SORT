<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>SORT PROJECT</title>
</head>
<body>
   Team A Exchange <br><br> 
   
   <!-- to show username  -->
   <!-- <h3 th:inline="text">Welcome [[${#httpServletRequest.remoteUser}]]</h3> -->
   
   <!-- logout button -->
   <!-- 
   <form th:action="@{/logout}" method="post">
    <input type="submit" value="Logout" />
   </form> 
   -->
   
   <form action="/input" method="POST">
   
	   	<input type="submit" name="input" value="Register"/>
	   	
	   	<br><br>
	   
	   	<input type="submit" name="input" value="Login"/>
   
   </form>
   
</body>
</html>