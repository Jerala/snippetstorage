function changeState(userId, snippetName) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/likes/changeState/" + userId + "-" + snippetName,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
           // var img_src = document.getElementById("like").getAttribute("src");
        },
        error: function (e) {

            // var json = "<h4>Ajax Response</h4><pre>"
            //     + e.responseText + "</pre>";
            // $('#feedback').html(json);
            if(document.getElementById("like").getAttribute("src") === "https://vignette.wikia.nocookie.net/rutube9658/images/1/1c/TgOZSxs_0s.jpg/revision/latest?cb=20170211070720&path-prefix=ru") {
                document.getElementById("like").setAttribute("src", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1VxRDH1MRYnaAmdHVGQH359Mq0_Bb1wfw8qIiUWZlQWbsUABmqg")
                like_count += 1;
            }
            else {
                document.getElementById("like").setAttribute("src","https://vignette.wikia.nocookie.net/rutube9658/images/1/1c/TgOZSxs_0s.jpg/revision/latest?cb=20170211070720&path-prefix=ru");
                like_count -= 1;
            }
            document.getElementById("start_like_count").innerText = like_count;
        }
    });
}

function chooseState(userId, snippetName) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/likes/" + userId + "-" + snippetName,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(data) {
            // var img_src = document.getElementById("like").getAttribute("src");
        },
        error: function (e) {

            if(e.responseText === "yes") {
                document.getElementById("like").setAttribute("src", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1VxRDH1MRYnaAmdHVGQH359Mq0_Bb1wfw8qIiUWZlQWbsUABmqg");
            }
            else
                document.getElementById("like").setAttribute("src","https://vignette.wikia.nocookie.net/rutube9658/images/1/1c/TgOZSxs_0s.jpg/revision/latest?cb=20170211070720&path-prefix=ru");
        }
    });
}
var like_count = +document.getElementById("start_like_count").innerText;