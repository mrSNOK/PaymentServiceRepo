$(document).ready( function() {
	$( "input" ).mouseover(function() {
		$( this ).css('border','1px solid #3998f5');
	  })
	  .mouseout(function() {
	    $( this ).css('border','1px solid #999');
	  });
	function checkErrors(){
		var emailError = $('#emailError').text();
		var passError = $('#passError').text();
		var confirmPassError = $('#confirmPassError').text();
		if (emailError.length > 0)
			$('#emailRegInput').css('border','1px solid red');
		if (passError.length > 0)
			$('#passwordRegInput').css('border','1px solid red');	
		if (confirmPassError.length > 0)
			$('#passwordConfirmRegInput').css('border','1px solid red');
	}
	checkErrors();
	
});