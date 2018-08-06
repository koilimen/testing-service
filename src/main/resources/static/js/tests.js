$(document).ready(function () {

    $('body').on('click', '.tests-list__item-edit-link', function(e){
        e.preventDefault();
        var $this = $(this);
        $this.hide();
        $this.siblings('.btn').hide();
        $this.siblings('.field').hide();
        $this.siblings('form').show();
    });
    $('body').on('click', '.tests-list__item-edit-cancel-link', function(e){
        e.preventDefault();
        var $this = $(this);
        let form = $this.parent();
        form.hide();
        form.siblings('div, a').show()
    })
});