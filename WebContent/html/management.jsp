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
<div id="body" class="management_main" number = "<%=number%>" company = "<%=company%>">
	<div class="form-horizontal time-input" role="form">
		<div class="form-group input-time">
			<label for="starttm" class="col-sm-2 control-label">Start</label>
			<div class="col-sm-10">
				<input name="starttm" type="text" class="form-control" id="starttm" placeholder="00:00:00" value="00:00:00">
			</div>
		</div>
		<div class="form-group input-time">
			<label for="endtm" class="col-sm-2 control-label">End</label>
			<div class="col-sm-10">
				<input name="endtm" type="text" class="form-control" id="endtm" placeholder="21:00:00" value="21:00:00">
			</div>
		</div>
		<div class="form-group input-time">
			<div class="col-sm-offset-2 col-sm-10">
				<button id="find-time-min-max" type="button" class="btn btn-default">Set Time</button>
			</div>
		</div>

		<div class="form-group input-time">
			<div class="col-sm-offset-2 col-sm-10">
				<button id="set-tree-change" type="button" class="btn btn-default">Set Change</button>
			</div>
		</div>
		
	</div>
	<!--  <div id="manage-graph" style="width: 100%; overflow:hidden; padding-top: 20px; margin-bottom:100px;"></div>-->
	<div id="tree-container"></div>
</div>



