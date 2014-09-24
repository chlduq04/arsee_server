<%@page import="java.sql.ResultSet"%>
<%@page import="core.ImagePage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="imagedata_info" class="core.ImagePage" scope="session" />
<%
	String pageName = request.getParameter("page");
	String phoneNum = request.getParameter("number");
	ResultSet rss = imagedata_info.checkImage(phoneNum);
	if(phoneNum == null || phoneNum.equals("")){
		phoneNum = "NULL";
	}
%>
<div class="pics_main">
	<div class="pics_main_contents">
<%
	rss.last();			
	if(rss.getRow() > 0){
		rss.beforeFirst();
		while(rss.next()){
			%>
			<div class="pics_list">
				<img class="uploadimages" src="<%=imagedata_info.getImagePath()%><%=rss.getString(1)%>" degree="0"/>
				<div class="uploadimage-rotate" data-toggle="tooltip" data-placement="right" title="" data-original-title="Rotate"></div>		
				<div class="uploadimage-title-rotate">회전</div>
			</div>
			<%
		}
	%>
	<%
	}else{
	%>
	<div class="image_not_found">No Image Founded</div>
	<%
	}
	%>
	</div>
</div>





