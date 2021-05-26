/*
*
* $.fn.datePickerCommon
*
* 옵션 설명
* id : 타겟 엘리먼트
* type : D:날짜만 있는거, DT: 날자랑 시간이랑 나오는거
* useSinglePicker : 싱글 데이트 픽커 사용여부 true or false
* startId : 시작시간 바인딩 엘리먼트
* endId : 종료시간 바인딩 엘리먼트
* useRanges : 좌측 범위 버튼 사용여부 true or false
* minDate : 최소선택날짜
* maxDate : 최대선택날짜
* 2020-06-29 작성됨
* */

(function($){
    $.fn.datePickerCommon = function (pOptions) {
        var datatime = new Date()
            ,year = datatime.getFullYear()
            ,month = datatime.getMonth() + 1
            ,day = datatime.getDate();
        var settings = {};
        var options = $.extend({
            "type":"DT", //
            "useSinglePicker":false,
            "startId":"",
            "endId":"",
            "useRanges" : true,
            "minDate":"",
            "maxDate":"",
            "startDate":"",
            "endDate":""
        }, pOptions);

        settings.startDate = (options.startDate == "")?year+"-"+month+"-"+day+" 08:00":options.startDate;
        settings.endDate = (options.endDate == "")? year+"-"+month+"-"+day+" 23:55":options.endDate;

        //settings.minDate = options.minDate;
        //settings.maxDate = options.maxDate;

        settings.locale = {
            "customRangeLabel": "직접입력",
            "applyLabel": "확인",
            "cancelLabel": "취소",
            "daysOfWeek": ["일","월","화","수","목","금","토"],
            "monthNames": ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
        };
        if (options.useRanges) {
            settings.ranges = {
                '오늘': [moment(), moment()],
                '어제': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '일주일 전': [moment().subtract(6, 'days'), moment()],
                '30일 전': [moment().subtract(29, 'days'), moment()],
                '이번 달': [moment().startOf('month'), moment().endOf('month')],
                '전 달': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            }
        }
        settings.locale.format = (options.type == "DT")?"YYYY-MM-DD HH:mm":"YYYY-MM-DD";

        settings.opens = "center";
        if (options.type == "DT") {
            settings.timePicker = true;
            settings.timePicker24Hour = true;
        }
        if (options.useSinglePicker) {
            settings.singleDatePicker = true;
        }

        $(options.id).daterangepicker(settings, function(start, end, label) {
            if (options.startId != "") {
                $(options.startId).val(start.format(settings.locale.format));
            }
            if (options.startId != "") {
                $(options.endId).val(end.format(settings.locale.format));
            }
        });
    }
}(jQuery))
