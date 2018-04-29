module.exports = function (app, passport) {

app.get('/profile', isLoggedIn, function (req, res) {
  res.status(200).send({ idUtilisateur : req.user.id});
});

app.get('/error', function (req, res) {
  res.status(401).send("Error, mail/password invalid");
});

app.get('/logout', function (req, res) {
    req.session.destroy(function() {
        res.clearCookie('connect.sid');
        res.redirect('/');
    });
});

// process the signup form
    app.post('/signup', passport.authenticate('local-signup', {
        successRedirect : '/profile', // redirect to the secure profile section
        failureRedirect : '/error', // redirect back to the signup page if there is an error
    }));

    // process the login form
     app.post('/login', passport.authenticate('local-login', {
        successRedirect : '/profile', // redirect to the secure profile section
        failureRedirect : '/error', // redirect back to the signup page if there is an error
    }));

 function isLoggedIn (req, res, next) {

    // if user is authenticated in the session, carry on 
    if (req.isAuthenticated())
        return next();

    // if they aren't redirect them to the home page
    res.redirect('/');
}



  
}
