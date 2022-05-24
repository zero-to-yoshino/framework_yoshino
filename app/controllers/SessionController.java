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
            return badRequest(views.html.login.render(loginForm, request, messagesApi.preferred(request)));
        } else {
            User formUser = loginForm.get();
            User foundUser = DB.find(User.class).where().eq("email", formUser.getEmail()).eq("password", formUser.getPassword()).findOne();
            if (foundUser == null) {
                return Results.redirect(routes.SessionController.login());
            } else {
                return Results.redirect(routes.HomeController.index()).addingToSession(request, "email", foundUser.getEmail());
            }
        }
    }

    public Result newUser(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class);
        return ok(views.html.new_user.render(userForm, request, messagesApi.preferred(request)));
    }

    public Result save(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
        if (userForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.new_user.render(userForm, request, messagesApi.preferred(request)));
        } else {
            User user = userForm.get();
            // タイムスタンプ外挿
            DB.save(user);
            return Results.redirect(routes.SessionController.login());
        }
    }
}
