var n, m, map_val, stack, yGroupMax, yStackMax, margin, x, y, color, xAxis, svg, layer, rect;
var pressTimer;

$( document ).ready(function() {
	clickDelete();
	clickAdd();
	clickResult();
	clickTotalResult();
	clickListOpen();
	clickMakeList();
	clickMouseLong();
});

function clickMouseLong(){
	if(jQuery){
		$.fn.extend({
			bind_clk_long : function(func, timeout){
				var pressTimer;
				var target;
				$(this).mouseup(function(){
					clearTimeout(pressTimer);
					return false;
				}).mousedown(function(){
					target = $(this);
					pressTimer = window.setTimeout(function(){func(target)}, timeout);
					return false; 
				});	
			}
		})
	}

	$(".label-optionsRadios").bind_clk_long(function(e){
		var target = e;
		target.next().css({"display":"block"}).focus().focusout(function(){
			target.next().css({"display":"none"});
			target.css({"display":"block"}).find("div").html(target.next().val());
		});
		target.css({"display":"none"});
	},500);
}


function clickMakeList(){
	$(".qna-add-question").click(function(){
		$(".qna-add-question").removeClass("active");
		$(this).addClass("active")
		$(".add-question-"+$(this).attr("class").match("type-.*")[0].replace(" active", "")).find("a").trigger("click");
	})
}

function clickListOpen(){
	$(".qna-list-question").on("click",function(e){
		var target = $(this).parent().find(".qlo-li");
		if(target.css("display") == "none"){
			$(this).find(".qna-list-open").css({"display":"none"});
			$(this).find(".qna-list-close").css({"display":"block"});
			target.css({"display":"block", "visibility":"visible"});
		}else{
			$(this).find(".qna-list-open").css({"display":"block"});
			$(this).find(".qna-list-close").css({"display":"none"});
			target.css({"display":"none", "visibility":"hidden"});
		}
	})

	$(".qna-list-open").on("click",function(e){
		var target = $(this).attr("class").match("qlo-.*");
		var find = $(this).parent().parent().find("li."+target);
		$(this).css({"display":"none"})
		$(this).parent().find(".qna-list-close").css({"display":"block"});
		find.css({"visibility":"visible", "display":"list-item"});
	})

	$(".qna-list-close").on("click",function(e){
		var target = $(this).attr("class").match("qlo-.*");
		var find = $(this).parent().parent().find("li."+target);
		$(this).css({"display":"none"})
		$(this).parent().find(".qna-list-open").css({"display":"block"});
		find.css({"visibility":"hidden", "display":"none"});
	});
}


function clickResult(){
	$(".qna-result").click(function(){
		var data = { 
				method : "each", 
				number : $(".qna_main").attr("number"), 
				company : $(".qna_main").attr("company"), 
				id : $(this).attr("id").split("_")[0] 
		};
		postAjax("method/qna-result.jsp", data, null, function(s){
			$(".result-pop-text").children().remove();
			drawChart(JSON.parse(s).items);
		}, function(e){alert(e);});
	});
};

function clickTotalResult(){
	$(".btn-total-result").click(function(){
		var data = { 
				method : "all", 
				number : $(".qna_main").attr("number"), 
				company : $(".qna_main").attr("company"), 
		};
		postAjax("method/qna-result.jsp", data, null, function(s){
			console.log(JSON.parse(s));

		}, function(e){alert(e);});
	})
}

function clickDelete(){
	$(".qna-delete").bind("click",function(){
		var data = {
				company: $("#body").attr("company"),
				number: $("#body").attr("number"),	
				method : "delete",
				id : $(this).attr("id")
		};

		postAjax("method/qna-insert.jsp", data, null, function(e){if( e != "" ){
			$(".qna-list-"+e.trim()).remove();
			$(".del-"+e.trim()).remove();
		}}, function(e){});
	});	
};

function clickAdd(){
	$(".qna-add").bind("click",function(){
	});
};

function drawChart(data){

	var barWidth = 40;
	var width = (barWidth + 10) * data.length;
	var height = 250;

	var x = d3.scale.linear().domain([0, data.length]).range([0, width]);
	var y = d3.scale.linear().domain([0, d3.max(data, function(datum) { return datum.value; })]).
	rangeRound([0, height]);

	//add the canvas to the DOM
	var barDemo = d3.select(".result-pop-text").
	append("svg:svg").
	attr("width", width).
	attr("height", height);


	barDemo.selectAll("rect").
	data(data).
	enter().
	append("svg:rect").
	attr("x", function(datum, index) { return x(index); }).
	attr("y", function(datum) { return height - y(datum.value); }).
	attr("height", function(datum) { return y(datum.value); }).
	attr("width", barWidth).
	attr("fill", "#2d578b")
	.on("mouseover",function(){
		$(this).attr("fill", "#111111");
	})
	.on("mouseout",function(){
		$(this).attr("fill", "#2d578b");
	});


	barDemo.selectAll("text").
	data(data).
	enter().
	append("svg:text").
	attr("x", function(datum, index) { return x(index) + barWidth; }).
	attr("y", function(datum) { return height - y(datum.value); }).
	attr("dx", -barWidth/2).
	attr("dy", "1.2em").
	attr("text-anchor", "middle").
	text(function(datum) { return datum.value;}).
	attr("fill", "white");

	$("svg").css({"height":280});

	barDemo.selectAll("text.yAxis").
	data(data).
	enter().append("svg:text").
	attr("x", function(datum, index) { return x(index) + barWidth; }).
	attr("y", height).
	attr("dx", -barWidth/2).
	attr("text-anchor", "middle").
	attr("style", "font-size: 12; font-family: Helvetica, sans-serif").
	text(function(datum) { return datum.label;}).
	attr("transform", "translate(0, 18)").
	attr("class", "yAxis");
};
