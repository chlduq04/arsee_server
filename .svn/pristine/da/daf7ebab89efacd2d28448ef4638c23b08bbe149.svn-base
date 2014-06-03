var find_company_ajax = "skt";
var find_number_ajax = "0000";
$( document ).ready(function() {
	draw_svg();
	click_add_database();
});

function draw_svg(){
	var m = [20, 120, 20, 120],
	w = 1280 - m[1] - m[3],
	h = 800 - m[0] - m[2],
	i = 0,
	root;

	var tree = d3.layout.tree().size([h, w]);
	var diagonal = d3.svg.diagonal().projection(function(d) { return [d.y, d.x]; });

	var vis = d3.select("#manage-graph").append("svg:svg")
	.attr("width", w + m[1] + m[3])
	.attr("height", h + m[0] + m[2])
	.append("svg:g")
	.attr("transform", "translate(" + m[3] + "," + m[0] + ")");

	d3.json("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=parse", function(json) {
		root = json;
		root.x0 = h / 2;
		root.y0 = 0;

		//Toggle children.
		function toggleAll(d) {
			if (d.children) {
				d.children.forEach(toggleAll);
				toggle(d);
			}
		}

		// Initialize the display to show a few nodes.
		root.children.forEach(toggleAll);
		/*
	  toggle(root.children[1]);
	  toggle(root.children[1].children[2]);
	  toggle(root.children[9]);
	  toggle(root.children[9].children[0]);
		 */
		update(root);
	});

	function toggle(d) {
		if (d.children) {
			d._children = d.children;
			d.children = null;
		} else {
			d.children = d._children;
			d._children = null;
		}
	}

	function update(source) {
		var duration = d3.event && d3.event.altKey ? 5000 : 500;

		// Compute the new tree layout.
		var nodes = tree.nodes(root).reverse();

		// Normalize for fixed-depth.
		nodes.forEach(function(d) { d.y = d.depth * 180; });

		// Update the nodes…
		var node = vis.selectAll("g.node")
		.data(nodes, function(d) { return d.id || (d.id = ++i); });

		// Enter any new nodes at the parent's previous position.
		var nodeEnter = node.enter().append("svg:g")
		.attr("xmlns","http://www.w3.org/1999/xlink")
		.attr("class", "node")
		.attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })


		nodeEnter.append("svg:circle")
		.attr("r", 1e-6)
		.style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; })
		.on("click", function(d) { 
			toggle(d); update(d); 
		});

		nodeEnter.append("svg:text")
		.attr("x", function(d) { return d.children || d._children ? -10 : 10; })
		.attr("dy", ".35em")
		.attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
		.text(function(d) { return d.name; })
		.style("fill-opacity", 1e-6);

		nodeEnter.append("svg:image")
		.attr("x","2")
		.attr("y","2")
		.attr("width","20")
		.attr("height","20")
		.attr("id",function(d){return "manage-plusbt-"+d.id;})
		.attr("xlink:href","../images/manage-plus-icon.png")
		.on("click", function(d) { 
			popup_css_block($(".manage-modify-div-svg-"+d.id),false);
			popup_css_block($("#manage-closebt-"+d.id),false);
			popup_css_block($("#manage-modifybt-"+d.id),true);
			
			popup_css_block($(".manage-div-svg-"+d.id),true);
			popup_css_block($("#manage-minusbt-"+d.id),true);
		})
		
		
		var tnode = document.createElementNS('http://www.w3.org/2000/svg', 'image')
		tnode.setAttribute("x","2")
		tnode.setAttribute("y","2")
		tnode.setAttribute("width","20")
		tnode.setAttribute("height","20")
		tnode.setAttribute("id",function(d){return "manage-plusbt-"+d.id;})
		tnode.setAttribute("xlink:href","../images/manage-plus-icon.png")
		tnode.onclick = function(d) { 
			popup_css_block($(".manage-modify-div-svg-"+d.id),false);
			popup_css_block($("#manage-closebt-"+d.id),false);
			popup_css_block($("#manage-modifybt-"+d.id),true);
			
			popup_css_block($(".manage-div-svg-"+d.id),true);
			popup_css_block($("#manage-minusbt-"+d.id),true);
		}

		
		nodeEnter.append("svg:image")
		.attr("x","2")
		.attr("y","2")
		.attr("width","20")
		.attr("height","20")
		.attr("id",function(d){return "manage-minusbt-"+d.id;})
		.attr("style","display:none;")
		.attr("xlink:href","../images/manage-minus-icon.png")
		.on("click", function(d) { 
			popup_css_block($(".manage-div-svg-"+d.id),false);
			popup_css_block($("#manage-minusbt-"+d.id),false);
		})


		nodeEnter.append("svg:image")
		.attr("x","5.2")
		.attr("y","24")
		.attr("width","15")
		.attr("height","15")
		.attr("id",function(d){return "manage-deletebt-"+d.id;})
		.attr("xlink:href","../images/manage-delete-icon.png")
		.on("click", function(d) { 
		})

		nodeEnter.append("svg:image")
		.attr("x","-11")
		.attr("y","15")
		.attr("width","15")
		.attr("height","15")
		.attr("id",function(d){return "manage-modifybt-"+d.id;})
		.attr("xlink:href","../images/manage-modify-icon.png")
		.on("click", function(d) { 
			popup_css_block($(".manage-div-svg-"+d.id),false);
			popup_css_block($("#manage-minusbt-"+d.id),false);
			
			popup_css_block($(".manage-modify-div-svg-"+d.id),true);
			popup_css_block($("#manage-closebt-"+d.id),true);
			popup_css_block($("#manage-modifybt-"+d.id),false);
			
		})

		
		nodeEnter.append("svg:image")
		.attr("x","-11")
		.attr("y","15")
		.attr("width","15")
		.attr("height","15")
		.attr("style","display:none")
		.attr("id",function(d){return "manage-closebt-"+d.id;})
		.attr("xlink:href","../images/manage-close-icon.png")
		.on("click", function(d) { 
			popup_css_block($(".manage-modify-div-svg-"+d.id),false);
			popup_css_block($("#manage-closebt-"+d.id),false);
			popup_css_block($("#manage-modifybt-"+d.id),true);
		})



		nodeEnter.append("svg:switch").append("svg:foreignObject")
		.attr("x", 0)
		.attr("y", 5)
		.attr("width", 300)
		.attr("height", 500)
		.append("xhtml:body")
		.style("font", "14px 'Helvetica Neue'")
		.append("div")
		.attr("class",function(d){return "manage-div-svg manage-div-svg-"+d.id;})
		.attr("node_id",function(d){return d.id;})
		.attr("node_parent",function(d){return d.parentc;})
		.attr("node_depth",function(d){return d.depth;})
		.attr("node_indexs",function(d){return d.indexs;})
		.attr("node_company",function(d){return d.company;})
		.html(function(d){ 
			var tag = "" 
				+"<input placeholder='Text' class='manage-insert-data-svg-"+d.id+" form-control' type='text' />"
				+"<input placeholder='Index' class='manage-insert-data-index-svg-"+d.id+" form-control' type='text'/>"
				+"<input placeholder='09:00:00' class='manage-insert-data-starttime-svg-"+d.id+" form-control' type='text'/>"
				+"<input placeholder='21:00:00' class='manage-insert-data-endtime-svg-"+d.id+" form-control' type='text' />"
				+"<input value='+' type='button' class='btn btn-lg btn-primary btn-block manage-submit submit-addchild-"+d.id+"' />";
			return tag;
		})
		
		nodeEnter.append("svg:switch").append("svg:foreignObject")
		.attr("x", 0)
		.attr("y", 5)
		.attr("width", 300)
		.attr("height", 500)
		.append("xhtml:body")
		.style("font", "14px 'Helvetica Neue'")
		.append("div")
		.attr("class",function(d){return "manage-modify-div-svg manage-modify-div-svg-"+d.id;})
		.attr("node_id",function(d){return d.id;})
		.html(function(d){ 
			var tag = "" 
				+"<input type='hidden' value='"+d.id+"' class='manage-modify-data-svg-"+d.id+" form-control' type='text'/>"
				+"<input value='"+d.name+"' class='manage-modify-data-text-svg-"+d.id+" form-control' type='text'/>"
				+"<input value='+' type='button' class='btn btn-lg btn-primary btn-block manage-modify submit-modchild-"+d.id+"' />";
			return tag;
		})


		// Transition nodes to their new position.
		var nodeUpdate = node.transition()
		.duration(duration)
		.attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

		nodeUpdate.select("circle")
		.attr("r", 4.5)
		.style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

		nodeUpdate.select("text")
		.style("fill-opacity", 1);

		// Transition exiting nodes to the parent's new position.
		var nodeExit = node.exit().transition()
		.duration(duration)
		.attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
		.remove();

		nodeExit.select("circle")
		.attr("r", 1e-6);

		nodeExit.select("text")
		.style("fill-opacity", 1e-6);

		// Update the links…
		var link = vis.selectAll("path.link")
		.data(tree.links(nodes), function(d) { return d.target.id; });

		// Enter any new links at the parent's previous position.
		link.enter().insert("svg:path", "g")
		.attr("class", "link")
		.attr("d", function(d) {
			var o = {x: source.x0, y: source.y0};
			return diagonal({source: o, target: o});
		})
		.transition()
		.duration(duration)
		.attr("d", diagonal);

		// Transition links to their new position.
		link.transition()
		.duration(duration)
		.attr("d", diagonal);

		// Transition exiting nodes to the parent's new position.
		link.exit().transition()
		.duration(duration)
		.attr("d", function(d) {
			var o = {x: source.x, y: source.y};
			return diagonal({source: o, target: o});
		})
		.remove();

		// Stash the old positions for transition.
		nodes.forEach(function(d) {
			d.x0 = d.x;
			d.y0 = d.y;
			$(".submit-addchild-"+d.id).bind("click",function(){
				var target = $(".manage-div-svg.manage-div-svg-"+d.id);
				var addparams = {
						n_id : target.attr("node_id"),
						n_depth : target.attr("node_depth"),
						n_parent : target.attr("node_parent"),
						n_parent_index : target.attr("node_indexs"),
						n_index : $(".manage-insert-data-index-svg-"+d.id).val(),
						n_text : $(".manage-insert-data-svg-"+d.id+".form-control").val(),
						n_start: $(".manage-insert-data-starttime-svg-"+d.id).val(),
						n_end : $(".manage-insert-data-endtime-svg-"+d.id).val()
				};
				postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=add", addparams, null, function(s){$("svg").remove();draw_svg();}, function(e){alert(e);});
			});
			
			$(".submit-modchild-"+d.id).bind("click",function(){
				var target = $(".manage-div-svg.manage-div-svg-"+d.id);
				var addparams = {
						n_id : target.attr("node_id"),
						n_text : $(".manage-modify-data-text-svg-"+d.id).val()
				};
				postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=mod", addparams, null, function(s){$("svg").remove();draw_svg();}, function(e){alert(e);});
			});
			
			$("#manage-deletebt-"+d.id).bind("click",function(){
				var target = $(".manage-div-svg.manage-div-svg-"+d.id);
				var addparams = {
						n_id : target.attr("node_id")
				};
				postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=del", addparams, null, function(s){$("svg").remove();draw_svg();}, function(e){alert(e);});
			});
		});
	}	
}


function click_add_database(){
	$("#manage-add-depth").click(function(){
		length = $("#manage-depth-buttons-option").attr("length");
		length++;
		$("#manage-depth-buttons-option").attr({"length":length});
		$("#manage-depth-buttons").append('<button type="button" class="btn btn-default" depthid="'+length+'">'+length+'</button>');
	});

	$("#manage-remove-depth").click(function(){
		length = $("#manage-depth-buttons-option").attr("length");
		$(".btn.btn-default")[depthid=length].remove();
		length--;
		$("#manage-depth-buttons-option").attr({"length":length});

	});

	$("#manage-add-depth").click(function(){

	});
}
