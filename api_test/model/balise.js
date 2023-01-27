const mongoose = require('mongoose');

const newBalise = new mongoose.Schema({
    id: {type: Number},
    nameBalise: String,
    longitude: Number,
    latitude: Number,
  },{
   
    versionKey: false,
   
    
  });

  module.exports = mongoose.model('Balise', newBalise,"Balise")