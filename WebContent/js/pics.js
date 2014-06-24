$(document).ready(function(){
	$(".uploadimages").each(function(){
		var width = $(this).css("width");
		var height = $(this).css("height");
		var max;
		if(parseInt(width) > parseInt(height)){
			max = width;
		}else{
			max = height;
		}
		
		$(this).parent().css({ height : max, "margin-bottom" : "10px"});
	})
	
	
	$(".uploadimage-rotate").click(function(){
		var deg = parseInt($(this).prev().attr("degree"));
		deg += 90;
		if(deg > 400){
			deg = 90;
		}
		$(this).prev().attr({"degree":deg})
		$(this).prev().css({ WebkitTransform: 'rotate(' + deg + 'deg)'});
	})
})