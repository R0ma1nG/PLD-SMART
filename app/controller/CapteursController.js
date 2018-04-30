var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var capteur = require("../models/capteur");



// middleware to use for all requests
router.use(function (req, res, next) {
  console.log('Router middleware log, request : ', req.url); // do logging
  next(); // make sure we go to the next routes and don't stop here
});


// get the list of all the sensors
router.get('/', function (req, res) {
  new Promise( (resolve, reject) => {
    capteur.find({}, function (err, sensors) {
      if (err) reject(res.status(500).send("There was a problem finding the sensors in db"));
      resolve(res.status(200).send(sensors));
    });
  });
});



// Créer un capteur
router.post('/', function (req, res) {
  var id = new mongoose.mongo.ObjectId();
  new Promise( (resolve, reject) => {
    capteur.create({
      _id: id,
      tokenCapteur: req.body.token,
      idPoubelle: req.body.idPoubelle,
    }, function (err, sensor) {
        if (err) reject(res.status(500).send("There was a problem creating your sensor in db : "+ err));
        resolve(res.status(200).send(sensor));
    });
  });
});

module.exports = router;
