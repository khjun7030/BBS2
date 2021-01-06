<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
 
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8");%>
<link rel="stylesheet" href="css/custom.css">

<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setPropertyname="user" property="userID" />
<jsp:setPropertyname="user" property="userPassword" />
<jsp:setPropertyname="user" property="userName" />
<jsp:setPropertyname="user" property="userGender" />
<jsp:setPropertyname="user" property="userEmail" />


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>JSP 웹사이트</title>
</head>
<body>
 
 <%
 
	String userID = null;
	if(session.getAttribute("userID") != null){
		userID = (String) session.getAttribute("userID");
	}
	
	if (userID != null) {
	   PrintWriter script = response.getWriter();
	   script.println("<script>");
	   script.println("alert('이미 로그인이 되어있습니다.')");
	   script.println("location.href = 'main.jsp'");
	   script.println("</script>"); 
	}
 
  if (user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null 
  	  || user.getUserGender() == null || user.getUserEmail() == null) {
		   PrintWriter script = response.getWriter();
		   script.println("<script>");
		   script.println("alert('모든 항목을 입력하세요')");
		   script.println("history.back()");
		   script.println("</script>");  
  } else {  
  UserDAO userDAO = new UserDAO();
  int result = userDAO.join(user);
  if(result == -1){
   PrintWriter script = response.getWriter();
   script.println("<script>");
   script.println("alert('이미 존재하는 아이디입니다.')");
   script.println("history.back()");
   script.println("</script>");    
  }
  else {
	  session.setAttribute("userID", user.getUserID());
   PrintWriter script = response.getWriter();
   script.println("<script>");
   script.println("location.href = 'main.jsp'");
   script.println("</script>");  
  }
  
  }
  
 %>
 
 
</body>
</html>