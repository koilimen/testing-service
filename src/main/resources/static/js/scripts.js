$(document).ready(function () {
    let $body = $('body');
    $body.on('click', '.ajax-link', function (E) {
        e.preventDefault();
        var $this = $(this);
        $.ajax({
            url: $this.attr('href'),
            method: $this.data('method')
        })
    });


    $body.on('click', '.add-answer', function (e) {
        e.preventDefault();
        var answersCount = $('.answer-group').length;
        var $this = $(this);
        $.ajax({
            url: "/tests/render/answer?index=" + answersCount,
            method: 'GET',
            success: function (response) {
                $(response).insertBefore($this.parent());
            }
        })
    });

    $body.on('click', '.select__trigger, .select__trigger-arrow', function () {
        const select = $(this).closest('.select');
        if (!select.hasClass('opened')) {
            $('.select').removeClass('opened');
            select.addClass('opened');
        }
    });
    $body.on('click', '.select__list-item', function () {
        const val = $(this).data('value');
        $(this).addClass('active');
        $(this).closest('.select').find('.select__trigger').first().text($(this).text())
        $(this).closest('.select').find('input').first().val(val);

    });
    $body.on('click', function (e) {
        if (!$(e.target).hasClass('select__trigger') && !$(e.target).hasClass('select__trigger-arrow')) {
            $('.select').removeClass('opened');
        }
    })

    $body.on('submit', '#editCourseForm', function (e) {
        e.preventDefault();
        var $this = $(this);
        var data = {
            name: $this.find("#name").val(),
            id: $this.find("#id").val()
        };

        var _csrf = $(this).find('input[name="_csrf"]').val();
        $.ajax({
            data: JSON.stringify(data),
            url: "/course/edit",
            method: 'PUT',
            contentType: 'application/json; charset=utf-8',
            headers: {
                'X-CSRF-TOKEN': _csrf
            },
            success: function (response) {
                if (response === 'SUCCESS')
                    window.location.reload(true);
                else {
                    //error
                }
            },
            error: function(xhr, status, err){
                console.log(err);
            }
        })
    });


    $body.on('submit', '#editSectionForm', function (e) {
        e.preventDefault();
        var $this = $(this);
        var data = {
            code: $this.find("#code").val(),
            name: $this.find("#name").val(),
            questionsCount: $this.find("#qCount").val(),
            errorsCount: $this.find("#errCount").val(),
            id: $this.find("#id").val(),
            course: {
                id: $this.find("#courseId").val()
            }
        };

        var _csrf = $(this).find('input[name="_csrf"]').val();
        $.ajax({
            data: JSON.stringify(data),
            url: "/section/edit",
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            headers: {
                'X-CSRF-TOKEN': _csrf
            },
            success: function (response) {
                if (response === 'OK')
                    window.location.reload(true);
                else {
                    var fancyBox = $("#editSection").parent();
                    $("#editSection").remove();
                    fancyBox.append(response);
                }
            },
            error: function(xhr, status, err){
                console.log(err);
            }
        })
    })
});