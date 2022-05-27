package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Optional;

import models.*;

public class Secured extends Security.Authenticator {

    @Override
    public Optional<String> getUsername(Http.Request req) {
        return req.session().getOptional("id");
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        return redirect(routes.SessionController.login()).
                flashing("danger",  "先にログインする必要があります。");
    }
}
