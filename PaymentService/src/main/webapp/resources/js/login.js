$(document).ready( function() {
	$('#passwordInput').focus(function(){
		$('#passwordLabel').hide('fast');
	});
	$( "input" ).mouseover(function() {
		$( this ).css('border','1px solid #3998f5');
	  })
	  .mouseout(function() {
	    $( this ).css('border','1px solid #999');
	  });
	$('#passwordInput').focusout(function(){
		text = $.trim($(this).val());
		if( text.length == 0 )
			$('#passwordLabel').show('fast');
	});
	$('#loginInput').focus(function(){
		$('#loginLabel').hide('fast');
	});
	$('#loginInput').focusout(function(){
		text = $.trim($(this).val());
		if( text.length == 0 )
			$('#loginLabel').show('fast');
	});
	function getUrlParamiter(sParam){
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++){
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam){
				return sParameterName[1];
			}
		}
	}
	function checkError(){
		var error = getUrlParamiter('error');
		if (error){
			$('#loginInput').css('border','1px solid red');
			$('#passwordInput').css('border','1px solid red');
		}	
	}
	checkError();
	
});