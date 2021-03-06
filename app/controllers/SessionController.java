package controllers;

import models.User;
import models.Entry;
import forms.LoginForm;
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

    // ログイン処理
    public Result login(Http.Request request) {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        return Results.ok(views.html.login.render(loginForm, request, messagesApi.preferred(request))).withNewSession();
    }

    public Result authenticate(Http.Request request) {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest(request);
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm, request, messagesApi.preferred(request)));
        } else {
            LoginForm inputUser = loginForm.get();
            User foundUser = DB.find(User.class).where().eq("email", inputUser.getEmail()).eq("password", inputUser.getPassword()).findOne();
            if (foundUser == null) {
                return Results.redirect(routes.SessionController.login())
                .flashing("failure", "メールアドレスかパスワードが間違っています。");
            } else {
                return Results.redirect(routes.HomeController.toppage())
                .addingToSession(request, "id", String.valueOf(foundUser.getUserId())).flashing("success", "ログインしました！");
            }
        }
    }

    // ログアウト処理
    public Result logout(Http.Request request) {
        return Results.redirect(routes.SessionController.login()).removingFromSession(request, "id");
    }
}
