var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var depot = require("../models/depot");
var depotEnCours = require("../models/depotEnCours");
var user = require("../models/utilisateur");
var association = require("../models/association");
var poubelle = require("../models/poubelle");
var capteur = require("../models/capteur");

// middleware to use for all requests
router.use(function(req, res, next) {
  if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});

// TEST : récupération de tous les dépots
router.get('/', function (req, res) {
  new Promise( (resolve, reject) => {
    depot.find({}, function (err, depot) {
      if (err) reject(res.status(500).send("There was a problem finding depots in db : "+ err));
      resolve(res.status(200).send(depot));
    });
  });
});

// TODO : limiter le nombre de dépots récupérés ?
router.get('/historique/:idUser', function (req, res) {
  var userId = req.params.idUser;
  console.log("HELLO IM TRYING TO GET THE DEPOTS OF USER "+userId)
  new Promise( (resolve, reject) => {
    depot.find({ 'idUtilisateur': userId}, "date montant idAssoc", function (err, depots) {
      if (err) reject(res.status(500).send("There was a problem finding your depots in db : "+ err));
        console.log(depots);
        resolve(depots);
    });
  })
  .then( (depots) => {
    Promise.all(depots.map( (depot) => {
      return new Promise( (resolve, reject) => {
        association.findById(depot.idAssoc, "nom", (err, asso) => {
          if (err) reject(res.status(500).send("Problem finding the name of the asso" + err));
          resolve(asso.nom);
        })
      })
      .then( (assoName) => {
        var newDepot = {
            _id: depot.id,
            date: depot.date,
            montant: depot.montant,
            nom: assoName
          };
          return newDepot;
      });
    }))
    .then( (newDepots) => res.status(200).send(newDepots));
  });
});


module.exports = router;
