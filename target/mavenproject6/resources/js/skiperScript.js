$(document).ready(function() {
   loadProfile();
});

function loadProfile() { 
    $(".propPhoto").attr("src", projectPath+"/avatar/"+$("#propId").val()); 
    $(".propQr").attr("src", projectPath+"/qr/"+$("#propId").val());    
}

