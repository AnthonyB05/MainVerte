const mongoose = require('mongoose');

const baliseData = new mongoose.Schema({
    idBalise: Number,
    degreCelsius:Number,
    humiditeExt: Number,
    luminosite: Number,
    longitude: Number,
    latitude: Number,
    date: { type: Date, default: Date.now },
  },{
    versionKey: false,

  });

  module.exports = mongoose.model('Data', baliseData,"Data")