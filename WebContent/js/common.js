function postAjax(url, params, beforeSend, successFunc, errorFunc){
	$.ajax({      
		type:"POST",  
		url:url,      
		data:params,      
		success:function(args){   
			successFunc(args);      
		},   
		beforeSend:beforeSend,  
		error:function(e){  
			errorFunc(e);
		}  
	});  	
}

function getAjax(url, params, beforeSend, successFunc, errorFunc){
	$.ajax({      
		type:"GET",  
		url:url,      
		data:params,      
		success:function(args){   
			successFunc(args);      
		},   
		beforeSend:beforeSend,  
		error:function(e){  
			errorFunc(e);
		}  
	});  	
}

function popup_css_block(tag, updown){
	if(updown){
		tag.css({display:"block",visibility : "visible"});
	}else{
		tag.css({display:"none",visibility : "hidden"});		
	}
}

function makeGetUrl(file, paramdic){
	var url = file;
	var count = 0;
	for(var key in paramdic){
		if(count==0){
			url+="?";
			count++;
		}else{
			url+="&";
		}
		url+=key+"="+paramdic[key];
	}
	return url;
}