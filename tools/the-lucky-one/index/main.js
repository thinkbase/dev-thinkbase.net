(function($) {
  "use strict";

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

  $(".main-menu a.homebutton").click(function() {
    $("#menu-container .content").slideUp("slow");
    $("#menu-container .homepage").slideDown("slow");
    $(".logo-top-margin").animate({ marginLeft: "40%" }, "slow");
    $(".logo-top-margin").animate({ marginTop: "20px" }, "slow");
    return false;
  });

  $(".main-menu a.aboutbutton").click(function() {
    $("#menu-container .content").slideUp("slow");
    $("#menu-container .about-section").slideDown("slow");
    $(".logo-top-margin").animate({ marginTop: "0" }, "slow");
    $(".logo-top-margin").animate({ marginLeft: "0" }, "slow");
    return false;
  });

  $(".main-menu a.projectbutton").click(function() {
    $("#menu-container .content").slideUp("slow");
    $("#menu-container .gallery-section").slideDown("slow");
    $(".logo-top-margin").animate({ marginTop: "0" }, "slow");
    $(".logo-top-margin").animate({ marginLeft: "0" }, "slow");
    return false;
  });

  $(".main-menu a.contactbutton").click(function() {
    $("#menu-container .content").fadeOut();
    $("#menu-container .contact-section").slideDown("slow");
    $(".logo-top-margin").animate({ marginTop: "0" }, "slow");
    $(".logo-top-margin").animate({ marginLeft: "0" }, "slow");
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

    var MAGIC_NUMBER = 30;

    var fitHi =
      wh -
      th -
      fh -
      parseInt($("#page-content").css("margin-top")) -
      MAGIC_NUMBER;
    $("#page-content").css("min-height", fitHi + "px");
  };
  $(window).resize(function() {
    _adjustFooter();
  });
  _adjustFooter();

  //初始化显示的内容
  $("#rolling-lucky-one").html("谁？");

  //关于存储的处理
  var DEFAULT_ROLLING_SECONDS = 10;
  var DEFAULT_PERSONS = "小明 小红 张三 李四";
  (function() {
    $(".settings-form").submit(function(e) {
      e.preventDefault();

      var rs = $("#settingRollingSeconds").val();
      $.jStorage.set("settingRollingSeconds", rs);

      ps = $("#settingPersons").val();
      $.jStorage.set("settingPersons", ps);

      alert("设置信息已保存。");
    });

    var rs = $.jStorage.get("settingRollingSeconds", DEFAULT_ROLLING_SECONDS);
    var ps = $.jStorage.get("settingPersons", DEFAULT_PERSONS);
    $("#settingRollingSeconds").val(rs);
    $("#settingPersons").val(ps);

    var STAT = {
      hits: {},
      targets: []
    };
    //开始 Rolling
    $("#startRolling").click(function() {
      if ("开始" == $("#startRolling").val()) {
        $("#startRolling").val("锁定");

        $("#rolling-prompt").css("visibility", "visible");

        var rs = parseInt($("#settingRollingSeconds").val());
        if (!rs) {
          rs = DEFAULT_ROLLING_SECONDS;
        }
        var ps = $("#settingPersons").val();
        if (ps) {
          ps = (ps + "").replace(/^\s+|\s+$/gm, ""); //Trim it
        }
        if (!ps) {
          alert("请到 ‘Settings’ 中输入参与人员");
        }

        //分析参与人员
        var personList = ps.split(/\s+/);

        var startMs = new Date().getTime();
        var rollFun = function() {
          var remainMs = rs * 1000 - (new Date().getTime() - startMs);
          $("#rolling-seconds").html(1 + parseInt(remainMs / 1000));
          if (remainMs < 0) {
            //完成
            $("#rolling-seconds").html(0);
            $("#startRolling").val("开始");
            STAT.targets.push($("#rolling-lucky-one").html());
            return;
          }
          if ("开始" == $("#startRolling").val()) {
            //强制完成
            $("#startRolling").val("开始");
            return;
          }
          var index = Math.round(Math.random() * (personList.length - 1));
          var pName = personList[index];
          $("#rolling-lucky-one").html(pName);
          STAT.hits[pName] = (STAT.hits[pName] || 0) + 1;

          setTimeout(rollFun, 10);

          return;
        };
        rollFun();
      } else {
        $("#startRolling").val("开始");
      }
    });

    //杂项
    $("#rolling-stop").click(function() {
      $("#startRolling").val("开始");
    });
    $(".site-brand").click(function() {
      //在 console 打印统计信息
      console.info(STAT);
    });
  })();
})(jQuery);
