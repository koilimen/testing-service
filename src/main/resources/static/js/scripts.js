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

    $body.on('submit', '#test-edit-form', function (e) {
        e.preventDefault();
        var data = {};
        $(this).find('input, textarea').each(function (i, el) {
            var name = $(el).attr('name');
            var value = $(el).val();
            if (name === 'section.id') {
                data['section'] = {
                    id: value
                }
            } else
                data[name] = value;
        });
        $.ajax({
            data: JSON.stringify(data),
            url: "/tests/edit",
            method: 'PUT',
            contentType: 'application/json; charset=utf-8',
            headers: getCSRF(),
            success: function (response) {
                if (response === 'OK')
                    window.location.reload(true);
                else {
                    var fancyBox = $("#test-edit-form").parent();
                    $("#test-edit-form").remove();
                    fancyBox.append($(response).find('#test-edit-form'));
                }
            },
            error: function (xhr, status, err) {
                console.log(err);
            }
        })
    });
    $body.on('submit', '.add-answer-form', function (e) {
        e.preventDefault();
        var form = $(this);
        var question = $(this).closest('.question');
        var data = {
            question: {
                id: form.find('input[name="questionId"]').val()
            },
            answerText: form.find('input[name="additionalAnswer"]').val(),
            correct: false
        }
        var $this = $(this);
        var _csrf = form.find('input[name="_csrf"]').val();

        $.ajax({
            url: "/questions/add-answer",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            headers: {
                'X-CSRF-TOKEN': _csrf
            },
            method: 'POST',
            success: function (response) {
                $(response).insertAfter(question.find('.question__answers-list-item').last());
                form.find('input[name="additionalAnswer"]').val('');
            }
        })
    });

    $body.on('click', '.add-answer-nq', function (e) {
        e.preventDefault();
        var parentForm = $(this).closest('form');
        var qid = $(this).data('qid');
        var length = parentForm.find('.answer-group').length;
        var data = {
            index: length
        };
        if (qid) {
            data['qid'] = qid;
        }
        $.ajax({
            url: "/tests/render-answer",
            data: data,
            headers: getCSRF(),
            method: 'GET',
            success: function (response) {
                $(response).insertAfter(parentForm.find('.answer-group').last());
            }
        })
    });
    $body.on('click', '.question__answers-list-item__remove', function (e) {
        var aId = $(this).data('id');
        var csrfHeaderName = $("meta[name='_csrf_header']").attr('content');
        var csrfHeaderContent = $("meta[name='_csrf']").attr('content');
        var headers = {};
        headers[csrfHeaderName] = csrfHeaderContent;
        var ans = $(this).closest('.answer-group');
        $.ajax({
            url: "/questions/delete-answer/" + aId,
            headers: headers,
            method: 'POST',
            success: function (response) {
                ans.remove();
            }
        })
    });

    $body.on('change', '.asnwer-correct', function (e) {
        var checked = e.target.checked;
        var id = $(this).data('id');
        var csrfHeaderName = $("meta[name='_csrf_header']").attr('content');
        var csrfHeaderContent = $("meta[name='_csrf']").attr('content');
        var headers = {};
        headers[csrfHeaderName] = csrfHeaderContent;

        $.ajax({
            url: "/questions/answer/correct/" + id + "/" + checked,
            headers: headers,
            method: 'POST',
            success: function (response) {
                ans.remove();
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
            id: $this.find("#id").val(),
            order: $this.find("#order").val(),
            text1: $this.find("#text1").val()
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
            error: function (xhr, status, err) {
                console.log(err);
            }
        })
    });

    $body.on('submit', "#add-literature", function (e) {
        e.preventDefault();
        var data = $(this).serialize();
        $.ajax({
            data: data,
            url: "/tests/add-literature",
            method: 'POST',
            headers: getCSRF(),
            success: function (response) {
                if (response === 'SUCCESS') {
                    window.location.reload(true);
                }
                console.log(response);
            },
            error: function (xhr, status, err) {
                console.log(err);
            }
        })
    });

    $body.on('click', ".delete-literature", function (e) {
        e.preventDefault();
        var id = $(this).data('id');
        var row = $(this).closest('tr');
        $.ajax({
            url: "/tests/del-literature/"+id,
            method: 'DELETE',
            headers: getCSRF(),
            success: function (response) {
                if (response === 'SUCCESS') {
                    row.remove();
                }
                console.log(response);
            },
            error: function (xhr, status, err) {
                console.log(err);
            }
        })
    });

    $body.on('submit', '#editSectionForm', function (e) {
        e.preventDefault();
        var $this = $(this);
        var data = {
            name: $this.find("#name").val(),
            id: $this.find("#id").val(),
            order: $this.find("#order").val(),
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
            error: function (xhr, status, err) {
                console.log(err);
            }
        })
    });
    $body.on('click', '#file-upload-trigger', function (e) {
        e.preventDefault();
        $("#file-upload").click();
    });
    $("#file-upload").on('change', function (e) {
        e.preventDefault();
        var $this = $(this);
        var form = $this.closest('form');
        var formData = new FormData(form[0]);
        $this.closest('.docs-form').addClass('loading');
        $.ajax({
            type: 'POST',
            url: form.attr('action'),
            data: formData,
            cache: false,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            headers: getCSRF(),
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

    $body.on('click', '.sidebar-toggler', function(e){
        $('.left-sidebar').toggleClass("shown");
    })

    $body.on('click', '.sort-up', function (e) {
        e.preventDefault();
        var parent = $(this).closest('.orderable:not(.new)');
        var prev = parent.prev(".orderable");
        if (prev) {
            prev.insertAfter(parent);
            var url;
            if (parent.hasClass('course-item')) {
                url = "/course/update-orders";
            } else if (parent.hasClass('section-item')) {
                url = "/section/update-orders";
            } else if (parent.hasClass('test-item')) {
                url = "/tests/update-orders";
            }
            swapOrders(parent, prev, url);
        }
    })
    $body.on('click', '.sort-down', function (e) {
        e.preventDefault();
        var parent = $(this).closest('.orderable');
        var next = parent.next(".orderable");
        if (next) {
            next.insertBefore(parent);
            var url;
            if (parent.hasClass('course-item')) {
                url = "/course/update-orders";
            } else if (parent.hasClass('section-item')) {
                url = "/section/update-orders";
            } else if (parent.hasClass('test-item')) {
                url = "/tests/update-orders";
            }
            swapOrders(next, parent, url);
        }
    })
});

function getCSRF() {
    var csrfHeaderName = $("meta[name='_csrf_header']").attr('content');
    var csrfHeaderContent = $("meta[name='_csrf']").attr('content');
    var headers = {};
    headers[csrfHeaderName] = csrfHeaderContent;
    return headers;
}


function swapOrders(prev, next, url) {
    var prevOrder = prev.data('order');
    var nextOrder = next.data('order');
    next.data('order', prevOrder);
    prev.data('order', nextOrder);
    var data = {
        "ids[]": [next.data('id'), prev.data('id')],
        'orders[]': [prevOrder, nextOrder]
    };
    $.ajax({
        url: url,
        data: data,
        method: 'POST',
        dataType: 'text',
        headers: getCSRF(),
        success: function (response) {
            console.log(repsonse)
        },
        error: function (xhr, status, err) {
            console.error(url + " -> " + status);
            console.error(err);
        }
    })
}

