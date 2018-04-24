var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;
var bcrypt = require('bcrypt-nodejs');

var UtilisateurSchema   = new Schema({
    mail: String,
    motDePasse: String,
    nom: String,
    adresse: String,
    dateNaissance: Date,
    sexe: Number,
    idAssoc: Schema.Types.ObjectId
});

UtilisateurSchema.methods.generateHash = function(password) {
    return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
};

// checking if password is valid
UtilisateurSchema.methods.validPassword = function(password) {
    return bcrypt.compareSync(password, this.motDePasse);
};


module.exports = mongoose.model('utilisateur', UtilisateurSchema, 'utilisateur');
