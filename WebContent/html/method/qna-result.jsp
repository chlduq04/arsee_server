<%@page import="java.util.Enumeration"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="qna_result_manage" class="core.Qna" scope="session" />
<%
request.setCharacterEncoding("UTF-8");

String company = request.getParameter("company");
String number = request.getParameter("number");
String method = request.getParameter("method");

if(method.equals("all")){
	out.println(qna_result_manage.resultDB(number,company));
}else if(method.equals("each")){
	String id = request.getParameter("id");
	out.println(qna_result_manage.resultDB(number, company, id));
}
 %>
