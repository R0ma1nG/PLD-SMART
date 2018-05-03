var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var DecheterieSchema = new Schema({
    _id: Schema.Types.ObjectId,
    id_grandlyon: String,
    adresse: String,
    code_insee: Number,
    code_postal: Number,
    commune: String,
    gestionnaire: String,
    numero_voie: String,
    voie: String,
    telephone: String,
    lattitude: Number,
    longitude: Number
});

module.exports = mongoose.model('decheterie', DecheterieSchema, 'decheterie');
