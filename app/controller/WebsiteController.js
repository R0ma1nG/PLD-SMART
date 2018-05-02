var express = require('express');
var router = express.Router();

// middleware to use for all requests
router.use(function(req, res, next) {
  if (req.method == "GET" && req.url == '/') next();
  else if (!req.isAuthenticated() || !req.user.isAdmin) res.status(401).send("You're not authenticated !");
  else {console.log('Authenticated request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
  }
});

router.get('/', function (req, res) {
  res.render('index');
});

router.get('/404', function (req, res) {
  res.render('404');
});

router.get('/accueil', function (req, res) {
  res.render('accueil');
});

router.get('/Dashboard', function (req, res) {
  res.render('Dash-Remplissage');
});

router.get('/Dashboard-Collecte', function (req, res) {
  res.render('Dash-Collecte');
});

module.exports = router;
