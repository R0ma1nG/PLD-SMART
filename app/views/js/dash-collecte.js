function dateChanged(e){
    $(document).ready(function () {
        var url = "/api/admin/releves/"+e.target.value;
        console.log(url);
        /*$.ajax
            ({
                type: "GET",
                contentType: "application/json",
                url: `${url}`,
                dataType: 'json',
                error: function (err) {
                    alert('An error occured !');
                }
            })
            .done(function (data) {
                alert("request ok");
            });*/
    });
}