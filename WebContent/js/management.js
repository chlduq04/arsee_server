var color_variation = ["#B4B1FF", "#8985FF", "#2D24FF", "#060097", "#040074"];
var find_company_ajax = $(".management_main").attr("company");
var find_number_ajax = $(".management_main").attr("number");
var find_by_min_time =  $(".management_main").attr("time");
var jsondata = [];
var click_by_id = {};
var click_by_before;
var click_by_mode = "add";
var root;
var mousex, mousey;
var click_by_root;
var day_or_holiday = "day";

function checkClick(func){
	return function(){
		if(click_by_id.n_id != undefined){
			func();
		}else{
			alert("원하시는 노드를 클릭해주세요")
		}
	}
}

$( document ).ready(function() {
	draw_svg();
	click_add_database();
	click_search_database();
	checkMousePosition();
	clickSettingTitle();
	clickSettingDetail();
	checkOtherData();
	console.log("새로운 분기 입니다. 상담원을 원하시면 1번, 문자로 하는 ARS로 돌아가시려면 2번을 눌러주세요");
	
	//analysis_data_tree();
	setTimeout(function(){	
		$("[data-toggle='switch']").wrap('<div class="switch" />').parent().bootstrapSwitch();
		$(".switch-left").html("week");
		$(".switch-right").html("holiday");
		$(".switch.has-switch").bind("click",function(){
			if($(".switch-animate").attr("class").match("switch-on") != null){
				day_or_holiday = "day";
				$("svg").remove();
				draw_svg();
			}else{
				day_or_holiday = "holiday";
				$("svg").remove();
				draw_svg();
			}
		})
		
		$(".company_input").val("olleh");
		$(".number_input").val("114");
	},10)
});

function clickSettingDetail(){
	$(".tab-detail-div").bind("click",function(e){
		$(".tab-detail-div").fadeOut(200,function(){
			$(".tab-detail-open").removeClass("display-none");
		});
	})	

	$(".management-close-bt").bind("click",function(e){
		$(".tab-detail-open").addClass("display-none");
		$(".tab-detail-div").fadeIn(300);
	})
}

function clickSettingTitle(){
	$(".management-add-question").bind("click",function(e){
		if((""+$(this).attr("class").match("type.*")).replace("type-","") == "analysis"){
		}else{
			$(this).next().focus();
		}
	})
}

function checkMousePosition(){
	$(document).bind('mousemove',function(e){ 
		mousex = e.pageX;
		mousey = e.pageY; 
	}); 
}



function draw_svg(){

	var colors = ["#bd0026","#fecc5c", "#fd8d3c", "#f03b20", "#B02D5D",
	              "#9B2C67", "#982B9A", "#692DA7", "#5725AA", "#4823AF",
	              "#d7b5d8","#dd1c77","#5A0C7A","#5A0C7A"];
	$(".company_input").val(find_company_ajax);
	$(".number_input").val(find_number_ajax);
	treeJSON = d3.json(makeGetUrl("method/management-"+day_or_holiday+".jsp",{number:find_number_ajax, company:find_company_ajax,func:"parse",t_min:find_by_min_time}), function(error, treeData) {
		// Get JSON data
		// Calculate total nodes, max label length
		var maxLabelLengthLimit = 40;
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

		// size of the diagram
		var viewerWidth = $(document).width();
		var viewerHeight = 850;

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
//			maxLabelLength = Math.max(d.name.length, maxLabelLength);
			maxLabelLength = maxLabelLengthLimit;

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
				if(selectedNode.children){
					for( var i = 0 ; i < selectedNode.children.length ; i++ ){
						if( d.indexs == selectedNode.children[i].indexs ){
							endDrag();
							return ;
						}	
					}			
				}
				if(selectedNode.indexs == "100"){
					endDrag();
					return ;					
				}
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
				check_save_tree();
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

		function makeEmptyNode(node, source){
			node.select('text')
			.attr("x", function(d) {
				return d.children || d._children ? -10 : 10;
			})
			.attr("text-anchor", function(d) {
				return d.children || d._children ? "end" : "start";
			})
			.text(function(d) {
				return "empty";
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
				return "translate(" + source.y+10 + "," + source.x+10 + ")";
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
				if(d.target.type == "info"){
					return "black";
				}else if(d.target.type == "error"){
					return "red";
				}else if(d.target.type == "star"){
					return "#0E53A7";
				}else if(d.target.type == "sharp"){
					return "#4512AE";
				}else{
					if(d.target.count/10000 >= 1){
						return color_variation[4];
					}else if(d.target.count/1000 >= 1){
						return color_variation[3];
					}else if(d.target.count/100 >= 1){
						return color_variation[2];
					}else if(d.target.count/10 >= 1){
						return color_variation[1];
					}else{
						return color_variation[0];
					}
				}
			})
			.attr("stroke-width", function (d,i) {
				return d.target.count%30;
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
			})
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
				return d.name.substring(0,maxLabelLengthLimit);
			})
			.on("click",function(d){

				if(click_by_before != null && click_by_before != undefined){
					click_by_before.attr({"fill":"black"})
				}
				$(this).attr({"fill":"blue"});
				click_by_before = $(this);
				
				click_by_id["n_id"] = d.id;
				click_by_id["n_parent"] = d.parentc;
				click_by_id["n_indexs"] = d.indexs;
				click_by_id["n_start"] = d.starttime;
				click_by_id["n_end"] = d.endtime;
				click_by_id["n_text"] = d.name;
				if(d.parentc == "-1"){
					click_by_id["n_depth"] = 0;
					click_by_id["n_parent"] = "";
					click_by_id["n_parent_index"] = "0";
				}else if(d.parent.parentc == "-1"){
					click_by_id["n_depth"] = 1;
					click_by_id["n_parent"] = "";
					click_by_id["n_parent_index"] =  d.indexs;
				}else{
					click_by_id["n_depth"] = d.parentc.length+1;
					click_by_id["n_parent"] = d.parentc;
					click_by_id["n_parent_index"] = d.indexs;
				}

				postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "tags_result" }), click_by_id, null, function(s){
					var inputs = $(".maq-div-3").find("input");
					var result = JSON.parse(s);
					var index = 0;
					for(var k in result) {
						$(inputs[index]).attr({"detail_id":k});
						$(inputs[index++]).val(result[k]);
					}

					for( ; index < inputs.length ; index++){
						$(inputs[index]).val("");
						$(inputs[index]).attr({"detail_id":-(index+1)});
					}

				}, function(e){alert(e);});		

				if(click_by_mode == "modify" || click_by_mode == "delete"){
					$("#addition-text").val(d.name);
					$("#addition-startt").val(d.starttime);
					$("#addition-endt").val(d.endtime);
					$("#addition-idx").val(d.indexs);
					$("#addition-type").val(d.type);
				}
				
				click_by_root = d;
				$(".graph-bts").css("opacity","1");

				//makeEmptyNode($(this.parentNode), source);
			})

			// phantom node to give us mouseover in a radius around it
			nodeEnter.append("circle")
			.attr('class', 'ghostCircle')
			.attr("r", 10)
			.attr("opacity", 0.2) // change this to zero to hide the target area
			.style("fill", "red")
			.attr('pointer-events', 'mouseover')
			.on("mouseover", function(node) {
				overCircle(node);
			})
			.on("mouseout", function(node) {
				outCircle(node);
			});

			// Update the text to reflect whether node has children or not.
			node.select('text')
			.attr("x", function(d) {
				return d.children || d._children ? -10 : 10;
			})
			.attr("text-anchor", function(d) {
				return d.children || d._children ? "end" : "start";
			})
			.text(function(d) {
				if(d.name.length <= maxLabelLengthLimit/2){
					return d.indexs + ". " + d.name;
				}
				return d.indexs + ". " + d.name.substring(0,maxLabelLengthLimit/2)+"...";
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
				if(d.target.type == "info"){
					return "black";
				}else if(d.target.type == "error"){
					return "red";
				}else if(d.target.type == "star"){
					return "#0E53A7";
				}else if(d.target.type == "sharp"){
					return "#4512AE";
				}else{
					if(d.target.count/10000 >= 1){
						return color_variation[4];
					}else if(d.target.count/1000 >= 1){
						return color_variation[3];
					}else if(d.target.count/100 >= 1){
						return color_variation[2];
					}else if(d.target.count/10 >= 1){
						return color_variation[1];
					}else{
						return color_variation[0];
					}
				}
			})
			.attr("stroke-width", function (d,i) {
				return d.target.count%30;
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
			})


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
	return root;
}

function click_search_database(){
	$("#find-time-min-max").bind("click",function(){
		find_by_min_time = $("#starttm").val();
		postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "parse" }), {t_min : find_by_min_time}, null, function(s){$("svg").remove();draw_svg();}, function(e){alert(e);});
	});
}

function aroundtree_to_json(root){
	var obj = new Object();
	obj.id = root.id;
	var checkdepth = parseInt(root.depth)-1;
	if(root.parent){
		if(checkdepth == 0){
			obj.parent = "0";
		}else if(checkdepth == 1){
			obj.parent = root.parent.indexs;															
		}else{
			obj.parent = root.parent.parentc + root.parent.indexs;	
		}
	}else{
		obj.parent = "-1";
	}
	root.parentc = obj.parent;
	obj.depth = checkdepth;
	obj.index = root.indexs;
	jsondata.push(obj); 

	if(root.children){
		for( var i=0 ; i < root.children.length ; i++){
			aroundtree_to_json(root.children[i]);		
		}
	}
}

function click_add_database(){

	$(".management-ars-list").bind("click",function(){
		click_by_mode = (""+$(this).attr("class").match("mal-.*")).replace("mal-","").replace(" click-border","");
		$(".management-ars-list").removeClass("click-border");
		$(this).addClass("click-border");
		if(click_by_mode == "add"){
			$(".management-info-div").find("input:not(.maq-5)").val("");
			$(".maq-input.maq-input-2-1").val("00:00:00");
			$(".maq-input.maq-input-2-2").val("23:59:59");
		}else if(click_by_mode == "modify" || click_by_mode == "delete"){
			$("#addition-text").val(click_by_id.n_text);
			$("#addition-startt").val(click_by_id.n_start);
			$("#addition-endt").val(click_by_id.n_end);
			$("#addition-idx").val(click_by_id.n_indexs);
			if(click_by_id.n_indexs == "s"){
				$("#addition-type").val("info")
			}else if(click_by_id.n_indexs == "*"){
				$("#addition-type").val("star")
			}else if(click_by_id.n_indexs == "#"){
				$("#addition-type").val("sharp")
			}else if(click_by_id.n_indexs == "-"){
				$("#addition-type").val("error")
			}else{
				$("#addition-type").val("normal")
			}
		}
	})
	
	$(".management-input-submit.maq-5").bind("click",checkClick(function(){
		if(click_by_mode == "modify"){
			click_by_id["n_text"] = $("#addition-text").val();
			click_by_id["n_start"] = $("#addition-startt").val();
			click_by_id["n_end"] = $("#addition-endt").val();
			click_by_id["n_type"] = $("#addition-type").val();
			click_by_id["n_indexs"] = $("#addition-idx").val();

			if(click_by_id["n_type"] == "info"){
				click_by_id["n_indexs"] = "s";
			}else if(click_by_id["n_type"] == "error"){
				click_by_id["n_indexs"] = "-"
			}else if(click_by_id["n_type"] == "sharp"){
				click_by_id["n_indexs"] = "#"
			}else if(click_by_id["n_type"] == "star"){
				click_by_id["n_indexs"] = "*"
			}
			postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "mod" }), click_by_id, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});
			tag_add();
		}else if(click_by_mode == "add"){
			if($("#addition-idx").val() != "auto"){
				click_by_id["n_text"] = $("#addition-text").val();
				click_by_id["n_indexs"] = $("#addition-idx").val();
				click_by_id["n_type"] = $("#addition-type").val();
				click_by_id["n_start"] = $("#addition-startt").val();
				click_by_id["n_end"] = $("#addition-endt").val();
				if(click_by_id["n_type"] == "info"){
					click_by_id["n_indexs"] = "s";
				}else if(click_by_id["n_type"] == "error"){
					click_by_id["n_indexs"] = "-"
				}else if(click_by_id["n_type"] == "sharp"){
					click_by_id["n_indexs"] = "#"
				}else if(click_by_id["n_type"] == "star"){
					click_by_id["n_indexs"] = "*"
				}
				postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "add_one" }), click_by_id, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});
			}else{
				click_by_id["n_text"] = $("#addition-text").val();
				click_by_id["n_start"] = $("#addition-startt").val();
				click_by_id["n_end"] = $("#addition-endt").val();
				if(click_by_id["n_type"] == "info"){
					click_by_id["n_indexs"] = "s";
				}else if(click_by_id["n_type"] == "error"){
					click_by_id["n_indexs"] = "-"
				}else if(click_by_id["n_type"] == "sharp"){
					click_by_id["n_indexs"] = "#"
				}else if(click_by_id["n_type"] == "star"){
					click_by_id["n_indexs"] = "*"
				}
				postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "add_text" }), click_by_id, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});
			}
			tag_add();
		}else if(click_by_mode == "delete"){
			jsondata = [];
			aroundtree_to_json(click_by_root);
			click_by_id["del_tree"] = JSON.stringify(jsondata);
			postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "del" }), click_by_id, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});
		}else if(click_by_mode == "mode"){
			jsondata = [];
			postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "duplicate" }), click_by_id, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});
		}
	}));

	$(".management-input-save").on("click",function(){
		if($(this).is(".active")){
			jsondata = [];
			aroundtree_to_json(root);
			postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "tree" }), {data : JSON.stringify(jsondata)}, null, function(s){
				$("svg").remove();draw_svg();
				$(".management-input-save").removeClass("active");
			}, function(e){alert(e);});
		}
	});

}

function tag_add(){
	if($(".tab-detail-open.display-none").length == 0){
		var inputs = $(".maq-div-3").find("input");
		var datadic = {};
		var detail_new = [];
		var detail_old = [];
		datadic["parent_id"] = click_by_id["n_id"];
		for(var i=0 ; i < inputs.length ; i++){
			if($(inputs[i]).attr("detail_id") < 0){
				if($(inputs[i]).val() != "" && $(inputs[i]).val() != null){
					detail_new.push($(inputs[i]).val());
				}else{
					break;
				}
			}else{
				var dic = {"text" : $(inputs[i]).val(), "id" : $(inputs[i]).attr("detail_id")};
				detail_old.push(dic);
			}
		}
		datadic["new"] = JSON.stringify(detail_new);
		datadic["old"] = JSON.stringify(detail_old);

		postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "tags_insert" }), datadic, null, function(s){console.log(s.trim());$("svg").remove();draw_svg();click_by_id = {};}, function(e){alert(e);});		
	}
}

function check_save_tree(){
	if(!$(".management-input-save").is(".active")){
		$(".management-input-save").addClass("active");
	}
}

function checkOtherData(){
	$(".type-analysis").bind("click",function(e){
		jsondata = [];
		postAjax(makeGetUrl("method/management-"+day_or_holiday+".jsp",{ number : find_number_ajax, company : find_company_ajax, func : "others" }), click_by_id, null, function(s){console.log(s.trim());
			$(".data-list-background").removeClass("display-none");
			$(".data-list").removeClass("display-none");
			var result = JSON.parse(s.trim());
			$(".data-list-ul").html("");
			var count = 0;
			for(var index in result){
				var arr = result[index].split("--");
				$(".data-list-ul").append($("<li></li>").html((count++)+". "+arr[0]+" ("+arr[1]+")"));
			}
			$(".data-list-ul>li").bind("click",function(){
				click_by_before.html($(this).html());
				$("#addition-text").val(arr[0]);
				$(".data-list").addClass("display-none");
				$(".data-list-background").addClass("display-none");
			})
		}, function(e){alert(e);});
	});
	
	$(".data-list-background").bind("click", function(e){
		$(".data-list").addClass("display-none");
		$(this).addClass("display-none");
	});
	
}

function aroundtree_to_tree(root){
	var obj = new Object();
	obj.id = root.id;
	if(root.parent){
		obj.parent = root.parent.parentc + root.parent.indexs;			
	}else{
		obj.parent = "-1";
	}
	obj.depth = root.depth;
	obj.children = [];
	console.log(root.name + ", " + root.id );
	if(root.parent){
		console.log("parent->parentc : "+parseInt(root.parent.parentc));		
		console.log("parent->indexs : "+parseInt(root.parent.indexs));		
	}
	if(root.children){
		for( var i=0 ; i < root.children.length ; i++){
			obj.children.push(aroundtree_to_tree(root.children[i]));		
		}
	}
	return obj;
}
