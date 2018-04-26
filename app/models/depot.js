var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var DepotSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    date: Date,
    montant: Number,
    idUtilisateur: Schema.Types.ObjectId,
    idAssoc: Schema.Types.ObjectId,
    idCapteur: Schema.Types.ObjectId
});

module.exports = mongoose.model('depot', DepotSchema, 'depot');
