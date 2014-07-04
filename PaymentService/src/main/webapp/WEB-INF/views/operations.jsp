<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><spring:message code="label.titleOperations" /></title>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="resources/css/style.css" />
	<link href='http://fonts.googleapis.com/css?family=Arimo:700&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Roboto+Slab:300&subset=cyrillic-ext,latin' rel='stylesheet' type='text/css'>
	<script src="resources/js/operations.js"></script>
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
					<li><a href="users"><spring:message code="label.usersList" /></a></li>
					<li class="active"><a href="operations" ><spring:message code="label.journal" /></a></li>
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
		 			<spring:message code="label.operationsJournal"/><br>
				</div>
				
				<div class = "controls">
					<div class = "inputBlock">
						<label for= "startDate" class ="startDateLabel" id ="startDateLabel"><spring:message code="label.startDate" /></label>
						<div class = "InputHolder">
							<label for= "startDate" class ="formatDateLabel" id ="formatDateLabelStart"><spring:message code="label.dateFormat" /></label>
							<input id="startDate" type="text"/>
						</div>
					</div>
					<div class = "inputBlock">
						<label for= "endDate" class ="endDateLabel" id ="endDateLabel"><spring:message code="label.endDate" /></label>
						<div class = "InputHolder">
							<label for= "endDate" class ="formatDateLabel" id ="formatDateLabelEnd"><spring:message code="label.dateFormat" /></label>
							<input id="endDate" type="text"/>
						</div>
					</div>
					<input type="button" id="sendDatesBtn" value="<spring:message code='button.searchByDates'/>" />
					<input type="button" id="resetDatesBtn" value="<spring:message code="button.reset"/>" />
				</div>
				<div class = "errors" id = "operationErrors"></div>
				<table id="operationsTable">
					<caption><spring:message code="label.titleOperations" /></caption>
					<thead>
        				<tr>
            				<th><spring:message code="table.operations.id"/></th>
    						<th><spring:message code="table.operations.admin"/></th>
            				<th><spring:message code="table.operations.user"/></th>
            				<th><spring:message code="table.operations.amount"/></th>
            				<th><spring:message code="table.operations.created"/></th>
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