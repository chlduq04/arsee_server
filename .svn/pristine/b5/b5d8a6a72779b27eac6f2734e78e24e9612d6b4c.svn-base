<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="manage" class="core.Management" scope="session" />
<%
	String number = request.getParameter("number");
	String company = request.getParameter("company");
	if(number == null || company == null || manage.isEmpty(company, number) ){
		pageContext.forward("/layout.jsp?page=login");
	}
%>
<div id="body" class="management_main">
	<div id="manage-graph" style="width: 100%; height: 1000px;padding-top: 20px;"></div>
</div>



