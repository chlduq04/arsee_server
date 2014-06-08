<%@page import="java.util.Enumeration"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="qna_manage" class="core.Qna" scope="session" />
<%
request.setCharacterEncoding("UTF-8");
String method = request.getParameter("method");
if(method.equals("delete")){
	out.println(qna_manage.deleteDB(request.getParameter("id")));
}

 %>
