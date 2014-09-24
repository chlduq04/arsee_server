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

function jquery_extends(){
	if(jQuery){
		$.fn.extend({
			bind_clk_toggle : function(on_func, off_func){
				var self = this;
				this.swt = false;
				$(this).bind("click",function(){
					this.swt = !this.swt;
					if(this.swt){
						on_func(this);
					}else{
						off_func(this);
					}
				})
			},
			
			bind_clk_toggles : function(){
				var self = this;
				this.swt = false;
				this.argus = arguments;
				return (function(target, argu){
					target.bind("click",function(){
						if(this.swt == undefined || this.swt+1 == argu.length){
							this.swt = 0;
						}else{
							this.swt++;
						}
						argu[this.swt](target);
					})
				})(self, arguments)
			},
			
			on_clk_toggle : function(on_func, off_func){
				var self = this;
				this.swt = false;
				$(this).on("click",function(){
					this.swt = !this.swt;
					if(this.swt){
						on_func(this);
					}else{
						off_func(this);
					}
				})
			},
			
			on_clk_toggles : function(){
				var self = this;
				this.swt = false;
				this.argus = arguments;
				return (function(target, argu){
					target.on("click",function(){
						if(this.swt == undefined || this.swt+1 == argu.length){
							this.swt = 0;
						}else{
							this.swt++;
						}
						argu[this.swt](target);
					})
				})(self, arguments)
			},
		})
	};
}

$(document).ready(function(){
	
	jquery_extends();
	
	$(".qna-a-img").mouseover(function(){
		$(".qna-a-img-over").fadeIn(500);
	})
	$(".qna-a-img-over").mouseout(function(){
		$(this).fadeOut(200);
	})

	$(".pics-a-img").mouseover(function(){
		$(".pics-a-img-over").fadeIn(500);
	})
	$(".pics-a-img-over").mouseout(function(){
		$(this).fadeOut(200);
	})

	$(".management-a-img").mouseover(function(){
		$(".management-a-img-over").fadeIn(500);
	})
	$(".management-a-img-over").mouseout(function(){
		$(this).fadeOut(200);
	})
})