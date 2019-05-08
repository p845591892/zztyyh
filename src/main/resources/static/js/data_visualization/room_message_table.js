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
				switch (msgObject) {
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
				/* 2019年新版 */
				case "TEXT":
					msgObject = "普通消息";
					break;
				case "REPLY":
					msgObject = "翻牌";
					break;
				case "IMAGE":
					msgObject = "图片";
					break;
				case "LIVEPUSH":
					msgObject = "生放送";
					break;
				case "FLIPCARD":
					msgObject = "付费翻牌";
					break;
				case "EXPRESS":
					msgObject = "特殊表情";
					break;
				case "VIDEO":
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
			title : "消息内容",
			formatter : function(value, row, index) {
				var msgObject = row.msgObject;
				var msgContent = row.msgContent;
				if (msgObject == "IMAGE" || msgObject == "image") {
					msgContent = msgContent.replace("<img>", "");
					msgContent = msgContent.replace("[图片]", "");
					return "<img src=\""+msgContent+"\" width=\"200\"/>";
				} else {
					return msgContent;
				}
			}
		}, {
			field : "msgTime",
			title : "消息发送时间",
			width : 138
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