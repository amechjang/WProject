<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
	<head>
		<title>My upload page</title>
	</head>
	<body>
		<form action="Upload" method="post" enctype="multipart/form-data"> 
			name:<input type="text" name="user"  /><br><br>
			file:<input type="file" name="img" /><br><br>
				<input type="submit" value="Submit"/>
		</form>
	</body>
</html>
