$(document)
		.ready(
				function() {

					/* 友情提示 */
					var unique_id = $.gritter
							.add({
								title : '友情提示',
								text : '需要到后台进行新增微博用户，本页面进行的配置后，需要在服务器使用QQ打开对应的聊天窗口后，才能在同步消息的同时把微博消息发送到对应的聊天窗口中。',
								image : '../assets/img/ui-sam.jpg',
								sticky : true,
								time : '',
								class_name : 'my-sticky-class'
							});

					/* 微博用户表格 */
					$("#weibo_table").bootstrapTable({
						columns : [ {
							field : "id",
							title : "用户ID",
							visible : false
						}, {
							field : "userName",
							title : "用户名"
						}, {
							field : "containerId",
							title : "容器ID",
							visible : false
						} ],
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
						url : "/resource/weibo",
						detailView : true,
						detailFormatter : function(index, row) {
							var htmltext = showDetailView(row);
							return htmltext;
						}
					});

				});

/* 展示微博动态发送目标QQ群(展示配置) */
function showDetailView(row) {
	openLoad();
	var htmltext = "";
	$.ajax({
		url : "/resource/weibo/dynamic-monitor/" + row.id,
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

/* 新增微博动态监控配置 */
function showAddMonitor(btn, userId) {
	openLoad();
	$.ajax({
		url : "/resource/weibo/add-monitor-layer/" + userId,
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
						var communityId = layero.find("select").val();
						$
								.ajax({
									url : "/dynamic-monitor/add",
									data : {
										"userId" : userId,
										"communityId" : communityId
									},
									type : "post",
									success : function(data) {
										closeLoad();
										layerMsg(data.status, data.cause);
										if (data.status == 200) {
											$("#weibo_table").bootstrapTable(
													"refresh");
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
			} else {
				layerMsg(data.status, data.cause);
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/* 删除微博动态监控数据 */
function deleteMonitor(id) {
	layer.confirm('确定要删除该条配置吗？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/dynamic-monitor/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#weibo_table").bootstrapTable("refresh");
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