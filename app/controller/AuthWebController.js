module.exports = function (app, passport) {

    // process the login form
     app.post('/connexion', passport.authenticate('web-login'), function (req, res) {
      res.status(200).send({redirect: `/Dashboard`});
     });

    app.get('/connexion', function (req, res) {
      res.render('connexion.html');
    });
};
