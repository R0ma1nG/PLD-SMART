var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");

// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});


// get the list of all the poubelles
router.get('/', function (req, res) {
  console.log("HELLO IM TRYING TO GET THE POUBELLES")
  poubelle.find({}, function (err, poubelles) {
    if (err) return res.status(500).send("There was a problem finding the poubelles in db");
    res.status(200).send(poubelles);
  });
});


module.exports = router;
