package controllers;

import models.User;
import forms.UserForm;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AdminController extends Controller {
    private final FormFactory formFactory;
    private MessagesApi messagesApi;
    
    @Inject
    public AdminController(FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    // 新規登録処理
    public Result newAdmin(Http.Request request) {
        Form<UserForm> userForm = formFactory.form(UserForm.class);
        return Results.ok(views.html.new_user.render(userForm, request, messagesApi.preferred(request)));
    }

    public Result save(Http.Request request) {
        Form<UserForm> userForm = formFactory.form(UserForm.class).bindFromRequest(request);
        if (userForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.new_user.render(userForm, request, messagesApi.preferred(request)));
        } else {
            UserForm inputUser = userForm.get();
            User user = new User(inputUser.getName(), inputUser.getEmail(), inputUser.getPassword());
            // ユーザーと管理者で異なる部分
            user.setHasAdmin(true);
            DB.save(user);
            return Results.redirect(routes.SessionController.login()).flashing("success", "新規登録しました！");
        }
    }

}
