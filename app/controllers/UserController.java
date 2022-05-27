package controllers;

import models.User;
import models.Entry;
import forms.EditUserForm;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserController extends Controller {
    private final FormFactory formFactory;
    private MessagesApi messagesApi;

    @Inject
    public UserController (FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    // 新規登録処理
    public Result newUser(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class);
        return Results.ok(views.html.new_user.render(userForm, request, messagesApi.preferred(request)));
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
            return Results.redirect(routes.SessionController.login()).flashing("success", "新規登録しました！");
        }
    }

    // ユーザー情報の更新
    @Security.Authenticated(Secured.class)
    public Result editUser(Http.Request request) {
        Long sessionId = Long.parseLong(request.session().getOptional("id").orElse("guest"));
        User user = DB.find(User.class).where().eq("id", sessionId).findOne();
        EditUserForm editUserInfo = new EditUserForm(user.getName(), user.getEmail());
        Form<EditUserForm> editUserForm = formFactory.form(EditUserForm.class).fill(editUserInfo);
        return Results.ok(views.html.edit_user.render(editUserForm, request, messagesApi.preferred(request)));
    }

    @Security.Authenticated(Secured.class)
    public Result updateUser(Http.Request request) {
        Long sessionId = Long.parseLong(request.session().getOptional("id").orElse("guest"));
        User user = DB.find(User.class).where().eq("id", sessionId).findOne();
        Form<EditUserForm> editUserForm = formFactory.form(EditUserForm.class).bindFromRequest(request);
        
        if (editUserForm.hasErrors()) {
            return badRequest(views.html.edit_user.render(editUserForm, request, messagesApi.preferred(request)));
        } else {
            EditUserForm inputUser = editUserForm.get();
            if (user.getPassword().equals(inputUser.getPrePassword())) {
                User updateUser = new User(user.getUserId(), inputUser.getName(), inputUser.getEmail(), inputUser.getPassword());
                DB.update(updateUser);
                return Results.redirect(routes.HomeController.toppage()).flashing("success", "ユーザー情報を編集しました！");
            } else {
                return Results.redirect(routes.UserController.editUser()).flashing("failure", "パスワードが間違ってます。");
            }
        }
    }
}
