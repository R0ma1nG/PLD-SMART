var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var poubelle = require("../models/poubelle");

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

module.exports = router;
