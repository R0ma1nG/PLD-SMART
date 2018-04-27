var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var association = require("../models/association");


// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});



// Récupération de toutes les associations
router.get('/', function (req, res) {
  console.log("HELLO IM TRYING TO GET ALL THE ASSOCIATIONS")
  association.find({}, "nom description logoUrl", function (err, associations) {
    if (err) return res.status(500).send("There was a problem finding charities in db : "+ err);
    res.status(200).send(associations);
  });
});

// Créer une assoc'
router.post('/', function (req, res) {
  console.log("HELLO IM TRYING TO CREATE AN ASSOC");
  var id = new mongoose.mongo.ObjectId();
  association.create({
    _id: id,
    nom: req.body.nom,
    rib: req.body.rib,
    description: req.body.description
  }, function (err, assoc) {
        if (err) return res.status(500).send("There was a problem creating your association in db : "+ err);
        res.status(200).send(assoc);
  });
});

// get the information about a charity
router.get('/:idAssoc', function (req, res) {
  console.log("HELLO IM TRYING TO GET AN ASSOC")
  association.findById(req.params.idAssoc, "nom description logoUrl", function (err, assoc) {
    if (err) return res.status(500).send("There was a problem finding your charity in db");
    res.status(200).send(assoc);
  });
});

module.exports = router;
