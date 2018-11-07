$(document).ready(function () {

    $("#searchForm").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {
    var tag = document.getElementById('searchArea').value;
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/search?tagName=" + tag,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            successCase(data);
        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);

        }

    });
}

function successCase(data) {
    var json = JSON.stringify(data);
//     data.forEach((i) => {
//         var artistName = "<div style='color:red;'>" + i.PL_ID + "</div>";
//     // var artistId = "<div style='color:green;'>" + i.artistId + "</div>";
//     // var artistLinkUrl = "<div style='color:blue;'>" + i.artistLinkUrl + "</div>";
//     json += JSON.stringify(data);
// });
    $('#response').html(json);

    console.log("SUCCESS : ", data);
}

function getMostPopularQueries() {

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/getMostPopularQueries",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
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

function displayQueries(data) {
    var json = JSON.stringify(data);

    $('#popularQueries').html(json);

    console.log("SUCCESS : ", data);
}