$(function() {
    var btnUpload = $('#upload');
    var status = $('#status');
    new AjaxUpload(btnUpload, {
        action: 'http://localhost:8080/profile/upload?_csrf='+$("#_csrf").val(),
        name: 'uploadfile',
        data: {},
        onSubmit: function(file, ext) {
           alert($("#_csrf").val());
            if (!(ext && /^(jpg|png|jpeg|gif)$/.test(ext))) {
                // extension is not allowed 
                status.text('Поддерживаемые форматы JPG, PNG или GIF');
                return false;
            }

            status.text('loading...');
        },
        onComplete: function(file, response) {
            //On completion clear the status
            status.text('');
            //Add uploaded file to list
            if (response === "success") {
                $('<li></li>').appendTo('#files').html('<img src="./uploads/' + file + '" alt="" /><br />' + file).addClass('success');
            } else {
                $('<li></li>').appendTo('#files').text('Файл не загружен' + file).addClass('error');
            }
        }
    });

});