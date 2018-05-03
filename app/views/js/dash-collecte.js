var map;
var markers = [];
var infowindow;

function dateChanged(e) {
    $(document).ready(function () {
        var url = "/api/admin/releves/" + e.target.value;
        console.log(url);
        fillTable(url);
    });
}

function init() {
    feather.replace();
    document.getElementById('datePicker').valueAsDate = new Date();
    var url = "/api/admin/releves/" + formatDate(new Date);
    console.log(url);
    fillTable(url);
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

function fillTable(url) {
    document.getElementById("loader").style.visibility = 'visible';
    document.getElementById("Progress_Bar").style.visibility = 'hidden';
    var table;
    if ($.fn.dataTable.isDataTable("#recap")) {
        table = $("#recap").DataTable();
    } else {
        table = $("#recap").DataTable({
            "orderClasses": false
        });
    }
    table.clear();
    $.ajax
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
            console.log("done, data length : ");
            console.log(data.length);
            document.getElementById("loader").style.visibility = 'hidden';
            var progressBar = document.getElementById("Progress_Bar");
            progressBar.style.visibility = 'visible';
            progressBar.value = 0;
            if (data.length == 0) {
                progressBar.style.visibility = 'hidden';
                alert("Aucune données pour cette date");
            } else {
                progressBar.max = data.length;
            }
            data.forEach(function (element) {
                var contenu = [
                    element._id,
                    element.date,
                    element.tauxRemplissage
                ];
                window.setTimeout(function () {
                    table.row.add(contenu).draw();
                    progressBar.value += 1;
                }, 30);
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

function initMap() {
    var lyon = { lat: 45.75, lng: 4.85 };
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: lyon
    });
    /*var marker = new google.maps.Marker({
        position: lyon,
        map: map
    });*/
}