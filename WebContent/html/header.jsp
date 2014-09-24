<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<jsp:useBean id="url_manage" class="core.PageInfoBean" scope="session" />	
<%
	String pagename = request.getParameter("page");	
%>
<div class="header-navigation-control" style="width: 1920px; height:70px; margin: 0 auto;">	
	<a href="layout.jsp?page=index">
		<img class="main-icon" src="../images/new/icon.png">
	</a>
	<div class="hnc-site-navigation">
		<a href="layout.jsp?page=qna">
			<img class="qna-a-img" src="../images/new/qna-a-img.png">
		<!-- 	<img class="qna-a-img-over" src="../images/new/qna-a-over.png"> -->
		</a>
		<a href="layout.jsp?page=pics">
			<img class="pics-a-img" src="../images/new/pics-a-img.png">
		<!-- 	<img class="pics-a-img-over" src="../images/new/pics-a-over.png"> -->
		</a>
		<a href="layout.jsp?page=management">
			<img class="management-a-img" src="../images/new/management-a-img.png">
		<!-- 	<img class="management-a-img-over" src="../images/new/management-a-over.png"> -->
		</a>
	</div>
	<div class="header-nav">
		<ul class="nav masthead-nav">
			<li>			
			<%if(session.getAttribute("email") != null){%>
				<div class="bt_logout">Logout</div>
			<%}else{%>
				<div class="bt_login">Login</div>
				<div class="update_view_url"><a class="bt_update_view_url" href="layout.jsp?page=update_view">Update</a></div>
			<%}%>			
			</li>
		</ul>
	</div>
</div>
