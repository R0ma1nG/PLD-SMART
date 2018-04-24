var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var depot = require("../models/depot");

// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});



router.post('/newDepot', function (req, res) {
  console.log("HELLO IM TRYING TO CREATE A DEPOT");
  var date1 = new Date('December 17, 1995 03:24:00');
  // var yvesMontant = req.body.montant;
  // var idUser = req.body.idUtilisateur;
  // var idAss = req.body.idAssoc;
  // var idCap = req.body.idCapteur;
  depot.create({
    // date: req.body.date,
    // date: date1,
    montant: req.body.montant,
    idUtilisateur: req.body.idUtilisateur,
    idAssoc: req.body.idAssoc,
    idCapteur: req.body.idCapteur
  }, function (err, depot) {
    if (err) return res.status(500).send("There was a problem creating your depot in db");
    res.status(200).send(idUtilisateur);
  });
  // res.status(200).send(date1 +'   '+ yvesMontant +'   '+ idUser +'   '+ idAss +'   '+ idCap);
});

router.get('/', function (req, res) {
  console.log("HELLO IM TRYING TO GET ALL THE DEPOTS")
  depot.find({}, function (err, depot) {
    if (err) return res.status(500).send("There was a problem finding depots in db");
    res.status(200).send(depot);
  });
});

router.get('/historique/:id', function (req, res) {
  var userId = req.params.id;
  console.log("HELLO IM TRYING TO GET THE DEPOTS OF USER "+userId)
  depot.find({ 'idUtilisateur': userId}, function (err, depots) {
    if (err) return res.status(500).send("There was a problem finding your depots in db");
    res.status(200).send(userId);
  });
});


module.exports = router;
