<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="core.Qna"%>
<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="qna_manage" class="core.Qna" scope="session" />
<%
	request.setCharacterEncoding("UTF-8");
	String method = request.getParameter("method");
	if (method != null && method != "") {
		if (method.equals("text")) {
			qna_manage.insertIsTextDB(request.getParameter("number"),
					request.getParameter("company"),
					request.getParameter("question"));
		} else if (method.equals("radio")) {
			qna_manage.insertIsRadioDB(request.getParameter("number"),
					request.getParameter("company"),
					request.getParameter("question"),
					request.getParameter("check1"),
					request.getParameter("check2"),
					request.getParameter("check3"),
					request.getParameter("check4"),
					request.getParameter("check5"));
		} else if (method.equals("point")) {
			qna_manage.insertIsPointDB(request.getParameter("number"),
					request.getParameter("company"),
					request.getParameter("question"),
					request.getParameter("check1"),
					request.getParameter("check2"),
					request.getParameter("check3"),
					request.getParameter("check4"),
					request.getParameter("check5"));
		} else if (method.equals("check")) {
			qna_manage.insertIsCheckDB(request.getParameter("number"),
					request.getParameter("company"),
					request.getParameter("question"),
					request.getParameter("check1"),
					request.getParameter("check2"),
					request.getParameter("check3"),
					request.getParameter("check4"),
					request.getParameter("check5"));
		} else if(method.equals("")){
			
		}
	}
	String pagename = request.getParameter("page");
	String number = request.getParameter("number");
	String company = request.getParameter("company");
	
	if (number == null || company == null) {
		company="NULL";
		number="NULL";
	}
	
%>

<!-- Modal -->
<div class="modal fade" id="result-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">Result</h4>

			</div>
			<div class="modal-body result-pop-text">
			</div>
		</div>
	</div>
</div>

<div id="">
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Q&A Information</h4>

				</div>
				<div class="modal-body">
					<ul class="nav nav-tabs">
						<li class="add-question-type-rating active"><a href="#qna-point-tab" data-toggle="tab">Point</a></li>
						<li class="add-question-type-text"><a href="#qna-text-tab" data-toggle="tab">Text</a></li>
						<li class="add-question-type-yorn"><a href="#qna-radio-tab" data-toggle="tab">Radio</a></li>
						<li class="add-question-type-multiple"><a href="#qna-check-tab" data-toggle="tab">Check</a></li>
					</ul>

					<div class="tab-content">
						<div class="tab-pane active" id="qna-point-tab">
							<div>
								<form class="form-horizontal" role="form" method="POST" action="layout.jsp?page=qna">
									<div class="form-group">
										<label for="inputQuestion" class="col-sm-2 control-label">Question?</label>
										<div class="col-sm-10">
											<input name="question" type="text" class="form-control"
												id="inputQuestion" placeholder="Question">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck1" class="col-sm-2 control-label">Number
											1</label>
										<div class="col-sm-10">
											<input name="check1" type="text" class="form-control"
												id="inputcheck1" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck2" class="col-sm-2 control-label">Number
											2</label>
										<div class="col-sm-10">
											<input name="check2" type="text" class="form-control"
												id="inputcheck2" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck3" class="col-sm-2 control-label">Number
											3</label>
										<div class="col-sm-10">
											<input name="check3" type="text" class="form-control"
												id="inputcheck3" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck4" class="col-sm-2 control-label">Number
											4</label>
										<div class="col-sm-10">
											<input name="check4" type="text" class="form-control"
												id="inputcheck4" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck5" class="col-sm-2 control-label">Number
											5</label>
										<div class="col-sm-10">
											<input name="check5" type="text" class="form-control"
												id="inputcheck5" placeholder="Subscription">
										</div>
									</div>

									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-10">
											<button type="submit" class="btn btn-primary">Save</button>
										</div>
									</div>
									<input type="hidden" name="method" value="point" /> <input
										type="hidden" name="company" value="<%=company%>" /> <input
										type="hidden" name="number" value="<%=number%>" />
								</form>
							</div>
						</div>
						<div class="tab-pane" id="qna-text-tab">
							<div>
								<form class="form-horizontal" role="form" method="POST"
									action="layout.jsp?page=qna">
									<div class="form-group">
										<label for="inputQuestion" class="col-sm-2 control-label">Question?</label>
										<div class="col-sm-10">
											<input name="question" type="text" class="form-control"
												id="inputQuestion" placeholder="Question">
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-10">
											<button type="submit" class="btn btn-primary">Save</button>
										</div>
									</div>
									<input type="hidden" name="method" value="text" /> <input
										type="hidden" name="company" value="<%=company%>" /> <input
										type="hidden" name="number" value="<%=number%>" />

								</form>
							</div>
						</div>
						<div class="tab-pane" id="qna-radio-tab">
							<div>
								<form class="form-horizontal" role="form" method="POST"
									action="layout.jsp?page=qna">
									<div class="form-group">
										<label for="inputQuestion" class="col-sm-2 control-label">Question?</label>
										<div class="col-sm-10">
											<input name="question" type="text" class="form-control"
												id="inputQuestion" placeholder="Question">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck1" class="col-sm-2 control-label">Number
											1</label>
										<div class="col-sm-10">
											<input name="check1" type="text" class="form-control"
												id="inputcheck1" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck2" class="col-sm-2 control-label">Number
											2</label>
										<div class="col-sm-10">
											<input name="check2" type="text" class="form-control"
												id="inputcheck2" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck3" class="col-sm-2 control-label">Number
											3</label>
										<div class="col-sm-10">
											<input name="check3" type="text" class="form-control"
												id="inputcheck3" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck4" class="col-sm-2 control-label">Number
											4</label>
										<div class="col-sm-10">
											<input name="check4" type="text" class="form-control"
												id="inputcheck4" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck5" class="col-sm-2 control-label">Number
											5</label>
										<div class="col-sm-10">
											<input name="check5" type="text" class="form-control"
												id="inputcheck5" placeholder="Subscription">
										</div>
									</div>

									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-10">
											<button type="submit" class="btn btn-primary">Save</button>
										</div>
									</div>
									<input type="hidden" name="method" value="radio" /> <input
										type="hidden" name="company" value="<%=company%>" /> <input
										type="hidden" name="number" value="<%=number%>" />
								</form>
							</div>
						</div>
						<div class="tab-pane" id="qna-check-tab">
							<div>
								<form class="form-horizontal" role="form" method="POST"
									action="layout.jsp?page=qna">
									<div class="form-group">
										<label for="inputQuestion" class="col-sm-2 control-label">Question?</label>
										<div class="col-sm-10">
											<input name="question" type="text" class="form-control"
												id="inputQuestion" placeholder="Question">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck1" class="col-sm-2 control-label">Number
											1</label>
										<div class="col-sm-10">
											<input name="check1" type="text" class="form-control"
												id="inputcheck1" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck2" class="col-sm-2 control-label">Number
											2</label>
										<div class="col-sm-10">
											<input name="check2" type="text" class="form-control"
												id="inputcheck2" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck3" class="col-sm-2 control-label">Number
											3</label>
										<div class="col-sm-10">
											<input name="check3" type="text" class="form-control"
												id="inputcheck3" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck4" class="col-sm-2 control-label">Number
											4</label>
										<div class="col-sm-10">
											<input name="check4" type="text" class="form-control"
												id="inputcheck4" placeholder="Subscription">
										</div>
									</div>
									<div class="form-group">
										<label for="inputcheck5" class="col-sm-2 control-label">Number
											5</label>
										<div class="col-sm-10">
											<input name="check5" type="text" class="form-control"
												id="inputcheck5" placeholder="Subscription">
										</div>
									</div>

									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-10">
											<button type="submit" class="btn btn-primary">Save</button>
										</div>
									</div>
									<input type="hidden" name="method" value="check" /> <input
										type="hidden" name="company" value="<%=company%>" /> <input
										type="hidden" name="number" value="<%=number%>" />

								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="body" class="qna_main" company="<%=company%>" number="<%=number%>">
	<div class="investigation-style-div">
		<div class="investigation-info-div">
			<div class="investigation-title">
				<img class="investigation-title-img" src="../images/new/inverstigation style.png" alt="" />
			</div>
			<div class="qna-add-questions">
				<img class="qna-add-question qaq-1 type-multiple" src="../images/new/style_icon_1.png" alt="" />
				<img class="qna-add-question qaq-2 type-yorn" src="../images/new/style_icon_2.png" alt="" />
				<img class="qna-add-question qaq-3 type-rating" src="../images/new/style_icon_3.png" alt="" />
				<img class="qna-add-question qaq-5 type-text" src="../images/new/style_icon_5.png" alt="" />
				<input class="investigation-input-submit" type="button"  value="OK" data-toggle="modal" data-target="#myModal"/>
			</div>
		</div>
	</div>
	<div class="qna-list-view">
	<% 	
	int inputCount = 1;
	ResultSet rs = qna_manage.findDB(company, number);
		while (rs.next()) {
	%>

	<ul class="qna-list-<%=rs.getString("id")%>">
		<li class="qna-list-question">
			<p class="qna-list-name">
			<%if(inputCount<10){%>0<%}%><%=inputCount%>.
			</p>
			<p class="qna-list-comment"><%=rs.getString("text")%></p>
			<img class="qna-list-open qlo qlo-<%=rs.getString("id")%>" src="../images/new/list-open.png"/>
			<img class="qna-list-close qlo qlo-<%=rs.getString("id")%>" src="../images/new/list-close.png"/>
		</li>
		<%
		if (rs.getString("isPoint").equals("1")) {
		%>
		<li class="qlo-li qlo-<%=rs.getString("id")%>">
			<%
				for (int i = 1; i < 6; i++) {
							if (rs.getString("question" + i) == null || rs.getString("question" + i).equals("")) {
								break;
							}
			%>
			<div class="radio">
				<label class="label-optionsRadios lor-<%=rs.getString("id")%>">
					<input type="radio" class="qna-<%=rs.getString("id")%>-optionsRadios qna-num-options-<%=i%>" name="qna-<%=rs.getString("id")%>-optionsRadios" id="optionsRadios5-<%=i%>" value="option5">
					<div class=""><%=rs.getString("question" + i)%></div>
				</label>
				<input type="text" class="form-control qna-inputRadios" name="qna-<%=rs.getString("id")%>-inputRadios" id="inputsRadios5" value="<%=rs.getString("question" + i)%>" >
			</div> 
			<% } %>
		</li>
		<% } %>
		<%
			if (rs.getString("isCheck").equals("1")) {
		%>
		<li class="qlo-li qlo-<%=rs.getString("id")%>">
			<%
				for (int i = 1; i < 6; i++) {
							if (rs.getString("question" + i) == null
									|| rs.getString("question" + i).equals("")) {
								break;
							}
			%> 
		<label class="checkbox-inline"> 
			<input name="qna-<%=rs.getString("id")%>-checkbox-1 qna-num-options-<%=i%>" type="checkbox" id="inlineCheckbox1-<%=i%>" value="option1"><%=rs.getString("question" + i)%>
		</label> <%
 	}
 %>
		</li>
		<%
			}
		%>

		<%
			if (rs.getString("isRadio").equals("1")) {
		%>
		<li class="qlo-li qlo-<%=rs.getString("id")%>">
			<%
				for (int i = 1; i < 6; i++) {
							if (rs.getString("question" + i) == null
									|| rs.getString("question" + i).equals("")) {
								break;
							}
			%>
			<div class="radio">
				<label> 
					<input type="radio" name="qna-<%=rs.getString("id")%>-selectsRadios qna-num-options-<%=i%>" id="optionsRadios1-<%=i%>" value="option1" checked> <%=rs.getString("question" + i)%>
				</label>
			</div> <%
 	}
 %>
		</li>
		<%
			}
		%>

		<%
			if (rs.getString("isText").equals("1")) {
		%>
		<li class="qlo-li qlo-<%=rs.getString("id")%>">
			<div class="col-sm-10">
				<input name="qna-<%=rs.getString("id")%>-textinput" type="text" class="form-control" id="inputNumber" placeholder="Phone number">
			</div>
		</li>
		<%
			}
		%>
		<li class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<img data-toggle="modal" data-target="#result-pop" class="bt-list-control qna-result res-<%=rs.getString("id")%>" id="<%=rs.getString("id")%>_res" src="../images/new/result.png" alt="" />
				<img class="bt-list-control qna-delete del-<%=rs.getString("id")%>" id="<%=rs.getString("id")%>" src="../images/new/delete.png" alt=""/>
				<img class="bt-list-control qna-save save-<%=rs.getString("id")%>" id="<%=rs.getString("id")%>_save" src="../images/new/save.png" alt=""/>
			</div>
		</li>
	</ul>
	<input type="hidden" name="qna-<%=rs.getString("id")%>-inputid" value="<%=rs.getString("id")%>" />
	<%
		inputCount++;
		}
	%>
	
	</div>
	<input type="hidden" name="input-count" value="<%=inputCount%>" />
</div>





