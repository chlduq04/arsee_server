<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="manage" class="core.Management" scope="session" />
<%
String func = request.getParameter("func");
String number = request.getParameter("number");
String company = request.getParameter("company");	

if(func.equals("add_one")){
	String depth = request.getParameter("n_depth");
	String parent = request.getParameter("n_parent");
	String index = request.getParameter("n_indexs");
	String text = request.getParameter("n_text");
	String starttime = request.getParameter("n_start");
	String endtime = request.getParameter("n_end");
	String parent_index = request.getParameter("n_parent_index");
	String type = request.getParameter("n_type");
	out.println(manage.insertDB(number, company, parent_index, parent, depth, index, text, starttime, endtime, type));
}else if(func.equals("add_text")){
	String depth = request.getParameter("n_depth");
	String parent = request.getParameter("n_parent");
	String text = request.getParameter("n_text");
	String starttime = request.getParameter("n_start");
	String endtime = request.getParameter("n_end");
	String parent_index = request.getParameter("n_parent_index");
	out.println(manage.insertDB(number, company, parent_index, parent, depth, text, starttime, endtime));
}else if(func.equals("del")){
	String tree_data = request.getParameter("del_tree");
	out.println(manage.deleteDB(tree_data));
}else if(func.equals("mod")){
	String id = request.getParameter("n_id");
	String text = request.getParameter("n_text");
	String starttime = request.getParameter("n_start");
	String endtime = request.getParameter("n_end");
	String index = request.getParameter("n_indexs");
	String type = request.getParameter("n_type");
	out.println(manage.modifyDB(id, text, starttime, endtime, index, type));	
}else if(func.equals("parse")){
	String starttime = request.getParameter("t_min");
	out.println(manage.findJson( number, company, starttime));
}else if(func.equals("tree")){
	String datas = request.getParameter("data");
	System.out.println(manage.jsonToDatabase(datas));
	out.println(manage.jsonToDatabase(datas));	
}else if(func.equals("tags_result")){
	String id = request.getParameter("n_id");
	out.println(manage.tagsFromDatabase(id));
}else if(func.equals("tags_insert")){
	String id = request.getParameter("parent_id");
	String mod_val = request.getParameter("old");
	String new_val = request.getParameter("new");
	out.println(manage.tagsToDatabase(id, mod_val, new_val));
}else if(func.equals("duplicate")){
	out.println(manage.duplicate(number,company));
}else if(func.equals("others")){
	String depth = request.getParameter("n_depth");
	String index = request.getParameter("n_indexs");
	String parent = request.getParameter("n_parent");
	out.println(manage.findOthers(number, company, depth, index, parent));	
}
%>
