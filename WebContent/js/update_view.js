$(document).ready(function(){
	var all_text = "";
	function log(text) {
		console.log(text);
		console.log(all_text);
		var date = new Date;
		all_text = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]"+text + all_text;
		$("#log").html(all_text);
	}

	if (!window.WebSocket) {
		alert("FATAL: WebSocket not natively supported. This demo will not work!");
	}

	var ws;

	$("#uriForm").submit(function(e) {
		e.preventDefault();
		ws = new WebSocket($("#uri").val());
		ws.onopen = function() {
			log("[WebSocket#onopen]\n");
		}
		ws.onmessage = function(e) {
			log("'" + e.data + "'\n");
		}
		ws.onclose = function() {
			log("[WebSocket#onclose]\n");
			$("#uri").attr("enable");
			$("#connect").attr("enable");
			$("disconnect").attr("disable");
			ws = null;
		}
		$("#uri").attr("disable");
		$("#connect").attr("disable");
		$("disconnect").attr("enable");
	});

	$("#disconnect").bind("click", function(e) {
		e.preventDefault();
		if (ws) {
			ws.close();
			ws = null;
		}
	});
	
	$("#uriForm").submit();
	
})
