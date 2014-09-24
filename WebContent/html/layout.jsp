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
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.css">
<link rel="stylesheet" href="<%=page_info.getCssUrl()%>bootstrap/flat-ui.css" type="text/css" media="screen" />
<link rel="stylesheet" href="<%=page_info.getCssUrl()%>reset.css" type="text/css" media="screen" />
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

		<%if(!nowPage.equals("index")){%>
		<div class="input-top-navigation">
			<div class="com-num-input-div">
				<form id="top-com-num-change-form" action="layout.jsp?page=<%=nowPage%>" method="POST">
					<%if(!nowPage.equals("pics")){%>
					<div class="company_input_name">Company</div>
					<input class="company_input" name="company"/>
					<%}%>

					<%if(nowPage.equals("pics")){%>
					<div class="number_input_name">Phone number</div>
					<%}else{%>
					<div class="number_input_name">ARS number</div>
					<%}%>
					<input class="number_input" name="number"/>

					<%if(nowPage.equals("management")){ %>
					<div class="time_input_name">Time</div>
					<input class="time_input" name="starttm" id="starttm" value="09:00:00"/>
					<%} %>
					<input class="com-num-input-submit" type="submit" value="OK"/>
				</form>
			</div>
		</div>
		<%}%>
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
    
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/bootstrap-switch.js"></script>
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/flatui-checkbox.js"></script>
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/flatui-radio.js"></script>
<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/jquery.tagsinput.js"></script>

<script type="text/javascript" src="<%=page_info.getJsUrl()%>bootstrap/jquery.placeholder.js"></script>

</body>
</html>