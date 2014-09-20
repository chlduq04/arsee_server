$(document).ready(function(){
	$(".ml_clickbox_img").mouseover(function(){
		$(".ml_clickbox_img_over").fadeIn(500);
	})
	$(".ml_clickbox_img_over").mouseout(function(){
		$(this).fadeOut(200);
	})
	$(".mc_clickbox_img").mouseover(function(){
		$(".mc_clickbox_img_over").fadeIn(500);
	})
	$(".mc_clickbox_img_over").mouseout(function(){
		$(this).fadeOut(200);
	})

	$(".mr_clickbox_img").mouseover(function(){
		$(".mr_clickbox_img_over").fadeIn(500);
	})
	$(".mr_clickbox_img_over").mouseout(function(){
		$(this).fadeOut(200);
	})

})