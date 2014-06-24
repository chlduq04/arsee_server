<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="core.ImageUpload"%>
<jsp:useBean id="image_infos" class="core.ImageUpload" scope="session" />

<%
	image_infos.imageFileUpload(request);
	//image_infos.fileToByte();
	//image_infos.fileToString("C:/APM_Setup/htdocs/ARSee_Server/WebContent/ImageDatas/01040750607_0.PNG")
%>


