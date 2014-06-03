<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="core.Qna"%>
<jsp:useBean id="qna_manage" class="core.Qna" scope="session" />
<%
	String pagename = request.getParameter("page");
	String number = request.getParameter("number");
	String company = request.getParameter("company");
	
	if(number == null || company == null || qna_manage.isEmpty(company, number) ){
		pageContext.forward("/layout.jsp?page=index");
	}
%>
<div id="body" class="qna_main">
	
</div>





