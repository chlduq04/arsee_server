
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="url_manage" class="core.PageInfoBean" scope="session" />
<%
	String pagename = request.getParameter("page");
%>
<div class="header-navigation-index" style="width: 100%; margin: 0 auto;">	
	<a href="layout.jsp?page=index">
		<img class="main-icon" src="../images/new/bt_home.png">
	</a>
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

<div class="index_main">
	<div class="main_left">
		<div class="ml_clickbox">
			<a href="layout.jsp?page=qna">
				<img class="ml_clickbox_img" src="../images/new/icon_1.png" alt=" "/>
				<img class="ml_clickbox_img_over" src="../images/new/overicon_1.png" alt=" "/>
			</a>
		</div>
	</div>
	<div class="main_center">
		<div class="mc_clickbox">
			<a href="layout.jsp?page=pics">
				<img class="mc_clickbox_img" src="../images/new/icon_2.png" alt=" "/>
				<img class="mc_clickbox_img_over" src="../images/new/overicon_2.png" alt=" "/>
			</a>
		</div>
	</div>
	<div class="main_right">
		<div class="mr_clickbox">
			<a href="layout.jsp?page=management">
				<img class="mr_clickbox_img" src="../images/new/icon_3.png" alt=" "/>
				<img class="mr_clickbox_img_over" src="../images/new/overicon_3.png" alt=" "/>
			</a>
		</div>
	</div>
</div>
