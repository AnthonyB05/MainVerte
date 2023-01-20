const mongoose = require('mongoose');

const newBalise = new mongoose.Schema({
    id: {type: Number},
    nameBalise: String,
    
  },{
   
    versionKey: false,
   
    
  });

  module.exports = mongoose.model('Balise', newBalise,"Balise")