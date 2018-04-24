var mongoose = require('mongoose');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://admin:admin@ds147459.mlab.com:47459/smart_db'); // connect to our database
