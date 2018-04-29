var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var association = require("../models/association");


// middleware to use for all requests
router.use(function(req, res, next) {
  if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});



// Récupération de toutes les associations
router.get('/', function (req, res) {
  new Promise( (resolve, reject) => {
    association.find({}, "nom description logoUrl", function (err, associations) {
      if (err) reject(res.status(500).send("There was a problem finding charities in db : "+ err));
      resolve(res.status(200).send(associations));
    });
  });
});

// Créer une assoc'
router.post('/', function (req, res) {
  var id = new mongoose.mongo.ObjectId();
  new Promise( (resolve, reject) => {
    association.create({
    _id: id,
    nom: req.body.nom,
    rib: req.body.rib,
    description: req.body.description
    }, function (err, assoc) {
          if (err) reject(res.status(500).send("There was a problem creating your association in db : "+ err));
          resolve(res.status(200).send(assoc));
    });
  });
});

// get the information about a charity
router.get('/:idAssoc', function (req, res) {
  new Promise( (resolve, reject) => {
    association.findById(req.params.idAssoc, "nom description logoUrl", function (err, assoc) {
    if (err) reject(res.status(500).send("There was a problem finding your charity in db"));
    resolve(res.status(200).send(assoc));
    });
  });
});

module.exports = router;
