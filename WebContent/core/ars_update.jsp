<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="text_parsing" class="core.ParseText" scope="session" />
<% 
request.setCharacterEncoding("UTF-8");
String company = request.getParameter("parsing_company");
String number = request.getParameter("parsing_number");

if(company != null && company != ""){
	text_parsing.setIsDayOrIsHoliday();
	out.println(text_parsing.checkUpdateByNumber(number,company));
}
%>