$(document).ready(function() {
	
	$("#room_message_table").bootstrapTable({
		columns : [/* {
			checkbox : true
		}, */{
			field : "id",
			title : "消息ID",
			visible : false
		}, {
			field : "senderId",
			title : "发送人ID",
			visible : false
		}, {
			field : "senderName",
			title : "发送人",
			width : 105
		}, {
			field : "roomId",
			title : "所属房间ID",
			visible : false
		}, {
			field : "msgObject",
			title : "消息类型",
			width : 70,
			formatter : function(value, row, index) {
				var msgObject = row.msgObject;
				switch (msgObject) {/*,,...*/
				case "text":
					msgObject = "普通消息";
					break;
				case "faipaiText":
					msgObject = "翻牌";
					break;
				case "image":
					msgObject = "图片";
					break;
				case "live":
					msgObject = "直播";
					break;
				case "diantai":
					msgObject = "电台";
					break;
				case "idolFlip":
					msgObject = "付费翻牌";
					break;
				case "audio":
					msgObject = "语音";
					break;
				case "videoRecord":
					msgObject = "视频";
					break;
				default:
					msgObject = "未知格式";
					break;
				}
				return msgObject;
			}
		}, {
			field : "msgContent",
			title : "消息内容"
		}, {
			field : "msgTime",
			title : "消息发送时间",
			width : 135
		}, {
			field : "isSend",
			title : "是否发送过QQ消息",
			width : 100
		} ],
		// clickToSelect : true,
		url : "/resource/room-message",
		method : "GET",
		responseHandler : function(res) {
			return {
				pageNum : res.data.pageNum,
				total : res.data.total,
				rows : res.data.list
			};
		},
		queryParamsType : '',
		/*queryParames : function(params) {
			alert(params);
			console.log(params);
			return {
				pageSize : params.pageSize,
				pageNum : params.pageNumber
			};
		},*/
		striped : true,
		pagination : true,
		sidePagination : "server",
		pageNumber : 1,
		pageSize : 15,
		paginationLoop : false,
		pageList : "unlimited",
		cache : false,
		searchAlign : "right",
		toolbar : "#toolbar",
		toolbarAlign : "left",
		showColumns : true,
		showRefresh : true
	});
	
	
});