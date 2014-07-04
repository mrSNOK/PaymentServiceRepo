$(document).ready( function() {
	var betweenDates = false;
	var pagSDate;
	var pagEDate;
function getOperations(pageSize,pageNumber){
	$.ajax({
		type:"GET",
		contentType: "application/json",
		url:"operations?size="+pageSize+"&page="+pageNumber,
		statusCode:{
			200 :function(response) {
				$('#operationsTable tbody').remove();
				$('#pagination').empty();
				$('#operationErrors').empty();
				$('#operationsTable').show();
				$('#pageSizeSelector').show();
				$('#operationErrors').slideUp('normal');
				$.each(response.data, function(i, item) {
					$('<tr>',{
						id:'row_operation_'+item.id
					}).append(
					$('<td>').text(item.id),
			 		$('<td>').text(item.admin),
					$('<td>').text(item.user),
					$('<td>').text(item.amount),
					$('<td>').text(item.created)
					).appendTo('#operationsTable');
				});
				var pgs = pageNumber;
				pgs++;
				for ( var i = 1; i <= response.pages; i++ ) {
					if(i == pgs){
						$('<div/>',{
						id:i-1,
						class:'selectedPage',
						text:' '+i+' '
						}).appendTo('#pagination');
					}else{
						$('<div/>',{
						id:i-1,
						class:'page',
						text:' '+i+' '
						}).appendTo('#pagination');
					};
					
				};
			},
			404 :function(response) {
				$('#operationsTable').hide();
				$('#pageSizeSelector').hide();
				$('#operationErrors').empty();
				$("#operationErrors").append(
				$('<span>').text(response.responseJSON.errors.message));
				$('#operationErrors').slideDown('normal');
			}
		}
	});
}

function getOperationsBetweenDates(startDate,endDate,pageSize,pageNumber){
	var jsonObject = new Object();
	if(startDate.length > 0) 
		jsonObject.startDate = startDate;
	if(endDate.length > 0) 
		jsonObject.endDate = endDate;
	var data = JSON.stringify(jsonObject);
	$.ajax({
		type:"POST",
		data:data,
		contentType: "application/json",
		url:"operations/betweendates?size="+pageSize+"&page="+pageNumber,
		statusCode:{
			200 :function(response) {
				$('#operationsTable tbody').remove();
				$('#pagination').empty();
				$('#operationErrors').empty();
				$('#operationsTable').show();
				$('#pageSizeSelector').show();
				$('#operationErrors').slideUp('normal');
				$.each(response.data, function(i, item) {
					$('<tr>',{
						id:'row_operation_'+item.id
					}).append(
					$('<td>').text(item.id),
			 		$('<td>').text(item.admin),
					$('<td>').text(item.user),
					$('<td>').text(item.amount),
					$('<td>').text(item.created)
					).appendTo('#operationsTable');
				});
				var pgs = pageNumber;
				pgs++;
				for ( var i = 1; i <= response.pages; i++ ) {
					if(i == pgs){
						$('<div/>',{
						id:i-1,
						class:'selectedPage',
						text:' '+i+' '
						}).appendTo('#pagination');
					}else{
						$('<div/>',{
						id:i-1,
						class:'page',
						text:' '+i+' '
						}).appendTo('#pagination');
					};
					
				};
			},
			400 :function(response) {
				$('#operationsTable').hide();
				$('#pageSizeSelector').hide();
				$('#operationErrors').empty();
				$('#operationsTable tbody').remove();
				$('#pagination').empty();
				$.each(response.responseJSON.errors, function(i, item) {
					$('#operationErrors').append(
							$('<span>').text(response.responseJSON.errors.message));
				});
				$('#operationErrors').slideDown('normal');
			},
			404 :function(response) {
				$('#operationsTable').hide();
				$('#pageSizeSelector').hide();
				$('#operationErrors').empty();
				$('#operationsTable tbody').remove();
				$('#pagination').empty();
				$.each(response.responseJSON.errors, function(i, item) {
					$('#operationErrors').append(
							$('<span>').text(response.responseJSON.errors.message));
				});
				$('#operationErrors').slideDown('normal');
			}
		}
	});
}

function getPagesize(){
	return $("#pageSizeSelector :selected").text();
}
$("#pagination").on("click",".page",function () {
	if (betweenDates)
		getOperationsBetweenDates(pagSDate,pagEDate,getPagesize(),$(this).attr('id'));
	else
		getOperations(getPagesize(),$(this).attr('id'));
});
$("#pageSizeSelector").change(function () {
	if (betweenDates)
		getOperationsBetweenDates(pagSDate,pagEDate,getPagesize(),0);
	else
		getOperations(getPagesize(),0);
});
$("#resetDatesBtn").click(function(e) {
	$("#startDate").val('');
	$("#endDate").val('');
	betweenDates = false;
	getOperations(getPagesize(),0);
});
$("#sendDatesBtn").click(function(e) {
	var startDate = $.trim($("#startDate").val());
	var endDate = $.trim($("#endDate").val());
	console.log(startDate);
	console.log(endDate);
	if (startDate.length>0 || endDate.length>0){
		betweenDates = true;
		pagSDate = startDate;
		pagEDate = endDate;
		getOperationsBetweenDates(startDate,endDate,getPagesize(),0);
	}
});
$('#startDate').focus(function(){
	$('#formatDateLabelStart').hide('fast');
});
$('#startDate').focusout(function(){
	text = $.trim($(this).val());
	if( text.length == 0 )
		$('#formatDateLabelStart').show('fast');
});
$('#endDate').focus(function(){
	$('#formatDateLabelEnd').hide('fast');
});
$('#endDate').focusout(function(){
	text = $.trim($(this).val());
	if( text.length == 0 )
		$('#formatDateLabelEnd').show('fast');
});
getOperations(getPagesize(),0);
});