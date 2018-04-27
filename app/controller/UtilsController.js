var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

// var poubelle = require("../models/poubelle");
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

// middleware to use for all requests
router.use(function(req, res, next) {
    console.log('Router middleware log, request : ', req.url); // do logging
    next(); // make sure we go to the next routes and don't stop here
});


// Scan d'un code barre
router.get('/codeBarre/:codeBarre', function (req, res) {
  let url = 'https://world.openfoodfacts.org/api/v0/product/'+req.params.codeBarre+'.json';
  new Promise( (resolve, reject) => {
      var xmlHttp = new XMLHttpRequest();
      xmlHttp.open("GET", url, false);
      xmlHttp.send(null);
      var json = JSON.parse(xmlHttp.responseText);
      var textResult = "{ \"product\" : []}";
      var jsonResult = JSON.parse(textResult);
      // Ajout de l'image
      jsonResult.product.image = json.product.selected_images.front.display.en;
      // Ajout du nom du produit
      jsonResult.product.nom = json.product.product_name_en;
      //ajout de l'emballage
      jsonResult.product.emballage = json.product.packaging;
      resolve(jsonResult);
  })
  .then( ( resultat ) => {
    console.log(resultat);
    // Return "{ product: []}"
    res.status(200).send(resultat);
  })
});




module.exports = router;
