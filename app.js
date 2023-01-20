const express = require('express')
const app = express()
const sensorsRoute = require("./api_test/routes/sensors");
const mongoose = require("mongoose");

//Middlewareapp.use(express.json())
/*app.get('/sensors', (req,res) => {    res.status(200).json(sensors)})
app.get('/sensors/:id', (req,res) => {    const id = (req.params.id)
console.log(typeof(id));
const sensor = sensors.find(sensor => sensor.id == id) 
res.status(200).json(sensor)})
app.post('/sensors', (req,res) => {  sensors.push(req.body)  
res.status(200).json(sensors)})*/
app.use('/sensors',sensorsRoute);
app.use('/data_sensors',sensorsRoute);
app.use((req,res,next)=>{
    res.status(200).json({
        message:'app is running'
    })
})


module.exports = app;
/*mongoose.set("strictQuery", true);
mongoose.connect("mongodb://localhost:27017/laerte", {
  useNewUrlParser: false,
 // useUnifiedTopology: true
});
const fruitSchema = new mongoose.Schema({
    name: String,
    rating: Number,
    review: String
  });
  
  const Fruit = mongoose.model("Fruit", fruitSchema);
  
  const fruit = new Fruit({
      name: "Apple",
      rating: 7,
      review: "Taste Good"
  });
  
  fruit.save();*/

