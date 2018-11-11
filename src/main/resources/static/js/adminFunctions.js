function getUsers() {
    var myHeaders = new Headers();
    var init = {
        method: 'GET',
        headers: myHeaders,
        mode: 'cors',
        cache: 'default'
    }
    fetch('/getUsers', init).then(function(res) { return res.json(); })
    .then(function (data) {
            data.forEach(function(user) {
                var div = document.createElement('div');
                div.id = 'id' + user.userId;
                var ref = document.createElement('a');
                ref.href = '/' + user.userName;
                ref.appendChild(document.createTextNode(user.userName));

                var btn = document.createElement('button');
                btn.appendChild(document.createTextNode("DELETE USER"));
                btn.style.marginRight = '1em';
                btn.onclick = function(e) {
                    fetch('/deleteUserById?id=' + user.userId, init)
                        .then(
                            function (res) {
                                console.log(res);
                                document.getElementById('usersRefs').removeChild(
                                    document.getElementById('id' + user.userId));
                            }
                                )
                }

                document.getElementById('usersRefs').appendChild(div);
                document.getElementById('id' + user.userId).appendChild(btn);
                document.getElementById('id' + user.userId).appendChild(ref);
            })
    })
}

function approveSnippet(uri) {
    var subUri = uri.split("/")[3].split("-");
    var userId = subUri[0];
    var snippetName = subUri[1];
    var myHeaders = new Headers();
    var init = {
        method: 'GET',
        headers: myHeaders,
        mode: 'cors',
        cache: 'default'
    }
    fetch('/approveSnippet?userId=' + userId + '&snippetName=' + snippetName, init)
        .then(function(res) { console.log(res); })
}

function deleteSnippet(uri) {
    var subUri = uri.split("/")[3].split("-");
    var userId = subUri[0];
    var snippetName = subUri[1];
    var myHeaders = new Headers();
    var init = {
        method: 'GET',
        headers: myHeaders,
        mode: 'cors',
        cache: 'default'
    }
    fetch('/deleteSnippet?userId=' + userId + '&snippetName=' + snippetName, init)
        .then(function(res) { console.log(res); })
}