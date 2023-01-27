const express = require('express')
const app = express()
const balisesRoute = require("./api_test/routes/balises");
const baliseDataRoute = require("./api_test/routes/balises-data");
const login = require("./api_test/routes/login");
const mongoose = require("mongoose");
const bodyParser = require('body-parser');
global.config = require('./config');



app.use(bodyParser.urlencoded({extended:false}));
app.use(bodyParser.json());
app.use('/login',login);
app.use('/balises',balisesRoute);
app.use('/balises-data',baliseDataRoute);
app.use('/balise/:id',balisesRoute)
app.use((req,res,next)=>{
    res.status(200).json({
        message:'app is running'
    })
})


module.exports = app;
////////////////////////////////// Connexion BDD //////////////////////////////////
mongoose.connect("mongodb+srv://root:root@lamainverte.eit2nfv.mongodb.net/LaMainVerte?retryWrites=true&w=majority", {
  useNewUrlParser: false
  
 // useUnifiedTopology: true
});
const database = mongoose.connection;
database.on('error',err=>{
    console.log("connexion failed");

});
mongoose.connection.on('connected',connected=>{
    console.log('connected with db...')
});

///////////////////////////////////////////////////////////////////////////////////