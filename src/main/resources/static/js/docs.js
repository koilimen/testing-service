function openFolder(event) {
    var target = $(event.target);
    if (target.hasClass("opened")) return;
    var depth = target.data('depth');
    var url = "/docs/folder/" + target.data('id');
    $.ajax({
        url: url,
        method: "get",

        dataType: "html",
        success: function (response) {
            if (response.length > 0) {
                target.parent().append(response);
                target.addClass('opened');
            }
        },
        error: function (xhr, status, err) {
            console.error(url + " -> " + status);
            console.error(err);
        }
    })
}

$(document).ready(function () {
    var $body = $('body');
    $body.on('click', ".folder-item label:not(.opened)", openFolder);

    $body.on('submit', '#edit-folder-form', function (e) {
        e.preventDefault();
        var $folderTitle = $("#folderTitleEdit");
        var folderTitle = $folderTitle.val();
        $folderTitle.removeClass('is-invalid');
        $('.invalid-feedback').hide();

        if (folderTitle.length === 0) {
            $folderTitle.addClass('is-invalid');
            $('#emptyTitle').show();
            return;
        }
        var data = {
            id: $('#folderIdEdit').val(),
            title: $folderTitle.val(),
            parentFolder: {
                id: $("#parentFolderEdit").val(),
                title: $("#parentFolderEditTitle").val()
            }

        };

        $.ajax({
            url: '/docs/folder-edit',
            method: "POST",
            data: JSON.stringify(data),
            headers: getCSRF(),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response === '0') {
                    window.location.reload(true);
                } else if (response === '1') {
                    $('#folderTitle').addClass('is-invalid');
                    $('#folderExist').show();
                }

            },
            error: function (xhr, status, err) {
                console.error(url + " -> " + status);
                console.error(err);
            }
        })

    })
});

function removeFile(id, event) {
    var url = "/docs/file/" + id;

    $.ajax({
        url: url,
        method: "DELETE",
        headers: getCSRF(),

        success: function (response) {
            $(event.target).closest('.tree__node-body__item').remove();
        },
        error: function (xhr, status, err) {
            console.error(url + " -> " + status);
            console.error(err);
        }
    })
}

function removeFolder(id, event) {
    var url = "/docs/folder/" + id;

    $.ajax({
        url: url,
        method: "DELETE",
        headers: getCSRF(),
        success: function (response) {
            $(event.target).closest('.tree__node-body__item').remove();
        },
        error: function (xhr, status, err) {
            console.error(url + " -> " + status);
            console.error(err);
        }
    })
}

