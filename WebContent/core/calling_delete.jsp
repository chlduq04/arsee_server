<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.ParseText"%>
<jsp:useBean id="call_manager" class="core.Calling" scope="session" />
<% 
String id = request.getParameter("parsing_id");
if(id != null && id != ""){
	call_manager.deleteDB(id);
}


%>
