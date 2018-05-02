var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");
var releve = require("../models/releve");

// middleware to use for all requests
router.use(function (req, res, next) {
  //console.log('Router middleware log, request : ', req.url); // do logging
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
router.get('/releves/:date', async function (req, res, next) {
  try {
    const releves = await releve.find({}).lean();
    const mesReleves = [];
    const poubelles = {};
    for (const releve of releves) {
      if (releve.idPoubelle) poubelles[releve.idPoubelle] = 1;
      else delete releves.releve;
    }
    for (const idPoubelle of Object.keys(poubelles)) {
      poubelles[idPoubelle] = await poubelle.findById(idPoubelle, "lattitude longitude");
    }
    for (const releve of releves) {
      var dateDemandee = new Date(req.params.date);
      var annee = dateDemandee.getUTCFullYear();
      var mois = dateDemandee.getUTCMonth();
      var jour = dateDemandee.getUTCDate();
      const actualDate = new Date(releve.date);
      if (actualDate.getUTCDate() == jour && actualDate.getUTCMonth() == mois && actualDate.getUTCFullYear() == annee) {
        console.log("GOOD BIN");
        mesReleves.push({
          _id: releve._id,
          date: releve.date,
          tauxRemplissage: releve.tauxRemplissage,
          idPoubelle: releve.idPoubelle,
          latitude: poubelles[releve.idPoubelle].lattitude,
          longitude: poubelles[releve.idPoubelle].longitude
        });
      }
    };
    res.status(200).send(mesReleves);
  }
  catch (e) {
    console.log("Error " + e);
    next(e);
  }
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
