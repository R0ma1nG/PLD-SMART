var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var utilisateur = require("../models/utilisateur");


// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});

 
// ROUTES FOR USERS
// =============================================================================

// create a new user
router.post('/', function (req, res) {

  var newUser = new utilisateur();

  // set the user's local credentials
  newUser.mail    = req.body.mail;
  newUser.motDePasse = newUser.generateHash(req.body.motDePasse);
  newUser.nom = req.body.nom;
  newUser.adresse = req.body.adresse;
  newUser.dateNaissance = req.body.dateNaissance;
  newUser.sexe = req.body.sexe;

  console.log("New User : "+newUser);

  // save the user
  newUser.save(function (err, utilisateur) {
    if (err){
    return res.status(500).send("There was a problem adding infos to db");
  } 
    console.log("User created ", utilisateur);
    res.status(200).send(utilisateur.nom+" has been created");
  });
});

// get the list of all users
router.get('/', function (req, res) {
  console.log("HELLO IM TRYING TO GET USERS")
  utilisateur.find({}, function (err, utilisateurs) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    res.status(200).send(utilisateurs);
  });
});

/* WILL BE IMPLEMENTED SOON
router.post('/', function (req, res) {
  User.create({
    username: req.body.username,
    password: req.body.password,
    association: req.body.association
  },
  function (err, user) {
    if (err) return res.status(500).send("There was a problem adding infos to db");
    res.status(200).send(user);
  });
});

router.get('/:username', function (req, res) {
  User.find({ 'username': req.params.username }, function(err, users) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    res.status(200).send(users);
  });
});
*/



module.exports = router;
