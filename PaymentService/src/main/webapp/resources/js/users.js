$(document).ready( function() {
	var currentPage = 0;
function getUsers(pageSize,pageNumber){
	$.ajax({
		type:"GET",
		contentType: "application/json",
		url:"users?size="+pageSize+"&page="+pageNumber,
		statusCode:{
			200 :function(response) {
				$('#usersTable tbody').remove();
				$('#pagination').empty();
				$('#userErrors').empty();
				$('#usersTable').show();
				$('#pageSizeSelector').show();
				$('#userErrors').slideUp('normal');
				$('#popups').empty();
				$.each(response.data, function(i, item) {
					$('<tr>',{
						class: 'row_user',
						id:'row_user_'+item.id
					}).append(
					$('<td>').text(item.id),
			 		$('<td>').text(item.email),
					$('<td>').text(item.balance),
					$('<td>').text(item.registred),
					$('<input>', {
						class :'showBtn',
						type :'button',
						value : response.i18n.replenish
						}),
					$('<input>', {
						class :'delBtn',
						type :'button',
						value : response.i18n.del
						})
					).appendTo('#usersTable');
					
					$('#popups').append($('<div/>',{
						class:'popup_shadow',
						id:'popup_shadow_'+item.id
						}).append($('<div/>',{
							class:'popup',
							id:'popup_'+item.id
							}).append($('<span/>',{
										id:'popup_user_'+item.id,
										class:'userEmail',
										text :item.email}),
										$('<div/>',{
											class:'replenishErrors'
											}),
										$('<div/>',{
											class:'controls',
											}).append(
												$('<div/>',{
													class:'inputBlock'
													}).append(
														$('<label for="amountInput_'+item.id+'">',{
															class:'amountLabel'
															}).text(amountLabel),
														$('<input>', {
															class :'amountInput',
															id:'amountInput_'+item.id,
															type :'text'
														})
													
													),
												$('<input>', {
													class :'replenishBtn',
													type :'button',
													value : response.i18n.replenish
													})
												),
										$('<input>', {
											class :'hideBtn',
											type :'button',
											})
											
										)));
					
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
				$('#usersTable tbody').remove();
				$('#pagination').empty();
				$('#userErrors').empty();
				$('#usersTable').hide();
				$('#pageSizeSelector').hide();
				$("#userErrors").append(
				$('<span>').text(response.responseJSON.errors.message));
				$('#userErrors').slideDown('normal');
			}
		}
	});
}

function getUserByEmail(email){
	$.ajax({
		type:"POST",
		data:email,
		contentType: "application/json",
		url:"users/userbyname",
		statusCode:{
			200 :function(response) {
				$('#usersTable tbody').remove();
				$('#pageSizeSelector').hide();
				$('#pagination').empty();
				$('#userErrors').empty();
				$('#usersTable').show();
				$('#userErrors').slideUp('normal');
				$('#popups').empty();
				var item = response.data[0];
				$('<tr>',{
					class: 'row_user',
					id:'row_user_'+item.id
				}).append(
				$('<td>').text(item.id),
		 		$('<td>').text(item.email),
				$('<td>').text(item.balance),
				$('<td>').text(item.registred),
				$('<input>', {
					class :'showBtn',
					type :'button',
					value : response.i18n.replenish
					}),
				$('<input>', {
					class :'delBtn',
					type :'button',
					value : response.i18n.del
					})
				).appendTo('#usersTable');
				$('#popups').append($('<div/>',{
					class:'popup_shadow',
					id:'popup_shadow_'+item.id
					}).append($('<div/>',{
						class:'popup',
						id:'popup_'+item.id
						}).append($('<span/>',{
									id:'popup_user_'+item.id,
									class:'userEmail',
									text :item.email}),
									$('<div/>',{
										class:'replenishErrors'
										}),
									$('<div/>',{
										class:'controls',
										}).append(
											$('<div/>',{
												class:'inputBlock'
												}).append(
													$('<label for="amountInput_'+item.id+'">',{
														class:'amountLabel'
														}).text(amountLabel),
													$('<input>', {
														class :'amountInput',
														id:'amountInput_'+item.id,
														type :'text'
													})
												
												),
											$('<input>', {
												class :'replenishBtn',
												type :'button',
												value : response.i18n.replenish
												})
											),
									$('<input>', {
										class :'hideBtn',
										type :'button',
										})
										
									)));
				},
			404 :function(response) {
				$('#usersTable tbody').remove();
				$('#popups').empty();
				$('#pagination').empty();
				$('#userErrors').empty();
				$('#usersTable').hide();
				$('#pageSizeSelector').hide();
				$("#userErrors").append(
				$('<span>').text(response.responseJSON.errors.message));	
				$('#userErrors').slideDown('normal');
			}
		}
	});
}

function refreshUserById(ID){
	$.ajax({
		type:"GET",
		contentType: "application/json",
		url:"users/"+ID,
		statusCode:{
			200 :function(response) {
				$('#usersTable').show();
				$('#userErrors').slideUp('normal');
				console.log('ID :'+ID);
				var item = response.data[0];
				console.log('item :'+item);
				var row = $('#row_user_'+ID);
				console.log('row :'+row);
				row.empty();
				row.append(
						$('<td>').text(item.id),
				 		$('<td>').text(item.email),
						$('<td>').text(item.balance),
						$('<td>').text(item.registred),
						$('<input>', {
							class :'showBtn',
							type :'button',
							value : response.i18n.replenish
							}),
						$('<input>', {
							class :'delBtn',
							type :'button',
							value : response.i18n.del
							})
						);
				},
			404 :function(response) {
				$('#userErrors').empty();
				$('#usersTable').hide();
				$('#pageSizeSelector').hide();
				$('#usersTable').hide();
				$('#pageSizeSelector').hide();
				$("#userErrors").append(
				$('<span>').text(response.responseJSON.errors.message));
				$('#userErrors').slideDown('normal');
			}
		}
	});
}

function userReplenish(id,amount,popup){
	popupErrors = popup.find('.replenishErrors') ;
	popupInput = popup.find('.amountInput') ;
	var jsonObject = new Object();
	jsonObject.amount = amount;
	var data = JSON.stringify(jsonObject);
	$.ajax({
		type:"PUT",
		data:data,
		contentType: "application/json",
		url:"users/"+id,
		statusCode:{
			200 :function(response) {
				popupErrors.slideUp('normal');
				popupErrors.empty();
				popupInput.val('');
				console.log('input cleared ');
				refreshUserById(id);
				$('#popup_shadow_'+id).hide('normal');
			},
			400 :function(response) {
				popupErrors.empty();
				$.each(response.responseJSON.errors, function(i, item) {
					popupErrors.append(
							$('<span>').text(response.responseJSON.errors.message));
				});
				popupErrors.slideDown('normal');
			}
		}
	});
}

function userDelete(ID){
	$.ajax({
		type:"DELETE",
		contentType: "application/json",
		url:"users/"+ID,
		statusCode:{
			200 :function(response) {
				$('#usersTable').show();
				$('#userErrors').slideUp('normal');
				getUsers(getPagesize(),currentPage);
			},
			404 :function(response) {
				$('#userErrors').empty();
				$('#usersTable').hide();
				$('#pageSizeSelector').hide();
				$.each(response.responseJSON.errors, function(i, item) {
					$("#userErrors").append(
							$('<span>').text(response.responseJSON.errors.message));
				});
				$('#userErrors').slideDown('normal');
			}
		}
	});
}

function getPagesize(){
	return $("#pageSizeSelector :selected").text();
}

$("#pagination").on("click",".page",function () {
	currentPage = $(this).attr('id');
	getUsers(getPagesize(),currentPage);
});
$("#popups").on("click",".replenishBtn",function (event) {
	var currentPopup = $(event.target).closest('.popup'); 
	var amountInput = currentPopup.find('.amountInput') ;
	var amount = $.trim(amountInput.val());
	var userId = (currentPopup.find('.userEmail').attr('id')).replace('popup_user_','');
	if( amount.length > 0 ) {
		userReplenish(userId,amount,currentPopup);
	}
});
$("#popups").on("click",".hideBtn",function (event) {
	var currentPopup = $(event.target).closest('.popup_shadow'); 
	currentPopup.hide();
});
$("#usersTable").on("click",".delBtn",function (event) {
	var userId = ($(this).closest("tr").attr('id')).replace('row_user_','');
	userDelete(userId);
});
$("#usersTable").on("click",".showBtn",function (event) {
	var userId = ($(this).closest("tr").attr('id')).replace('row_user_','');
	 $('#popup_shadow_'+userId).show();
});
$("#pageSizeSelector").change(function () {
	getUsers(getPagesize(),0);
});
$("#sendEmailBtn").click(function(e) {
	var email = $.trim($("#email").val());
	if( email.length > 0 ) 
		getUserByEmail(email);
});
$("#resetEmailBtn").click(function(e) {
	$("#email").val('');
	getUsers(getPagesize(),currentPage);
});

getUsers(getPagesize(),0);
});