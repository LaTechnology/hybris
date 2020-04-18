$(document).ready(function ()
{
	
	var stateIsoCode=$("#hiddenState").val();
	var isocode=$("#country").val();
	var regionsUrl= $("#regionsUrl").text()
	var $select= $("#regionsDiv select");
	var listItems='';
	
	if(isocode){
		$("#stateTextBox").hide();
		$("#regionsDiv").removeClass('hide');
		$.get(regionsUrl+"?isocode="+isocode, function(data){
			$select.html('');
			$select.append('<option value="">Please Select </option>');
			for(var i=0;i<data.length;i++)
			{
				if(stateIsoCode ===data[i].isocode){
					$select.append('<option value="' + data[i].isocode + '" selected="selected">' + data[i].name + '</option>');
				}else{
					$select.append('<option value="' + data[i].isocode + '">' + data[i].name + '</option>');
				}
			}
		});
		$select.append(listItems);
	}
	$("#country").change(function(){
		
		var isocode=$("#country").val();
		var regionsUrl= $("#regionsUrl").text()
		var $select= $("#regionsDiv select");
		var listItems='';
		$("#stateTextBox").hide();
		$("#regionsDiv").removeClass('hide');
		$.get(regionsUrl+"?isocode="+isocode, function(data){
			$select.html('');
			$select.append('<option value="" selected="selected">Please Select </option>');
			for(var i=0;i<data.length;i++)
			{
				$select.append('<option value="' + data[i].isocode + '">' + data[i].name + '</option>');
			}
		});
		$select.append(listItems);
		
		$select.next(".holder").text("Please Select");
		
	});
	
	
	$('#printableRef').click(function(){
	     window.print();
	});
	
	
});