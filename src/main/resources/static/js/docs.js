function openFolder(event){
    var target = $(event.target);
    if(target.hasClass("opened")) return;
    var url = "/docs/folder/"+target.data('id');
    $.ajax({
        url: url,
        method: "get",
        dataType: "html",
        success: function(response){
            if(response.length > 0){
                target.parent().append(response);
                target.addClass('opened');
            }
        },
        error: function(xhr, status, err){
            console.error(url+" -> "+status);
            console.error(err);
        }
    })
}

$(document).ready(function(){
    var $body = $('body');
    $body.on('click', ".folder-item label:not(.opened)", openFolder);
});