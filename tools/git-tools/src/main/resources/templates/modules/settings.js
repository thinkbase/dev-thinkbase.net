(function($) {
  "use strict";

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
    $(".site-brand").click(function() {
      //在 console 打印统计信息
      console.info(STAT);
    });
  })();
})(jQuery);
