<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="text_parsing" class="core.ParseText" scope="session" />
<% 
String company = request.getParameter("parsing_company");
String number = request.getParameter("parsing_number");
String variable = request.getParameter("parsing_variable");
if(company != null && company != ""){
	out.println(text_parsing.checkMaxKwrdByCount(number, company, variable));	
}
%>