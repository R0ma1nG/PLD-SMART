var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TokenSchema   = new Schema({
    idUser: Schema.Types.ObjectId,
    idToken: String
});

module.exports = mongoose.model('pwdToken', TokenSchema, 'pwdToken');
