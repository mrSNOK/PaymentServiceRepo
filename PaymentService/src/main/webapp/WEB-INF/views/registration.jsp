<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><spring:message code="label.titleRegister" /> </title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
	<link href='http://fonts.googleapis.com/css?family=Arimo:700&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Roboto+Slab:300&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="resources/js/registration.js"></script>
</head>
<body>

<div class="header">	
    <div class="header-top">
       <div class="wrap"> 
	         <div class="logo">
				<img src="resources/images/logo.png" alt=""/>
			 </div>
			 <div class="cssmenu">
				<ul>
					<li><a href="${pageContext.request.contextPath}"><spring:message code="label.main" /></a></li>
					<li class="active"><a href="${pageContext.request.contextPath}/register"><spring:message code="label.register" /></a></li>
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
		 <div class = "regholder">
		 <div class = "register">
		 	<form:form  method="POST" action="register" commandName="registrationForm">
				<label for= "emailRegInput" class ="emailRegLabel" id ="emailRegLabel"><spring:message code="label.login" /><span class="regError" id="emailError" ><form:errors path="email"/></span></label>
				<form:input path="email" class = "emailRegInput" id = "emailRegInput"/>
				<label for= "passwordRegInput" class ="passwordRegLabel" id ="passwordRegLabel"><spring:message code="label.password" /><span class="regError" id="passError"><form:errors path="password"/></span></label>
				<form:password path="password" class = "passwordRegInput" id = "passwordRegInput"/>
				<label for= "passwordConfirmRegInput" class ="passwordConfirmRegLabel" id ="passwordConfirmRegLabel"><spring:message code="label.confirmPassword" /><span class="regError" id="confirmPassError"><form:errors path="confirmPassword"/></span></label>
				<form:password path="confirmPassword" class = "passwordConfirmRegInput" id = "passwordConfirmRegInput"/>
				<input type="submit" class = "confirmButton" id = "confirmButton" value=<spring:message code="button.register" /> />
			
			</form:form>
		 </div>
		 </div>
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