var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var utilisateur = require("../models/utilisateur");
const nodemailer = require('nodemailer');
var pwdToken = require("../models/pwdToken");



// middleware to use for all requests
router.use(function (req, res, next) {
  if (req.url.match('forgotPassword') || req.url.match('success')) next();
  else if (!req.isAuthenticated()) res.status(401).send("You're not authenticated !");
  else {
    console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});


// ROUTES FOR USERS
// =============================================================================

// create a new user
router.post('/', function (req, res) {

  new Promise( (resolve, reject) => {
    utilisateur.findOne({ 'mail': req.body.mail }, function (err, user) {
      if (err) reject(res.status(500).send("There was a problem finding users in db"));
      else if (user) reject(res.status(500).send("This email adress is already used"));
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

        resolve(newUser);
      }
    });
  })
  .then( (newUser) => {
    // save the user
      newUser.save(function (err, utilisateur) {
        if (err) {
          res.status(500).send("There was a problem adding infos to db");
        }
        var username = utilisateur.nom || "No name";
        var resMsg = `${username} has been created ans his id is : ${utilisateur.id}`;
        res.status(200).send(resMsg);
      });
  });
});

// get the list of all users
router.get('/', function (req, res) {
  new Promise((resolve, reject) => {
    utilisateur.find({}, (err, users) => {
      if (err) reject(res.status(500).send("Unable to find users in database"));
      resolve(res.status(200).send(users));
    });
  });
  /*utilisateur.find({}, function (err, utilisateurs) {
    if (err) return res.status(500).send("There was a problem finding users in db");
    res.status(200).send(utilisateurs);
  });*/
});

// get the user by Id
router.get('/:id', function (req, res) {
  var userId = req.params.id;
  new Promise( (resolve, reject) => {
    utilisateur.findById(userId, function (err, user) {
    if (err) reject(res.status(500).send("Wrong ID, no user found"));
    resolve(res.status(200).send(user));
    });
  });
});

// Send reset email
router.post('/forgotPassword', function (req, res) {
  new Promise( (resolve, reject) => {
    return utilisateur.findOne({ 'mail': req.body.mail }, function (err, user) {
      if (err || !user) reject(res.status(500).send("Ce mail ne correspond à aucun compte"));
      else resolve(user);
    });
  })
  .then( (user) => {
    var createdToken = user.id.toString().substring(4,9);
    createdToken += Math.floor(Math.random()*50);
    return new Promise( (resolve, reject) => pwdToken.create({
      idUser: user.id,
      idToken: createdToken
    }, function (err, token) {
      if (err) reject(res.status(500).send("Unable to save the token"));
      else {
        resolve(token);
      }
    }))
    .then( (token) => {
      var link = `http://localhost:8080/api/users/forgotPassword/${token.idUser}/${token.idToken}`;
        // setup email data with unicode symbols
        let mailOptions = {
          from: '"Recyclyon" <recyclyon.app@gmail.com>', // sender address
          to: req.body.mail, // list of receivers
          subject: 'Reinitialisation de mot de passe', // Subject line
          html: "<b>Bonjour "+ user.nom +",<br/><br/>"+
          "Nous avons reçu une demande de réinitialisation de votre mot de passe Recyclyon.<br/> Si vous n'avez pas fait cette demande, veuillez ignorer cet email.<br/>"+
          "<a href="+link+"> Pour changer votre mot de passe, cliquez ici <a/>" +
          "<br/><br/> Si le lien ci-dessus ne fonctionne pas, c'est dommage ..." // plain text body
        };
        // send mail with defined transport object
        var transporter = nodemailer.createTransport({
          service: 'gmail',
          auth: {
            user: 'recyclyon.app@gmail.com',
            pass: 'pldsmartrpz'
          }
        });
        return transporter.sendMail(mailOptions, (error, info) => {
          if (error) return res.status(500).send("Mail cannot be send");
          else return res.status(200).send(mailOptions.html);
      });
    });
  });
});

router.get('/forgotPassword/:idUser/:token', function (req, res) {
  res.render('forgotpwd');
});

router.post('/forgotPassword/:idUser/:idToken', function (req, res) {
  var userId = req.params.idUser;
  var tokenId = req.params.idToken;
  return new Promise( (resolve, reject) => {
    pwdToken.findOne({idToken: tokenId}, function(err, token) {
      if (err || !token) res.status(500).send("Error finding the token");
      resolve(token);
    });
  })
  .then( (token) => {
    utilisateur.findById(token.idUser, function (err, user) {
      if (err || !user) return res.status(500).send("Cannot find user corresponding to the token");
      else {
        var newPassword = user.generateHash(req.body.password);
        user.motDePasse = newPassword;
        user.save(function (err, user) {
          if (err) res.status(500).send("Password cant be modifed");
          res.status(200).send({redirect: `http://localhost:8080/`});
        })
      }
    });
  });
});

// Get user by mail
router.get('/mail/:mail', function (req, res) {
  new Promise( (resolve, reject) => {
    utilisateur.findOne({ 'mail': req.params.mail }, function (err, user) {
    if (err) reject(res.status(500).send("There was a problem finding users in db"));
    resolve(res.status(200).send(user));
    });
  });
});


// Modifier les informations d'un utilisateur
router.put('/:id', function (req, res) {
  var userId = req.params.id;
  if (req.body.idAssoc) {
    new Promise((resolve, reject) => {
      var newIdAssoc = mongoose.Types.ObjectId(req.body.idAssoc);
      utilisateur.findByIdAndUpdate(userId, { idAssoc: newIdAssoc }, function (err, user) {
      if (err || !user) reject(res.status(500).send("Unable to modify user's assoc"));
      else resolve(user);
      });
    })
    .then((user) => utilisateur.findById(userId, (err, updatedUser) => res.status(200).send(updatedUser)));
  } else {
    new Promise ( (resolve, reject) => {
      utilisateur.findByIdAndUpdate(userId,
      {
        nom : req.body.nom,
        adresse: req.body.adresse,
        mail: req.body.mail,
        dateNaissance: req.body.dateNaissance,
        sexe: req.body.sexe
      }, function (err, user) {
      if (err || !user) reject(res.status(500).send("Unable to modify the whole user"));
      else resolve(user);
      });
    })
    .then((user) => utilisateur.findById(userId, (err, updatedUser) => res.status(200).send(updatedUser)))
  }
});

// Supprimer un utilisateur
router.delete('/:id', function (req, res) {
  var userId = req.params.id;
  new Promise( (resolve, reject) => {
    utilisateur.findByIdAndRemove(userId, (err) => err ? reject(res.status(500).send("Unable to delete the user")) : resolve(res.status(200).send("User deleted")));
  });
});

module.exports = router;
