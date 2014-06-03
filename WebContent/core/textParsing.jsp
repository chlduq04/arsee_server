<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="text_parsing" class="core.ParseText" scope="session" />
<% 
request.setCharacterEncoding("UTF-8");
String postPage = request.getParameter("ars_data");

String nowPage = request.getParameter("parsing_text");
String nowPageNum = request.getParameter("parsing_number");
String nowPageCompany = request.getParameter("parsing_company");
String nowPageParent = request.getParameter("parsing_parent");
String nowPageDepth = "0";
if(nowPageParent == null || nowPageParent == ""){
	nowPageParent = "0";
}else{
	nowPageDepth = ""+nowPageParent.length();
}


String nowDate = request.getParameter("get_text_by_number");
String nowDateParent = request.getParameter("get_text_by_number_parent");
String nowDateDepth = "0";
if(nowDateParent == null || nowDateParent == ""){
	nowDateParent = "0";
}else{
	nowDateDepth = ""+nowDateParent.length();
}


String nowUpdateArs = request.getParameter("now_ars_update");

String arsKwrd = request.getParameter("find_ars_table");
String arsKwrdVar = request.getParameter("find_ars_table_variable");

boolean checkupdate = false;
String depthData;
if(nowPage != null && nowPage != ""){
	checkupdate = text_parsing.parsingForGoal(nowPage,nowPageNum,nowPageDepth,nowPageParent,nowPageCompany);
}

if(postPage != null){
	checkupdate = text_parsing.parsingForGoal(postPage);	
}

if(nowDate != null && nowDate != ""){
	if(nowDateDepth != null && nowDateDepth!= ""){
		depthData = text_parsing.getDepthText(nowDate, nowDateDepth, nowDateParent);
		%>
		<div>depthData : <%=depthData %></div>
		<%	
	}else{
		depthData = text_parsing.getAllText(nowDate);
		%>
		<div>depthData : <%=depthData %></div>
		<%	
	}
}
if(nowUpdateArs != null){
	String nowupdate = text_parsing.checkUpdateByNumber(nowUpdateArs);
	%>
	<div>nowUpdate : <%=nowupdate %></div>
	<%	
	out.println(nowupdate);
}

if(arsKwrd != null && arsKwrdVar != null){
	String nowKwrd = text_parsing.checkMaxKwrdByCount(arsKwrd, arsKwrdVar);	
	%>
	<div>가장 가까운 번호 : <%=nowKwrd%></div>
	<%	
}

out.println(checkupdate);
%>
<div>원문 : <%=nowPage %></div>
<div>업데이트 확인 : <%=checkupdate %></div>
<br />
<div>업데이트</div>
<div style="border:1px solid black;padding:5px;">
	<form method="POST" action="textParsing.jsp">
		<label>텍스트 : <input type="text" value="" name="parsing_text"></input></label><br />
		<label>ARS 번호 : <input type="text" value="0000" name="parsing_number"></input></label><br />
		<label>ARS 이전 번호 : <input type="text" value="" name="parsing_parent"></input></label><br />
		<label>통신사 : <input type="text" value="skt" name="parsing_company"></input></label><br />
		<input type="submit"></input>
	</form>
</div>
<br />
<div>불러오기</div>
<div style="border:1px solid black;padding:5px;">
	<form method="POST" action="textParsing.jsp">
		<label>ARS 번호 : <input type="text" value="0000" name="get_text_by_number"></input></label><br />
		<label>ARS 깊이 : <input type="text" value="0" name="get_text_by_number_depth"></input></label><br />
		<label>ARS 이전 번호 : <input type="text" value="" name="get_text_by_number_parent"></input></label><br />
		<input type="submit"></input>
	</form>
</div>
<br />

<div>상태 확인</div>
<div style="border:1px solid black;padding:5px;">
	<form method="POST" action="textParsing.jsp">
		<input type="text" value="0000" name="now_ars_update"></input>
		<input type="submit"></input>
	</form>
</div>
<br />

<div>검색어 찾기</div>
<div style="border:1px solid black;padding:5px;">
	<form method="POST" action="textParsing.jsp">
		<label>ARS 번호 : <input type="text" value="0000" name="find_ars_table"></input></label><br />
		<label>찾고자 하는 단어 : <input type="text" value="어린이" name="find_ars_table_variable"></input></label><br />
		<input type="submit"></input>
	</form>
</div>
<br />

