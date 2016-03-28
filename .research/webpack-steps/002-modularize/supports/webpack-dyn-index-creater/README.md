# About

This is a tool to create `src/index.js` which include "window._require" function to make html-inline scripts could get modules dynamicly.

For example, in html the code should like:
```html
<script>
var doTestBizobjUtils = function(){
    _require("bizobj-utils", function(util){
        alert("stdDate2String="+util.stdDate2String(new Date()));
    });
}
... ...
</script>
```

## Dependencies
```shell

```
