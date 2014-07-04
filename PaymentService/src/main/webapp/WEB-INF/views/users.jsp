<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><spring:message code="label.titleUsers" /></title>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
	<link href='http://fonts.googleapis.com/css?family=Arimo:700&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Roboto+Slab:300&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<script> var amountLabel="<spring:message code='label.amount'/>"; </script>
	<script src="resources/js/users.js"></script>
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
					<li class="active"><a href="users"><spring:message code="label.usersList" /></a></li>
					<li><a href="operations" ><spring:message code="label.journal" /></a></li>
					<li><a href="logout" ><spring:message code="label.Logout" /></a></li>
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
		 <div class = "contentholder">
		 	<div class = "adminContent">
		 		<div class ="pageTitle">
		 			<spring:message code="label.balanceControl" /><br>
				</div>
				
				
				<div class = "controls">
					<div class = "inputBlock">
						<label for= "startDate" class ="startDateLabel" id ="startDateLabel"><spring:message code="table.users.email" /></label>
						<input id="email" type="text"/>
					</div>
					<input type="button" id="sendEmailBtn" value="<spring:message code='button.searchByEmail'/>" />
					<input type="button" id="resetEmailBtn" value="<spring:message code="button.reset"/>" />
				
				</div>
				
				<div class = "errors" id = "userErrors"></div>
				<table id="usersTable">
					<caption><spring:message code="label.titleUsers" /></caption>
					<thead>
        				<tr>
           					<th><spring:message code="table.users.id"/></th>
    						<th><spring:message code="table.users.email"/></th>
            				<th><spring:message code="table.users.balance"/></th>
            				<th><spring:message code="table.users.registred"/></th>
            				<th></th>
        				</tr>
    				</thead>
    				<tbody>
    				</tbody>
				</table>
	
				<div class = "pagination" id = "pagination"></div>

				<select class="pageSizeSelector" id="pageSizeSelector">
					<option value="1">5</option>
					<option value="2">10</option>
					<option value="3">15</option>
					<option value="4">20</option>
				</select>
				<div id = "popups"></div>
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