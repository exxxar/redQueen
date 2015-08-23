var projectPath = "http://94.248.11.170:8080/redQueen";

function getUserByQR(qr) {

    $.post(projectPath+"/camera/", {_csrf: $("#_csrf").val(), imgdata: "qr"}, function(data) {
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

    $.post(projectPath+"/camera/logs", {_csrf: $("#_csrf").val()}, function(data) {
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

var intId = setInterval(function(){ //clearInterval(timerId);
    var isPong = false;
    $.post(projectPath+"/ping", {_csrf: $("#_csrf").val()}, function(data) {
        isPong = true;  
    });
    var timeoutId = setTimeout(function(){
        if (isPong==true){
            clearTimeout(timeoutId);
            isPong = false;           
        }
        else
        {         
            clearInterval(intId);
            window.location.href = "./login";        
        }
    },5000);
    
},10000);
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

    $("#getPhoto").click(function(){
        photo = true;
        captureToCanvas();
        photo = false;      
    });
    
    
    $('#addGuestBtn').click(function() {
        var personal = $("#w-personal-search").val();    
        
        $(".alert-main span").html("ERROR!!!!");
        $(".alert-main").removeClass("hide").addClass("show");

        $.get(projectPath+"/guest", {}, function(data) {
            $(".alert-main span").html(data);
        });
    });

    $("#myphoto").dblclick(function(){
        $(this).attr("src", "./resources/img/no_avatar.jpg");
     
    });
    
    $("#w-personal-search").autocomplete({
        source: function(request, response) {
        
            $.getJSON(projectPath+"/camera/plist", {
                personalName: extractLast(request.term)
            }, response);
        },
        select: function(event, ui) {
            var newWin = window.open(projectPath+"/camera/profile/"+ui.item.value,
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
        
            $.getJSON(projectPath+"/camera/plist", {
                personalName: extractLast(request.term)
            }, response);
        },
        select: function(event, ui) {
            var newWin = window.open(projectPath+"/camera/profile/"+ui.item.value,
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
