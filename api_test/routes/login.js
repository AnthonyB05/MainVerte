var express = require('express');
var router = express.Router();
let jwt = require('jsonwebtoken');


// Use to generate tok
/* GET users listing. */
router.get('/', function (req, res, next) {

    res.send('respond with a resource');
});

/* Post users Login. */
router.post('/', function (req, res, next) {
    let userdata = {
        username: req.body.username,
        password: req.body.password
    };

    //Go to server for user varificarion
    if (userdata.username == "test" && userdata.password == "test") {
        let token = jwt.sign(userdata, global.config.secretKey, {
            algorithm: global.config.algorithm,
            expiresIn: '30d'
        });

        res.status(200).json({
            message: 'Login Successful',
            jwtoken: token
        });
    }
    else {
        res.status(401).json({
            message: 'Login Failed'
        });
    }
});

module.exports = router;