//var markers = [];
var poubelles = [];

function getMarker(img) {
    var marker = markers.find(function (element) {
        return element.position == img.id;
    });
    map.setCenter(marker.position);
    map.setZoom(16);
    google.maps.event.trigger(marker, 'click');
    window.scrollTo(0, 0);
};

function init() {
    document.getElementById("loader").style.visibility = 'visible';
    var table;
    if ($.fn.dataTable.isDataTable("#recap")) {
        table = $("#recap").DataTable();
    } else {
        table = $("#recap").DataTable({
            "orderClasses": false
        });
    }
    var url = "/api/poubelles";
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
            poubelles = data;
            document.getElementById("loader").style.visibility = 'hidden';
            var progressBar = document.getElementById("Progress_Bar");
            progressBar.value = 0;
            progressBar.max = data.length;
            data.forEach(function (element) {
                var pos = new google.maps.LatLng(element.lattitude, element.longitude);
                var etatPoubelle=element.remplissage ? "plein" : "disponible";
                var contenu = [
                    '<img id="' + pos + '" src= "ic_zoom.png" height= 20 width= 20 onclick= "getMarker(this);" onmouseover= "" style= "cursor: pointer;" > </img>',
                    element.id_grandlyon,
                    element.adresse,
                    etatPoubelle,
                    element.gestionnaire
                ];
                var marker = new google.maps.Marker({
                    position: pos,
                    map: map,
                    visible: true,
                    icon: icons[element.remplissage].icon,
                });
                marker.addListener('click', function () {
                    if (infowindow) {
                        infowindow.close();
                    }
                    var contentString = '<h1> Silo '+element.id_grandlyon +'</h1>' +
                        '<div>' +
                        'adresse: ' + element.adresse +'</br>' +
                        'gestionnaire: ' + element.gestionnaire +'</br>'+
                        'Etat de remplissage: ' + etatPoubelle +
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

function toogleRemplissage(element) {
    var table = $("#recap").DataTable();
    table.column(3).search( element.checked ? "plein" : "disponible" ).draw();
    if(element.checked) {
        var i = 0;
        markers.forEach(function (marker) {
            if (!poubelles[i].remplissage) {
                marker.setVisible(false);
            }
            i++;
        });
    } else {
        markers.forEach(function (marker) {
            marker.setVisible(true);
        });
    }
}

function changeLocationFilter(element) {
    var table = $("#recap").DataTable();
    table.column(2).search(element.value).draw();
    var data = table.rows().data();
    console.log(data);
    console.log(data.count);
    /*data.forEach(function(element) {
        var found = poubelles.find(function(poubelle) {
            return element. > 10;
        });
    })*/
}

function submitSearch(element) {
    var table = $("#recap").DataTable();
    table.search(element.value).draw();
    markers.forEach(function (marker) {
        marker.setVisible(false);
    });
    var data = table.rows().data();
    console.log(data.count());
    console.log(table.rows( { filter : 'applied'} ).nodes().length);
    /*data.forEach(function(elem) {
        markers.forEach(function(marker) {
            
        });
    });*/
}