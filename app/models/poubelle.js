var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var PoubelleSchema = new Schema({
    _id: Schema.Types.ObjectId,
    id_grandlyon: String,
    adresse: String,
    code_insee: Number,
    code_postal: Number,
    commune: String,
    gestionnaire: String,
    observation: String,
    numero_voie: String,
    voie: String,
    lattitude: Number,
    longitude: Number,
    remplissage: Number
});

module.exports = mongoose.model('poubelle', PoubelleSchema, 'poubelle');
