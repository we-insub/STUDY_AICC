(function($) {
  "use strict"; // Start of use strict

  // Toggle the side navigation
  $("#sidebarToggle").on('click', function(e) {
    
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    $(".util").toggleClass("on");

    if ($(".sidebar").hasClass("toggled")) {
      $('#wrapper').css('margin-left', '65px');
      $('.topbar').css('left', '65px');
      //열린메뉴접기
      $('.lnb-menu .nav-item .nav-link').removeClass('opened');
      $('.lnb-menu .nav-item .nav-link').addClass('folded');
    }else{
      $('#wrapper').css('margin-left', '228px');
      $('.topbar').css('left', '228px');
    }
  });

  // 로드 : 1400 이하 LNB 축소하기 
  $(window).load(function() {
    // 모달팝업 modal-body 높이값 설정
    $('.modal-body').each(function(){
        var htmlH = $('html').outerHeight();
        var windowH = $(window).outerHeight();
        var documentH = $(document).outerHeight();

        $('.modal-body').css('max-height', (htmlH * 0.8) +'px');
    });
    
    if ($(window).width() < 1400) {
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.util').addClass("on");
      $('#wrapper').css('margin-left', '65px');
      $('.topbar').css('left', '65px')
      //열린메뉴접기
      $('.lnb-menu .nav-item .nav-link').removeClass('opened');
      $('.lnb-menu .nav-item .nav-link').addClass('folded');
    }else{
      $("body").removeClass("sidebar-toggled");
      $(".sidebar").removeClass("toggled");
      $('.util').removeClass("on");
      $('#wrapper').css('margin-left', '228px');
      $('.topbar').css('left', '228px');
    }
  });

  // 리사이즈 : 1400 이하 LNB 축소하기 
  $(window).resize(function() {
    if ($(window).width() < 1400) {
      //console.log("$(window).width()",$(window).width());
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.util').addClass("on");
      $('#wrapper').css('margin-left', '65px');
      $('.topbar').css('left', '65px')
      //열린메뉴접기
      $('.lnb-menu .nav-item .nav-link').removeClass('opened');
      $('.lnb-menu .nav-item .nav-link').addClass('folded');
    }else{
      $("body").removeClass("sidebar-toggled");
      $(".sidebar").removeClass("toggled");
      $('.util').removeClass("on");
      $('#wrapper').css('margin-left', '228px');
      $('.topbar').css('left', '228px');
    }
  });

  // lnb click
  $('.lnb-menu .nav-item .nav-link').on('click', function() {

    //접혀있으면 열기 
    if($(this).hasClass("folded")){
      //열린메뉴접기
      $('.lnb-menu .nav-item .nav-link').removeClass('opened');
      $('.lnb-menu .nav-item .nav-link').addClass('folded');
      $(this).removeClass('folded');
      $(this).addClass('opened');

    //열려있으면 닫기
    }else{
      $(this).removeClass('opened');
      $(this).addClass('folded');
    }
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
      var e0 = e.originalEvent,
        delta = e0.wheelDelta || -e0.detail;
      this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });

  // 테이블 리스트 [돋보기-검색] 클릭시 검색 input 나타나기
  $('button[name=btn-search]').click(function(){
      $(this).parents('.filter-wrap').hide();
      $(this).parents('.filter-wrap').siblings('.current').show();
  });
  // 테이블 리스트 [X] 클릭시 검색 input 숨기고 title 나타나기
  $('button[name=btn-clear]').click(function(){
      $(this).parents('.current').hide();
      $(this).parents('.current').siblings('.filter-wrap').show();
  });
  // 테이블 리스트 [날짜 + 시간] - 달력
  $('button[name="btn-time-picker"]').click(function(){
      $(this).parents('.filter-wrap').next('.filter-wrap.current').show();
      var timePicker = $(this).parents('.filter-wrap').next('.filter-wrap.current').find('input[name="timepicker"]');
      timePicker.daterangepicker({
          timePicker: true,
          locale: {
              format: 'MM/DD/YYYY h:mm A'
          },
          singleDatePicker: true,
          showDropdowns: true,
          drops: "down",
          minDate: '01/01/2020',
          maxDate: '12/31/2020',
          opens: 'right'
      }).click();
  });
  // [날짜 + 시간] - 달력
  $('input[name="timepicker"]').daterangepicker({
      timePicker: true,
      locale: {
          format: 'MM/DD/YYYY h:mm A'
      },
      singleDatePicker: true,
      showDropdowns: true,
      drops: "down",
      minDate: '01/01/2020',
      maxDate: '12/31/2020',
      opens: 'right'
  });
  // 테이블 리스트 [날짜] - 달력
  $('button[name="btn-date-picker"]').click(function(){
      $(this).parents('.filter-wrap').next('.filter-wrap.current').show();
      var dataPicker = $(this).parents('.filter-wrap').next('.filter-wrap.current').find('input[name="datepicker"]');
      dataPicker.daterangepicker({
          timePicker: false,
          locale: {
              format: 'MM/DD/YYYY'
          },
          singleDatePicker: true,
          showDropdowns: true,
          drops: "down",
          minDate: '01/01/2020',
          maxDate: '12/31/2020',
          opens: 'right'
      }).click();
  });
  // [날짜] - 달력
  $('input[name="datepicker"]').daterangepicker({
      timePicker: false,
      locale: {
          format: 'MM/DD/YYYY'
      },
      singleDatePicker: true,
      showDropdowns: true,
      drops: "down",
      minDate: '01/01/2020',
      maxDate: '12/31/2020',
      opens: 'right'
  });
  // 기간검색 [기간] - 달력
  var options = {};
      options.locale = {
      direction: $('#rtl').is(':checked') ? 'rtl' : 'ltr',
      format: 'MM/DD/YYYY HH:mm',
      separator: ' - ',
      applyLabel: 'Apply',
      cancelLabel: 'Cancel',
      fromLabel: 'From',
      toLabel: 'To',
      customRangeLabel: 'Custom',
      daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
      monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
      firstDay: 1,
      opens: 'right'
  };
  $('input[name="daterangepicker"]').daterangepicker(options, function(start, end, label) { 
      console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')'); 
  });

  // tooltip
  $('[data-toggle="tooltip"]').tooltip();
  
})(jQuery); // End of use strict
