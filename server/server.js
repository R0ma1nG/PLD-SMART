// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var mongoose   = require('mongoose');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/database'); // connect to our database
var User       = require('./app/models/user');
var Association= require('./app/models/association');

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;        // set our port

// ROUTES FOR OUR API
// =============================================================================
var router = express.Router();              // get an instance of the express Router

// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log'); // do logging
    next(); // make sure we go to the next routes and don't stop here
});

// test route to make sure everything is working (accessed at GET http://localhost:8080/api)
router.get('/', function(req, res) {
    res.json({ message: 'Welcome to Recyc\'Lyon api!' });
});

// more routes for our API will happen here

// GET all the associations
router.route('/association')
    .get(function(req, res) {
        Association.find(function(err, associations) {
            if (err)
                res.send(err);
            res.json(associations);
        })
    });

// GET the association with assoc_id
router.route('/association/:assoc_id')
    .get(function(req, res){
        Association.findById(req.params.assoc_id, function(err, association){
            if(err)
                res.send(err);
            res.json(association);
        });
    }
);

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/api', router);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Server is now listening on port ' + port);
