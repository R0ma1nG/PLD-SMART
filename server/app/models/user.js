var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var UserSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    mail: String,
    motDePasse: String,
    nom: String,
    adresse: String,
    dateNaissance: Date,
    sexe: String,
    idAssoc: Schema.Types.ObjectId
});

module.exports = mongoose.model('user', UserSchema);
