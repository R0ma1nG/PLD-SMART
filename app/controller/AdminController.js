var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");
var releve = require("../models/releve");

/// middleware to use for all requests
router.use(function (req, res, next) {
  if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {
    console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});


// Recupérer la liste des releves d'une benne
router.get('/benneDetails/:idPoubelle', function (req, res) {
  new Promise((resolve, reject) => {
    //   poubelle.findById(req.params.idPoubelle, function (err, poubelle) {
    //     if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
    //     resolve(poubelle);
    //   });
    // })
    // .then( (bin) => {
    // console.log(bin);
    releve.find({ idPoubelle: req.params.idPoubelle }, function (err, releves) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(releves));
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
      if (releve.idPoubelle && !poubelles[releve.idPoubelle]) poubelles[releve.idPoubelle] = 1;
      else if (!releve.idPoubelle) {
        delete releves.releve;
      }
    }
    for (const idPoubelle of Object.keys(poubelles)) {
      poubelles[idPoubelle] = await poubelle.findById(idPoubelle, "lattitude longitude");
    }
    var dateDemandee = new Date(req.params.date);
    var annee = dateDemandee.getUTCFullYear();
    var mois = dateDemandee.getUTCMonth();
    var jour = dateDemandee.getUTCDate();
    for (const rel of releves) {
      const actualDate = new Date(rel.date);
      if (actualDate.getUTCDate() == jour && actualDate.getUTCMonth() == mois && actualDate.getUTCFullYear() == annee) {
        mesReleves.push({
          _id: rel._id,
          date: rel.date,
          tauxRemplissage: rel.tauxRemplissage,
          idPoubelle: rel.idPoubelle,
          lattitude: poubelles[rel.idPoubelle].lattitude,
          longitude: poubelles[rel.idPoubelle].longitude
        });
      }

    }
    res.status(200).send(mesReleves);
  }
  catch (e) {
    console.log("Error " + e);
    next(e);
  }
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

module.exports = router;
