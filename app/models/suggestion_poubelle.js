var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var SuggestionPoubelleSchema = new Schema({
    _id: Schema.Types.ObjectId,
    lattitude: String,
    longitude: String
});

module.exports = mongoose.model('suggestion_poubelle', SuggestionPoubelleSchema, 'suggestion_poubelle');
