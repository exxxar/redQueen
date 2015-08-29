/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    loadProfile();
    
    $("#save-profile").click(function(){
        saveProfile();
    });    
    
    $("#reset-photo").click(function(){
         $.get(projectPath+"/avatar/reset/"+$("#propId").val());
    });
    
    $("#take-skiper").click(function(){
          var newWin = window.open(projectPath + "/skiper/"+$("#propId").val() ,
                    "JSSite",
                    "width=420,height=250,location=no,status=no,resizable=no"
                    );

            newWin.focus();
    });
    $("form").attr("action",projectPath + "/profile/upload?_csrf="+$("#_csrf").val());
});
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
    var propId = $("#propId");    

    var sendingData = "{\"fname\":\""+fname.val()+"\",\"sname\":\""+sname.val()
            +"\",\"tname\":\""+tname.val()+"\",\"pasport\":\""+pasport.val()+"\",\"address\":\""+address.val()
            +"\",\"comment\":\""+comment.val()+"\",\"phone\":\""+phone.val()+"\",\"stage\":\""+stage.val()
            +"\",\"office\":\""+office.val()+"\",\"post\":\""+post.val()+"\",\"propId\":\""+propId.val()+"\"}";

    $.post(projectPath + "/profile/update", {_csrf: $("#_csrf").val(), sdata: sendingData}, function(data) {
        $("#status").html("Информация успешно обновлена.");
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
    
     $("#photo").attr("src", projectPath+"/avatar/" + propId.val());   
     $("#photo-qr").attr("src", projectPath+"/qr/" + propId.val());   

    $.post(projectPath + "/profile/info/", {_csrf: $("#_csrf").val(),userData:propId.val()}, function(data) {

        var js = jQuery.parseJSON(data);       
        fname.val(js.personal.fname);
        sname.val(js.personal.sname);
        tname.val(js.personal.tname);
        pasport.val(js.personal.passportSeria+js.personal.passportNum);
        address.val(js.personal.addres);
        comment.val(js.personal.info);
        phone.val(js.personal.phone);
        stage.val(js.personal.stage);
        office.val(js.personal.office);
        post.val(js.personal.post);
        propId.val(js.personal.accessNumber);
        lastUpdate =   js.personal.lastUpdate;
    });
}

setInterval(function(){
    var propId = $("#propId"); 
    $.post(projectPath+"/profile/test/", {_csrf: $("#_csrf").val(),userData:propId.val()},function(data){
        if (lastUpdate!=data)
            loadProfile();       
    });
},INTERVALE_TEST_UPDATE);

