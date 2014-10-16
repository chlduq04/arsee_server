<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="login_manage" class="core.Login" scope="session" />
<jsp:useBean id="aes_manage" class="core.AES128" scope="session" />
<jsp:useBean id="url_manage" class="core.PageInfoBean" scope="session" />
<jsp:useBean id="validate_manage" class="core.CheckValidation" scope="session" />

<%
	request.setCharacterEncoding("utf-8");
	if(session.getAttribute("email") != null){
		pageContext.forward(url_manage.getBaseUrl("page=main"));
	}
	
	boolean signup_equal = false;
	boolean signup_duple = false;
	boolean login_fail = false;
	String method = request.getParameter("method");
	if( !(method == null || method.equals("")) ){
		if (method.equals("user")) {
//			String user = aes_manage.encrypt(request.getParameter("user_number"));
			String user = request.getParameter("user_number");
			login_manage.setSession(request, "email", user);
			session.setAttribute("email", user);
			session.setAttribute("type", "user");
			pageContext.forward(url_manage.getBaseUrl("page=pics","phonenum="+user));
			
		}else if (method.equals("manager")) {
			String manager_email = request.getParameter("input_email");
			String manager_passwd = request.getParameter("input_pass");
			if(login_manage.loginManager(manager_email, manager_passwd)){
				session.setAttribute("email", manager_email);
				session.setAttribute("type", "manager");
				pageContext.forward(url_manage.getBaseUrl("page=index"));
			}else{
				login_fail = true;	
			}

		}else if (method.equals("signup")) {
			String manager_email = request.getParameter("input_email");
			String manager_passwd = request.getParameter("input_pass");
			String manager_passwd_confirm = request.getParameter("input_confirm");
			String manager_key = request.getParameter("input_key");
			String manager_name = request.getParameter("input_name");
			String manager_phone = request.getParameter("input_phone");
			if(manager_passwd.equals(manager_passwd_confirm)){

				if(validate_manage.checkValidateEmail(manager_email, 3, 30)){
				}

				if(login_manage.loginEmail(manager_email)){
					login_manage.signUp(manager_email, manager_passwd, manager_key, manager_name, manager_phone);
					pageContext.forward(url_manage.getBaseUrl("page=index"));
					session.setAttribute("email", manager_email);
					session.setAttribute("type", "manager");
				}else{
					signup_duple = true;					
				}
			}else{
				signup_equal = true;
			}
		}
			
	}
%>
<div id="body" class="login_main">
	<div id="container">
		<div id="holder"></div>
	</div>
	<form id="form-manager" class="form-horizontal" role="form"
		action="layout.jsp?page=login" method="POST">
		<input type="hidden" name="method" value="manager">
		<h2 class="manager-form form-title">Manager</h2>
		<div class="form-div">
			<div class="form-group <%if(login_fail){out.println("has-error");}%>">
				<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
				<div class="col-sm-10">
					<input type="email" name="input_email" class="form-control"
						id="inputEmail3" placeholder="Email">
				</div>
			</div>
			<div class="form-group <%if(login_fail){out.println("has-error");}%>">
				<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
				<div class="col-sm-10">
					<input type="password" name="input_pass" class="form-control"
						id="inputPassword3" placeholder="Password">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">Login
						Manager</button>
					<button type="button" id="sign-up-manager" class="btn btn-default">Sign
						Up Manager</button>
				</div>
			</div>
		</div>
	</form>

	<div class="div-signup-manager">
		<form id="form-manager" class="form-horizontal" role="form"
			action="layout.jsp?page=login" method="POST">
			<input type="hidden" name="method" value="signup">

			<h2 class="manager-form form-title">Manager Sign Up</h2>
			<div class="form-div">
				<div
					class="form-group <%if(signup_duple){out.println("has-error");}%>">
					<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
					<div class="col-sm-10">
						<input type="email" name="input_email" class="form-control"
							id="inputEmail3" placeholder="Email">
					</div>
				</div>
				<div class="form-group ">
					<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
					<div class="col-sm-10">
						<input type="password" name="input_pass" class="form-control"
							id="inputPassword3" placeholder="패스워드">
					</div>
				</div>

				<div
					class="form-group <%if(signup_equal){out.println("has-error");}%>">
					<label for="inputconfirm" class="col-sm-2 control-label">Confirm</label>
					<div class="col-sm-10">
						<input type="password" name="input_confirm" class="form-control"
							id="inputconfirm" placeholder="Password Confirm">
					</div>
				</div>

				<div class="form-group ">
					<label for="inputkey" class="col-sm-2 control-label">Key</label>
					<div class="col-sm-10">
						<input type="password" name="input_key" class="form-control"
							id="inputkey" placeholder="Key">
					</div>
				</div>

				<div class="form-group">
					<label for="inputname" class="col-sm-2 control-label">Name</label>
					<div class="col-sm-10">
						<input type="text" name="input_name" class="form-control"
							id="inputname" placeholder="name">
					</div>
				</div>

				<div class="form-group">
					<label for="inputphone" class="col-sm-2 control-label">Phone</label>
					<div class="col-sm-10">
						<input type="text" name="input_phone" class="form-control"
							id="inputphone" placeholder="phone number">
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-default">Sign Up</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>




