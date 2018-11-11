function changeState(userId, snippetName) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/likes/changeState/" + userId + "-" + snippetName,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            // displayQueries(data);
        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);

        }

    });
}