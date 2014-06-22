<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="call_manager" class="core.Calling" scope="session" />
<% 
String company = request.getParameter("parsing_company");
String number = request.getParameter("parsing_number");
String target = request.getParameter("parsing_target");
String where = request.getParameter("parsing_where");
if(company != null && company != ""){
	call_manager.insertDB(company, target, where, number);			
}


%>
