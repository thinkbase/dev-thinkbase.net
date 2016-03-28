
### module `bizobj.org/bizobj-utils`
```shell
npm init

## Install dependencies
# "-S" is the alias for option "--save"
npm install date-format -S
npm install tempusjs --save

## Install devDependencies(For unit test)
# "-D" is the alias for option "--save-dev"
npm install mocha -D
# After install mocha, edit "package.json"
#  * change "scripts.test" to "mocha"
#  * then edit "test/test.js"
#  * RUN "npm test" to test, or run "mocha" directly

## NOTES
# Use "uninstall" to remove useless modules, such as:
npm uninstall datetime-ext --save
```

### module `thinkbase.net/thinkbase-ajax`
```shell
npm init
npm install jquery -S
npm install mocha -D

npm install JSON2 -S

# Instead of installing webpack, install webpack-dev-web-test is recommanded
npm install ../../../supports/webpack-dev-web-test -D
## NOTES
# After install `webpack-dev-web-test`, edit `package.json` to modify reference to `webpack-dev-web-test` with relative path
```

### NOTES
#### The default `src/index.js`(or `index.js` at module root)
```js
"use strict";

/* The implementations */
var _f1 = function(){
    //... ...
};
var _f2 = function(a){
    //... ...
};
var _fN = function(a, b, c){
    //... ...
};

/** Define the export point for module */
module.exports = {
    function1: _f1,
    function2: _f2,
    functionN: _fN
}
```
#### The default `test/test.js`
```js
var assert = require("assert");
var testObj = require("../src");    //Or require("..") if index.js was placed at module root.

describe('<module name>', function() {

    describe('#function1()', function() {
        it('<description of test1>', function() {
            //The test code
            assert.equal(expected, functionResult);
        });
    });

    describe('#functionN(a, b, c)', function() {
        it('<description of testN>', function() {
            //The test code
            assert.equal(expected, functionResult);
        });
    });

});
```

### END
