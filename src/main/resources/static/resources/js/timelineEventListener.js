var functionVar;

function layer_confirm(msg, fn) {
    functionVar = fn;
    var html = handlebarsFn("#systemMessageBlock",{
        massage:msg,
        isButton:true
    })
    $("#confirm_popup_box")
        .show()
        .find(".popup_content")
        .html(html);
}

function executeFunction(queue) {
    var fn = functionVar;
    if (queue) {
        fn();
    } else {
        $("#confirm_popup_box")
            .hide();
    }
}

$.ajaxSetup({
    type : "POST",
    async : true,
    timeout : 1000,
    dataType: "html",
    beforeSend:function(xhr) {
        $('.loading-bg').show();
        // xhr.setRequestHeader(headerName, token);
    },
    complete:function(xhr) {
        $('.loading-bg').hide();
    },
    /*beforeSend: function(xhr) {
        $("#popup_box button").hide();
        $("#popup_box .waitTxt").text("잠시만 기다려주세요.");
    },*/
    /*complete: function(xhr,status){
        $("#popup_box button").show();
        $("#popup_box .waitTxt").text("");
        $("#popup_box")
            .hide()
            .find(".popup_content")
            .html("");
    },*/
    dataFilter: function (data, type) {
        //		if (type =='html'){
        var bodyIdx = (data + '').indexOf('body');
        var loginIdx = (data + '').indexOf('세션이 만료');
        if (loginIdx > -1 &&  loginIdx > bodyIdx){
            document.location.href= '/expired';
            return '';
        }
//		} else {
//			//script 에러 500
//		}
        return data;
    },

    error : function(xhr,status,error) {
        console.log("에러발생!!");
        console.log(error);
    },
    cache : false
});


/* 그룹 영역 방송 건별 등록*/
$(document).on("click",".addItem", function (e) {
    var html, groupNo, groupName, hourArr, minuteArr, startHourArr, startMinuteArr, endHourArr, endMinuteArr;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {
        var parentEl = $(this).parents(".channel_box");
        groupNo = parentEl.attr("groupNo");
        groupName = parentEl.attr("groupName");
        startHourArr = getOptionTimeObj(-1, 1, 24);
        startMinuteArr = getOptionTimeObj(-1, 5, 60);
        endHourArr = getOptionTimeObj(-1, 1, 24);
        endMinuteArr = getOptionTimeObj(-1, 5, 60);
        var schedTypeSelObj = getSchedTypeSelObj("");
        html = handlebarsFn("#regSchedBlock", {
            groupNo:groupNo,
            groupName:groupName,
            startHourArr:startHourArr,
            endHourArr:endHourArr,
            startMinuteArr:startMinuteArr,
            endMinuteArr:endMinuteArr,
            schedTypeSelObj:schedTypeSelObj,
            toDay:datatime.getFullYear()+numberPad((datatime.getMonth()+1),2)+numberPad(datatime.getDate(), 2),
            startDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
            endDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
            mode:"write"
        });
    }

    $("#popup_box")
        .show()
        .find(".popup_content")
        .html(html);

    $.fn.datePickerCommon({
        id : "#startDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
        startId : "#popup_box input[name=startDay]"
    });

    $.fn.datePickerCommon({
        id : "#endDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
        startId : "#popup_box input[name=endDay]"
    });

});
/* 작업외 일정 건별 등록*/
$(document).on("click",".addNoWork", function (e) {
    var html, groupNo, groupName, startHourArr, endHourArr, startMinuteArr, endMinuteArr;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {
        var groupTeamList = getGroupTeamList();
        var parentEl = $(this).parents(".channel_box");
        groupNo = parentEl.attr("groupNo");
        groupName = parentEl.attr("groupName");

        startHourArr = getOptionTimeObj(-1, 1, 24);
        endHourArr = getOptionTimeObj(-1, 1, 24);
        startMinuteArr = getOptionTimeObj(-1, 5, 60);
        endMinuteArr = getOptionTimeObj(-1, 5, 60);

        // 배정구분 셀렉박스 생성
        var schedGbArr = [{value:"R",name:"휴식"},{value:"E",name:"식사"}];
        html = handlebarsFn("#regNoWorkBlock", {
            groupNo:groupNo,
            groupName:groupName,
            startHourArr:startHourArr,
            endHourArr:endHourArr,
            startMinuteArr:startMinuteArr,
            endMinuteArr:endMinuteArr,
            groupTeamList:groupTeamList,
            schedGbArr:schedGbArr,
            startDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
            endDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
            mode:"write"
        });
    }

    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);

    $.fn.datePickerCommon({
        id : "#startDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
        startId : "#popup_box input[name=startDay]"
    });

    $.fn.datePickerCommon({
        id : "#endDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2),
        startId : "#popup_box input[name=endDay]"
    });

});

$(document).on("click",".modiNoWork", function (e) {
    var html, groupNo, groupName, startHourArr, endHourArr, startMinuteArr, endMinuteArr;
    var roleAdmin = $("#roleAdmin");
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    var memStartTime = item.start
        , memEndTime = new Date(item.start.getTime());
    memStartTime.setMinutes(memStartTime.getMinutes()+item.member.start);
    memEndTime.setMinutes(memStartTime.getMinutes()+item.member.workTime);

    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {
        var groupTeamList = getGroupTeamList();
        var groupTeamMemberList = getGroupTeamMemList(item.member.groupTeamType);
        var parentEl = $(this).parents(".channel_box");

        startHourArr = getOptionTimeObj(memStartTime.getHours(), 1, 24);
        startMinuteArr = getOptionTimeObj(memStartTime.getMinutes(), 5, 60);
        endHourArr = getOptionTimeObj(memEndTime.getHours(), 1, 24);
        endMinuteArr = getOptionTimeObj(memEndTime.getMinutes(), 5, 60);

        var endMemAmt = (memEndTime.getTime() - memStartTime.getTime()) / 60000; // 분 단위로 변경.

        // 배정구분 셀렉박스 생성
        var schedGb = (item.className.indexOf("rest") > -1)?"R":"E";
        var schedGbArr = [{value:"R",name:"휴식"},{value:"E",name:"식사"}];
        for (var i = 0, gbLen = schedGbArr.length; i < gbLen; i++) {
            schedGbArr[i].isSelect = false;
            if (schedGb == schedGbArr[i].value) {
                schedGbArr[i].isSelect = true;
            }
        }
        // 채널팀
        for (var i = 0, groupLen = groupTeamList.length; i < groupLen; i++) {
            groupTeamList[i].isSelect = false;
            if (groupTeamList[i].grpType == item.member.groupTeamType) {
                groupTeamList[i].isSelect = true;
            }
        }
        // 채널팀 팀원 select box
        for (var i = 0, groupMemLen = groupTeamMemberList.length; i < groupMemLen; i++) {
            groupTeamMemberList[i].isSelect = false;
            if (groupTeamMemberList[i].memNo == item.member.mno) {
                groupTeamMemberList[i].isSelect = true;
            }
        }
        html = handlebarsFn("#regNoWorkBlock", {
            groupNo:item.groupNo,
            groupName:item.group,
            startHourArr:startHourArr,
            endHourArr:endHourArr,
            startMinuteArr:startMinuteArr,
            endMinuteArr:endMinuteArr,
            groupTeamList:groupTeamList,
            schedGbArr:schedGbArr,
            groupTeamMemberList:groupTeamMemberList,
            memSchedNo:item.member.memSchedNo,
            memNm:item.member.name,
            startDay:item.start.getFullYear()+"-"+numberPad((item.start.getMonth()+1),2)+"-"+numberPad(item.start.getDate(), 2),
            endDay:item.end.getFullYear()+"-"+numberPad((item.end.getMonth()+1),2)+"-"+numberPad(item.end.getDate(), 2),
            endMemAmt:endMemAmt,
            mode:"edit"
        });
    }
    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);

    $.fn.datePickerCommon({
        id : "#startDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : item.start.getFullYear()+"-"+numberPad((item.start.getMonth()+1),2)+"-"+numberPad(item.start.getDate(), 2),
        startId : "#popup_box input[name=startDay]"
    });

    $.fn.datePickerCommon({
        id : "#endDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : item.end.getFullYear()+"-"+numberPad((item.end.getMonth()+1),2)+"-"+numberPad(item.end.getDate(), 2),
        startId : "#popup_box input[name=endDay]"
    });

});

/* 작업외 블록 삭제 */
$(document).on("click",".deleteNoWork", function (e) {

    var roleAdmin = $("#roleAdmin");
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    var groupNo = item.groupNo;
    var memSchedNo = item.member.memSchedNo;

    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
        $("#popup_box")
            .show()
            .find(".popup_content")
            .html(html);
    } else {
        layer_confirm("정말로 삭제 하시겠습니까?", function () {
            $.ajax({
                url: "/api/schedule/deleteMemberSchedule",
                type:"POST",
                contentType: 'application/json',
                data:JSON.stringify({
                    memSchedNo:memSchedNo,
                    chnlNo:groupNo,
                    toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
                }),
                dataType: 'json',
                success: function (result) {
                    if (result.status == "success") {
                        timeline.deleteItem(itemIndex, false);
                        $("#confirm_popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                    } else {
                        $("#confirm_popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                        popupSystemMessagePrint(result.massage, "alert");
                    }

                }
            });
        })
    }
})

/* 그룹 영역 방송 대량 등록*/
$(document).on("click",".addItems", function (e) {
    var html, groupNo, groupName, hourArr, minuteArr;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {
        var parentEl = $(this).parents(".channel_box");
        groupNo = parentEl.attr("groupNo");
        groupName = parentEl.attr("groupName");
        html = handlebarsFn("#regMultiSchedBlock", {
            groupNo:groupNo,
            groupName:groupName
        });
    }
    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);
});

/* 업로드 이력 엑셀 다운로드 팝업열기 */
$(document).on("click", ".openUploadHistoryExcelBlock", function (e) {
   e.preventDefault();
    var html, groupNo, groupName, hourArr, minuteArr;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {
        var parentEl = $(this).parents("#box_form");
        groupNo = parentEl.find("input[name='chnlNo']").val();
        groupName = parentEl.find("input[name='chnlNm']").val();
        var historyList = [];
        // 파일 업로드 이력 가져오기
        $.ajax({
            url: "/api/schedule/getUploadHistoryData",
            type:"POST",
            contentType: 'application/json',
            data:JSON.stringify({chnlNo:groupNo}),
            dataType: 'json',
            async:false,
            success: function (json) {
                var result = json.data;
                if (json.status == 'success') {
                    historyList = result.scheduleFiles;
                    html = handlebarsFn("#uploadHistoryExcelBlock", {
                        groupNo:groupNo,
                        groupName:groupName,
                        historyList:historyList
                    });
                    $("#popup_box")
                        .show()
                        .find(".popup_content")
                        .html(html);
                }
            }/*,
            complete:function(xhr) {
                $("#popup_box button").show();
                $("#popup_box .waitTxt").text("");
            }*/
        });


    }

});
/* 엑셀 파일 업로드 대량등록 */
$(document).on("click",".openExcelUploadPopup", function (e) {
    e.preventDefault();
    var chnlNo = $("#popup_box").find("input[name='chnlNo']").val();
    var chnlNm = $("#popup_box").find("input[name='chnlNm']").val();
    var html = handlebarsFn("#regExcelSchedBlock", {
        groupNo:chnlNo,
        groupName:chnlNm,
        toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
    });
    // 데이트 피커

    $("#popup_box")
        .find(".popup_content")
        .html(html);

    $.fn.datePickerCommon({
        id : "#today",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        maxDate:datatime.getFullYear()+"-"+(datatime.getMonth()+1)+"-"+datatime.getDate()
    });
});
/* 기등록 스케줄 복제등록 */
$(document).on("click",".copySchedRegPopup", function (e) {
    e.preventDefault();
    var chnlNo = $("#popup_box").find("input[name='chnlNo']").val();
    var chnlNm = $("#popup_box").find("input[name='chnlNm']").val();
    var html = handlebarsFn("#copySchedBlock", {
        groupNo:chnlNo,
        groupName:chnlNm,
        year:datatime.getFullYear(),
        month:numberPad((datatime.getMonth()+1),2),
        day:numberPad(datatime.getDate(), 2)
    });
    // 데이트 피커

    $("#popup_box")
        .find(".popup_content")
        .html(html);

    $.fn.datePickerCommon({
        id : "#chooseDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        maxDate:datatime.getFullYear()+"-"+(datatime.getMonth()+1)+"-"+datatime.getDate()
    });
})

/* 기등록 작업외 일정 복제등록 */
$(document).on("click",".copyNoWork", function (e) {
    e.preventDefault();
    var chnlNo = $("#popup_box").find("input[name='chnlNo']").val();
    var chnlNm = $("#popup_box").find("input[name='chnlNm']").val();
    var html;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage: "관리자권한이 필요합니다."
        });
    } else {
        html = handlebarsFn("#copyNoWorkBlock", {
            groupNo:chnlNo,
            groupName:chnlNm,
            year:datatime.getFullYear(),
            month:numberPad((datatime.getMonth()+1),2),
            day:numberPad(datatime.getDate(), 2)
        });
    }

    // 데이트 피커
    $("#popup_box")
        .show()
        .find(".popup_content")
        .html(html);

    $.fn.datePickerCommon({
        id : "#chooseDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        maxDate:datatime.getFullYear()+"-"+(datatime.getMonth()+1)+"-"+datatime.getDate()
    });
})

/* 그룹 영역 방송 일괄 삭제*/
$(document).on("click",".allDelete", function (e) {
    var parentEl = $(this).parents(".channel_box");
    var groupNo = parentEl.attr("groupNo");
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage: "관리자권한이 필요합니다."
        });
        $("#popup_box")
            .show()
            .find(".popup_content")
            .html(html);
    } else {
        layer_confirm("정말로 삭제 하시겠습니까?", function () {
            $.ajax({
                url: "/api/schedule/deleteSchedule",
                type:"POST",
                contentType: 'application/json',
                data:JSON.stringify({
                    chnlNo:groupNo,
                    toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
                }),
                dataType: 'json',
                success: function (result) {
                    if (result) {
                        timeline.deleteGroupItems({no:groupNo});
                        $("#confirm_popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                    }
                }
            });
        })
    }
        
});

/* 아이템 영역 프로그램 수정*/
$(document).on("click", ".modiWork", function (e) {
    var html, startHourArr, endHourArr, startMinuteArr, endMinuteArr;
    var roleAdmin = $("#roleAdmin");
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {

        startHourArr = getOptionTimeObj(item.start.getHours(), 1, 24);
        startMinuteArr = getOptionTimeObj(item.start.getMinutes(), 5, 60);
        endHourArr = getOptionTimeObj(item.end.getHours(), 1, 24);
        endMinuteArr = getOptionTimeObj(item.end.getMinutes(), 5, 60);
        // 배정구분 셀렉박스 생성

        var schedTypeSelObj = getSchedTypeSelObj(item.schedType);
        html = handlebarsFn("#regSchedBlock", {
            groupNo:item.groupNo,
            groupName:item.group,
            schedNo:item.schedNo,
            progNm:item.content,
            startHourArr:startHourArr,
            endHourArr:endHourArr,
            startMinuteArr:startMinuteArr,
            endMinuteArr:endMinuteArr,
            schedTypeSelObj:schedTypeSelObj,
            toDay:datatime.getFullYear()+numberPad((datatime.getMonth()+1),2)+numberPad(datatime.getDate(), 2),
            startDay:item.start.getFullYear()+"-"+numberPad((item.start.getMonth()+1),2)+"-"+numberPad(item.start.getDate(), 2),
            endDay:item.end.getFullYear()+"-"+numberPad((item.end.getMonth()+1),2)+"-"+numberPad(item.end.getDate(), 2),
            mode:"edit"
        });
    }
    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);


    $.fn.datePickerCommon({
        id : "#startDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : item.start.getFullYear()+"-"+numberPad((item.start.getMonth()+1),2)+"-"+numberPad(item.start.getDate(), 2),
        startId : "#popup_box input[name=startDay]"
    });

    $.fn.datePickerCommon({
        id : "#endDay",
        type : "D",
        useSinglePicker : true,
        useRanges : false,
        startDate : item.end.getFullYear()+"-"+numberPad((item.end.getMonth()+1),2)+"-"+numberPad(item.end.getDate(), 2),
        startId : "#popup_box input[name=endDay]"
    });
})
/* 아이템 영역 프로그램 삭제*/
$(document).on("click", ".delSched", function (e) {

    var roleAdmin = $("#roleAdmin");
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);

    var groupNo = item.groupNo;
    var schedNo = item.schedNo;

    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
        $("#popup_box")
            .show()
            .find(".popup_content")
            .append(html);
    } else {
        layer_confirm("정말로 삭제 하시겠습니까?", function () {
            $.ajax({
                url: "/api/schedule/deleteSchedule",
                type:"POST",
                contentType: 'application/json',
                data:JSON.stringify({
                    chnlNo:groupNo,
                    schedNo:schedNo,
                    toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
                }),
                dataType: 'json',
                success: function (result) {
                    if (result) {
                        timeline.deleteItem(itemIndex, false);
                        $("#confirm_popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                    }
                }
            });
        })
    }
});

/* 아이템 영역 프로그램 삭제*/
$(document).on("click", ".fa-file-download", function (e) {
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    var groupNo = item.groupNo;
    var schedNo = item.schedNo;
    schedNo = (schedNo) ? schedNo * 1:0;

    $.ajax({
        url: "/schedule/textDownload",
        type:"GET",
        data:{
            schedNo:schedNo
        },
        dataType: 'html',
        success: function (result) {
            if (result) {
                location.href = "/schedule/textDownload?"+"schedNo="+schedNo;
            } else {
                html = handlebarsFn("#systemMessageBlock", {
                    massage:"작업데이터가 없습니다."
                });
                $("#popup_box")
                    .show()
                    .find(".popup_content")
                    .append(html);
            }
        }
    });
})

/* 아이템 영역 프로그램에 작업자 추가*/
$(document).on("click", ".addWorker", function (e) {

    var html, groupNo, groupName, startHourArr, endHourArr, startMinuteArr, endMinuteArr;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {

        var itemIndex = getSelectedRow();
        var item = timeline.getItem(itemIndex);
        var amt = (item.end.getTime() - item.start.getTime()) / 60000;
        var ord = ((item.member)?item.member.length:0) + 1;
        var startAmtArr = getWorkerAmt(-1, amt);
        var endAmtArr = getWorkerAmt(-1, amt);
        // 채널팀 팀원 select box
        var chnlTeamMemberList = getChnlTeamMemList(item.groupNo);
        for (var i = 0, chnlMemLen = chnlTeamMemberList.length; i < chnlMemLen; i++) {
            chnlTeamMemberList[i].isSelect = false;
            if (item.member) {
                if (chnlTeamMemberList[i].memNo == item.member.mno) {
                    chnlTeamMemberList[i].isSelect = true;
                }
            }
        }

        html = handlebarsFn("#regMemSchedBlock", {
            groupNo:item.groupNo,
            groupName:item.group,
            schedNo:item.schedNo,
            chnlTeamMemberList:chnlTeamMemberList,
            startAmtArr:startAmtArr,
            endAmtArr:endAmtArr,
            progNm:item.content,
            ord:ord,
            mode:"write",
            toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
        });
    }
    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);

});

/* 일정 상세 내역 */
$(document).on("click", ".showDetails", function (e) {
    var html;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
        $("#popup_box")
            .show()
            .find(".popup_content")
            .append(html);
    } else {
        var parentEl = $(this).parents(".channel_box");
        var groupNo = parentEl.attr("groupNo");
        var groupName = parentEl.attr("groupName");

        $.ajax({
            url: "/api/schedule/getWorkDetails",
            type:"POST",
            contentType: 'application/json',
            data:JSON.stringify({
                chnlNo:groupNo,
                toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
            }),
            dataType: 'json',
            success: function (json) {
                var result = json.data;
                if (result) {
                    console.log(result);
                    if (result.status == 'SUCCESS') {
                        timeline.showDetail({
                            groupNo:groupNo,
                            groupName:groupName,
                            member:result.dashBoardList,
                        });
                    } else {
                        popupSystemMessagePrint("작업 내역이 존재하지 않습니다.", "alert");
                    }
                }
            },
        })
    }
})

function getWorkerAmt(workerAmt, totalAmt) {
    var amt = [];
    for (var i = 0; i <= totalAmt; i += 5) {
        var obj = {};
        obj.value = i;
        obj.isSelect = (workerAmt == i) ? true:false;
        amt.push(obj);
    }
    return amt;
}
function setInputTime (el) {
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    var value = $(el).val();
    var startTime, endTime, startVal;

    if ($(el).hasClass("startMemAmt")) {
        startTime = new Date(item.start.getTime() + (value * 60000));
        $("#popup_box").find("input[name='startH']").val(startTime.getHours());
        $("#popup_box").find("input[name='startM']").val(startTime.getMinutes());
        $("#popup_box").find("input[name='startDay']").val(startTime.getFullYear()+"-"+numberPad((startTime.getMonth()+1),2)+"-"+numberPad(startTime.getDate(), 2));
    } else if ($(el).hasClass("endMemAmt")) {
        startVal = $("#popup_box").find("select[name='startMemAmt']").val();
        endTime = new Date(item.start.getTime() + (value * 60000));
        $("#popup_box").find("input[name='endH']").val(endTime.getHours());
        $("#popup_box").find("input[name='endM']").val(endTime.getMinutes());
        $("#popup_box").find("input[name='endDay']").val(endTime.getFullYear()+"-"+numberPad((endTime.getMonth()+1),2)+"-"+numberPad(endTime.getDate(), 2));
    }

}

function setInputAmt () {

    var startDay = $(".modal-content").find("input[name=startDay]").val();
    var startH = $(".modal-content").find("#startH").val();
    var startM = $(".modal-content").find("#startM").val();

    var endDay = $(".modal-content").find("input[name=endDay]").val();
    var endH = $(".modal-content").find("#endH").val();
    var endM = $(".modal-content").find("#endM").val();
    var endAmt;

    startDay = (startDay)?startDay:"";
    startH = (startH)?startH:0;
    startM = (startM)?startM:0;
    endDay = (endDay)?endDay:"";
    endH = (endH)?endH:0;
    endM = (endM)?endM:0;

    if (startDay && endDay) {
        var startDayArr = startDay.split("-");
        var endDayArr = endDay.split("-");
        var startDate = new Date(startDayArr[0], startDayArr[1], startDayArr[2], startH, startM);
        var endDate = new Date(endDayArr[0], endDayArr[1], endDayArr[2], endH, endM);
        endAmt = (endDate.getTime() - startDate.getTime()) / 60000; // 분 단위로 변경.
        $(".modal-content input[name=endMemAmt]").val(endAmt);
    }

}

function setMemNm (el) {
    var memNm = $(el).find("option:selected").attr("memNm");
    $("#popup_box").find("input[name='memNm']").val(memNm);
}

/* 아이템 작업자영역 작업자 수정 */
$(document).on("click", ".stt_box.on .working .edit", function (e) {
    var html;
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
    } else {

        var itemIndex = getSelectedRow();
        var item = timeline.getItem(itemIndex);
        var indexNo = $(".stt_box.on .working .edit").index(this);
        var memSchedNo = item.member[indexNo].memSchedNo;
        var amt = (item.end.getTime() - item.start.getTime()) / 60000;
        var startAmtArr = getWorkerAmt(item.member[indexNo].start, amt);
        var endAmtArr = getWorkerAmt(item.member[indexNo].start+item.member[indexNo].workTime, amt);
        var memNm;
        // 채널팀 팀원 select box
        var chnlTeamMemberList = getChnlTeamMemList(item.groupNo);
        for (var i = 0, chnlMemLen = chnlTeamMemberList.length; i < chnlMemLen; i++) {
            chnlTeamMemberList[i].isSelect = false;
            if (item.member) {
                if (chnlTeamMemberList[i].memNo == item.member[indexNo].mno) {
                    chnlTeamMemberList[i].isSelect = true;
                    memNm = chnlTeamMemberList[i].memNm;
                }
            }
        }
        var startTime, endTime, startVal;
        startTime = new Date(item.start.getTime() + (item.member[indexNo].start * 60000));
        endTime = new Date(item.start.getTime() + (item.member[indexNo].start * 60000) + (item.member[indexNo].workTime * 60000));

        html = handlebarsFn("#regMemSchedBlock", {
            groupNo:item.groupNo,
            groupName:item.group,
            schedNo:item.schedNo,
            chnlTeamMemberList:chnlTeamMemberList,
            startAmtArr:startAmtArr,
            endAmtArr:endAmtArr,
            progNm:item.content,
            memSchedNo:memSchedNo,
            memNm:memNm,
            startH:startTime.getHours(),
            startM:startTime.getMinutes(),
            endH:endTime.getHours(),
            endM:endTime.getMinutes(),
            startDay:startTime.getFullYear()+"-"+numberPad((startTime.getMonth()+1),2)+"-"+numberPad(startTime.getDate(), 2),
            endDay:endTime.getFullYear()+"-"+numberPad((endTime.getMonth()+1),2)+"-"+numberPad(endTime.getDate(), 2),
            mode:"edit",
            toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
        });
    }
    $("#popup_box")
        .show()
        .find(".popup_content")
        .append(html);

});


/* 아이템 작업자영역 작업자 삭제 */
$(document).on("click", ".stt_box.on .working .delete", function (e) {
    var roleAdmin = $("#roleAdmin");
    var itemIndex = getSelectedRow();
    var item = timeline.getItem(itemIndex);
    var groupNo = item.groupNo;
    var indexNo = $(".stt_box.on .working .delete").index(this);
    var memSchedNo = item.member[indexNo].memSchedNo;

    if (roleAdmin.length == 0) {
        html = handlebarsFn("#systemMessageBlock", {
            massage:"관리자권한이 필요합니다."
        });
        $("#popup_box")
            .show()
            .find(".popup_content")
            .append(html);
    } else {
        layer_confirm("정말로 삭제 하시겠습니까?", function () {
            $.ajax({
                url: "/api/schedule/deleteMemberSchedule",
                type:"POST",
                contentType: 'application/json',
                data:JSON.stringify({
                    memSchedNo:memSchedNo,
                    chnlNo:groupNo,
                    toDay:datatime.getFullYear()+"-"+numberPad((datatime.getMonth()+1),2)+"-"+numberPad(datatime.getDate(), 2)
                }),
                dataType: 'json',
                success: function (result) {
                    if (result.status == "success") {
                        var newMember = [];
                        for (var m = 0, mLen = item.member.length; m < mLen; m++) {
                            if (item.member[m].memSchedNo == memSchedNo) {
                                continue;
                            }
                            newMember.push(item.member[m]);
                        }
                        item.member = newMember;
                        timeline.changeItem(getSelectedRow(), item, false);
                        $("#confirm_popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                    }
                }
            });
        })
    }
});


/* 레이어 팝업 폼 서브밋 */
function excuSubmit(el) {
    var form = $(el).parents("#box_form");
    var data = form.serializeObject();
    var action = form.attr("action");
    var msg = (form.find("input[name=mode]").val() == "edit") ? "수정 하시겠습니까?":"등록 하시겠습니까?";
    layer_confirm(msg, function () {

        $("#popup_box .systemMsg").remove();
        $.ajax({
            url: action,
            type:"POST",
            contentType: 'application/json',
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (json) {
                if (json) {
                    if (json.status == 'success') {
                        var result = json.data;
                        if (result.schedule) {
                            if (result.schedForm.mode == "edit") {
                                var itemIndex = getSelectedRow();
                                var item = timeline.getItem(itemIndex);
                                if (result.schedForm.diffStart) {
                                    if (item.member) {
                                        $.each(item.member, function (idx, value) {
                                            value.start += (result.schedForm.diffStart);
                                        })
                                    }
                                }
                                timeline.changeItem(itemIndex, getWorkObject(result.schedule, result.schedForm.chnlNo, result.schedForm.chnlNm, item.member), false);
                            } else {
                                addSchedule(result.schedule, result.schedForm.chnlNo, result.schedForm.chnlNm);
                            }
                        } else if (result.scheduleList) { // 복제 스케줄 추가
                            if (0 < result.scheduleList.length) {
                                var dataArr = [];
                                var curArr = result.scheduleList[0].bgnTime.split("T")[0].split("-");
                                addScheduleList(result.scheduleList, result.copySchedule.chnlNo, result.copySchedule.chnlNm, curArr, dataArr);
                                timeline.addItemArr(dataArr);
                            }
                        } else if (result.memberSchedule) { // 맴버 스케줄 추가 수정
                            var ms = result.memberSchedule;
                            if (result.memSchedForm.isWork == "true") {
                                var itemIndex = getSelectedRow();
                                var item = timeline.getItem(itemIndex);
                                if (result.memSchedForm.mode == "edit") {
                                    for (var i = 0, memLen = item.member.length; i < memLen; i++) {
                                        if (item.member[i].memSchedNo == ms.memSchedNo) {
                                            ms.memNm = result.memSchedForm.memNm;
                                            item.member[i] = setMemberScheduleObj(ms);
                                        }
                                    }
                                } else {
                                    item.member = (! item.member)?[]:item.member;
                                    ms.memNm = result.memSchedForm.memNm;
                                    item.member.push(setMemberScheduleObj(ms));
                                }
                                timeline.changeItem(getSelectedRow(), item, false);
                            } else {
                                var schStartDateArr = ms.bgnTime.split("T")[0].split("-");
                                var schStartTimeArr = ms.bgnTime.split("T")[1].split(":");
                                var schEndDateArr = ms.endTime.split("T")[0].split("-");
                                var schEndTimeArr = ms.endTime.split("T")[1].split(":");
                                var noWorkType = (ms.schedGb == "R")?"rest":"meal";
                                var dataObj = {
                                    'start': new Date(schStartDateArr[0], schStartDateArr[1]-1, schStartDateArr[2], schStartTimeArr[0], schStartTimeArr[1], schStartTimeArr[2], 0),
                                    'end': new Date(schEndDateArr[0], schEndDateArr[1]-1, schEndDateArr[2], schEndTimeArr[0], schEndTimeArr[1], schEndTimeArr[2], 0),  // end is optional
                                    'content': result.memSchedForm.memNm,
                                    'group': "REST",
                                    'groupNo':0,
                                    'className': 'stt_box_etc '+noWorkType,
                                    'editable': true,
                                    'type':'floatingRange'
                                };
                                ms.memNm = result.memSchedForm.memNm;
                                dataObj.member = setMemberScheduleObj(ms);
                                if (result.memSchedForm.mode == "edit") {
                                    dataObj.member.memSchedNo = result.memSchedForm.memSchedNo;
                                    timeline.changeItem(getSelectedRow(), dataObj, false);
                                } else {
                                    dataObj.member.memSchedNo = ms.memSchedNo;
                                    timeline.addItem(dataObj);
                                }
                            }
                        } else if (result.noWorkmemberScheduleList) {
                            // 작업외 일정 복수 추가 (복제 기능)
                            var msl = result.noWorkmemberScheduleList;
                            var mslLen = msl.length;

                            var checkEl = $(".check-unit .custom-checkbox input[type=checkbox]:checked");
                            var checkLen = checkEl.length;
                            var itemObjArr = [];
                            for (var i = 0; i < mslLen; i++ ) {

                                var schStartDateArr = msl[i].bgnTime.split("T")[0].split("-");
                                var schStartTimeArr = msl[i].bgnTime.split("T")[1].split(":");
                                var schEndDateArr = msl[i].endTime.split("T")[0].split("-");
                                var schEndTimeArr = msl[i].endTime.split("T")[1].split(":");


                                var chnlTeamArr = msl[i].chnlTeamList;
                                var chnlTeamArrLen = chnlTeamArr.length;
                                var isFilter = (checkLen > 0) ? true:false;
                                checkFilter : for (var ck = 0; ck < checkLen; ck++) {
                                    for (var ct = 0; ct < chnlTeamArrLen; ct++) {
                                        if (checkEl.eq(ck).val() == chnlTeamArr[ct].chnlNo) {
                                            isFilter = false;
                                            break checkFilter;
                                        }
                                    }
                                }
                                if (isFilter) {
                                    continue;
                                }
                                var noWorkType = (msl[i].schedGb == "R")?"rest":"meal";
                                var itemObj = {
                                    'start': new Date(schStartDateArr[0], schStartDateArr[1]-1, schStartDateArr[2], schStartTimeArr[0], schStartTimeArr[1], schStartTimeArr[2], 0),
                                    'end': new Date(schEndDateArr[0], schEndDateArr[1]-1, schEndDateArr[2], schEndTimeArr[0], schEndTimeArr[1], schEndTimeArr[2], 0),  // end is optional
                                    'content': msl[i].memNm,
                                    'group': "REST",
                                    'groupNo':0,
                                    'className': 'stt_box_etc '+noWorkType,
                                    'editable': true,
                                    'type':'floatingRange'
                                };
                                itemObj.member = setMemberScheduleObj(msl[i]);
                                itemObjArr.push(itemObj)
                            }
                            timeline.addItemArr(itemObjArr);
                        }
                        $("#popup_box button").show();
                        $("#popup_box .waitTxt").text("");
                        $("#popup_box")
                            .hide()
                            .find(".popup_content")
                            .html("");
                        $("#confirm_popup_box").hide()
                            .find(".popup_content")
                            .html("");
                    } else {
                        $("#confirm_popup_box").hide()
                            .find(".popup_content")
                            .html("");
                        var code = json.code, message = json.message;
                        // 기존 시간이 수정될 시간보다 크면 안됨
                        popupSystemMessagePrint(message,"tag");
                    }
                }
                isWebposButtonExposure();
            },
            error:function(xhr, error) {
                $("#confirm_popup_box").hide();
                $(".modal-content .form-control").removeClass("is-invalid");
                var response = xhr.responseJSON;
                if (response) {
                    var errors = response.errors;
                    console.log(errors);
                    $.each(errors, function (index, value) {
                        $(".modal-content #"+value.field).addClass("is-invalid").next().text(value.reason);
                    })
                } else {
                    $("#popup_box")
                        .show()
                        .find(".popup_content")
                        .html("");
                    $("#confirm_popup_box").hide()
                        .find(".popup_content")
                        .html("");
                }
            }
        })
    })


}

function popupSystemMessagePrint(msg, type) {
    switch (type) {
        case "alert" :
            html = handlebarsFn("#systemMessageBlock", {
                massage:msg
            });
            $("#popup_box")
                .show()
                .find(".popup_content")
                .html(html);
            break;
        case "tag" :
            $("#popup_box .systemMsg").remove();
            $("#popup_box")
                .find(".popup_content")
                .find(".modal-body")
                .append("<small class='systemMsg'>"+msg+"</small>");
            break;
        default :
            break;
    }
}

/* 일정 추가 */
function addSchedule (schedule, chnlNo, chnlNm) {
    timeline.addItem(getWorkObject(schedule, chnlNo, chnlNm), false);
}
function addScheduleList (scheduleList, chnlNo, chnlNm, curArr, dataArr) {
    var sl = scheduleList;
    var slLen = sl.length;
    var msl, mslLen, mslData, schStartTimeArr, schStartDateArr, schEndTimeArr, schEndDateArr, noWorkType, workTypeClass;
    for(var s = 0; s < slLen; s++) {
        mslData = [];
        msl = sl[s].memberSchedules;
        mslLen = (msl)?msl.length:0;
        if (chnlNo > 0 && sl[s].bgnTime && sl[s].endTime) {

            workTypeClass = getWorkTypeClass(sl[s].schedType);
            // 일정 타입 01 : 생방송, 02 : 재방송, 03 : 관리자 지원, 04 : 미지원재방 , 05 : 필러
            if (sl[s].memberSchedules && sl[s]) {
                for (var ms = 0; ms < mslLen; ms++) {
                    mslData.push(setMemberScheduleObj(msl[ms]));
                }
            }
            dataArr.push(getWorkObject(sl[s], chnlNo, chnlNm, mslData));
        } else {

            for (var ms = 0; ms < mslLen; ms++) {
                schStartDateArr = msl[ms].bgnTime.split("T")[0].split("-");
                schStartTimeArr = msl[ms].bgnTime.split("T")[1].split(":");
                schEndDateArr = msl[ms].endTime.split("T")[0].split("-");
                schEndTimeArr = msl[ms].endTime.split("T")[1].split(":");

                noWorkType = (msl[ms].schedGb == "R")?"rest":"meal";
                var itemObj = {
                    'start': new Date(schStartDateArr[0], schStartDateArr[1]-1, schStartDateArr[2], schStartTimeArr[0], schStartTimeArr[1], schStartTimeArr[2], 0),
                    'end': new Date(schEndDateArr[0], schEndDateArr[1]-1, schEndDateArr[2], schEndTimeArr[0], schEndTimeArr[1], schEndTimeArr[2], 0),  // end is optional
                    'content': msl[ms].memNm,
                    'group': "REST",
                    'groupNo':0,
                    'className': 'stt_box_etc '+noWorkType,
                    'editable': true,
                    'type':'floatingRange'
                };
                itemObj.member = setMemberScheduleObj(msl[ms]);
                dataArr.push(itemObj);
            }
        }
    }

    return dataArr;
}

/* 엑셀 업로드 */
function excelUploadSubmit (el) {

    layer_confirm("등록 하시겠습니까?", function () {
        var form = $(el).parents("#box_form");
        var data = new FormData(document.getElementById("box_form"));
        var action = form.attr("action");
        $.ajax({
            url: action,
            type:"POST",
            processData: false,    // 반드시 작성
            contentType: false,
            dataType:"json",
            data:data,
            cache: false,
            success: function (json) {
                var result = json.data;
                if (result) {
                    console.log(result);
                    if (result.status == 'SUCCESS') {
                        var schedList = result.scheduleList;
                        var schedExcelUpload = result.schedExcelUpload;
                        /*var curArr = schedList[0].bgnTime.split("T")[0].split("-");
                        for (var i = 0, schedLen = schedList.length; i < schedLen; i++) {
                            addSchedule(schedList[i], schedExcelUpload.chnlNo, schedExcelUpload.chnlNm);
                        }*/

                        if (0 < schedList.length) {
                            var dataArr = [];
                            var curArr = schedList[0].bgnTime.split("T")[0].split("-");
                            addScheduleList(schedList, schedExcelUpload.chnlNo, schedExcelUpload.chnlNm, curArr, dataArr);
                            timeline.addItemArr(dataArr);
                        }

                        var html = handlebarsFn("#systemMessageBlock", {
                            massage:"완료 되었습니다."
                        });
                        var popEl = $("#popup_box")
                            .show()
                            .find(".popup_content");
                        popEl.html(html);
                        $("#confirm_popup_box").hide()
                            .find(".popup_content")
                            .html("");
                    } else {
                        var html = handlebarsFn("#systemMessageBlock", {
                            massage:result.res
                        });
                        var popEl = $("#popup_box")
                            .show()
                            .find(".popup_content").find(".invalid-feedback").html(result.res);
                        $("#confirm_popup_box").hide()
                            .find(".popup_content")
                            .html("");
                        $("#schedExcel").val("");
                        $("input[name=input-file-name]").val("");
                        console.log("에러", result.res);

                    }
                }
                isWebposButtonExposure();
            },
            error:function(xhr, error) {
                $("#popup_box button").show();
                $(".modal-content .form-control").removeClass("is-invalid");
                var response = xhr.responseJSON;
                if (response) {
                    var errors = response.errors;
                    console.log(errors);
                    $.each(errors, function (index, value) {
                        $(".modal-content #"+value.field).addClass("is-invalid").next().text(value.reason);
                    })
                } else {
                    $("#popup_box")
                        .show()
                        .find(".popup_content")
                        .html("");
                    $("#confirm_popup_box").hide()
                        .find(".popup_content")
                        .html("");
                }

            }/*,
            complete:function (xhr) {

            }*/
        })
    });
}

/* 스케줄 엑셀 다운로드 */
function scheduleExcelDown (el,type) {
    var chnlNo, chnlNm, toDay, fileNo, params;
    var parentEm = $(el).parents(".modal-content");
    chnlNo = parentEm.find("input[name='chnlNo']").val();
    chnlNm = parentEm.find("input[name='chnlNm']").val();
    toDay = parentEm.find("#today").val();
    fileNo = parentEm.find("#fileNo").val();
    params = (chnlNo)?"chnlNo="+chnlNo:"";
    params += (chnlNm)?"&chnlNm="+chnlNm:"";
    params += (toDay)?"&toDay="+toDay:"";
    params += (fileNo)?"&fileNo="+fileNo:"";
    params += (type)?"&type="+type:"";
    if (type == "history") {
        location.href = "/schedule/history_excel_down?"+params;
    } else {
        location.href = "/schedule/timeline_excel_down?"+params;
    }
}

function numberPad(n, width) {
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}

$(document).on("change", "#groupTeam", function (e) {

    var groupType = $(this).find("option:selected").val();
    var memArr = getGroupTeamMemList(groupType);
    var tag = "<option value='' >선택하세요.</option>";
    if (memArr.length != null) {
        for (var i = 0, len = memArr.length; i < len; i++) {
            tag += "<option value='"+memArr[i].memNo+"' >"+memArr[i].memNm+"</option>";
        }
    }

    $("#memNo").html(tag);
})

$(document).on("change", "select#memNo", function (e) {
    var optionEl = $(this).find("option:selected");
    var memNm = optionEl.text();
    var parentEl = $(this).parents(".modal-content");
    parentEl.find("input[name='memNm']").val("");
    parentEl.find("input[name='memNm']").val(memNm);
});

/* 그룹팀 가져오기 */
function getGroupTeamList() {
    var dataArr = [];
    $.ajax({
        url: "/api/schedule/getGroupTeam",
        type:"POST",
        contentType: 'application/json',
        data:JSON.stringify({}),
        dataType: 'json',
        async:false,
        success: function (result) {
            dataArr = result.data.groupTeams;
        },
        error:function(error) {
            console.log("에러발생!!");
            console.log(error);
        }
    });
    return dataArr;
}

/* 그룹팀 회원 가져오기 */
function getGroupTeamMemList(grpType) {
    var dataArr = [];
    $.ajax({
        url: "/api/schedule/getGroupTeamMem",
        type:"POST",
        contentType: 'application/json',
        data:JSON.stringify({grpType:grpType}),
        dataType: 'json',
        async:false,
        success: function (result) {
            if (result.data.groupTeams) {
                dataArr = result.data.groupTeams;
            }
        },
        error:function(error) {
            console.log("에러발생!!");
            console.log(error);
        }
    });
    return dataArr;
}

/* 채널팀 회원 가져오기 */
function getChnlTeamMemList(chnlNo) {
    var dataArr = [];
    $.ajax({
        url: "/api/schedule/getChnlTeamMem",
        type:"POST",
        contentType: 'application/json',
        data:JSON.stringify({chnlNo:chnlNo}),
        dataType: 'json',
        async:false,
        success: function (result) {
            if (result.data.chnlTeams) {
                dataArr = result.data.chnlTeams;
            }
        },
        error:function(error) {
            console.log("에러발생!!");
            console.log(error);
        }
    });
    return dataArr;
}

function getSchedTypeSelObj(selectedSchedType) {
    var schedTypeSelObj = [
            {name:"생방송",value:"01"},
            {name:"재방송",value:"02"},
            {name:"관리자지원",value:"03"},
            {name:"미지원재방",value:"04"}
            //{name:"필러",value:"05"}
        ];
    for (var i = 0; i < schedTypeSelObj.length; i++) {
        schedTypeSelObj[i].isSelect = false;
        if (schedTypeSelObj[i].value == selectedSchedType) {
            schedTypeSelObj[i].isSelect = true;
        }
    }
    return schedTypeSelObj;
}

function getWorkObject (schedule, chnlNo, chnlNm, mslData) {

    var curArr = schedule.bgnTime.split("T")[0].split("-");

    var schStartDateArr = schedule.bgnTime.split("T")[0].split("-");
    var schStartTimeArr = schedule.bgnTime.split("T")[1].split(":");
    var schEndDateArr = schedule.endTime.split("T")[0].split("-");
    var schEndTimeArr = schedule.endTime.split("T")[1].split(":");

    var workTypeClass = getWorkTypeClass(schedule.schedType);

    var obj = {
        'start': new Date(schStartDateArr[0], schStartDateArr[1]-1, schStartDateArr[2], schStartTimeArr[0], schStartTimeArr[1], schStartTimeArr[2], 0),
        'end': new Date(schEndDateArr[0], schEndDateArr[1]-1, schEndDateArr[2], schEndTimeArr[0], schEndTimeArr[1], schEndTimeArr[2], 0),  // end is optional
        'content': schedule.progNm,
        'group': chnlNm,
        'groupNo':chnlNo,
        'schedNo':schedule.schedNo,
        'schedType':schedule.schedType,
        'className': 'stt_box',
        'editable': true,
        'type':'floatingRange',
        'workTypeClass':workTypeClass,
        'webposButton':schedule.webposButton,
        'member':mslData
    };
    return obj;
}

// 날짜 네비게이션 하루전
$(document).on("click", ".stt_day_top .b_sttday_prev", function () {
    if (setDataTime(datatime.getDate() - 1)) {
        printDate();
        drawTimeLine();
    }
})

// 날짜 네비게이션 내일
$(document).on("click", ".stt_day_top .b_sttday_next", function () {
    if (setDataTime(datatime.getDate() + 1)) {
        printDate();
        drawTimeLine();
    }
})

// 날짜 네비게이션 일주일전
$(document).on("click", ".stt_day_top .b_sttday_start", function () {
    if (setDataTime(datatime.getDate() - 7)) {
        printDate();
        drawTimeLine();
    }
})

// 날짜 네비게이션 일주일후
$(document).on("click", ".stt_day_top .b_sttday_end", function () {
    if (setDataTime(datatime.getDate() + 7)) {
        printDate();
        drawTimeLine();
    }
})

$(document).on("click", "#moveToDay", function (e) {
    e.preventDefault();
    datatime = new Date();
    printDate()
    drawTimeLine();
})

function setDataTime(setDate) {
    // 관리자가 아니라면 최대 일주일 전까지만 조회가능하다.
    var roleAdmin = $("#roleAdmin");
    if (roleAdmin.length == 0) {
        var maxDayCnt = 7;
        var tmpDate = new Date(datatime.getTime());
        tmpDate.setDate(setDate);
        var diffCnt, diffDate, sDataTime = new Date(), eDataTime = new Date(tmpDate.getFullYear(),tmpDate.getMonth(),tmpDate.getDate());
        sDataTime.setHours(eDataTime.getHours());
        sDataTime.setMinutes(eDataTime.getMinutes());
        sDataTime.setSeconds(eDataTime.getMinutes());
        sDataTime.setMilliseconds(eDataTime.getMilliseconds());
        diffDate = (sDataTime.getTime() - eDataTime.getTime());
        diffCnt = Math.ceil(diffDate / (24 * 60 * 60 * 1000));
        if (diffCnt > maxDayCnt) {
            alert(maxDayCnt + "일전 데이터는 조회할수 없습니다.");
            return false;
        } else {
            datatime.setDate(setDate);
            return true;
        }
    } else {
        datatime.setDate(setDate);
        return true;
    }
}

// 날짜 출력
function printDate() {
    nextDayDate = new Date(datatime.valueOf() + (24*60*60*1000));
    var toDayStr = datatime.getFullYear()+"-"+(datatime.getMonth()+1)+"-"+datatime.getDate();
    var toWeekStr = weekArr[datatime.getDay()];
    var nextDayStr = nextDayDate.getFullYear()+"-"+(nextDayDate.getMonth()+1)+"-"+nextDayDate.getDate();
    var nextWeekStr = weekArr[nextDayDate.getDay()];
    var isDiff = false, diffCnt, diffDate, diffCntStr, sDataTime = new Date(), eDataTime = new Date(datatime.getFullYear(),datatime.getMonth(),datatime.getDate());
    sDataTime.setHours(eDataTime.getHours());
    sDataTime.setMinutes(eDataTime.getMinutes());
    sDataTime.setSeconds(eDataTime.getMinutes());
    sDataTime.setMilliseconds(eDataTime.getMilliseconds());

    if (sDataTime > eDataTime) {
        diffDate = Math.abs(sDataTime.getTime() - eDataTime.getTime());
        diffCntStr = "전";
    } else if (eDataTime > sDataTime) {
        diffDate = Math.abs(eDataTime.getTime() - sDataTime.getTime());
        diffCntStr = "후";
    } else {
        diffDate = 0;
    }
    diffCnt = Math.ceil(diffDate / (24 * 60 * 60 * 1000));
    diffCntStr =diffCnt + "일 " + diffCntStr;
    if (diffCnt != 0) {
        isDiff = true;
    }
    var html = handlebarsFn("#dateNaviBlock", {
        toDayStr:toDayStr,
        toWeekStr:toWeekStr,
        nextDayStr:nextDayStr,
        nextWeekStr:nextWeekStr,
        isDiff:isDiff,
        diffCntStr:diffCntStr
    });
    $("#dateNavi").html(html);
}

function setMemberScheduleObj (obj) {
    var memSchedObj = {};
    var noWorkType = (obj.schedGb == "R")?"rest":"meal";
    memSchedObj.memSchedNo = (obj.memSchedNo)?obj.memSchedNo:"";
    memSchedObj.name = (obj.memNm)?obj.memNm:"";
    memSchedObj.start = (obj.startAmt)?Math.floor(obj.startAmt):"";
    memSchedObj.workTime = (obj.workAmt)?Math.floor(obj.workAmt):"";
    memSchedObj.mno = (obj.memNo)?obj.memNo:"";
    memSchedObj.mName = (obj.memId)?obj.memId:"";
    memSchedObj.profImgData = (obj.profImgData)?obj.profImgData:"";
    memSchedObj.isWebpos = (obj.isWebpos)?obj.isWebpos:false;
    memSchedObj.class = (obj.schedGb)?noWorkType:"";
    if (obj.groupTeam) {
        memSchedObj.groupTeamType = (obj.groupTeam.grpType)?obj.groupTeam.grpType:"";
    }
    if (obj.chnlTeamList) {
        memSchedObj.chnlTeamList = (obj.chnlTeamList)?obj.chnlTeamList:[];
    }
    memSchedObj.thumbSysFileNm = (obj.thumbSysFileNm)?obj.thumbSysFileNm:false;
    return memSchedObj;
}