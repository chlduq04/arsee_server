<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String pagename = request.getParameter("page");
%>
<div class="main_main">
	<form id="wer" action="https://srv01.cloudconvert.org/process/v4cw72hf3" method="POST" enctype="multipart/form-data">
    <input type="hidden" name="input" value="upload">
    <input type="hidden" name="outputformat" value="wav">
    <input type="file" name="file">
    <input type="submit">
	</form>
</div>