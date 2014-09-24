<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="core.Management"%>
<jsp:useBean id="manage" class="core.Management" scope="session" />
<%
	String number = request.getParameter("number");
	String company = request.getParameter("company");
	String time = request.getParameter("starttm");
	if(number == null || company == null || time == null){
		number = "";
		company = "";
		time = "09:00:00";
	}
%>
<div id="">
	<div class="data-list-background display-none"></div>
	<div class="data-list display-none">
		<ul class="data-list-ul"></ul>
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
							<div class="form-horizontal" role="form"></div>
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
<div id="body" class="management_main" number = "<%=number%>" company = "<%=company%>" time = "<%=time%>">
	<div class="management-style-div">
		<div class="management-info-div">
			<div class="management-title">
				<img class="management-title-img" src="../images/new/management-setting.png" alt="">
			</div>
			<div class="management-add-questions">
				<div class="maq-div-1">
					<img class="list-numbering-icon" src="../images/new/list-icon.png"/>
					<div style="float: left;font-size: 16px;color: white;line-height: 28px;margin-right: 15px;margin-left: 10px;">Index</div>
					<select id="addition-idx" class="maq-input maq-input-3">
						<option selected="selected">auto</option>
						<option>0</option>
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>100</option>
						<option>-</option>
						<option>s</option>
						<option>*</option>
						<option>#</option>
					</select>
					<img class="management-add-question maq-2 type-time" src="../images/new/management-time.png" alt="">
					<input class="maq-input maq-input-2-1" name="addition-startt-pop" id="addition-startt" type="text" value="09:00:00"/>
					<span class="flow-point">~</span>
					<input class="maq-input maq-input-2-2" name="addition-endt-pop" id="addition-endt" type="text" value="23:59:59"/>
					<img class="management-add-question maq-3 type-type" src="../images/new/management-type.png" alt="">
					<select id="addition-type" class="maq-input maq-input-3">
						<option selected="selected">normal</option>
						<option>info</option>
						<option>error</option>
						<option>sharp</option>
						<option>star</option>
						<option>etc..</option>
					</select>
					<img class="management-add-question maq-4 type-analysis" src="../images/new/management-analysis.png" alt="">
				</div>
				<div class="maq-div-2">
					<img class="management-add-question maq-1 type-text" src="../images/new/management-text.png" alt="">
					<input class="maq-input maq-input-1" id="addition-text" type="text" />
					<input class="management-input-save maq-5" type="button" value="SAVE">
					<input class="management-input-submit maq-5" type="button" value="OK">
				</div>
			</div>
			<div class="tab-detail-div">
				<span class="allow-down"></span>
			</div>
			<div class="tab-detail-open display-none">
				<div class="management-contents">
					<div class="maq-div-3">
						<div class="maq-tag-list">
							<img class="management-add-question qaq-5 type-teg" src="../images/new/management-tag.png" alt="">
							<input class="maq-input maq-input-5 mi-1" id="modify-detail1" type="text" />
						</div>
						<div class="maq-tag-list">
							<input class="maq-input maq-input-5 mi-2" id="modify-detail2" type="text" />
						</div>
						<div class="maq-tag-list">
							<input class="maq-input maq-input-5 mi-3" id="modify-detail3" type="text" />
						</div>
						<div class="maq-tag-list">
							<input class="maq-input maq-input-5 mi-4" id="modify-detail4" type="text" />
						</div>
						<div class="maq-tag-list">
							<input class="maq-input maq-input-5 mi-5" id="modify-detail5" type="text" />
						</div>
					</div>
				</div>
				<div class="management-close-bt">
					<span class="allow-up"></span>
				</div>
			</div>
		</div>
	</div>
	<div class="management-input-sub-options">
		<div class="miso-center">
			<img class="management-ars-list-title" src="../images/new/miso-title.png" alt="" />

			<input class="management-ars-list mal-day" type="checkbox" checked data-toggle="switch" />
			<img class="management-ars-list mal-mode" src="../images/new/miso-mode.png" alt="" />
			<img class="management-ars-list mal-delete" src="../images/new/miso-delete.png" alt="" />
			<img class="management-ars-list mal-modify" src="../images/new/miso-cancel.png" alt="" />
			<img class="management-ars-list mal-add click-border" src="../images/new/miso-add.png" alt="" />
		</div>
	</div>
	<div class="open-detail-div">
	</div>
	<div id="tree-container"></div>
	<div id="tree-container-holiday"></div>
</div>



