/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {

    $("#save-profile").click(function(){
        saveProfile();
    });    
  
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
    var sendingData = "{'fname':'"+fname.val()+"','sname':'"+sname.val()
            +"','tname':'"+tname.val()+"','pasport':'"+pasport+"','address':'"+address
            +"','comment':'"+comment.val()+"','phone':'"+phone.val()+"','stage':'"+stage.val()
            +"','office':'"+office.val()+"','post':'"+post.val()+"','propId':'"+propId.val()+"'}";
    alert(sendingData);
    $.post(projectPath + "/profile/update", {_csrf: $("#_csrf").val(), data: sendingData}, function(data) {
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


