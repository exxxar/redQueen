var projectPath = "http://localhost:8080/redQueen";
var autoDiscon = false;

function saveProfile() {
    var fname = $("#fname");
    var sname = $("#sname");
    var tname = $("#tname");
    var pasport = $("#pasport");
    var address = $("#address");
    var comment = $("#comment");
    var phone = $("#phone");
    var stage = $("#stage");
    var office = $("#office");
    var post = $("#post");
    
    $.post(projectPath + "/profile/update", {_csrf: $("#_csrf").val(), imgdata: "qr"}, function(data) {
        
    });
}

function loadProfile() {
   
    var fname = $("#fname");
    var sname = $("#sname");
    var tname = $("#tname");
    var pasport = $("#pasport");
    var address = $("#address");
    var comment = $("#comment");
    var phone = $("#phone");
    var stage = $("#stage");
    var office = $("#office");
    var post = $("#post");
    var propId = $("#propId");    
    alert(projectPath+"/avatar/" + propId.val());
     $("#photo").attr("src", projectPath+"/avatar/" + propId.val());   
     $("#photo-qr").attr("src", projectPath+"/qr/" + propId.val());   
    
    $.post(projectPath + "/personal/profile/1", {_csrf: $("#_csrf").val()}, function(data) {
        var js = jQuery.parseJSON(data);
        fname.val(js.user[0].fname);
        sname.val(js.user[0].sname);
        tname.val(js.user[0].tname);
        pasport.val(js.user[0].pasport);
        address.val(js.user[0].address);
        comment.val(js.user[0].comment);
        phone.val(js.user[0].phone);
        stage.val(js.user[0].stage);
        office.val(js.user[0].office);
        post.val(js.user[0].post);
        propId.val(js.user[0].propId);
          
    });
}
function getUserByQR(qr) {

    $.post(projectPath + "/camera/", {_csrf: $("#_csrf").val(), imgdata: "qr"}, function(data) {
        var js = jQuery.parseJSON(data);
        $(".json-propNumber").html(js.user[0].propNumber);
        $(".json-propDate").html(js.user[0].propDate);
        $(".json-propTname").html(js.user[0].tname);
        $(".json-propSname").html(js.user[0].sname);
        $(".json-propFname").html(js.user[0].fname);
        $(".json-propPasport").html(js.user[0].pasport);
        $(".json-propLevel").html(js.user[0].level);
        var userId = js.user[0].userId;
        $(".json-propPhoto").attr("src", "./camera/avatar/" + userId);
        setTimeout(function() {
            $("[class*='json-prop']").html("-empty-");
            $(".json-propPhoto").attr("src", "./resources/img/no_avatar.jpg");
        }, 10000);
    });

}

function getLogs() {
    if ($(".logs").attr("time-out") === "false") {
        $(".logs").attr("time-out", true);
        setInterval(function() {
            getLogs();
        }, 5000);
    }

    $.post(projectPath + "/camera/logs", {_csrf: $("#_csrf").val()}, function(data) {
        var js = jQuery.parseJSON(data);

        var html = "";
        $.each(js.logs, function(idx, obj) {
            html += "<div class='log-item panel panel-default'>" +
                    "<h5>" + obj.Time + "</h5>" +
                    "<h6 class='" + (obj.Type == 'error' ? 'bg-red' : 'bg-green') + "'>" + obj.Message + "</h6>" +
                    "<p>" + obj.User + "</p>" +
                    "</div>";
        });
        $("#menu_log .logs").html(html);
    });
}

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
$(document).ready(function() {

    $(".logs").attr("time-out", false);
    $("[class*='json-prop']").html("-empty-");
    $(".json-propPhoto").attr("src", "./resources/img/no_avatar.jpg");
    setwebcam();


    $(".alert-main .close").click(function() {
        $(".alert-main").removeClass("show").addClass("hide");
    });

    $('#logShowBtn').click(function() {
        getLogs();
    });

    $("#getPhoto").click(function() {
        photo = true;
        captureToCanvas();
        photo = false;
    });


    $('#addGuestBtn').click(function() {
        var personal = $("#w-personal-search").val();

        $(".alert-main span").html("ERROR!!!!");
        $(".alert-main").removeClass("hide").addClass("show");

        $.get(projectPath + "/guest", {}, function(data) {
            $(".alert-main span").html(data);
        });
    });

    $("#myphoto").dblclick(function() {
        $(this).attr("src", "./resources/img/no_avatar.jpg");

    });

    $("#w-personal-search").autocomplete({
        source: function(request, response) {

            $.getJSON(projectPath + "/camera/plist", {
                personalName: extractLast(request.term)
            }, response);
        },
        select: function(event, ui) {
            var newWin = window.open(projectPath + "/profile/" + ui.item.value,
                    "JSSite",
                    "width=420,height=250,location=no,status=no,resizable=no"
                    );

            newWin.focus();
            setTimeout(function() {
                newWin.close();
            }, 30000);
        }
    });

    $("#s-personal-search").autocomplete({
        source: function(request, response) {

            $.getJSON(projectPath + "/camera/plist", {
                personalName: extractLast(request.term)
            }, response);
        },
        select: function(event, ui) {
            var newWin = window.open(projectPath + "/camera/profile/" + ui.item.value,
                    "JSSite",
                    "width=420,height=250,location=no,status=no,resizable=no"
                    );

            newWin.focus();
            setTimeout(function() {
                newWin.close();
            }, 30000);
        }
    });
});

