var find_company_ajax = $(".management_main").attr("company");
var find_number_ajax = $(".management_main").attr("number");
var find_by_min_time = $("#starttm").val();
var find_by_max_time = $("#endtm").val();

$( document ).ready(function() {
	draw_svg();
	click_add_database();
	click_search_database();
});

function draw_svg(){
	 var colors = ["#bd0026","#fecc5c", "#fd8d3c", "#f03b20", "#B02D5D",
	               "#9B2C67", "#982B9A", "#692DA7", "#5725AA", "#4823AF",
	               "#d7b5d8","#dd1c77","#5A0C7A","#5A0C7A"];
	
	treeJSON = d3.json("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=parse"+"&t_min="+find_by_min_time+"&t_max="+find_by_max_time, function(error, treeData) {
		// Get JSON data
		// Calculate total nodes, max label length
		var totalNodes = 0;
		var maxLabelLength = 0;
		// variables for drag/drop
		var selectedNode = null;
		var draggingNode = null;
		// panning variables
		var panSpeed = 200;
		var panBoundary = 20; // Within 20px from edges will pan when dragging.
		// Misc. variables
		var i = 0;
		var duration = 750;
		var root;

		// size of the diagram
		var viewerWidth = $(document).width();
		var viewerHeight = $(document).height();

		var tree = d3.layout.tree()
		.size([viewerHeight, viewerWidth]);

		// define a d3 diagonal projection for use by the node paths later on.
		var diagonal = d3.svg.diagonal()
		.projection(function(d) {
			return [d.y, d.x];
		});

		// A recursive helper function for performing some setup by walking through all nodes

		function visit(parent, visitFn, childrenFn) {
			if (!parent) return;

			visitFn(parent);

			var children = childrenFn(parent);
			if (children) {
				var count = children.length;
				for (var i = 0; i < count; i++) {
					visit(children[i], visitFn, childrenFn);
				}
			}
		}

		// Call visit function to establish maxLabelLength
		visit(treeData, function(d) {
			totalNodes++;
			maxLabelLength = Math.max(d.name.length, maxLabelLength);

		}, function(d) {
			return d.children && d.children.length > 0 ? d.children : null;
		});


		// sort the tree according to the node names

		function sortTree() {
			tree.sort(function(a, b) {
				return b.name.toLowerCase() < a.name.toLowerCase() ? 1 : -1;
			});
		}
		// Sort the tree initially incase the JSON isn't in a sorted order.
		sortTree();

		// TODO: Pan function, can be better implemented.

		function pan(domNode, direction) {
			var speed = panSpeed;
			if (panTimer) {
				clearTimeout(panTimer);
				translateCoords = d3.transform(svgGroup.attr("transform"));
				if (direction == 'left' || direction == 'right') {
					translateX = direction == 'left' ? translateCoords.translate[0] + speed : translateCoords.translate[0] - speed;
					translateY = translateCoords.translate[1];
				} else if (direction == 'up' || direction == 'down') {
					translateX = translateCoords.translate[0];
					translateY = direction == 'up' ? translateCoords.translate[1] + speed : translateCoords.translate[1] - speed;
				}
				scaleX = translateCoords.scale[0];
				scaleY = translateCoords.scale[1];
				scale = zoomListener.scale();
				svgGroup.transition().attr("transform", "translate(" + translateX + "," + translateY + ")scale(" + scale + ")");
				d3.select(domNode).select('g.node').attr("transform", "translate(" + translateX + "," + translateY + ")");
				zoomListener.scale(zoomListener.scale());
				zoomListener.translate([translateX, translateY]);
				panTimer = setTimeout(function() {
					pan(domNode, speed, direction);
				}, 50);
			}
		}

		// Define the zoom function for the zoomable tree

		function zoom() {
			svgGroup.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
		}


		// define the zoomListener which calls the zoom function on the "zoom" event constrained within the scaleExtents
		var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);

		function initiateDrag(d, domNode) {
			draggingNode = d;
			d3.select(domNode).select('.ghostCircle').attr('pointer-events', 'none');
			d3.selectAll('.ghostCircle').attr('class', 'ghostCircle show');
			d3.select(domNode).attr('class', 'node activeDrag');

			svgGroup.selectAll("g.node").sort(function(a, b) { // select the parent and sort the path's
				if (a.id != draggingNode.id) return 1; // a is not the hovered element, send "a" to the back
				else return -1; // a is the hovered element, bring "a" to the front
			});
			// if nodes has children, remove the links and nodes
			if (nodes.length > 1) {
				// remove link paths
				links = tree.links(nodes);
				nodePaths = svgGroup.selectAll("path.link")
				.data(links, function(d) {
					return d.target.id;
				}).remove();
				// remove child nodes
				nodesExit = svgGroup.selectAll("g.node")
				.data(nodes, function(d) {
					return d.id;
				}).filter(function(d, i) {
					if (d.id == draggingNode.id) {
						return false;
					}
					return true;
				}).remove();
			}

			// remove parent link
			parentLink = tree.links(tree.nodes(draggingNode.parent));
			svgGroup.selectAll('path.link').filter(function(d, i) {
				if (d.target.id == draggingNode.id) {
					return true;
				}
				return false;
			}).remove();

			dragStarted = null;
		}

		// define the baseSvg, attaching a class for styling and the zoomListener
		var baseSvg = d3.select("#tree-container").append("svg")
		.attr("width", "100%")
		.attr("height", viewerHeight)
		.attr("class", "overlay")
		.call(zoomListener);


		// Define the drag listeners for drag/drop behaviour of nodes.
		dragListener = d3.behavior.drag()
		.on("dragstart", function(d) {
			if (d == root) {
				return;
			}
			dragStarted = true;
			nodes = tree.nodes(d);
			d3.event.sourceEvent.stopPropagation();
			// it's important that we suppress the mouseover event on the node being dragged. Otherwise it will absorb the mouseover event and the underlying node will not detect it d3.select(this).attr('pointer-events', 'none');
		})
		.on("drag", function(d) {
			if (d == root) {
				return;
			}
			if (dragStarted) {
				domNode = this;
				initiateDrag(d, domNode);
			}

			// get coords of mouseEvent relative to svg container to allow for panning
			relCoords = d3.mouse($('svg').get(0));
			if (relCoords[0] < panBoundary) {
				panTimer = true;
				pan(this, 'left');
			} else if (relCoords[0] > ($('svg').width() - panBoundary)) {

				panTimer = true;
				pan(this, 'right');
			} else if (relCoords[1] < panBoundary) {
				panTimer = true;
				pan(this, 'up');
			} else if (relCoords[1] > ($('svg').height() - panBoundary)) {
				panTimer = true;
				pan(this, 'down');
			} else {
				try {
					clearTimeout(panTimer);
				} catch (e) {

				}
			}

			d.x0 += d3.event.dy;
			d.y0 += d3.event.dx;
			var node = d3.select(this);
			node.attr("transform", "translate(" + d.y0 + "," + d.x0 + ")");
			updateTempConnector();
		}).on("dragend", function(d) {
			if (d == root) {
				return;
			}
			domNode = this;
			if (selectedNode) {
				// now remove the element from the parent, and insert it into the new elements children
				var index = draggingNode.parent.children.indexOf(draggingNode);
				if (index > -1) {
					draggingNode.parent.children.splice(index, 1);
				}
				if (typeof selectedNode.children !== 'undefined' || typeof selectedNode._children !== 'undefined') {
					if (typeof selectedNode.children !== 'undefined') {
						selectedNode.children.push(draggingNode);
					} else {
						selectedNode._children.push(draggingNode);
					}
				} else {
					selectedNode.children = [];
					selectedNode.children.push(draggingNode);
				}
				// Make sure that the node being added to is expanded so user can see added node is correctly moved
				expand(selectedNode);
				sortTree();
				endDrag();
			} else {
				endDrag();
			}
		});

		function endDrag() {
			selectedNode = null;
			d3.selectAll('.ghostCircle').attr('class', 'ghostCircle');
			d3.select(domNode).attr('class', 'node');
			// now restore the mouseover event or we won't be able to drag a 2nd time
			d3.select(domNode).select('.ghostCircle').attr('pointer-events', '');
			updateTempConnector();
			if (draggingNode !== null) {
				update(root);
				centerNode(draggingNode);
				draggingNode = null;
			}
		}

		// Helper functions for collapsing and expanding nodes.

		function collapse(d) {
			if (d.children) {
				d._children = d.children;
				d._children.forEach(collapse);
				d.children = null;
			}
		}

		function expand(d) {
			if (d._children) {
				d.children = d._children;
				d.children.forEach(expand);
				d._children = null;
			}
		}

		var overCircle = function(d) {
			selectedNode = d;
			updateTempConnector();
		};
		var outCircle = function(d) {
			selectedNode = null;
			updateTempConnector();
		};

		// Function to update the temporary connector indicating dragging affiliation
		var updateTempConnector = function() {
			var data = [];
			if (draggingNode !== null && selectedNode !== null) {
				// have to flip the source coordinates since we did this for the existing connectors on the original tree
				data = [{
					source: {
						x: selectedNode.y0,
						y: selectedNode.x0
					},
					target: {
						x: draggingNode.y0,
						y: draggingNode.x0
					}
				}];
			}
			var link = svgGroup.selectAll(".templink").data(data);

			link.enter().append("path")
			.attr("class", "templink")
			.attr("d", d3.svg.diagonal())
			.attr('pointer-events', 'none');

			link.attr("d", d3.svg.diagonal());

			link.exit().remove();
		};

		// Function to center node when clicked/dropped so node doesn't get lost when collapsing/moving with large amount of children.

		function centerNode(source) {
			scale = zoomListener.scale();
			x = -source.y0;
			y = -source.x0;
			x = x * scale + viewerWidth / 2;
			y = y * scale + viewerHeight / 2;
			d3.select('g').transition()
			.duration(duration)
			.attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
			zoomListener.scale(scale);
			zoomListener.translate([x, y]);
		}

		// Toggle children function

		function toggleChildren(d) {
			if (d.children) {
				d._children = d.children;
				d.children = null;
			} else if (d._children) {
				d.children = d._children;
				d._children = null;
			}
			return d;
		}

		// Toggle children on click.

		function click(d) {
			if (d3.event.defaultPrevented) return; // click suppressed
			update(d);
			//centerNode(d);
		}

		function update(source) {
			// Compute the new height, function counts total children of root node and sets tree height accordingly.
			// This prevents the layout looking squashed when new nodes are made visible or looking sparse when nodes are removed
			// This makes the layout more consistent.
			var levelWidth = [1];
			var childCount = function(level, n) {

				if (n.children && n.children.length > 0) {
					if (levelWidth.length <= level + 1) levelWidth.push(0);

					levelWidth[level + 1] += n.children.length;
					n.children.forEach(function(d) {
						childCount(level + 1, d);
					});
				}
			};
			childCount(0, root);
			var newHeight = d3.max(levelWidth) * 25; // 25 pixels per line  
			tree = tree.size([newHeight, viewerWidth]);

			// Compute the new tree layout.
			var nodes = tree.nodes(root).reverse(),
			links = tree.links(nodes);

			// Set widths between levels based on maxLabelLength.
			nodes.forEach(function(d) {
				d.y = (d.depth * (maxLabelLength * 10)); //maxLabelLength * 10px
				// alternatively to keep a fixed scale one can set a fixed depth per level
				// Normalize for fixed-depth by commenting out below line
				// d.y = (d.depth * 500); //500px per level.
			});

			// Update the nodes��
			node = svgGroup.selectAll("g.node")
			.data(nodes, function(d) {
				return d.id || (d.id = ++i);
			});

			// Enter any new nodes at the parent's previous position.
			var nodeEnter = node.enter().append("g")
			.call(dragListener)
			.attr("class", "node")
			.attr("transform", function(d) {
				return "translate(" + source.y0 + "," + source.x0 + ")";
			})
			.on('click', click);

			nodeEnter.append("circle")
			.attr('class', 'nodeCircle')
			.attr("r", 0)
			.style("fill", function(d) {
				return d._children ? "lightsteelblue" : "#fff";
			})
			.on('click',function(d){
				d = toggleChildren(d);
			})

			nodeEnter.append("text")
			.attr("x", function(d) {
				return d.children || d._children ? -10 : 10;
			})
			.attr("dy", ".35em")
			.attr('class', 'nodeText')
			.attr("text-anchor", function(d) {
				return d.children || d._children ? "end" : "start";
			})
			.text(function(d) {
				return d.name;
			})
			.style("fill-opacity", 0);

			// phantom node to give us mouseover in a radius around it
			nodeEnter.append("circle")
			.attr('class', 'ghostCircle')
			.attr("r", 30)
			.attr("opacity", 0.2) // change this to zero to hide the target area
			.style("fill", "red")
			.attr('pointer-events', 'mouseover')
			.on("mouseover", function(node) {
				overCircle(node);
			})
			.on("mouseout", function(node) {
				outCircle(node);
			});
			
			nodeEnter.append("svg:image")
			.attr("x","2")
			.attr("y","3.5")
			.attr("width","10")
			.attr("height","10")
			.attr("id",function(d){return "manage-plusbt-"+d.id;})
			.attr("xlink:href","../images/manage-plus-icon.png")
			.on("click", function(d) { 
				popup_css_block($(".manage-modify-div-svg-"+d.id),false);
				popup_css_block($("#manage-closebt-"+d.id),false);
				popup_css_block($("#manage-modifybt-"+d.id),true);

				popup_css_block($(".manage-div-svg-"+d.id),true);
				popup_css_block($("#manage-minusbt-"+d.id),true);
			})

			nodeEnter.append("svg:image")
			.attr("x","2")
			.attr("y","3.5")
			.attr("width","10")
			.attr("height","10")
			.attr("id",function(d){return "manage-minusbt-"+d.id;})
			.attr("style","display:none;")
			.attr("xlink:href","../images/manage-minus-icon.png")
			.on("click", function(d) { 
				popup_css_block($(".manage-div-svg-"+d.id),false);
				popup_css_block($("#manage-minusbt-"+d.id),false);
			})


			nodeEnter.append("svg:image")
			.attr("x","12")
			.attr("y","4.5")
			.attr("width","8")
			.attr("height","8")
			.attr("id",function(d){return "manage-deletebt-"+d.id;})
			.attr("xlink:href","../images/manage-delete-icon.png")
			.on("click", function(d) { 
			})

			nodeEnter.append("svg:image")
			.attr("x","-5")
			.attr("y","5")
			.attr("width","7.5")
			.attr("height","7.5")
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
			.attr("x","-5")
			.attr("y","5")
			.attr("width","7.5")
			.attr("height","7.5")
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
					+"<input placeholder='Text' class='manage-insert-data-svg-"+d.id+" form-control text-input' type='text' />"
					+"<select placeholder='Index' class='manage-insert-data-index-svg-"+d.id+" form-control select-input' type='text'>"
					+"<option value='0'>"+0+"</option>"
					+"<option value='1'>"+1+"</option>"
					+"<option value='2'>"+2+"</option>"
					+"<option value='3'>"+3+"</option>"
					+"<option value='4'>"+4+"</option>"
					+"<option value='5'>"+5+"</option>"
					+"<option value='6'>"+6+"</option>"
					+"<option value='7'>"+7+"</option>"
					+"<option value='8'>"+8+"</option>"
					+"<option value='9'>"+9+"</option>"
					+"</select>"
					+"<input value='10:00:00' placeholder='10:00:00' class='manage-insert-data-starttime-svg-"+d.id+" form-control' type='text'/>"
					+"<input value='21:00:00' placeholder='21:00:00' class='manage-insert-data-endtime-svg-"+d.id+" form-control' type='text' />"
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
					+"<input value='"+d.name+"' class='manage-modify-data-text-svg-"+d.id+" form-control text-input' type='text'/>"
					+"<select placeholder='Index' class='manage-modify-data-index-svg-"+d.id+" form-control select-input' type='text'>"
					for( var i=0 ; i < 10 ; i++ ){
						if(i == d.indexs){
							tag += "<option value='"+i+"' selected>"+i+"</option>";
						}else{
							tag += "<option value='"+i+"'>"+i+"</option>";
						}
					}
				tag 
				+= "</select>"
					+"<input value='"+d.starttime+"' class='manage-modify-data-starttm-svg-"+d.id+" form-control' type='text'/>"
					+"<input value='"+d.endtime+"' class='manage-modify-data-endtm-svg-"+d.id+" form-control' type='text'/>"
					+"<input value='+' type='button' class='btn btn-lg btn-primary btn-block manage-modify submit-modchild-"+d.id+"' />";
				return tag;
			})


			// Update the text to reflect whether node has children or not.
			node.select('text')
			.attr("x", function(d) {
				return d.children || d._children ? -10 : 10;
			})
			.attr("text-anchor", function(d) {
				return d.children || d._children ? "end" : "start";
			})
			.text(function(d) {
				return d.indexs + ". " + d.name;
			});

			// Change the circle fill depending on whether it has children and is collapsed
			node.select("circle.nodeCircle")
			.attr("r", 4.5)
			.style("fill", function(d) {
				return d._children ? "lightsteelblue" : "#fff";
			});

			// Transition nodes to their new position.
			var nodeUpdate = node.transition()
			.duration(duration)
			.attr("transform", function(d) {
				return "translate(" + d.y + "," + d.x + ")";
			});

			// Fade the text in
			nodeUpdate.select("text")
			.style("fill-opacity", 1);

			// Transition exiting nodes to the parent's new position.
			var nodeExit = node.exit().transition()
			.duration(duration)
			.attr("transform", function(d) {
				return "translate(" + source.y + "," + source.x + ")";
			})
			.remove();

			nodeExit.select("circle")
			.attr("r", 0);

			nodeExit.select("text")
			.style("fill-opacity", 0);

			// Update the links��
			var link = svgGroup.selectAll("path.link")
			.data(links, function(d) {
				return d.target.id;
			});

			// Enter any new links at the parent's previous position.
			link.enter().insert("svg:path", "g")
			.attr("class", "link")
			.attr("stroke",function(d){
				return "blue";
			})
			.attr("stroke-width", function (d,i) {
				return d.target.count;
			})
			.attr("stroke-opacity",function(d,i){
				return 0.3;
			})
			.attr("d", function(d) {
				var o = {
						x: source.x0,
						y: source.y0
				};
				return diagonal({
					source: o,
					target: o
				});
			});

			// Transition links to their new position.
			link.transition()
			.duration(duration)
			.attr("d", diagonal);

			// Transition exiting nodes to the parent's new position.
			link.exit().transition()
			.duration(duration)
			.attr("d", function(d) {
				var o = {
						x: source.x,
						y: source.y
				};
				return diagonal({
					source: o,
					target: o
				});
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
					postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=add", addparams, null, function(s){alert(s.trim());$("svg").remove();draw_svg();}, function(e){alert(e);});
				});

				$(".submit-modchild-"+d.id).bind("click",function(){
					var target = $(".manage-div-svg.manage-div-svg-"+d.id);
					var addparams = {
							n_id : target.attr("node_id"),
							n_indexs : $(".manage-modify-data-index-svg-"+d.id).val(),
							n_text : $(".manage-modify-data-text-svg-"+d.id).val(),
							n_start: $(".manage-modify-data-starttm-svg-"+d.id).val(),
							n_end : $(".manage-modify-data-endtm-svg-"+d.id).val()
					};
					postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=mod", addparams, null, function(s){alert(s.trim());$("svg").remove();draw_svg();}, function(e){alert(e);});
				});

				$("#manage-deletebt-"+d.id).bind("click",function(){
					var target = $(".manage-div-svg.manage-div-svg-"+d.id);
					var addparams = {
							n_id : target.attr("node_id")
					};
					postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=del", addparams, null, function(s){alert(s.trim());$("svg").remove();draw_svg();}, function(e){alert(e);});
				});
			});
		}

		// Append a group which holds all nodes and which the zoom Listener can act upon.
		var svgGroup = baseSvg.append("g");

		// Define the root
		root = treeData;
		root.x0 = viewerHeight / 2;
		root.y0 = 0;

		// Layout the tree initially and center on the root node.
		update(root);
		centerNode(root);
	});
}

function click_search_database(){
	$("#find-time-min-max").bind("click",function(){
		find_by_min_time = $("#starttm").val();
		find_by_max_time = $("#endtm").val();
		postAjax("method/management-insert.jsp?number="+find_number_ajax+"&company="+find_company_ajax+"&func=parse", {t_min : find_by_min_time, t_max : find_by_max_time}, null, function(s){$("svg").remove();draw_svg();}, function(e){alert(e);});
	});
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

