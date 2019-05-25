$(document).ready(function () {

    $("#room_message_table").bootstrapTable({
        columns: [/* {
			checkbox : true
		}, */{
                field: "id",
                title: "消息ID",
                visible: false
            }, {
                field: "senderId",
                title: "发送人ID",
                visible: false
            }, {
                field: "senderName",
                title: "发送人",
                width: 105
            }, {
                field: "roomId",
                title: "所属房间ID",
                visible: false
            }, {
                field: "msgObject",
                title: "消息类型",
                width: 70,
                formatter: msgObjectHtml
            }, {
                field: "msgContent",
                title: "消息内容",
                formatter: msgContentHtml
            }, {
                field: "msgTime",
                title: "消息发送时间",
                width: 138
            }, {
                field: "isSend",
                title: "是否发送过QQ消息",
                width: 100,
                formatter: isSendHtml
            }],
        // clickToSelect : true,
        url: "/resource/room-message",
        method: "GET",
        responseHandler: responseHandler,
        queryParamsType: '',
        queryParames: queryParames,
        striped: true,
        pagination: true,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 15,
        paginationLoop: false,
        pageList: "unlimited",
        cache: false,
        toolbar: "#toolbar",
        toolbarAlign: "left",
        showColumns: true,
        showRefresh: true
    });

    /* 查询 */
    $("#btn_select").on("click", function () {
        $("#room_message_table").bootstrapTable("refresh");
    });

});

/**
 * 消息类型
 */
var message_type = {
    "text": "普通消息(旧)", "faipaiText": "翻牌(旧)", "image": "图片(旧)", "live": "直播(旧)", "diantai": "电台(旧)", "idolFlip": "付费翻牌(旧)",
    "audio": "语音(旧)", "videoRecord": "视频(旧)", "TEXT": "普通消息", "REPLY": "翻牌", "IMAGE": "图片", "LIVEPUSH": "生放送", "FLIPCARD": "付费翻牌",
    "EXPRESS": "特殊表情", "VIDEO": "视频"
}

/**
 * 消息类型列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var msgObjectHtml = function (value, row, index) {
    var msgObject = row.messageObject;
    return message_type[msgObject];
}

/**
 * 消息内容列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var msgContentHtml = function (value, row, index) {
    var msgObject = row.messageObject;
    var msgContent = row.msgContent;
    console.log(row);
    console.log(msgObject);

    if (msgObject == "IMAGE" || msgObject == "image") {
        msgContent = msgContent.replace("<img>", "");
        msgContent = msgContent.replace("[图片]", "");
        return "<img src=\"" + msgContent + "\" width=\"200\"/>";

    } else if (msgObject == "live" || msgObject == "diantai" || msgObject == "LIVEPUSH") {
        var temp = null;
        if (msgContent.indexOf("<img>") != -1) {
            temp = msgContent.split("<img>");
            temp = temp[0] + "<br>" + "<img src=\"" + temp[1] + "\" width=\"200\"/>"
        } else if (msgContent.indexOf("[图片]" != -1)) {
            temp = msgContent.split("[图片]");
            temp = temp[0] + "<img src=\"" + temp[1] + "\" width=\"200\"/>"
        }
        return temp;

    } else {
        return msgContent;
    }
}

/**
 * 是否发送过QQ消息列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var isSendHtml = function (value, row, index) {
    var isSend = row.isSend;
    if (isSend == 1) {
        isSend = "未发送";
    } else if (isSend == 2) {
        isSend = "已发送";
    }
    return isSend;
}

/**
 * 表格响应处理
 * @param {*} res 
 */
var responseHandler = function (res) {
    return {
        pageNum: res.data.number,
        total: res.data.totalElements,
        rows: res.data.content
    };
}

/**
 * 表格请求参数
 * @param {*} params 
 */
var queryParames = function (params) {
    var searchText = $("#exampleInput1").val();
    params["searchText"] = searchText;
    return params;
}