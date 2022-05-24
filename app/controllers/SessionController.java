package controllers;

import models.User;
import models.Entry;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionController extends Controller {
    private final FormFactory formFactory;
    private MessagesApi messagesApi;

    @Inject
    public SessionController (FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    public Result login(Http.Request request) {
        Form<User> loginForm = formFactory.form(User.class);
        return ok(views.html.login.render(loginForm, request, messagesApi.preferred(request)));
    }

    public Result authenticate(Http.Request request) {
        Form<User> loginForm = formFactory.form(User.class).bindFromRequest(request);
        if (loginForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.login.render(loginForm, request, messagesApi.preferred(request)));
        } else {
            User user = loginForm.get();
            if (user.getEmail().equals("a@b") && user.getPassword().equals("yoshino")) {
                return Results.redirect(routes.HomeController.index());
            } else {
                return Results.redirect(routes.SessionController.login());
            }
        }
    }
}
