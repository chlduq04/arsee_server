<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="qna_manager" class="core.Qna" scope="session" />
<% 
String company = request.getParameter("parsing_company");
String number = request.getParameter("parsing_number");
if(company != null && !company.equals("")){
	out.println(qna_manager.getQnaListJson(company, number));	
}
%>
