function TestGetRequest() {
    $.get("/api/association", function(data, status){
        document.getElementById("demo").style.color = "red";
        alert("Data id: " + data[0]._id + "\nStatus: " + status);
    });
}
