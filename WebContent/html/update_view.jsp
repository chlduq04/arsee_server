<%@page import="java.sql.ResultSet"%>
<%@page import="core.Updateview"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="update_message" class="core.Updateview" scope="session" />
<%update_message.startWebsocket();%>
<div class="textarea_div">
	<form id="uriForm">
		<input type="text" id="uri" value="ws://localhost:9000"	style="width: 200px;"> 
		<input type="submit" id="connect" value="Connect">
		<input type="button" id="disconnect" value="Disconnect" disabled="disabled">
	</form>

	<br>
	<form id="sendForm">
		<input type="text" id="textField" value="" style="width: 200px;">
	</form>

	<br>
	<form>
		<textarea id="log" rows="30" cols="100" style="font-family: monospace; color: red;"></textarea>
	</form>
	<br>
</div>