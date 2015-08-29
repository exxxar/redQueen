var projectPath = "http://localhost:8080/redQueen";
var autoDiscon = false;
var lastUpdate = "";

var INTERVAL_PING = 10000;
var INTERVAL_PONG = 5000;
var INTERVAL_LOG_UPDATE = 5000;
var INTERVALE_TEST_UPDATE = 5000;
var TIMEOUT_SKIPER_HIDE = 30000;
var TIMEOUT_QR_CLEAR = 5000;


function split(val) {
    return val.split(/,\s*/);
}
function extractLast(term) {
    return split(term).pop();
}

var intId = setInterval(function() { //clearInterval(timerId);
    if (autoDiscon == true) {
        var isPong = false;
        $.post(projectPath + "/ping", {_csrf: $("#_csrf").val()}, function(data) {
            isPong = true;
        });
        var timeoutId = setTimeout(function() {
            if (isPong == true) {
                clearTimeout(timeoutId);
                isPong = false;
            }
            else
            {
                clearInterval(intId);
                window.location.href = projectPath+"/login";
            }
        }, INTERVAL_PONG);
    }
}, INTERVAL_PING);