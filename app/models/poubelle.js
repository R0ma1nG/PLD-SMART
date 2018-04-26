var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var PoubelleSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    adresse: String,
    lattitude: Number,
    longitude: Number,
    remplissage: Number
});

module.exports = mongoose.model('poubelle', PoubelleSchema, 'poubelle');
