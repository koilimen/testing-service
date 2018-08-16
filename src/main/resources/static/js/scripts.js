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

    $('body').on('click', '.add-answer', function(e){
        e.preventDefault();
        var answersCount = $('.answer-group').length + 1;
        $(" <div class=\"form-group row answer-group\"  class=\"answer-group\"><label for=\"answer{}\" class=\"col-sm-2 col-form-label\">Ответ {}</label><div class=\"col-sm-5\"><input type='text' class=\"form-control\" id=\"answer{}\" named=\"answers[{}].answerText}\"></input></div><div class=\"col-sm-5\"><label for=\"answer{}-correct\" class=\"form-check-label\"><input type='checkbox' class=\"form-check-input\" id=\"answer{}-correct\" name=\"answers[{}].correct\"></input>Верный ответ</label></div></div>".replace(/{}/g,  answersCount))
        .insertBefore($(this).parent());
    });

});