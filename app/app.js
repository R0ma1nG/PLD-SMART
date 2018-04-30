//app.js


//Packages needed
var express = require('express');
var app = express();
var db = require('./db');
var bodyParser = require('body-parser');
var passport = require('passport');
var cookieParser = require('cookie-parser');
var session = require('express-session');

// Set parameters (templates, middlewares..)
app.engine('ejs', require('ejs').renderFile);
app.set('view engine', 'ejs');
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Passport required
app.use(session({ secret: 'smart' })); // session secret
app.use(passport.initialize());
app.use(passport.session()); // persistent login sessions

// Configure the passport
require('./config/passport.js')(passport);


// Controller needeed to control routes
var UsersController = require('./controller/UsersController');
var PoubellesController = require('./controller/PoubellesController');
var DepotsController = require('./controller/DepotsController');
var AssociationsController = require('./controller/AssociationsController');
var CapteursController = require('./controller/CapteursController');
var DepotsEnCoursController = require('./controller/DepotsEnCoursController');
var UtilsController = require('./controller/UtilsController');
var AdminController = require('./controller/AdminController');
var AuthController = require('./controller/AuthController')(app, passport);
var AuthWebController = require('./controller/AuthWebController')(app, passport);
var WebsiteController = require('./controller/WebsiteController');


// Bind routes to controllers
app.use('/api/users', UsersController);
app.use('/api/poubelles', PoubellesController);
app.use('/api/depots', DepotsController);
app.use('/api/associations', AssociationsController);
app.use('/api/capteurs', CapteursController);
app.use('/api/depotsEnCours', DepotsEnCoursController);
app.use('/api/admin', AdminController);
app.use('/api/utils', UtilsController);
app.use('/', WebsiteController);


//First redirect (non définitif)
app.get('/', function (req, res) {
  res.render('index');
});

//export the app
module.exports = app;
