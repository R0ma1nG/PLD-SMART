var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ReleveSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    date: Date,
    tauxRemplissage: Number,
    idPoubelle: Schema.Types.ObjectId
});

module.exports = mongoose.model('releve', ReleveSchema, 'releve');
