var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var suggestion = require("../models/suggestion_poubelle");

// middleware to use for all requests
router.use(function(req, res, next) {
  if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});


// get the list of all the sugestion's location
router.get('/locations/', function (req, res) {
  new Promise( (resolve, reject) => {
    suggestion.find({}, "lattitude longitude" ,function (err, suggs) {
      if (err) reject(res.status(500).send("There was a problem finding the sugestions in db"));
      resolve(res.status(200).send(suggs));
    });
  });
});

module.exports = router;
