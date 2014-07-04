<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<title><spring:message code="label.titleLogin" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
	<link href='http://fonts.googleapis.com/css?family=Arimo:700&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Roboto+Slab:300&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="resources/js/login.js"></script>
</head>
<body>

<div class="header">	
    <div class="header-top">
       <div class="wrap"> 
	         <div class="logo">
				<img src="resources/images/logo.png" alt=""/>
			 </div>
			 <div class = "csslogin">
			 	<form method="POST" action="<c:url value="/j_spring_security_check" />">
			 	<div class="InputHolder">
					<label for= "loginInput" class ="loginLabel" id ="loginLabel"><spring:message code="label.login" /></label>
					<input type="text" name="j_username" class = "loginInput" id = "loginInput"/>
				</div>
				<div class="InputHolder">
					<label for= "passwordInput" class ="passwordLabel" id ="passwordLabel"><spring:message code="label.password" /></label>
					<input type="password" name="j_password" class = "passwordInput" id = "passwordInput"/>
				</div>
					<input type="checkbox" name="_spring_security_remember_me" class = "rememberInput" id = "rememberInput"/> 
					<label for= "rememberInput"><spring:message code="label.remember" /></label>
					<input type="submit" class = "loginButton" id = "loginButton" value=<spring:message code="button.login" /> />	
			 	</form>
			 </div>
			 <div class="cssmenu">
				<ul>
					<li class="active"><a href=""><spring:message code="label.main" /></a></li>
					<li><a href="register"><spring:message code="label.register" /></a></li>
				</ul>
		    <div class = "csslang">
		    	<a href="?lang=en"><img src="resources/images/en.png" alt="" width="30" height="30"></a> 
				<a href="?lang=ru"><img src="resources/images/ru.png" alt="" width="30" height="30"></a>
		    </div>
		    </div>
		    <div class="clear"></div>
	   </div>
	 </div>
     <div class="header-bottom" id="tour">
		 <div class="wrap">
		 	<spring:message code="label.introduction" />
		   	  <img src="resources/images/banner.png" alt=""/>
		   	<spring:message code="label.try" />
		   	  <div class="but"><a href="register"> <spring:message code="button.join" /><img src="resources/images/arrow.png" alt=""></a></div>
		   	  <spring:message code="label.desc" />
		 </div>
	</div>
</div>
 <div class="footer">
  	  <div class="wrap">
		<div class="copy">
		    <p class="copy">© 2014 PayPaul</p>
	    </div>
	    <div class="clear"></div>
     </div>
  </div>

</body>
</html>