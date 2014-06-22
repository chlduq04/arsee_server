<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:useBean id="url_manage" class="core.PageInfoBean" scope="session" />	
<%
	String pagename = request.getParameter("page");	
%>
<div style="width: 92%; margin: 0 auto;">
	<img class="main-icon" src="../images/logo_small.png">
	<div class="header-nav">
		<ul class="nav masthead-nav">
			<li class="active"><a href="<%=url_manage.getToUrl("page=main")%>">Home</a></li>
			<li>			
			<%if(session.getAttribute("email") != null){%>
				<a href="<%=url_manage.getToUrl("page=logout")%>">Logout</a>
			<%}else{%>
				<a href="<%=url_manage.getToUrl("page=login")%>">Login</a>
			<%}%>			
			</li>
		</ul>
	</div>
</div>
