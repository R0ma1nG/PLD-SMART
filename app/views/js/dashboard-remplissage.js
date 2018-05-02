function init() {
    document.getElementById("loader").style.visibility = 'visible';
    var table;
    if($.fn.dataTable.isDataTable("#recap")) {
        table = $("#recap").DataTable();
    } else {
        table = $("#recap").DataTable({
            "orderClasses": false
        });
    }
    var url = "http://localhost:8080/api/poubelles";
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
            poubelles=data;
            document.getElementById("loader").style.visibility = 'hidden';
            var progressBar = document.getElementById("Progress_Bar");
            progressBar.value = 0;
            progressBar.max = data.length;
            data.forEach(function (element) {
                var pos = new google.maps.LatLng(element.lattitude, element.longitude);
                var contenu = [
                    '<img id="' + pos + '" src= "ic_zoom.png" height= 20 width= 20 onclick= "getMarker(this)" onmouseover= "" style= "cursor: pointer;" > </img>',
                    element.id_grandlyon,
                    element.adresse,
                    element.remplissage ? "oui" : "non",
                    element.gestionnaire
                ];
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    visible: true,
                    icon: icons[element.remplissage].icon
                });
                marker.addListener('click', function () {
                    if (infowindow) {
                        infowindow.close();
                    }
                    var contentString = '<h1> Info poubelle </h1>' +
                        '<div>' +
                        'adresse: ' + element.adresse + '</br>' +
                        'remplissage: ' + element.remplissage +
                        '</div>';
                    marker.contentStr = contentString;
                    infowindow = new google.maps.InfoWindow({
                        content: contentString
                    });
                    infowindow.open(map, marker);
                });
                markers.push(marker);
                window.setTimeout(function () {
                    table.row.add(contenu).draw();
                    progressBar.value += 1;
                }, 30);
            });
        });
        feather.replace()
};