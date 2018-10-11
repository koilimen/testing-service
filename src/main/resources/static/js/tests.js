$(document).ready(function () {
    $('body').on('click', '.sort-up', function (e) {
        e.preventDefault();
        var parent = $(this).closest('.tests-list__item');
        var prev = parent.prev(".tests-list__item:not(.new)");
        if (prev) {
            prev.insertAfter(parent);
            swapOrders(parent, prev);
        }
    })
    $('body').on('click', '.sort-down', function (e) {
        e.preventDefault();
        var parent = $(this).closest('.tests-list__item');
        var next = parent.next(".tests-list__item:not(.new)");
        if (next) {
            next.insertBefore(parent);
            swapOrders(next, parent)
        }
    })
});

function swapOrders(prev, next) {
    var prevOrder = prev.data('order');
    var nextOrder = next.data('order');
    next.data('order', prevOrder);
    prev.data('order', nextOrder);
    var data = {
        "ids[]": [next.data('id'), prev.data('id')],
        'orders[]': [prevOrder, nextOrder]
    };

    var url = '/tests/update-orders';
    $.ajax({
        url: url,
        data: data,
        method: 'POST',
        dataType: 'text',
        success: function (response) {
            console.log(repsonse)
        },
        error: function (xhr, status, err) {
            console.error(url + " -> " + status);
            console.error(err);
        }
    })
}

