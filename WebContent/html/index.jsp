<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="session_manage" class="core.PageInfoSession"
	scope="session" />

<%
	String pagename = request.getParameter("page");
%>
<div class="index_main">
	<div class="main_left">
		<div class="ml_clickbox"></div>
		<form action="layout.jsp?page=qna" method="POST">
			<input class="mc_insertcompany form-control" type="text" name="company" placeholder="Input Company" /> 
			<input class="mc_insertnumber form-control" type="text" name="number" placeholder="Input Ars Number" /> 
			<input type="submit" class="btn btn-lg btn-primary m_all_clickbt" value="^" />
		</form>
	</div>
	<div class="main_center">
		<div class="mc_clickbox"></div>
		<form action="layout.jsp?page=pics" method="POST">
			<input class="mc_insertphonenum form-control" type="text" name="phonenum" placeholder="Input Your Phone Number" /> 
			<input type="submit" class="btn btn-lg btn-primary m_all_clickbt" value="^" />
		</form>
	</div>
	<div class="main_right">
		<div class="mr_clickbox"></div>
		<form action="layout.jsp?page=management" method="POST">
			<input class="mr_insertcompany form-control" type="text" name="company" placeholder="Input Company" /> 
			<input class="mr_insertnumber form-control" type="text" name="number" placeholder="Input Ars Number" /> 
			<input type="submit" class="btn btn-lg btn-primary m_all_clickbt" value="^" />
		</form>
	</div>
</div>