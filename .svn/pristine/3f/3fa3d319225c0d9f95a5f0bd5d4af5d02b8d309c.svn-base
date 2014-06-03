<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="login_manage" class="core.Login" scope="session" />
<jsp:useBean id="url_manage" class="core.PageInfoBean" scope="session" />
<%
	login_manage.logoutManager(request, "email", "type");
	pageContext.forward(url_manage.getBaseUrl("page=main"));
%>





