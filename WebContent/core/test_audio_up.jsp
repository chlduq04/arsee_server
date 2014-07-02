<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="audio_infos" class="core.AudioManager" scope="session" />
<%
	request.setCharacterEncoding("UTF-8");
	audio_infos.audioFileUpload(request);
%>


