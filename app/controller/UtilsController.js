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
//TODO : product not found
router.get('/codeBarre/:codeBarre', function (req, res) {
  let url = 'https://world.openfoodfacts.org/api/v0/product/'+req.params.codeBarre+'.json';
  new Promise( (resolve, reject) => {
      var xmlHttp = new XMLHttpRequest();
      xmlHttp.open("GET", url, false);
      xmlHttp.send(null);
      var json = JSON.parse(xmlHttp.responseText);
      if(json.status_verbose == "product not found"){
        resolve(res.status(404).send("Produit non trouvé"));
      }
      var textResult = "{ \"product\": { \"image\":\"\", \"nom\":\"\", \"emballage\":\"\"}}";
      var jsonResult = JSON.parse(textResult);
      // Ajout de l'image
      if(json.product.selected_images.front.display.fr == null) {
        jsonResult.product.image = json.product.selected_images.front.display.en;
      } else {
        jsonResult.product.image = json.product.selected_images.front.display.fr;
      }
      // Ajout du nom du produit
      jsonResult.product.nom = json.product.product_name;
      //ajout de l'emballage
      jsonResult.product.emballage = json.product.packaging_tags;
      resolve(jsonResult);
  })
  .then( ( resultat ) => {
    res.status(200).send(resultat);
  })
});




module.exports = router;
