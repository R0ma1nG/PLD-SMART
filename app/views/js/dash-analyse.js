var map;
var infowindow;
var markers = [];

function initMap() {
    var lyon = { lat: 45.75, lng: 4.85 };
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 11,
        center: lyon
    });
    /*var marker = new google.maps.Marker({
        position: lyon,
        map: map
    });*/
}

function init() {
    feather.replace();
    var url = "/api/suggestionPoubelles/locations";
    $.ajax
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
            console.log(data.length);
            data.forEach(function (element) {
                var pos = new google.maps.LatLng(element.lattitude, element.longitude);
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    visible: true,
                    icon: '/ic_bin.png'
                });
                marker.addListener('click', function () {
                    if (infowindow) {
                        infowindow.close();
                    }
                    var contentString = '<h4> Suggestion </h4>' +
                        '<div>' +
                        'latitude : ' + element.lattitude + '</br>' +
                        'longitude : ' + element.longitude +
                        '</div>';
                    infowindow = new google.maps.InfoWindow({
                        content: contentString
                    });
                    infowindow.open(map, marker);
                });
                markers.push(marker);
            });
        });
}