$(document)
		.ready(
				function() {

					/* 友情提示 */
					var unique_id = $.gritter
							.add({
								// (string | mandatory) the heading of the
								// notification
								title : '友情提示',
								// (string | mandatory) the text inside the
								// notification
								text : '本页面进行的配置后，需要在服务器使用QQ打开对应的聊天窗口后，才能在同步消息的同时把房间消息发送到对应的聊天窗口中。',
								// (string | optional) the image to display on
								// the left
								image : '../assets/img/ui-sam.jpg',
								// (bool | optional) if you want it to fade out
								// on its own or just sit there
								sticky : true,
								// (int | optional) the time you want it to be
								// alive for before fading out
								time : '',
								// (string | optional) the class name you want
								// to apply to that specific message
								class_name : 'my-sticky-class'
							});

					/* 成员表格 */
					$("#member_table")
							.bootstrapTable(
									{
										columns : [
												{
													field : "id",
													title : "成员ID",
													visible : false
												},
												{
													field : "avatar",
													title : "公式照",
													width : 65,
													formatter : function(value,
															row, index) {
														var avatar = row.avatar;
														return "<div class=\"project-wrapper\"><div class=\"project\"><div class=\"photo-wrapper img-circle\"><div class=\"photo\"><a class=\"fancybox\" href=\""
																+ avatar
																+ "\"><img src=\""
																+ avatar
																+ "\" class=\"img-circle\" width=\"60\" alt=\"\"/></a></div><div class=\"overlay\"></div></div></div></div>";
													}
												},
												{
													field : "name",
													title : "名字",
													width : 65
												},
												{
													field : "pinyin",
													title : "名字拼音"
												},
												{
													field : "groupId",
													title : "所属团体ID",
													visible : false
												},
												{
													field : "groupName",
													title : "所属团体"
												},
												{
													field : "teamId",
													title : "所属队伍ID",
													visible : false
												},
												{
													field : "teamName",
													title : "所属队伍"
												},
												{
													field : "roomId",
													title : "口袋房间ID",
													visible : false
												},
												{
													field : "roomName",
													title : "口袋房间名字"
												},
												{
													field : "topic",
													title : "话题"
												},
												{
													field : "roomMonitor",
													title : "口袋房间监控",
													width : 65,
													formatter : function(value,
															row, index) {
														var roomMonitor = row.roomMonitor;
														var id = row.id;
														if (roomMonitor == 1) {// 开启状态
															return "<button type=\"button\" class=\"btn btn-theme04\" onclick=\"updateRoomMonitor(this,"
																	+ id
																	+ ",'close')\"><i class=\"fa fa-power-off fa-2x\"></i> 关闭</button>";
														} else if (roomMonitor == 2) {// 关闭状态
															return "<button type=\"button\" class=\"btn btn-success\"  onclick=\"updateRoomMonitor(this,"
																	+ id
																	+ ",'open')\"><i class=\"fa fa-play-circle-o fa-2x\"></i> 开启</button>";
														} else if (roomMonitor == 40003) {
															return "房间不存在";
														}
													}
												} ],
										// cardView : true,
										striped : true,
										pagination : true,
										sidePagination : "client",
										pageNumber : 1,
										pageSize : 10,
										paginationLoop : false,
										pageList : "unlimited",
										cache : false,
										search : true,
										searchAlign : "right",
										showColumns : true,
										showRefresh : true,
										toolbar : "#toolbar",
										toolbarAlign : "left",
										url : "/resource/meber",
										detailView : true,
										detailFormatter : function(index, row) {
											var htmltext = showDetailView(row);
											return htmltext;
										}
									});

					jQuery(".fancybox").fancybox();
					$("select.styled").customSelect();

				});

/* 开关同步房间消息 */
function updateRoomMonitor(btn, id, type) {
	openLoad();
	var roomMonitor = 0;
	/* 1开启，2关闭 */
	if (type == "open") {
		roomMonitor = 1;
	} else {
		roomMonitor = 2;
	}
	$
			.ajax({
				url : "/member/update/room-monitor",
				data : {
					"id" : id,
					"roomMonitor" : roomMonitor
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						var parent = btn.parentNode;
						btn.remove();
						var newbtn = "";
						if (type == "open") {
							newbtn = "<button type=\"button\" class=\"btn btn-theme04\" onclick=\"updateRoomMonitor(this,"
									+ id
									+ ",'close')\"><i class=\"fa fa-power-off fa-2x\"></i> 关闭</button>";
						} else if (type == "close") {
							newbtn = "<button type=\"button\" class=\"btn btn-success\"  onclick=\"updateRoomMonitor(this,"
									+ id
									+ ",'open')\"><i class=\"fa fa-play-circle-o fa-2x\"></i> 开启</button>";
						}
						parent.innerHTML = newbtn;
					}
				},
				error : function(data) {
					closeLoad();
					layerMsg(500, "请求失败");
				}
			});
}

/* 展示房间的消息发送目标QQ群(展示配置) */
function showDetailView(row) {
	openLoad();
	var htmltext = "";
	$.ajax({
		url : "/resource/meber/room-monitor/" + row.roomId,
		type : "get",
		async : false,
		success : function(data) {
			if (data.status == 200) {
				htmltext = data.data;
			}
			closeLoad();
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
	return htmltext;
}

/* 新增房间监控配置 */
function showAddMonitor(btn, roomId) {
	openLoad();
	$.ajax({
		url : "/resource/meber/add-monitor-layer/" + roomId,
		type : "get",
		success : function(data) {
			closeLoad();
			if (data.status == 200) {
				layer.open({
					title : "房间新增监控配置",
					type : 1,
					content : data.data,
					area : "600px",
					scrollbar : false,
					btn : [ "保存", "取消" ],
					yes : function(index, layero) {
						openLoad();
						var keyword = layero.find("textarea").val();
						var communityId = layero.find("select").val();
						$.ajax({
							url : "/room-monitor/add",
							data : {
								"keywords" : keyword,
								"communityId" : communityId,
								"roomId" : roomId
							},
							type : "post",
							success : function(data) {
								closeLoad();
								layerMsg(data.status, data.cause);
								if (data.status == 200) {
									$("#member_table")
											.bootstrapTable("refresh");
									layer.close(index);
								}
							},
							error : function(data) {
								closeLoad();
								layerMsg(500, "请求失败");
							}
						});
					}
				});
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/* 关键字配置修改按钮 */
function updateKeyword(id) {
	openLoad();
	var keyword = "";
	$.ajax({
		url : "/resource/room-monitor/" + id + "/keyword",
		type : "get",
		async : false,
		success : function(data) {
			closeLoad();
			if (data.status == 200) {
				keyword = data.data.keywords;
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
	layer
			.open({
				title : "修改监控的关键字",
				type : 1,
				content : "<form id=\"monitor-form\" class=\"form-horizontal style-form\">"
						+ "<div class=\"form-group\">"
						+ "<div class=\"col-sm-1\"></div>"
						+ "<label class=\"col-sm-2 control-label\">关键字：</label><div class=\"col-sm-7\">"
						+ "<textarea class=\"form-control\" rows=\"3\" name=\"keyword\"></textarea>"
						+ "<span class=\"help-block\">对监控的消息进行关键字筛选，只发送包含关键字的消息。（关键字用逗号隔开，为空则不做筛选）</span>"
						+ "</div>" + "</div>" + "</form>",
				area : "600px",
				scrollbar : false,
				btn : [ "保存", "取消" ],
				success : function(layero, index) {
					$(layero).find("textarea").val(keyword);
				},
				yes : function(index, layero) {
					openLoad();
					keyword = layero.find("textarea").val();
					$.ajax({
						url : "/room-monitor/update/keyword",
						type : "post",
						data : {
							"id" : id,
							"keyword" : keyword
						},
						success : function(data) {
							closeLoad();
							layerMsg(data.status, data.cause);
							if (data.status == 200) {
								$("#member_table").bootstrapTable("refresh");
								layer.close(index);
							}
						},
						error : function(data) {
							closeLoad();
							layerMsg(500, "请求失败");
						}
					});
				}
			});
}

/* 配置删除按钮 */
function deleteMonitor(id) {
	layer.confirm('确定要删除该条配置吗？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/room-monitor/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#member_table").bootstrapTable("refresh");
					layer.close(index);
				}
			},
			error : function(data) {
				closeLoad();
				layerMsg(500, "请求失败");
			}
		});
	});
}

/* 同步最新的成员房间信息 */
function refreshVideoShow() {
	openLoad();
	$.ajax({
		url : "/member/refresh",
		type : "get",
		success : function(data) {
			closeLoad();
			layerMsg(data.status, data.cause);
			$("#member_table").bootstrapTable("refresh");
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}