var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");

// middleware to use for all requests
router.use(function(req, res, next) {
  if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});


// get the list of all the poubelles
router.get('/infos', function (req, res) {
  new Promise( (resolve, reject) => {
    poubelle.find({}, "lattitude longitude remplissage" ,function (err, poubelles) {
      if (err) reject(res.status(500).send("There was a problem finding the poubelles in db"));
      resolve(res.status(200).send(poubelles));
    });
  });
});


// get the adresse of one poubelle
router.get('/adresse/:idPoubelle', function (req, res) {
  new Promise( (resolve, reject) => {
    poubelle.findById(req.params.idPoubelle, "adresse", function (err, poubelle) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(poubelle));
    });
  });
});


// Créer une poubelle
router.post('/newPoubelle', function (req, res) {
  var id = new mongoose.mongo.ObjectId();
  new Promise( (resolve, reject) => {
    poubelle.create({
      _id: id,
      adresse: req.body.adresse,
      lattitude: req.body.latitude,
      longitude: req.body.longitude,
      remplissage: req.body.statut
    }, function (err, bin) {
        if (err) reject(res.status(500).send("There was a problem creating your poubelle in db : "+ err));
        resolve(res.status(200).send(bin));
    });
  });
});

module.exports = router;
