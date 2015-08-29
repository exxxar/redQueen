$(function() {
    var btnUpload = $('#upload');
    var status = $('#status');
    new AjaxUpload(btnUpload, {
        action: projectPath + '/profile/upload?_csrf='+$("#_csrf").val(),
        name: 'uploadfile',
        data: {propId:$("#propId").val()},
        onSubmit: function(file, ext) {
          
            if (!(ext && /^(jpg|png|jpeg|gif)$/.test(ext))) {
                // extension is not allowed 
                status.text('Поддерживаемые форматы JPG, PNG или GIF');
                return false;
            }

            status.text('loading...');
        },
        onComplete: function(file, response) {           
            status.text('');          
        }
    });

});
