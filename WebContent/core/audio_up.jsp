<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="audio_infos" class="core.AudioManager" scope="session" />
<%
	request.setCharacterEncoding("UTF-8");
	audio_infos.audioFileUpload(request);

	//audio_infos.wavsToText();
	///audio_infos.amrToWav("http://srv91.cloudconvert.org/download/Iv0zqhXVpndlsEOcCW26");
//	audio_infos.download("http://srv21.cloudconvert.org/download/ymZtWifMIPoqBVbuk06K", "ar1.wav");
//audio_infos.download("http//srv23.cloudconvert.org/download/JWgAq58VnkldNK0LuoRs");
//	audio_infos.testSErve();
%>


