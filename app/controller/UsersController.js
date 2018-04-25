var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var utilisateur = require("../models/utilisateur");


// middleware to use for all requests
router.use(function (req, res, next) {
  console.log('Router middleware log, request : ', req.url); // do logging
  next(); // make sure we go to the next routes and don't stop here
});


// ROUTES FOR USERS
// =============================================================================

// create a new user
router.post('/', function (req, res) {

  utilisateur.findOne({ 'mail': req.body.mail }, function (err, user) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    else if (user) return res.status(500).send("This email adress is already used");
    else {
      var newUser = new utilisateur();
      var idAssoc = mongoose.Types.ObjectId(req.body.idAssoc);
      // set the user's local credentials
      newUser.mail = req.body.mail;
      newUser.motDePasse = newUser.generateHash(req.body.motDePasse);
      newUser.nom = req.body.nom;
      newUser.adresse = req.body.adresse;
      newUser.dateNaissance = req.body.dateNaissance;
      newUser.sexe = req.body.sexe;
      newUser.idAssoc = idAssoc;

      // save the user
      newUser.save(function (err, utilisateur) {
        if (err) {
          return res.status(500).send("There was a problem adding infos to db");
        }
        console.log("User created ", utilisateur);
        var username = utilisateur.nom || "No name";
        var resMsg = `${username} has been created ans his id is : ${utilisateur.id}`;
        res.status(200).send(resMsg);
      });
    }
  });


});

// get the list of all users
router.get('/', function (req, res) {
  utilisateur.find({}, function (err, utilisateurs) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    res.status(200).send(utilisateurs);
  });
});

// get the user by Id
router.get('/:id', function (req, res) {
  var userId = req.params.id;
  utilisateur.findById(userId, function (err, user) {
    if (err) return res.status(500).send("Wrong ID, no user found");
    res.status(200).send(user);
  });
});

// Get user by mail
router.get('/mail/:mail', function (req, res) {
  utilisateur.findOne({ 'mail': req.params.mail }, function (err, user) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    res.status(200).send(user);
  });
});

router.put('/:id', function (req, res) {
  var userId = req.params.id;
  if (req.body.idAssoc) {
    var newIdAssoc = mongoose.Types.ObjectId(req.body.idAssoc);
    utilisateur.findByIdAndUpdate(userId, { idAssoc: newIdAssoc }, function (err, user) {
      if (err || !user) return res.status(500).send("Unable to modify user's assoc");
      return utilisateur.findById(userId, (err, updatedUser) => res.status(200).send("Updated User (asso) : "+updatedUser));
    });
  } else {
    console.log("Change the whole user");
    utilisateur.findByIdAndUpdate(userId,
    {
      nom : req.body.nom,
      adresse: req.body.adresse,
      mail: req.body.mail,
      dateNaissance: req.body.dateNaissance,
      sexe: req.body.sexe
    }, function (err, user) {
      if (err || !user) return res.status(500).send("Unable to modify the whole user");
      return utilisateur.findById(userId, (err, updatedUser) => res.status(200).send(updatedUser));
    });
  }
  });

router.delete('/:id', function (req, res) {
  var userId = req.params.id;
  return utilisateur.findByIdAndRemove(userId, (err) => err ? res.status(500).send("Unable to delete the user") : res.status(200).send("User deleted"));
});

module.exports = router;
