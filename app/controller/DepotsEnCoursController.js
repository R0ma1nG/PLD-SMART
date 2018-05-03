var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var depot = require("../models/depot");
var depotEnCours = require("../models/depotEnCours");
var user = require("../models/utilisateur");
var poubelle = require("../models/poubelle");
var capteur = require("../models/capteur");

// middleware to use for all requests
router.use(function(req, res, next) {
  if (req.url.match('ajoutDechet')) next();
  else if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});

// Créer un dépot en cours
// TODO : crash server si la benne est pleine
// TODO Vérifier que le capteur n'est pas déjà utilisé.
router.post('/demarrerScan', function (req, res) {
  new Promise( (resolve, reject) => {
    // On récupère l'id de la poubelle à partir de l'idCapteur
    capteur.findById(req.body.idCapteur, "idPoubelle", function (err, bin) {
      if (err) reject(res.status(500).send("Ce code QR n'est pas reconnu ou n'est associé à aucune benne."));
      else resolve(bin);
    });
  })
  // On vérifie que la poubelle existe et n'est pas pleine
  .then( (bin) => {
      return poubelle.findById(bin.idPoubelle, "remplissage", function (err, benne) {
        if (err) return(res.status(500).send("Impossible de vérifier l'état de la benne"));
        if (benne === null) res.status(500).send("Impossible de trouver la benne recherchée");
        if (benne.remplissage == 1) res.status(200).send("Cette benne est pleine. Veuillez réessayer plus tard.");
        return benne;
      });
  })
  // On récupère l'association de l'utilisateur
  .then( (benne) => {
    return user.findById(req.body.idUtilisateur, "idAssoc",function (err, utilisateur) {
        if (err) res.status(500).send("Impossible de trouver l'association que vous avez choisie.");
        else return utilisateur;
      });
  })
  // On créé le dépotEnCours avec un montant initial = 0
  .then( (utilisateur) => {
    var dateNow = new Date();
    var id = new mongoose.mongo.ObjectId();
    var montantInitial = 0;
    depotEnCours.create({
      _id: id,
      date: dateNow,
      idUtilisateur: req.body.idUtilisateur,
      idAssoc: utilisateur.idAssoc,
      montant: montantInitial,
      idCapteur: req.body.idCapteur
    }, function (err, dept) {
      if (err) res.status(500).send("Impossible de créer votre dépot. Veuillez réessayer plus tard : "+ err);
      else res.status(200).send(dept);
    });
  });
});

// Incrémenter un dépot en cours
router.put('/ajoutDechet/:idCapteur',function (req, res) {
  // Regarder si un dépot est en cours
  new Promise( (resolve, reject) => {
    var token = req.body.token;
    capteur.findById(req.params.idCapteur, function (err, capteur) {
      console.log(req.body.token);
      console.log(capteur.tokenCapteur);
      if (capteur.tokenCapteur != token) reject(res.status(500).send("Le token ne correspond pas : "+ err));
      else resolve();
    });
  })
    .then( () => {
      return depotEnCours.findOne({idCapteur: req.params.idCapteur}, function (err, depotEC) {
        if (err) return(res.status(500).send("Erreur : "+ err));
        if (depotEC === null ) return(res.status(200).send("Pas de dépot associé à ce capteur actuellement"));
        else return(depotEC);
      });
    })
  // Incrémenter le montant du dépot
  .then( (depotEC) => {
    var nouveauMontant = parseInt(depotEC.montant + 1);
    return depotEnCours.findByIdAndUpdate(depotEC._id, {montant: nouveauMontant}, {new: true}, function(err, depotMaJ) {
      if (err) return (res.status(500).send("Impossible d'incrémenter le montant' : "+ err));
      else return res.status(200).send(depotMaJ);
    });
  });
});

// Supprimer un dépot en cours et créer le dépot terminé associé
router.post('/terminerScan/:idCapteur', function (req, res) {
  var idDepotTermine = new mongoose.mongo.ObjectId();
  new Promise( (resolve, reject) => {
    // On récupère le dépot en cours
    depotEnCours.findOne({idCapteur: req.params.idCapteur}, function(err, dep) {
      if (err) reject(res.status(500).send("Erreur : "+ err));
      if (dep === null ) reject(res.status(500).send("Ce depot est deja terminé."));
      else resolve(dep);
    });
  })
  // Création du nouveau dépot
  .then( (dep) => {
    if (dep.montant == 0) return;
    else return depot.create({
      _id: idDepotTermine,
      date: dep.date,
      montant: dep.montant,
      idUtilisateur: dep.idUtilisateur,
      idCapteur: dep.idCapteur,
      idAssoc: dep.idAssoc
     }, function (err, depotSaved) {
      console.log('Depot terminé : '+depotSaved);
      if (err) return res.status(500).send("There was a problem validating your depot in db : "+ err);
      else return depotSaved
    })
  })
  // Suppression du dépot en cours
  .then( () => {
    return depotEnCours.findOneAndRemove({idCapteur: req.params.idCapteur}, function (err, depotSupprime) {
      console.log(depotSupprime+' a été delete')
      if (err) res.status(500).send("There was a problem validating your depot in db : "+ err);
      else return depotSupprime;
    });
  })
  // On retourne le nouveau depot
  .then( () => {
    depot.findById(idDepotTermine, "date montant idAssoc",function (err, infos) {
      if (err) res.status(500).send("There was a problem validating your depot in db : "+ err);
      res.status(200).send(infos);
    });
  });
});


// TEST : récupération de tous les dépots en cours
router.get('/', function (req, res) {
  new Promise( (resolve, reject) => {
    depotEnCours.find({}, function (err, depot) {
      if (err) reject(res.status(500).send("There was a problem finding depots in db : "+ err));
      resolve(res.status(200).send(depot));
    });
  });
});



module.exports = router;
