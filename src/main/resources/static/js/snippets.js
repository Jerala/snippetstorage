function getMetaData(user_id, snippet_name) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/snippets/" + userId + "-" + snippetName,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            $("<b>" + data.snippetName + "</b>").prependTo($(".SnipName"));
            $("<span>" + data.uploadDate + "</span>").prependTo($(".release"));
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }

    });
}