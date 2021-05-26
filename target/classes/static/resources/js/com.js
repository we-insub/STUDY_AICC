var com = {
	nvl: function (str, defaultStr) {
				if (str === null || str === undefined) {
					if (defaultStr === null || defaultStr === undefined) {
						return '';
					} else {
						return defaultStr;
					}
				} else {
					return str;
				}
	},
	maskMobile :function(str){
			return  str.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
	},
	formatNumber  : function(num){
			return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	},
	isNumberKey : function(event){
		var charCode = (event.which) ? event.which : event.keyCode;
             if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))return false;
             return true;
	},
	alert : function(msg){
		$("#com-alert-message").text(msg);
		$("#com-alert").modal("show");
	},
	confirm : function(msg,callback){
	  $("#com-confirm-message").text(msg);
	  $("#com-confirm").modal('show');
	  $("#com-confirm-ok").on("click", function(){
		    callback(true);
		    $("#com-confirm").modal('hide');
		  });
	  $("#com-confirm-close").on("click", function(){
		    callback(false);
		    $("#com-confirm").modal('hide');
		  });
	/** 사용법
		com.confirm('테스트',function(confirm){
		if(confirm){
			alert('확인');
		});
	 */
	}
}
$.ajaxSetup({
	type : "POST",
//	contentType : "application/json; charset=utf-8",
//	async : false,
	timeout : 1000,
//	dataType: "html",
	beforeSend: function(xhr) {
		 $('.loading-bg').show();
	},
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
	complete: function(xhr,status){
		setTimeout(function(){
			$('.loading-bg').hide();
		}, 500);
	},
	error : function(xhr,status,error) {
		alert("error");
	}
});
