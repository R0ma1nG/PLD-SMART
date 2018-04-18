var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var UtilisateurSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    mail: String,
    motDePasse: String,
    nom: String,
    adresse: String,
    dateNaissance: Date,
    sexe: Number,
    idAssoc: Schema.Types.ObjectId
});

module.exports = mongoose.model('utilisateur', UtilisateurSchema, 'utilisateur');
