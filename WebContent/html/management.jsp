<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="manage" class="core.Management" scope="session" />
<%
	String number = request.getParameter("number");
	String company = request.getParameter("company");
	if(number == null || company == null ){
		pageContext.forward("/layout.jsp?page=login");
	}
%>
<div id="">
	<!-- Modal -->
	<div class="modal fade" id="modify-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Modify</h4>

				</div>
				<div class="modal-body">
					<div class="form-horizontal" role="form">
						<div class="form-group">
							<label for="modify-text" class="col-sm-2 control-label">Text</label>
							<div class="col-sm-10">
								<input name="modify-text-pop" type="text" class="form-control" id="modify-text" placeholder="Text">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-startt" class="col-sm-2 control-label">Start</label>
							<div class="col-sm-10">
								<input name="modify-startt-pop" type="text" class="form-control" id="modify-startt" placeholder="Start Time">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-endt" class="col-sm-2 control-label">End</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-endt" placeholder="End Time" value="23:59:59">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-type" class="col-sm-2 control-label">Type</label>
							<div class="col-sm-10">
								<select id="modify-type" class="form-control">
									<option selected="selected">normal</option>
									<option>info</option>
									<option>error</option>
									<option>sharp</option>
									<option>star</option>
									<option>etc..</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" id="set-mod-submit" data-dismiss="modal" class="btn btn-primary" >Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="">
	<!-- Modal -->
	<div class="modal fade" id="addition-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Addition</h4>

				</div>
				<div class="modal-body">
					<ul class="nav nav-tabs">
						<li addition-type="one" class="active"><a href="#addition-pop-one-tab" data-toggle="tab">One</a></li>
						<li addition-type="text"><a href="#addition-pop-text-tab" data-toggle="tab">Text</a></li>
					</ul>

					<div class="tab-content">
						<div class="tab-pane active" id="addition-pop-one-tab">
							<div class="form-horizontal" role="form">
								<div class="form-group">
									<label for="addition-text" class="col-sm-2 control-label">Text</label>
									<div class="col-sm-10">
										<input name="addition-text-pop" type="text" class="form-control" id="addition-text" placeholder="Text">
									</div>
								</div>
								<div class="form-group">
									<label for="addition-idx" class="col-sm-2 control-label">Index</label>
									<div class="col-sm-10">
										<select id="addition-idx" class="form-control">
											<option selected="selected">0</option>
											<option>1</option>
											<option>2</option>
											<option>3</option>
											<option>4</option>
											<option>5</option>
											<option>6</option>
											<option>7</option>
											<option>8</option>
											<option>9</option>
											<option>s</option>
											<option>*</option>
											<option>#</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="addition-type" class="col-sm-2 control-label">Type</label>
									<div class="col-sm-10">
										<select id="addition-type" class="form-control">
											<option selected="selected">normal</option>
											<option>info</option>
											<option>error</option>
											<option>sharp</option>
											<option>star</option>
											<option>etc..</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="addition-startt" class="col-sm-2 control-label">Start</label>
									<div class="col-sm-10">
										<input name="addition-startt-pop" type="text" class="form-control" id="addition-startt" placeholder="Start Time" value="09:00:00">
									</div>
								</div>
								<div class="form-group">
									<label for="addition-endt" class="col-sm-2 control-label">End</label>
									<div class="col-sm-10">
										<input name="addition-endt-pop" type="text" class="form-control" id="addition-endt" placeholder="End Time"  value="23:59:59">
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="addition-pop-text-tab">
							<div class="form-horizontal" role="form">
								<div class="form-group">
									<label for="addition-text-noidx" class="col-sm-2 control-label">Text</label>
									<div class="col-sm-10">
										<input name="addition-text-pop" type="text" class="form-control" id="addition-text-noidx" placeholder="Text">
									</div>
								</div>
								<div class="form-group">
									<label for="addition-type-noidx" class="col-sm-2 control-label">Type</label>
									<div class="col-sm-10">
										<select id="addition-type-noidx" class="form-control">
											<option selected="selected">normal</option>
											<option>info</option>
											<option>error</option>
											<option>sharp</option>
											<option>star</option>
											<option>etc..</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="addition-startt-noidx" class="col-sm-2 control-label">Start</label>
									<div class="col-sm-10">
										<input name="addition-startt-pop" type="text" class="form-control" id="addition-startt-noidx" placeholder="Start Time" value="09:00:00">
									</div>
								</div>
								<div class="form-group">
									<label for="addition-endt-noidx" class="col-sm-2 control-label">End</label>
									<div class="col-sm-10">
										<input name="addition-endt-pop" type="text" class="form-control" id="addition-endt-noidx" placeholder="End Time"  value="23:59:59">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" id="set-add-submit" data-dismiss="modal" class="btn btn-primary" >Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="">
	<!-- Modal -->
	<div class="modal fade" id="delete-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Delete</h4>

				</div>
				<div class="modal-body delete-pop-text">
					해당 번호와 하위항목들을 <br />
					정말로 삭제하시겠습니까?
				</div>
				<div class="modal-footer">
					<button type="button" id="set-del-submit" data-dismiss="modal" class="btn btn-primary" >Yes</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="">
	<!-- Modal -->
	<div class="modal fade" id="delete-child-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Delete</h4>

				</div>
				<div class="modal-body delete-child-pop-text">
					하위 항목들을 <br />
					정말로 삭제하시겠습니까?
				</div>
				<div class="modal-footer">
					<button type="button" id="set-del-child-submit" data-dismiss="modal" class="btn btn-primary" >Yes</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="">
	<!-- Modal -->
	<div class="modal fade" id="detail-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Detail</h4>

				</div>
				<div class="modal-body">
					<div class="form-horizontal" role="form">
						<div class="form-group">
							<label for="modify-detail1" class="col-sm-2 control-label">Tag 1</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-detail1" placeholder="Detail Message">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-detail2" class="col-sm-2 control-label">Tag 2</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-detail2" placeholder="Detail Message">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-detail3" class="col-sm-2 control-label">Tag 3</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-detail3" placeholder="Detail Message">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-detail4" class="col-sm-2 control-label">Tag 4</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-detail4" placeholder="Detail Message">
							</div>
						</div>
						<div class="form-group">
							<label for="modify-detail5" class="col-sm-2 control-label">Tag 5</label>
							<div class="col-sm-10">
								<input name="modify-endt-pop" type="text" class="form-control" id="modify-detail5" placeholder="Detail Message">
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" id="set-det-submit" data-dismiss="modal" class="btn btn-primary" >Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="">
	<!-- Modal -->
	<div class="modal fade" id="analysis-pop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 800px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Detail</h4>

				</div>
				<div class="modal-body analysis-pop-text">
					<iframe id="myiframe" src="tree_graph.jsp" width="100%" height="510px">
					</iframe>
				</div>
				<div class="modal-footer">
					<button type="button" id="set-anal-submit" data-dismiss="modal" class="btn btn-primary" >Yes</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="body" class="management_main" number = "<%=number%>" company = "<%=company%>">
	<div class="form-horizontal time-input" role="form">
		<div class="form-group input-time">
			<label for="starttm" class="col-sm-2 control-label">Start</label>
			<div class="col-sm-10">
				<input name="starttm" type="text" class="form-control" id="starttm" placeholder="09:00:00" value="09:00:00">
			</div>
		</div>
		<div class="form-group input-time">
			<div class="col-sm-offset-2 col-sm-10">
				<button id="find-time-min-max" type="button" class="btn btn-default">Set Time</button>
			</div>
		</div>
		<div class="form-group input-time">
			<div class="col-sm-offset-2 col-sm-10">
				<button id="set-tree-change" type="button" class="btn btn-default">Set Change</button>
			</div>
		</div>
		<div class="form-group day-change">
			<div class="radio">
				<label>
					<input type="radio" name="optionsRadios" id="optionsRadios1" value="day" checked>
					Day
				</label>
			</div>
			<div class="radio">
				<label>
					<input type="radio" name="optionsRadios" id="optionsRadios2" value="holiday">
					Holiday
				</label>
			</div>
		</div>
		<div id="cursor_popup">
			<div id="cursor_popup_black">
			</div>
			<div id="cursor_popup_bg">
				<div id="set-tree-analy-bt" class="graph-bts" data-toggle="modal" data-target="#analysis-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Analysis">
					</div>
				</div>
				<div id="set-tree-del-bt" class="graph-bts" data-toggle="modal" data-target="#delete-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Delete">
					</div>		
				</div>		
				<div id="set-tree-det-bt" class="graph-bts" data-toggle="modal" data-target="#detail-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Tag">
					</div>		
				</div>
				<div id="set-tree-del-child-bt" class="graph-bts" data-toggle="modal" data-target="#delete-child-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Delete Child">
					</div>		
				</div>
				<div id="set-tree-add-bt" class="graph-bts" data-toggle="modal" data-target="#addition-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Add Child">
					</div>		
				</div>
				<div id="set-tree-mod-bt" class="graph-bts" data-toggle="modal" data-target="#modify-pop">
					<div style="height:100%;" data-toggle="tooltip" data-placement="top" title="Modify">
					</div>		
				</div>
			</div>
		</div>	
	</div>
	<div id="tree-container"></div>
	<div id="tree-container-holiday"></div>
</div>



