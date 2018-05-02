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

function initMap() {
    var lyon = { lat: 45.75, lng: 4.85 };
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 11,
        center: lyon
    });
    var marker = new google.maps.Marker({
        position: lyon,
        map: map
    });
}

function init() {
    feather.replace();
    document.getElementById('datePicker').valueAsDate = new Date();
    var url = "/api/admin/releves/";
    /*$.ajax 
        ({
            type: "GET",
            contentType: "application/json",
            //the url where you want to sent the userName and password to
            url: `${url}`,
            dataType: 'json',
            error: function (err) {
                alert('An error occured !');
            }
        })
        .done(function (data) {
            data.forEach(function (element) {
                var contenu = '<tr>';
                contenu += '<td>'+element._id+'</td><td>'+element.date+'</td><td>'+element.tauxRemplissage+'</td>';
                contenu += '</tr>';
                $("#recap").append(contenu);
            });
        });*/
}