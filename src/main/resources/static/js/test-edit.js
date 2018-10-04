$(document).ready(function () {
    var XHR_FILE_UPLOADER;

    function validateFiles($form) {
        var fileInput = $form.find('#docxFile');
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

    $("#questionFilesForm").on('submit', function (e) {
        e.preventDefault();
        var $this = $(this);
        $('.invalid-feedback').hide();
        var filesValid = validateFiles($this);
        if (!filesValid) {
            $('.invalid-feedback').show();
            return;
        }
        var formData = new FormData($this[0]);
        $('.progress').show();
        $.ajax({
            type: 'POST',
            url: $this.attr('action'),
            data: formData,
            cache: false,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            // xhr: function () {
            //     XHR_FILE_UPLOADER = $.ajaxSettings.xhr();
            //     if (XHR_FILE_UPLOADER.upload) {
            //         XHR_FILE_UPLOADER.upload.addEventListener('progress', progressFileUpload, false);
            //     }
            //     return XHR_FILE_UPLOADER;
            // },
            success: function (data) {
                if (data !== 'ok') {
                    console.log("data")
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

    function progressFileUpload(e) {
        if (e.lengthComputable) {
            var progressControl = $('.progress-bar');
            var percentsComplete = Math.floor((e.loaded / e.total) * 100);
            console.info(percentsComplete);
            progressControl.css('width', percentsComplete + "%");
        }
    }

});