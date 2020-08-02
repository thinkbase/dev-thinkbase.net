(function($) {
  "use strict";

  //初始化显示的内容
  $("#rolling-lucky-one").html("谁？");

  //杂项
  $("#rolling-stop").click(function() {
    $("#startRolling").val("开始");
  });
})(jQuery);
