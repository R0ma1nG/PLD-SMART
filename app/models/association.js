var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var AssociationSchema   = new Schema({
    _id: Schema.Types.ObjectId,
    nom: String,
    rib: Number,
    description: String
});

module.exports = mongoose.model('association', AssociationSchema, 'association');
