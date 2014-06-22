<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="text_parsing" class="core.ParseText" scope="session" />
<% 
request.setCharacterEncoding("UTF-8");
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
if(nowPage != null && nowPage != ""){
	text_parsing.setIsDayOrIsHoliday();
	out.println(text_parsing.parsingForGoal(nowPage, nowPageNum, nowPageDepth, nowPageParent, nowPageCompany));
}
%>