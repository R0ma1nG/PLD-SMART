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
}