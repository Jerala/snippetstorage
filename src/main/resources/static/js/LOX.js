$(document).ready(function () {
    $(".icon").click(function (event) {
        event.preventDefault();
        $(".usersSnippets").hide();
        //  $(".searchResults").fadeToggle("fast");
    });
});

$(':password').keyup(function () {
    var pas1 = $('input[name=pass1]');
    console.log(pas1);
    console.log(123);
    var pas2 = $('input[name=pass2]');
    if (pas1.val() == pas2.val()) {
        pas2.next().replaceWith('<span></span>');
    } else {
        pas2.next().replaceWith('<span id="xxx"></span>');
    }
});

function displayQueries(data) {

    var items = [];

    for (i = 0; i < data.length; i++) {
        items.push('<li>' + data[i].query + '</li>');
    }
    $('<ul/>', {
        'class': 'popSearchBlock',
        html: items.join('')
    }).appendTo('.popularSearch');
}

//});

function getPopQueries() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/getMostPopularQueries",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            displayQueries(data);
        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);

        }

    });
}

function getSearchRes() {
    document.querySelector('.searchResults').innerHTML = '';
    var tag = document.getElementById('search').value;
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/search?tagName=" + tag,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            displaySearchRes(data);
        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);

        }

    });
}

function displaySearchRes(data) {
    var items = [];
    for (var i = 0; i < data.length; i++) {
        var tags = separator(data[i].tags);
        items.push('<li><div class="tags">' + tags
            + '</div><a href="snippets/' + data[i].tags.split('#')[1] + '/' + data[i].user_id + '-' + data[i].snippet_name
            + '" id="' + data[i].snippetId + '">' + data[i].snippet_name + '</a></li>');
    }
    $('<ul/>', {
        'class': 'my-new-list snippets',
        html: items.join('')
    }).appendTo('.searchResults');
}

function getUserSnippets(json) {
    console.log("here");
    var data = JSON.parse(json);
    var items = [];

    for (var i = 0; i < data.length; i++) {
        var tags = separator(data[i].tags);
        items.push('<li><div class="tags">' + tags
            + '</div><a href="snippets/' + data[i].tags.split('#')[1] + '/' + data[i].user_id + '-' + data[i].snippet_name
            + '" id="' + data[i].snippetId + '">' + data[i].snippet_name + '</a></li>');
    }
    console.log(items);
    $('<ul/>', {
        'class': 'my-new-list snippets',
        html: items.join('')
    }).appendTo('.userSnippetContainer');
}

function getUserSnippets(tags) {

   // var data = JSON.parse(tags);
    var items = [];
   // var tags = separator(data);
    var sepTags = separator(tags);
    items.push('<li><div class="tags">' + sepTags
        + '</div>');
    $('<ul/>', {
        'class': 'my-new-list snippets',
        html: items.join('')
    }).appendTo('.tagsContainer');
}

function getPLName(pl_id) {
    var myHeaders = new Headers();
    var init = {
        method: 'GET',
        headers: myHeaders,
        mode: 'cors',
        cache: 'default'
    }

    fetch('/getPLName?plId=' + pl_id, init)
        .then(function (res) {
            return res.text()
        })
        .then(function (value) {
            console.log(value)
        });
}


$(':password').keyup(function () {
    var pas1 = $('input[name=pass1]');
    console.log(pas1);
    console.log(123);
    var pas2 = $('input[name=pass2]');
    if (pas1.val() == pas2.val()) {
        pas2.next().replaceWith('<span></span>');
    } else {
        pas2.next().replaceWith('<span id="xxx"></span>');
    }
});

$(document).ready(function () {
    $('.login-button').click(function () {
        $(this).next('.login-content').slideToggle();
        $(this).toggleClass('active');

        if ($(this).hasClass('active')) $(this).find('span').html('&#x25B2;')
        else $(this).find('span').html('&#x25BC;')
    })
});


$(init);

function init() {
    $('span').click(function () {
        if ($('tagField').val() == '') {
            $('.tagField').val($(this).text())
        }
        else {
            $('.tagField').val($('.tagField').val() + ' ' + $(this).text())
        }
    })
}

function separator(tags) {
    var tagsString = '';
    someVar = tags;
    splittedTags = someVar.split('#');
    for (i = 1; i < splittedTags.length; i++) {
        tagsString += '<span>#' + splittedTags[i] + '</span>';
    }
    return tagsString;
}

