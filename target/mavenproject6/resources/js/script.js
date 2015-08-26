var projectPath = "http://localhost:8080/redQueen";
var autoDiscon = false;
var lastUpdate = "";

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
                window.location.href = "./login";
            }
        }, 5000);
    }
}, 10000);