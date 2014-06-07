$( document ).ready(function() {
	clickDelete();
	clickAdd();
});

function clickDelete(){
	$(".qna-delete").bind("click",function(){
		var data = {
				company: $("#body").attr("company"),
				number: $("#body").attr("number"),	
				method : "delete",
				id : $(this).attr("id")	
		};
		postAjax("method/qna-insert.jsp", data, null, function(e){if( e!="" ){
			$(".qna-list-"+e.trim()).remove();
			$(".del-"+e.trim()).remove();
		}}, function(e){});
	});	
}

function clickAdd(){
	$(".qna-add").bind("click",function(){
	});

}
