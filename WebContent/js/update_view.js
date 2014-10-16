$(document).ready(function(){
	var id;
	var dummy = true;
	var dummy_array = [
	                   "'4 th word : 청구 요금 확인 일반인 실시간 요금이란'",
	                   "'3 th word : 청구 요금 확인 일반인 실시간 요금'",
	                   "'2 th word : 요금확인 일반인 실시간 요금'",
	                   "'1 th word : 청구요금 사진 일바인 실시간 요금'",
	                   "'0 th word : 청구 요금 확인 일반일 실시간 요금'",
	                   "''",
	                   "'3 th word : 고객님의 생년월일이 연락주세요'",
	                   "'2 th word : 고객님의 생년월일 연락 주세요'",
	                   "'1 th word : 고객님의 생년월일 연락주세요'",
	                   "'0 th word : 고객님의 생년월일이 연락주세요'",
	                   "''",
	                   "'4 th word : 요금조회 비 나쁜 일반 요금제 부가서비스 신청 및 해지'",
	                   "'3 th word : 요금조회 및 일반 요금제 변경 부가서비스 신청 및 해지'",
	                   "'2 th word : 요금조회 및 일반 요금제 변경 서비스 신청 및 해지'",
	                   "'1 th word : 요금조회 비 나쁜 일반 요금제 변경 부가서비스 신청 및 해지'",
	                   "'0 th word : 요금조회 비 나쁜 일반 요금제 변경 서비스 신청 및 해지'",
	                   "'File -> 114--212--01021861953 : 2 Times'",
	                   "'File -> 114--21--01021861953 : 3 Times'",
	                   "'File -> 114--2--01021861953 : 1 Times'",
	                   "''",
	                   "'File -> 114--2--01021861953 : 1 Times'",
	                   "'File -> 114--212--01021861953 : 2 Times'",
	                   "'File -> 114--21--01021861953 : 3 Times'"
	                   ]
	var dummy_count = dummy_array.length-1;
	function intervalTrigger() {
		return window.setInterval( function() {
			if (dummy_count < 0) {
				window.clearInterval(id);
			}else{
				log(dummy_array[dummy_count--]+"\r\n");
			}
		}, parseInt(Math.random()*500+1000) );
	};
	if(dummy){
		id = intervalTrigger();
	}

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





