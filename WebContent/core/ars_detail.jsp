<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="text_parsing" class="core.ParseText" scope="session" />
<% 

String nowDate = request.getParameter("parsing_number");
String nowDataCompany = request.getParameter("parsing_company");
String nowDateParent = request.getParameter("parsing_parent");
String nowDateDepth = "0";
if(nowDateParent == null || nowDateParent == ""){
	nowDateParent = "0";
}else{
	nowDateDepth = ""+nowDateParent.length();
}
text_parsing.setIsDayOrIsHoliday();
out.println(text_parsing.getDetailText(nowDate, nowDateDepth, nowDateParent, nowDataCompany));
%>


