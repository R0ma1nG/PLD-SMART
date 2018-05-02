var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var depot = require("../models/depot");
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

// Créer un dépot
router.post('/demarrerScan', function (req, res) {
  new Promise( (resolve, reject) => {
    capteur.findById(req.body.idCapteur, "idPoubelle", function (err, bin) {
      if (err) reject(res.status(500).send("Ce code QR n'est pas reconnu ou n'est associé à aucune benne."));
      else resolve(bin);
    });
  })
  .then( (bin) => {
      return poubelle.findById(bin.idPoubelle, "remplissage", function (err, benne) {
        if (err) return(res.status(500).send("Impossible de vérifier l'état de la benne"));
        if (benne === null) res.status(500).send("Impossible de trouver la benne recherchée");
        if (benne.remplissage == 1) res.status(200).send("Cette benne est pleine. Veuillez réessayer plus tard.");
        return benne;
      });
  })
  .then( (benne) => {
    return user.findById(req.body.idUtilisateur, "idAssoc",function (err, utilisateur) {
        if (err) res.status(500).send("There was a problem finding your association in db");
        else return utilisateur;
      });
  })
  .then( (utilisateur) => {
    var dateNow = new Date();
    var id = new mongoose.mongo.ObjectId();
    depot.create({
      _id: id,
      date: dateNow,
      idUtilisateur: req.body.idUtilisateur,
      idAssoc: utilisateur.idAssoc,
      idCapteur: req.body.idCapteur
    }, function (err, depot) {
      if (err) res.status(500).send("There was a problem creating your depot in db : "+ err);
      else res.status(200).send(depot);
    });
  });
});

// Terminer un dépot en ajoutant le montant
router.put('/terminerScan/:idDepot', function (req, res) {
  new Promise( (resolve, reject) => {
    depot.update({ _id: req.params.idDepot }, { montant: req.body.montant }, function (err, raw) {
      if (err) reject(res.status(500).send("There was a problem validating your depot in db : "+ err));
      resolve();
    });
  })
  .then( () => {
    depot.findById(req.params.idDepot, "date montant idAssoc",function (err, infos) {
      if (err) res.status(500).send("There was a problem validating your depot in db : "+ err);
      res.status(200).send(infos);
    });
  });
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
