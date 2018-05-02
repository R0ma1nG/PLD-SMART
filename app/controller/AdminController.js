var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");
var releve = require("../models/releve");

// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});


// Récupérer la liste des bennes avec quelques infos
// router.get


// Recupérer les détails d'une benne
router.get('/benneDetails/:idPoubelle', function(req, res) {
  new Promise( (resolve, reject) => {
    poubelle.findById(req.params.idPoubelle, function (err, poubelle) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(poubelle));
    });
  });
});


// Recupérer la liste des relèves correspondant à la date
router.get('/releves/:date', function(req, res) {
  new Promise( (resolve, reject) => {
    // var dateDemandee = new Date(req.params.date);
    var dateDemandee = new Date();
    //var dateDemandee = Date.parse(req.params.date);
    console.log(dateDemandee.getUTCDate());
    releve.find({}, function (err, releves) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(releves));
    });
  });
});


// TEST : Recupérer la liste des relèves
router.get('/releves/', function(req, res) {
  new Promise( (resolve, reject) => {
    releve.find({}, function (err, releves) {
      if (err) reject(res.status(500).send("There was a problem finding your poubelle in db"));
      resolve(res.status(200).send(releves));
    });
  });
});

module.exports = router;
