<%@page import="java.util.Vector"%>
<%@page import="core.PageInfo"%>
<%@page import="core.PageInfoResource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:useBean id="page_info" class="core.PageInfoBean" scope="session" />
<jsp:useBean id="page_resource" class="core.PageInfoResource" scope="session" />

<%
	request.setCharacterEncoding("utf-8");
	String nowPage = request.getParameter("page");
	String picsPhoneNum = request.getParameter("phonenum");
	String nowPageJsp = nowPage+".jsp";
	if (!page_resource.nowPage(nowPage)) {
%>
<jsp:forward page="error.jsp" />
<%
	} 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=page_info.getCssUrl()%>reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.css">
<link rel="stylesheet" href="<%=page_info.getCssUrl()%>layout.css" type="text/css" media="screen" />
<%
	Vector<String> includes = page_resource.getPageCss(nowPage);
	for (int i = 0; i < includes.size(); i++) {
%>
<link rel="stylesheet" href="<%=page_info.getCssUrl() + includes.get(i)%>" type="text/css" media="screen" />
<%
	}
%>


</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top header">
		<jsp:include page="header.jsp">
			<jsp:param value="<%=nowPage%>" name="page" />
		</jsp:include>
	</div>

	<div class="center">
		<jsp:include page="<%=nowPageJsp%>">
			<jsp:param value="<%=nowPage%>" name="page" />
		</jsp:include>
	</div>

	<div class="navbar navbar-inverse navbar-fixed-bottom footer">
		<jsp:include page="footer.jsp"></jsp:include>
	</div>
	<script type="text/javascript" src="<%=page_info.getJsUrl()%>jquery.js"></script>
	
<%
	includes = page_resource.getPageJs(nowPage);
	for (int i = 0; i < includes.size(); i++) {
%>
<script type="text/javascript" src="<%=page_info.getJsUrl() + includes.get(i)%>"></script>
<%
	}
%>
<!-- Latest compiled and minified JavaScript -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
</body>
</html>