//app.js


//Packages needed
var express = require('express');
var app = express();
var db = require('./db');
var bodyParser = require('body-parser');

// Set parameters (templates, middlewares..)
app.engine('ejs', require('ejs').renderFile);
app.set('view engine', 'ejs');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Controller needeed to control routes
var UsersController = require('./controller/UsersController');
var PoubellesController = require('./controller/PoubellesController');
var DepotsController = require('./controller/DepotsController');


// Bind routes to controllers
app.use('/api/users', UsersController);
app.use('/api/poubelles', PoubellesController);
app.use('/api/depots', DepotsController);

//First redirect (non définitif)
app.get('/', function (req, res) {
  res.render('index');
});

//export the app
module.exports = app;
