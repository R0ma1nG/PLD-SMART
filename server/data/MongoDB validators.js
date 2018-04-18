// Ce fichier est juste un memo

db.createCollection("utilisateur", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["mail", "motDePasse", "nom", "adresse", "dateNaissance", "sexe", "idAssoc"],
         "properties": {
            "mail": {
               "bsonType": "string"
            },
            "motDePasse": {
               "bsonType": "string"
            },
            "nom": {
               "bsonType": "string"
           },
           "dateNaissance": {
              "bsonType": "date"
           },
            "adresse": {
               "bsonType": "string"
           },
            "sexe": {
                "bsonType": "int"
           },
           "idAssoc": {
              "bsonType": "objectId"
            }
         }
      }
   }
})

db.createCollection("association", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["nom", "rib"],
         "properties": {
            "nom": {
               "bsonType": "string"
            },
            "rib": {
               "bsonType": "int"
            },
            "description": {
               "bsonType": "string"
            }
         }
      }
   }
})

db.createCollection("releve", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["date", "tauxRemplissage", "idPoubelle"],
         "properties": {
            "date": {
               "bsonType": "date"
            },
            "tauxRemplissage": {
               "bsonType": "double"
           },
           "idPoubelle": {
               "bsonType": "objectId"
           }
         }
      }
   }
})

db.createCollection("poubelle", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["adresse", "lattitude", "longitude", "remplissage"],
         "properties": {
            "adresse": {
               "bsonType": "string"
            },
            "lattitude": {
               "bsonType": "double"
            },
            "longitude": {
               "bsonType": "double"
            },
            "remplissage": {
               "bsonType": "double"
            }
         }
      }
   }
})

db.createCollection("capteur", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["tokenCapteur", "idPoubelle"],
         "properties": {
            "tokenCapteur": {
               "bsonType": "int"
           },
           "idPoubelle": {
               "bsonType": "objectId"
           }
         }
      }
   }
})

db.createCollection("depot", {
   "validator": {
      "$jsonSchema": {
         "bsonType": "object",
         "required": ["date", "montant", "idUtilisateur", "idAssoc", "idCapteur"],
         "properties": {
            "date": {
               "bsonType": "date"
           },
           "montant": {
               "bsonType": "double"
           },
           "idUtilisateur": {
               "bsonType": "objectId"
           },
           "idAssoc": {
               "bsonType": "objectId"
           },
           "idCapteur": {
               "bsonType": "objectId"
           }
         }
      }
   }
})



// REMPLISSAGE DE LA BASE DE DONNEES

{
    "nom": "TestAsso",
    "description": "test description",
    "rib": NumberInt(1234567890)
}

{
    "adresse": "Une adresse au hasard dans le monde.",
    "lattitude": Number(44.7),
    "longitude": Number(45.1),
    "remplissage": Number(0.5)
}

{
    "tokenCapteur": NumberInt(123),
    "idPoubelle": ObjectId("5ad71a30a15938d3d083353c")
}

var mydate = ISODate();
db.utilisateur.insert({
    "mail": "jd@gmail.com",
    "motDePasse": "mdp",
    "nom": "Jean Dupont",
    "adresse": "Une autre adresse au hasard",
    "dateNaissance": mydate,
    "sexe": NumberInt(0),
    "idAssoc": ObjectId("5ad727a9a15938d3d0833540")
});
