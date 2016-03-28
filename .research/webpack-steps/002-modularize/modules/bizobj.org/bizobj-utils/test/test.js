var assert = require("assert");
var bizobjUtils = require("..");

var testDateTime=1459134548107; //2016-03-28 11:09:08
describe('Utils(bizobj-utils)', function() {

    describe('#stdDate2String(d)', function() {
        it('Output date with standard format', function() {
            assert.equal("2016-03-28", bizobjUtils.stdDate2String(new Date(testDateTime)));
        });
    });

    describe('#stdString2Date(s)', function() {
        var testDate = testDateTime- testDateTime%(24*60*60*1000);  //truncate time
        it('Parse string in standard format as Date', function() {
            assert.equal(testDate, bizobjUtils.stdString2Date("2016-03-28").getTime());
        });
    });

});
