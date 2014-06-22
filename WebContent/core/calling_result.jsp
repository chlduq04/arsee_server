<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="call_manager" class="core.Calling" scope="session" />
<% 
String company = request.getParameter("parsing_company");
String number = request.getParameter("parsing_number");
String order = request.getParameter("parsing_order");
String start = request.getParameter("parsing_startidx");
String length = request.getParameter("parsing_lengthidx");
if(company != null && company != ""){
	if(order != null && order != "" ){
		out.println(call_manager.findDB(company,number, start, length));	
	}else{
		out.println(call_manager.findDB(company, number, order));			
	}
}


%>
