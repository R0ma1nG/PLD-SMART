function logout() {
    var url = "/logout";
    $.ajax 
        ({
            type: "GET",
            url: `${url}`,
            error: function (err) {
                alert('An error occured !');
            }
        })
        .done(function (data) {
            window.location.href = "/";
        });
}