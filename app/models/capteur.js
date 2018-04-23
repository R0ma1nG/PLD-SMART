var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var CapteurSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    tokenCapteur: Number,
    idPoubelle: Schema.Types.ObjectId
});

module.exports = mongoose.model('capteur', CapteurSchema, 'capteur');
