<%@page import="java.sql.ResultSet"%>
<%@page import="core.ImagePage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="imagedata_info" class="core.ImagePage" scope="session" />
<%
	String pageName = request.getParameter("page");
	String phoneNum = request.getParameter("phonenum");
	ResultSet rss = imagedata_info.checkImage(phoneNum);
%>
<div class="pics_main">
<%
	rss.last();			
	if(rss.getRow() > 0){
		rss.beforeFirst();
		while(rss.next()){
			%>
			<div class="pics_list">
				<img class="uploadimages" src="<%=imagedata_info.getImagePath()%><%=rss.getString(1)%>" degree="0"/>
				<div class="uploadimage-rotate" data-toggle="tooltip" data-placement="right" title="" data-original-title="Rotate">
				</div>		
			</div>
			<%
		}
	%>
	<%
	}else{
	%>
	<div class="image_not_found">No Image Found</div>
	<%
	}
	%>
</div>





