$(document).ready(function () {
    var XHR_FILE_UPLOADER;

    function validateFiles(fileInput) {
        var files = fileInput[0].files;
        if (files.length === 0) return false;
        for (var i = 0; i < files.length; i++) {
            var mimeType = files.item(i).type;
            if (mimeType !== "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {
                return false;
            }

        }
        return true;
    }

    $("#docxFile").on('change', function (e) {
        e.preventDefault();
        var $this = $(this);
        $('.invalid-feedback').hide();
        var filesValid = validateFiles($this);
        if (!filesValid) {
            $('.invalid-feedback').show();
            return;
        }
        var form = $this.closest('form');
        var formData = new FormData(form[0]);
        $this.closest('.file-input-wrapper').addClass('loading');
        var _csrf = form.find('input[name="_csrf"]').val();
        $.ajax({
            type: 'POST',
            url: form.attr('action'),
            data: formData,
            cache: false,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            headers: {
                'X-CSRF-TOKEN': _csrf
            },
            success: function (data) {
                if (data !== 'ok') {
                    console.log(data);
                } else {
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000)
                }

            },
            error: function (request, status, error) {
                console.log("Status: " + status);
                console.log("Error: " + error);
            }
        });
    })

    $('body').on('click', '.open-file-dialog', function(){
       $("#docxFile").click();
    });

    function progressFileUpload(e) {
        if (e.lengthComputable) {
            var progressControl = $('.progress-bar');
            var percentsComplete = Math.floor((e.loaded / e.total) * 100);
            console.info(percentsComplete);
            progressControl.css('width', percentsComplete + "%");
        }
    }

});