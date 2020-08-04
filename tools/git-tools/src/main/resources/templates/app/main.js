(function($) {
  "use strict";

  //复制 #nav .main-menu 到 #show-menu .main-menu
  $("#show-menu .main-menu").html($("#nav .main-menu").html());

  $(".main-menu a").click(function() {
    var id = $(this).attr("class");
    id = id.split("-");
    $("a.active").removeClass("active");
    $(this).addClass("active");
    $("#menu-container .content").slideUp("slow");
    $("#menu-container #menu-" + id[1]).slideDown("slow");
    $("#menu-container .homepage").slideUp("slow");
    return false;
  });

  $(".toggle-menu").click(function() {
    $(".show-menu")
      .stop(true, true)
      .slideToggle();
    return false;
  });

  $(".show-menu a").click(function() {
    $(".show-menu").fadeOut("slow");
  });

  //计算 row 区域的最小高度
  var _adjustFooter = function() {
    var wh = $(window).height();
    var th = $(".top-header").outerHeight();
    var fh = $(".site-footer").outerHeight();

    var MAGIC_NUMBER = 0; //与 .site-footer CSS 中 margin-top 有关

    var fitHi = wh - th - fh -
      parseInt($("#page-content").css("margin-top")) -
      MAGIC_NUMBER;
    $("#page-content").css("min-height", fitHi + "px");
  };
  $(window).resize(function() {
    _adjustFooter();
  });
  _adjustFooter();

})(jQuery);
