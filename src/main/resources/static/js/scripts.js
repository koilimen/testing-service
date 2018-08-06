$(document).ready(function () {
    $('body').on('click', '.ajax-link', function (E) {
        e.preventDefault();
        var $this = $(this);
        $.ajax({
            url: $this.attr('href'),
            method: $this.data('method')
        })
    });

    $('body').on('click', '.tests-list__item-remove-link', function (e) {
        e.preventDefault();
        var $this = $(this);
        $.ajax({
            url: $this.attr('href'),
            method: 'DELETE',
            success: function(response){
                $this.parent().remove();
            }
        })
    })
});