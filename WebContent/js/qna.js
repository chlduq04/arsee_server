var n, m, map_val, stack, yGroupMax, yStackMax, margin, x, y, color, xAxis, svg, layer, rect;

$( document ).ready(function() {
	clickDelete();
	clickAdd();
	clickResult();
	clickTotalResult();
});
var kkk;
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
