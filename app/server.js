// server.js

//Get the app
var app = require('./app');

//set the port
var port = process.env.PORT || 8080;

//Run the server
var server = app.listen(port, function() {
  console.log('Our server listening on port '+port);
});
