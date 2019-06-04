$(document).ready(function () {
    /* 轮询配置表格 */
    $("#quartz_confing_table").bootstrapTable({
        columns: [{
            checkbox: true
        }, {
            field: "id",
            title: "ID",
            visible: false
        }, {
            field: "jobName",
            title: "轮询任务"
        }, {
            field: "cronTrigger",
            title: "定时任务触发器"
        }, {
            field: "cron",
            title: "轮询规则"
        }/*, {
            field: "",
            title: "操作",
            formatter: operateHtml
        }*/],
        // clickToSelect: true,
        striped: true,
        pagination: true,
        sidePagination: "client",
        pageNumber: 1,
        pageSize: 15,
        pageList: [15, 25],
        paginationLoop: false,
        cache: false,
        search: true,
        searchAlign: "right",
        toolbar: "#toolbar",
        toolbarAlign: "left",
        showColumns: true,
        showRefresh: true,
        url: "/resource/quartz-config"
    });
})

/* 修改按钮弹窗 */
function updateVideoShow() {
    var selections = $("#quartz_confing_table").bootstrapTable("getSelections");
    if (selections.length > 1) {
        layerMsg(417, "修改操作只能选择一条信息");
    } else if (selections.length == 0) {
        layerMsg(417, "请选择一条信息");
    } else {
        var quartzConfig = selections[0];
        var html = "<div id=\"layer_add\"> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">轮询任务：</label> <div class=\"col-xs-8\"><input name=\"jobName\" class=\"form-control\" value=\""
            + quartzConfig.jobName
            + "\"></div> </div> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">定时任务触发器：</label> <div class=\"col-xs-8\"> <input name=\"cronTrigger\" class=\"form-control\" value=\""
            + quartzConfig.cronTrigger
            + "\"></div> </div> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">轮询规则：</label> <div class=\"col-xs-8\"> <input name=\"cron\" class=\"form-control\" value=\""
            + quartzConfig.cron + "\"></div> </div> </div>";
        layer.open({
            title: "修改轮询配置",
            type: 1,
            content: html,
            area: "600px",
            scrollbar: false,
            btn: ["保存", "取消"],
            yes: function (index, layero) {
                openLoad();
                var id = quartzConfig.id;
                var jobName = layero.find("input[name='jobName']").val();
                var cronTrigger = layero.find("input[name='cronTrigger']")
                    .val();
                var cron = layero.find("input[name='cron']").val();
                $.ajax({
                    url: "/quartz-config/update",
                    data: {
                        "id": id,
                        "jobName": jobName,
                        "cronTrigger": cronTrigger,
                        "cron": cron
                    },
                    type: "post",
                    success: function (data) {
                        closeLoad();
                        layerMsg(data.status, data.cause);
                        if (data.status == 200) {
                            $("#quartz_confing_table")
                                .bootstrapTable("refresh");
                            layer.close(index);
                        }
                    },
                    error: function (data) {
                        closeLoad();
                        layerMsg(500, "请求失败");
                    }
                });
            }
        });
    }
}

/**
 * 操作列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var operateHtml = function (value, row, index) {
    return "<button type=\"button\" class=\"btn btn-theme04\">开启</button>";
}