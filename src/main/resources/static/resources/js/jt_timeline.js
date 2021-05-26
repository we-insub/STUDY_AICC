/**
 * @file jt_timeline.js
 *
 * @author  김지태
 * @date    2020-08-05
 */

links = {};

// Internet Explorer 8 and older does not support Array.forEach,
// so we define it here in that case
// https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/Array/forEach
if (!Array.prototype.forEach) {
    Array.prototype.forEach = function(fn, scope) {
        for(var i = 0, len = this.length; i < len; ++i) {
            fn.call(scope || this, this[i], i, this);
        }
    }
}

// 생성자
links.JtTimeline = function(container, options) {
    if (!container) {
        // this call was probably only for inheritance, no constructor-code is required
        return;
    }

    // create variables and set default values
    this.dom = {};
    this.groups = [];
    this.groupIndexes = {};
    this.groupMember = []; //예 [{'groupIndex':0,'groupName':'SBS','member':[{'name':'김철수', 'mNo':1, 'isOn':true},{'name':'김지태', 'mNo':2, 'isOn':false}]]
    this.items = [];
    this.selection = undefined; // stores index and item which is currently selected
    this.dom.container = container;
    this.toDayMs = new Date(options.min.getFullYear(), options.min.getMonth(), options.min.getDate(), 0, 0, 0, 0);
    this.options = {
        'width': "100%",
        'height': "auto",
        'minHeight': 0,        // minimal height in pixels
        'groupMinHeight': 0,
        'dragAreaWidth': 10,   // pixels
        'min': undefined,
        'max': undefined,
        'selectable': true,
        'unselectable': true,
        'toDayMs': undefined
    };

    this.clientTimeOffset = 0;    // difference between client time and the time
    // set via Timeline.setCurrentTime()
    var dom = this.dom;

    this.setOptions(options);

    // initialize data
    this.data = [];
    this.firstDraw = true;
    this.isMouseMove = false;
    this.eventParams = {};
    this.selectItemIdx = undefined;
    this.renderQueue = {
        show: [],   // Items made visible but not yet added to DOM
        hide: [],   // Items currently visible but not yet removed from DOM
        update: []  // Items with changed data but not yet adjusted DOM
    };
    this.week = ["월","화","수","목","금","토","일"];
    this.memNo = 0;
};


// 오브잭트 그리기
links.JtTimeline.prototype.draw = function(data, options) {
    if (options) {
        console.log("WARNING: Passing options in draw() is deprecated. Pass options to the constructur or use setOptions() instead!");
        this.setOptions(options);
    }

    // read the data
    this.setData(data);
    this.repaintCurrentTime();
    this.firstDraw = false;
};


// getAxisDom 사간표 축 레이어 돔정보 가져오기
links.JtTimeline.prototype.getAxisDom = function() {
    return this.dom.axis.frame;
}

// 웹포스 작업자 여부
links.JtTimeline.prototype.setWebpos = function(arr) {
    var items = this.items;
    arrfor : for (var i = 0; i < arr.length; i++) {
        itemsfor : for (var i2 = 0; i2 < items.length; i2++) {
            if (items[i2].schedNo == arr[i].schedNo) {
                if (Array.isArray(items[i2].member)) {
                    memberfor : for (var m = 0; m < items[i2].member.length; m++) {
                        if (items[i2].member[m].mName == arr[i].memId) {
                            items[i2].dom.find(".line").eq(m).find("i.on_off").removeClass("on");
                            items[i2].dom.find(".line").eq(m).find("i.on_off").removeClass("off");
                            items[i2].member[m].isWebpos = arr[i].isWebpos;
                            if (arr[i].isWebpos) {
                                items[i2].dom.find(".line").eq(m).find("i.on_off").addClass("on");
                            } else {
                                items[i2].dom.find(".line").eq(m).find("i.on_off").addClass("off");
                            }
                            break itemsfor;
                        }
                    }
                }
            }
        }
    }
}

// 작업자 상세 정보 보기
links.JtTimeline.prototype.showDetail = function(arr) {
    var dom = this.dom;
    var group = this.getGroupObj(arr.groupNo);
    var groupIndex = this.groupIndexes[arr.groupNo];
    $(dom.itemArea).find(".stt_total_list").remove();
    var top = 35;

    for (var i = 0; i < groupIndex; i++) {
        top += (35 + this.groups[i].height);
    }

    var left = $(dom.itemArea).scrollLeft();
    var html = handlebarsFn("#detailsBlock", {
        top:top,
        left:left,
        groupName:arr.groupName,
        timeRange:datatime.getFullYear()+"-"+numberPad(datatime.getMonth()+1, 2)+"-"+numberPad(datatime.getDate(), 2)+"("+this.week[datatime.getDay()]+")"+" 08:00 ~ " +
            nextDayDate.getFullYear()+"-"+numberPad(nextDayDate.getMonth()+1, 2)+"-"+numberPad(nextDayDate.getDate(), 2)+"("+this.week[nextDayDate.getDay()]+")"+" 08:00",
        member:arr.member,

    });
    var itemAreaScroll = function (e) {
        $(".stt_total_list").css({left:$(e.target).scrollLeft()+"px"});
    }
    $(dom.itemArea).append($(html));
    $(dom.itemArea).on("scroll", itemAreaScroll);
    $(dom.itemArea).find(".stt_total_list .close").on("click", function (e) {
        $(dom.itemArea).find(".stt_total_list").remove();
        $(dom.itemArea).off("scroll", itemAreaScroll);
    })
    $(dom.itemArea).find(".stt_total_list button.btn-secondary").on("click", function (e) {
        location.href = "/dashboard/work_detail";
    })
}



// 웹포스 버튼 노출 여부
links.JtTimeline.prototype.setWebposButtonDisplay = function(arr) {
    var items = this.items;
    var item;
    itemsfor : for (var i = 0; i < items.length; i++) {
        item = items[i];
        if (item.dom.find(".webpos").hasClass("pOpen")) {
            continue;
        }
        item.dom.find(".webpos").css({"display":"none"});
        arrfor : for (var i2 = 0; i2 < arr.length; i2++ ) {
            if (arr[i2].schedNo == item.schedNo) {
                // 아이템의 돔을 직접 제어
                var webposButton = arr[i2].webposButton;
                if (arr[i2].webposButton) {
                    item.dom.find(".webpos").css({"display":""});
                    break arrfor;
                }
            }
        }
        /*this.updateData(i, items[i]);*/
    }
    /*this.render({
        animate: false
    });*/
}

/**
 * Set options for the timeline.
 * Timeline must be redrawn afterwards
 * @param {Object} options A name/value map containing settings for the
 *                                 timeline. Optional.
 */
// 옵션 셋팅하기
links.JtTimeline.prototype.setOptions = function(options) {
    if (options) {
        // retrieve parameter values
        for (var i in options) {
            if (options.hasOwnProperty(i)) {
                this.options[i] = options[i];
            }
        }
    }
    // validate options
    // this.options.autoHeight = (this.options.height === "auto");
};

/**
 * Get options for the timeline.
 *
 * @return the options object
 */
links.JtTimeline.prototype.getOptions = function() {
    return this.options;
};

/**
 * Set data for the timeline
 * @param {google.visualization.DataTable | Array} data
 */
// 오브 잭트 세팅

links.JtTimeline.prototype.setData = function(data) {
    // unselect any previously selected item
    // this.unselectItem();

    if (!data) {
        data = [];
    }

    this.clearItems();
    this.data = data;
    var items = this.items;
    //console.log(this);
    //this.deleteGroups();

    if (links.JtTimeline.isArray(data)) {
        // 받은 데이터를 아이템에 세팅한다.
        for (var row = 0, rows = data.length; row < rows; row++) {
            var itemData = data[row];
            var item = this.createItem(itemData);
             items.push(item);
        }
    }
    else {
        throw "Unknown data type. DataTable or Array expected.";
    }

    this.render();
};

/**
 * Return the original data table.
 */
links.JtTimeline.prototype.getData = function  () {
    return this.data;
};

links.JtTimeline.prototype.deleteGroups = function () {
    this.groups = [];
    this.groupIndexes = {};
};

links.JtTimeline.prototype.clearItems = function() {
    this.items = [];
};

links.JtTimeline.prototype.createItem = function(itemData) {

    // clone 새로운 아이템 객체를 만든다.

    var data = links.JtTimeline.clone(itemData);
    // 그룹 세팅함
    data.group = this.getGroup(itemData);

    return new links.JtTimeline.Item(data);
};

links.JtTimeline.clone = function (object) {
    var clone = {};
    for (var prop in object) {
        if (object.hasOwnProperty(prop)) {
            clone[prop] = object[prop];
        }
    }
    return clone;
};

links.JtTimeline.isArray = function (obj) {
    if (obj instanceof Array) {
        return true;
    }
    return (Object.prototype.toString.call(obj) === '[object Array]');
};

links.JtTimeline.prototype.setGroup = function (groupObjArr) {
    var groups = this.groups,
        groupIndexes = this.groupIndexes,
        groupObj;

    for (var g = 0; g < groupObjArr.length; g++) {
        groupObj = groupObjArr[g];
        groups.push(groupObj);
        groupIndexes[groupObj.groupNo] = g;
    }
}

links.JtTimeline.prototype.getGroup = function (itemData) {
    var groups = this.groups,
        groupIndexes = this.groupIndexes,
        groupObj = undefined,
        groupMemberArr = undefined,
        groupMember = undefined,
        hasMem = false
    ;

    var groupIndex = groupIndexes[itemData.groupNo];
    if (groupIndex == undefined && itemData.groupNo != undefined) { // not null or undefined 새로운 그룹 이름, 그룹 생성
        groupObj = {
            'content': itemData.group,
            'groupNo': itemData.groupNo
        };
        groups.push(groupObj);

        for (var i = 0, iMax = groups.length; i < iMax; i++) {
            groupIndexes[groups[i].groupNo] = i;
        }
    }
    else {
        groupObj = groups[groupIndex]; // 기존의 그룹 인덱스를 리턴함.
    }
    // 그룹에 새로운 작업자가 있으면 추가 시킴
    /*if (itemData.member) {
        if (! groupObj.member) {
            groupObj.member = [];
        }
        groupMemberArr = groupObj.member;
        for (var m = 0; m < itemData.member.length; m++) {
            hasMem = false;
            for (var i = 0, mMax = groupMemberArr.length; i < mMax; i++) {
                groupMember = groupMemberArr[i];
                hasMem = (groupMember.mno == itemData.member[m].mno) ? true : false;
                if (hasMem) break;
            }
            if (! hasMem) {
                groupMemberArr.push({
                    'name':itemData.member[m].name,
                    'mno':itemData.member[m].mno
                })
            }
        }
    }*/
    return groupObj;
};

links.JtTimeline.prototype.addItem = function(item) {
    this.items.push(this.createItem(item));
    this.render();
}

links.JtTimeline.prototype.addItemArr = function(itemArr) {

    for (var i = 0; i < itemArr.length; i++) {
        this.items.push(this.createItem(itemArr[i]));
    }
    this.render();
}

links.JtTimeline.prototype.changeItem = function (index, itemData, preventRender) {
    this.items[index].dom.remove();
    var oldItem = this.items[index];
    if (!oldItem) {
        throw "Cannot change item, index out of range";
    }

    // replace item, merge the changes
    var newItem = this.createItem({
        'start':   itemData.hasOwnProperty('start') ?   itemData.start :   oldItem.start,
        'end':     itemData.hasOwnProperty('end') ?     itemData.end :     oldItem.end,
        'content': itemData.hasOwnProperty('content') ? itemData.content : oldItem.content,
        'group':   itemData.hasOwnProperty('group') ?   itemData.group :   oldItem.group.content,
        'groupNo':   itemData.hasOwnProperty('groupNo') ?   itemData.groupNo :   oldItem.groupNo,
        'schedNo': itemData.hasOwnProperty('schedNo') ? itemData.schedNo : oldItem.schedNo,
        'schedType': itemData.hasOwnProperty('schedType') ? itemData.schedType : oldItem.schedType,
        'workTypeClass': itemData.hasOwnProperty('workTypeClass') ? itemData.workTypeClass : oldItem.workTypeClass,
        'className': itemData.hasOwnProperty('className') ? itemData.className : oldItem.className,
        'webposButton': itemData.hasOwnProperty('webposButton')? itemData.webposButton:oldItem.webposButton,
        'editable':  itemData.hasOwnProperty('editable') ?  itemData.editable :  oldItem.editable,
        'type':      itemData.hasOwnProperty('type') ?      itemData.type :      oldItem.type,
        'member':itemData.hasOwnProperty('member') ?      itemData.member :      oldItem.member,
    });
    this.items[index] = newItem;
    this.render();

};

links.JtTimeline.prototype.deleteItem = function(index, preventRender) {
    this.items[index].dom.remove();
    var item = this.items.splice(index, 1)[0];
    this.render();
};

links.JtTimeline.prototype.render = function() {

    // 그룹별 아이템 순서및 적재
    this.setItemSortStack();
    // 그룹 높이 계산
    this.setGroupsHeight();
    // 아이템 위치 계산
    this.setItemPosition();
    // this.stackItems(animate);
    // this.recalcItems();
    var needsReflow = this.repaint();

    //this.setVisibleChartRangeNow();

    /*$(".stt_table_cont.hfull.dragscroll > div").draggable({
        axis: "x",
        grid:[50,0],
        create: function( event, ui ) {
            console.log("create");
        },
        drag: function (event, ui) {
            console.log("drag");
        },
        start: function (event, ui) {
            dragscrollFnRemove();
            console.log("start");
            $(this).draggable( "enable" );
        },
        stop: function (event, ui) {
            layer_confirm("변경 하시겠습니까?", function () {

            })
            if (confirm("변경 하시겠습니까?")) {
                links.JtTimeline.moveItem({moveLeft: ui.position.left - ui.originalPosition.left});
            } else {
                // 되돌리기
                ui.helper.context.style.left = ui.originalPosition.left+"px";
            }
            dragscrollFn();
            $(this).draggable( "disable" );
        },
        revertDuration: 0,
        zIndex: 300,
        distance: 50,
        scroll: false,
    });
    $(".stt_table_cont.hfull.dragscroll > div").draggable( "disable" );*/

};

links.JtTimeline.moveItem = function (obj) {
    var idx = timeline.getSelectedRow();
    //var item = timeline.getItem(idx);
}

links.JtTimeline.prototype.setItemSortStack = function() {
    var groups = this.groups;
    var items = this.items;

    // 일정 순서 정렬
    items.sort(links.JtTimeline.compareFunction);
    var itemsByGroup = this.getItemsByGroup(items);
    var item, item2, group, itemStackLevel = 0, groupStackLevel = 0;
    this.itemsByGroup = itemsByGroup;
    var groupItems, isCollision, collisionNo = 0;
    var stackLevelObj = {};

    // 일정 작업자 순서 정렬
    for (var i = 0; i < items.length; i++) {
        if (Array.isArray(items[i].member)) {
            items[i].member.sort(function (a, b) {
                if (a.start < b.start) {
                    return -1;
                }
                if (a.start > b.start) {
                    return 1;
                }
                if (a.start == b.start) {
                    if (a.workTime > b.workTime) {
                        return -1;
                    }
                    if (a.workTime < b.workTime) {
                        return 1;
                    }
                }
                return 0;
            })
        }
    }

    // 채널 순서 정렬
    groups.sort(function (a, b) {
        if (a.groupNo < b.groupNo) {
            return -1;
        }
        if (a.groupNo > b.groupNo) {
            return 1;
        }
        return 0;
    });
    for (var i = 0, iMax = groups.length; i < iMax; i++) {
        this.groupIndexes[groups[i].groupNo] = i;
    }
    for (var g = 0; g < groups.length; g++) {
        group = groups[g];
        groupItems = this.itemsByGroup[group.content];
        groupStackLevel = 0;
        for (var i = 0; i < groupItems.length; i++) {
            item = groupItems[i];
            itemStackLevel = 0;
            isCollision = false;
            for (var i2 = 0; i2 < i; i2++) {
                item2 = groupItems[i2];
                if (item.start < item2.end ) {
                    itemStackLevel++;
                }
            }
            itemStack : for (var sl = 0; sl < itemStackLevel; sl++) {
                if (stackLevelObj) {
                    if ((stackLevelObj[sl].end <= item.start)) {
                        break itemStack;
                    }
                }
            }
            stackLevelObj[sl] = item;
            item.stackLevel = sl;
            if (groupStackLevel < itemStackLevel) {
                groupStackLevel = itemStackLevel;
            }
        }
        group.stackLevel = groupStackLevel
    }
};

links.JtTimeline.prototype.setGroupsHeight = function() {
    var groups = this.groups, group, height, itemHeight;
    var groupIndexs = this.groupIndexes;
    var items = this.items;

    for (var g = 0; g < groups.length; g++) {
        group = groups[g];
        if (group.groupNo == 0) {
            itemHeight = 26;
            height = 99;
        } else {
            itemHeight = 290;
            height = 259;
        }
        group.height = height + (itemHeight * (group.stackLevel));
    }
};



links.JtTimeline.prototype.setItemPosition = function() {
    var groups = this.groups;
    var items = this.items;
    var group, groupItems, item;
    var toDayMs = this.toDayMs;
    var top, left, groupHeight = 0;
    var startMs,endMs,resMs,resMi,programTime=0,workWidth=0,startWidth=0;
    for (var g = 0; g < groups.length; g++) {
        group = groups[g];
        groupItems = this.itemsByGroup[group.content];
        for (var i = 0; i < groupItems.length; i++) {
            item = groupItems[i];
            // 시작 시간
            startMs = item.start.getTime();
            endMs = toDayMs.getTime();
            resMs = startMs - endMs;
            resMi = links.JtTimeline.msToMinutes(resMs);
            left = resMi * 10;
            left = left - 4800;
            item.left = left;
            // 작업 분량
            item.width = links.JtTimeline.msToMinutes(item.end.getTime() - item.start.getTime()) * 10;
            // top 좌표
            if(group.groupNo == 0) {
                // 작업외 일정의 베이스 top = 35, itemHeight = 26
                item.top = (26 * item.stackLevel) + 35; // (아이템 높이 * 스텍 레벨) + 베이스 높이
            } else {
                item.top = (g * 1) + (290 * item.stackLevel) + groupHeight + (g * 34) + 35; // (아이템 높이 * 스텍 레벨) 이전 그룹 총 높이 + (그룹 버튼 영역 * 이전 그룹 개수) + 베이스 높이

                // 작업자 배정 시간
                if (item.member) {
                    programTime = (item.end.getTime() - item.start.getTime()) / 60000;
                    for (var m = 0; m < item.member.length; m++) {
                        workWidth = (100 / (programTime / 5)) * (item.member[m].workTime / 5);
                        startWidth = (item.member[m].start > 0)?(100 / (programTime / 5)) * (item.member[m].start / 5):item.member[m].start;
                        item.member[m].workWidth = workWidth;
                        item.member[m].startWidth = startWidth;
                    }
                }
            }
        }
        groupHeight += group.height;
    }
}

links.JtTimeline.msToMinutes = function (ms) {
    return (ms / (1000 * 60)).toFixed();
}

links.JtTimeline.compareFunction = function (a, b) {
    if (a.group.groupNo < b.group.groupNo) {
        return -1;
    }
    if (a.group.groupNo > b.group.groupNo) {
        return 1;
    }
    if (a.group.groupNo == b.group.groupNo) {
        if (a.start < b.start) {
            return -1;
        }
        if (a.start > b.start) {
            return 1;
        }
    }

    return 0;
}

links.JtTimeline.prototype.repaintCurrentTime = function() {
    var toDayMs = this.toDayMs;
    var currentDate = new Date();
    var dom = this.dom;
    var currentMs = currentDate.getTime() - toDayMs.getTime();
    var currentMi =  (currentMs / (1000 * 60));
    var left = (currentMi * 10) - 4800;
    var currentTime = document.createElement("DIV");

    currentTime.className = "timeline-bar";
    currentTime.style.position = "absolute";
    currentTime.style.top = "0px";
    currentTime.style.left = left+"px";
    currentTime.style.height = "100%";
    currentTime.style.zIndex = "110";
    currentTime.style.backgroundColor = "#ff7f6e";
    currentTime.style.boxSizing = "border-box";
    currentTime.style.width = "2px";
    dom.currentTime = currentTime;
    dom.itemArea.appendChild(dom.currentTime);
    var intervalFn =  function () {
        currentDate = new Date();
        currentMs = currentDate.getTime() - toDayMs.getTime();
        currentMi = (currentMs / (1000 * 60));
        left = (currentMi * 10) - 4800;
        dom.currentTime.style.left = left+"px";
        if (currentMi > 1440 || currentMi < 0) {
            clearInterval(setIntervalId);
            dom.currentTime.style.display = "none";
        }

    }
    var setIntervalId = setInterval(intervalFn, 3000);

}

links.JtTimeline.prototype.setVisibleChartRangeNow = function () {
    var dom = this.dom;
    var toDayMs = this.toDayMs;
    var totalWidth = dom.itemArea.offsetWidth;
    var currentDate = new Date();
    var currentMs = currentDate.getTime() - toDayMs.getTime();
    var currentMi = (currentMs / (1000 * 60));
    var left = ((currentMi * 10) - (totalWidth/2)) - 4800;
    //console.log(left);
    //dom.itemArea.scrollTo(left, dom.itemArea.scrollTop);
    if (left > 0 && left < (14400)- (totalWidth/2)) {
        $(dom.itemArea).scroll().scroll
        $(dom.itemArea).scroll().scrollLeft(left);
    }
}

links.JtTimeline.prototype.repaint = function(options) {

    var groups = this.groups;
    var items = this.items;
    var group, groupItems, item, html, dom = this.dom,
        titEl, label, timeTable, chnlBox, backTable,
        itemBox, chnlTeamList;

    // main frame
    if (! dom.frame) {
        dom.frame = document.createElement("DIV");
        dom.frame.className = "timeline_content";
        $(dom.container).html($(dom.frame));
    }
    if (! dom.groupArea) {
        dom.groupArea = document.createElement("DIV");
        dom.groupArea.className = "stt_tquick";
            titEl = document.createElement("SPAN");
            titEl.className = "sttq_tit";
            titEl.innerText = "스케줄";
        dom.groupArea.appendChild(titEl);
        dom.frame.appendChild(dom.groupArea);
        dom.chnlArea = document.createElement("UL");
        dom.chnlArea.className = "stt_tquick_channel";
        dom.groupArea.appendChild(dom.chnlArea);
    }

    // 그룹 그리기
    for (var g = 0; g < groups.length; g++) {
        group = groups[g];
        label =  document.createElement("DIV");
        if (group.groupNo == 0) {
            html = handlebarsFn("#nonWorkSch", {
                height:group.height
            });
        } else {
            html = handlebarsFn("#WorkSch", {
                height:group.height,
                groupName:group.content,
                groupMember:group.member,
                groupNo:group.groupNo
            });
        }
        if (! group.dom) {
            group.dom = $(html);
        } else {
            group.dom.remove();
            group.dom = $(html);
        }
        $(dom.chnlArea).append(group.dom);
    }

    // 아이템 영역 그리기
    var timeArr = [], ttObj;
    var ttDate = new Date(this.toDayMs.getFullYear(), this.toDayMs.getMonth(), this.toDayMs.getDate(), 8, 0, 0, 0);
    for (var t = 0; t < 288; t++) {
        ttObj = {};
        ttObj.timeStr = numberPad(ttDate.getHours(), 2)+":"+numberPad(ttDate.getMinutes(), 2);
        timeArr.push(ttObj);
        ttDate.setMinutes(ttDate.getMinutes()+5);
    }
    if (! dom.itemArea) {
        dom.itemArea = document.createElement("DIV");
        dom.itemArea.className = "stt_table_cont hfull dragscroll";
        dom.frame.appendChild(dom.itemArea);
        html = handlebarsFn("#timeTableBlock", {
            timeArr: timeArr
        });

        // 스크롤 이벤트
        //$(dom.itemArea).on()
        /*dom.backLayer = document.createElement("DIV");
        dom.backLayer.className = "backLayer";
        dom.backLayer.style.display = "block";
        dom.backLayer.style.height = "100%";
        dom.backLayer.style.width = "100%";
        dom.backLayer.style.position = "absolute";
        dom.backLayer.style.top = "0px";
        dom.backLayer.style.left = "0px";
        dom.backLayer.style.zIndex = "101";
        dom.backLayer.style.backgroundColor = "#333333";
        dom.backLayer.style.opacity = "0.5";*/
        $(dom.itemArea).append($(html));
        //$(dom.itemArea).append($(dom.backLayer));
    }


   /* var me = this;
    var params = this.eventParams;
    if (! params.onMouseDownBackLine) {
        params.onMouseDownBackLine = function (e) {me.onMouseDownBackLine(e)};
        dom.itemArea.addEventListener("mousedown", params.onMouseDownBackLine);
    }*/


    // 그룹별 아이템 그리기
    var idxNo = 0;
    var itemsByGroup = this.itemsByGroup, groupClass;
    for (var g = 0; g < groups.length; g++) {
        group = groups[g];
        groupItems = itemsByGroup[group.content];
        if (group.groupNo == 0) {
            groupClass = "stt_table etc";
        } else {
            groupClass = "stt_table";
        }
        html = handlebarsFn("#backgroundLine", {
            timeArr:timeArr,
            height:group.height + 34,
            groupClass:groupClass
        });

        if (! group.backTableDom) {
            group.backTableDom = $(html);

        } else {
            group.backTableDom.remove();
            group.backTableDom = $(html);
        }
        $(dom.itemArea).append(group.backTableDom);

        for (var i = 0; i < groupItems.length; i++) {
            item = groupItems[i];
            var programTime = (item.end.getTime() - item.start.getTime()) / 60000;
            if (group.groupNo == 0) {
                var restTxt = (item.className.indexOf("rest") > -1)?"휴식시간":"식사시간";
                var isInfo = (programTime <= 15) ? true:false;
                html = handlebarsFn("#nonWorkBlock", {
                    content: item.content,
                    class: item.className,
                    programTime:programTime,
                    restTxt:restTxt,
                    left:item.left,
                    width:item.width,
                    top:item.top,
                    thumbSysFileNm:item.member.thumbSysFileNm,
                    isInfo:isInfo
                });
            } else {
                if (item.member) {
                    for (var m = 0, mLen = item.member.length; m < mLen; m++) {
                        // 작업자가 4이상이고 배정시간이 5분이면 작업자정보를 보이지 않음
                        item.member[m].isTextBox = true;
                        if (item.member.length >= 4 && item.member[m].workTime == 5) {
                            item.member[m].isTextBox = false;
                        }
                        // 채널팀 속해있는지 여부 검사
                        item.member[m].isNotTeam = true;
                        chnlTeamList = item.member[m].chnlTeamList;
                        if (chnlTeamList) {
                            chnlTeamListFor : for (var ct = 0; ct < chnlTeamList.length; ct++) {
                                if (chnlTeamList[ct].chnlNo == group.groupNo) {
                                    item.member[m].isNotTeam = false;
                                    break chnlTeamListFor;
                                }
                            }
                        }

                    }
                }

                html = handlebarsFn("#WorkBlock", {
                    members:item.member,
                    content:item.content,
                    class:item.className,
                    programTime:programTime,
                    workType:item.workTypeClass,
                    schedNo:item.schedNo,
                    progNm:item.content,
                    schedType:item.schedType,
                    webposButton:! item.webposButton,
                    left:item.left,
                    width:item.width,
                    top:item.top
                });
            }

            if (! item.dom) {
                item.dom = $(html);
            } else {
                item.dom.remove();
                item.dom = $(html);
                //item.dom.css({"top":item.top+"px", "left":item.left+"px"})
            }
            item.dom.data("idxNo", idxNo);
            $(dom.itemArea).append(item.dom);
            idxNo++;
        }

    }
    // tooltip
    $('[data-toggle="tooltip"]').tooltip();
};


links.JtTimeline.prototype.updateDOM = function () {
    var dom = this.dom;
};


links.JtTimeline.prototype.onMouseDownBackLine = function (e) {
    e = e || window.event;
    var me = this;
    var params = me.eventParams;
    console.log(e);
    if (! params.onMouseMoveBackLine) {
        params.onMouseMoveBackLine = function (e) {me.onMouseMoveBackLine(e);};
        me.dom.itemArea.addEventListener("mousemove", params.onMouseMoveBackLine);
    }
    if (! params.onMouseUpBackLine) {
        params.onMouseUpBackLine = function (e) {me.onMouseUpBackLine(e);};
        me.dom.itemArea.addEventListener("mouseup", params.onMouseUpBackLine);
    }
}

links.JtTimeline.prototype.onMouseUpBackLine = function (e) {
    e = e || window.event;
    var me = this;
    var params = me.eventParams;
    if (params.onMouseMoveBackLine) {
        me.dom.itemArea.removeEventListener("mousemove", params.onMouseMoveBackLine);
        delete params.onMouseMoveBackLine;
    }
    if (params.onMouseUpBackLine) {
        me.dom.itemArea.removeEventListener("mouseup", params.onMouseUpBackLine);
        delete params.onMouseUpBackLine;
    }
}

links.JtTimeline.prototype.onMouseMoveBackLine = function (e) {
    e = e || window.event;
    console.log(e);
}

links.JtTimeline.prototype.onMouseLeaveBackLine = function (e) {
    e = e || window.event;
    this.isMouseMove = false;
    console.log(e);
}

links.JtTimeline.prototype.getItemsByGroup = function(items) {
    var itemsByGroup = {};
    var groups = this.groups;
    for (var g = 0; g < groups.length; g++) {
        if (!itemsByGroup[groups[g].content]) {
            itemsByGroup[groups[g].content] = [];
        }
        for (var i = 0; i < items.length; ++i) {
            var item = items[i];
            if (groups[g].content == item.group.content) {
                itemsByGroup[groups[g].content].push(item);
            }
        }
    }
    return itemsByGroup;
};

links.JtTimeline.prototype.setSelectItem = function(idx) {
    this.selectItemIdx = idx;
}

links.JtTimeline.prototype.unSetSelectItem = function(idx) {
    this.selectItemIdx = undefined;
}

links.JtTimeline.prototype.getSelectedRow = function() {
    return this.selectItemIdx;
}

links.JtTimeline.prototype.getGroupObj = function(chnlNo) {
    var groupObj;
    var groups = this.groups;
    for (var g = 0; g < groups.length; g++) {
        if (groups[g].groupNo == chnlNo) {
            groupObj = groups[g];
            break;
        }
    }
    return groupObj;
}

links.JtTimeline.prototype.getItem = function(idx) {
    this.items.sort(links.JtTimeline.compareFunction);
    return this.items[idx];
}

/**
 * 그룹의 아이템 삭제하기
 * @param Object group {no:0,name:""}
 * @return void
 * */
links.JtTimeline.prototype.deleteGroupItems = function (obj) {
    var me = this;
    var items = this.items;
    var indexArr = [];
    for (var i = 0, len = items.length; i < len; i++) {
        if (items[i].group.groupNo == obj.no && items[i].content != "") {
            items[i].dom.remove();
            items.splice(i, 1)[0];
            this.data.splice(i,1);
            i--;
            len--;
        }
    }

    this.render();
}




links.JtTimeline.Item = function (data) {
    if (data) {

        this.start = data.start;
        this.end = data.end;
        this.content = data.content;
        this.className = data.className;
        this.editable = data.editable;
        this.group = data.group;
        this.groupNo = data.groupNo;
        this.schedType = data.schedType;
        this.schedNo = data.schedNo;
        this.webposButton = data.webposButton;
        this.type = data.type;
        this.member = data.member;
        this.workTypeClass = data.workTypeClass;
    }
    this.top = 0;
    this.left = 0;
    this.width = 0;
    this.height = 0;
    this.lineWidth = 0;
    this.dotWidth = 0;
    this.dotHeight = 0;

    this.rendered = false; // true when the item is draw in the Timeline DOM
    //this.parentDom = parentDom;

};

links.JtTimeline.Item.prototype.select = function () {
    // Should be implemented by sub-prototype
};
links.JtTimeline.Item.prototype.unselect = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.createDOM = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.showDOM = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.hideDOM = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.updateDOM = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.setPosition = function () {
    // Should be implemented by sub-prototype
};

links.JtTimeline.Item.prototype.updatePosition = function () {
    // Should be implemented by sub-prototype
};