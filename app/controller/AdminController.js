var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");
var releve = require("../models/releve");

// middleware to use for all requests
router.use(function (req, res, next) {
  console.log('Router middleware log, request : ', req.url); // do logging
  next(); // make sure we go to the next routes and don't stop here
});



// Recupérer les détails d'une benne
router.get('/benneDetails/:idPoubelle', function (req, res) {
  new Promise((resolve, reject) => {
    poubelle.findById(req.params.idPoubelle, function (err, poubelle) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(poubelle));
    });
  });
});


// Recupérer la liste des relèves correspondant à la date
router.get('/releves/:date', function (req, res) {
  new Promise((resolve, reject) => {
    releve.find({}, function (err, releves) {
      if (err) reject(res.status(500).send("There was a problem comparing the dates"));
      //var mesReleves= {'data': []};
      console.log("releves trouvés: " + releves);
      resolve(releves);
    });
  })
    .then((releves) => {
      console.log("Here come the promise.all");
      return Promise.all(releves.map( (releve) => {
        console.log("INITIAL RELEVE");
        console.log(releve);
        return new Promise((resolve, reject) => {
          var dateDemandee = new Date(req.params.date);
          var annee = dateDemandee.getUTCFullYear();
          var mois = dateDemandee.getUTCMonth();
          var jour = dateDemandee.getUTCDate();
          if (releve.date.getUTCDate() != jour || releve.date.getUTCMonth() != mois || releve.date.getUTCFullYear() != annee) {
            reject("Cant pass the condition");
          }
            poubelle.findById(releve.idPoubelle, "lattitude longitude", function (err, bin) {
              if (err) reject(res.status(500).send("impossible de trouver la poubelle associée à ce relevé " + err));
              resolve(bin);
            });
        })
          .then((bin) => {
            var newReleve = {
              _id: releve._id,
              date: releve.date,
              tauxRemplissage: releve.tauxRemplissage,
              idPoubelle: releve.idPoubelle,
              latitude: bin.lattitude,
              longitude: bin.longitude
            };
            console.log("NEW RELEVE");
            console.log(newReleve);
            return newReleve;
          });
      }));
    })
    .then((mesReleves) => {
      console.log("FINISH");
      res.status(200).send(mesReleves);
    });
});
  


router.put('/releves/:idReleve', function (req, res) {
  // Regarder si un dépot est en cours
  new Promise((resolve, reject) => {
    releve.findByIdAndUpdate(req.params.idReleve, { idPoubelle: req.body.idPoubelle }, { new: true }, function (err, releveMaj) {
      if (err) return (res.status(500).send("Impossible de mettre à jour l'id poubelle' : " + err));
      else resolve(res.status(200).send(releveMaj));
    });
  });
});

// Créer une relève
router.post('/releves', function (req, res) {
  var id = new mongoose.mongo.ObjectId();
  new Promise((resolve, reject) => {
    releve.create({
      _id: id,
      idPoubelle: req.body.idPoubelle,
      tauxRemplissage: req.body.tauxRemplissage,
      date: req.body.date
    }, function (err, relev) {
      if (err) reject(res.status(500).send("There was a problem creating your releve in db : " + err));
      resolve(res.status(200).send(relev));
    });
  });
});


// TEST : Recupérer la liste des relèves
router.get('/releves/', function (req, res) {
  new Promise((resolve, reject) => {
    releve.find({}, function (err, releves) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(releves));
    });
  });
});

module.exports = router;
