<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="manage" class="core.Management" scope="session" />
<%
String func = request.getParameter("func");
String number = request.getParameter("number");
String company = request.getParameter("company");	
if(func.equals("add")){
	String depth = request.getParameter("n_depth");
	String parent = request.getParameter("n_parent");
	String index = request.getParameter("n_index");
	String text = request.getParameter("n_text");
	String starttime = request.getParameter("n_start");
	String endtime = request.getParameter("n_end");
	String parent_index = request.getParameter("n_parent_index");
	out.println(manage.insertDB(number, company, parent_index, parent, depth, index, text, starttime, endtime));
}else if(func.equals("del")){
	String id = request.getParameter("n_id");
	out.println(manage.deleteDB(id));
}else if(func.equals("mod")){
	String id = request.getParameter("n_id");
	String text = request.getParameter("n_text");
	out.println(manage.modifyDB(id, text));	
}else if(func.equals("parse")){
	out.println(manage.findJson(number,company));
}
%>
