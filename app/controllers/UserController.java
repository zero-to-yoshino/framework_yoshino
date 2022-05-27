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
    // @Security.Authenticated(Secured.class)
    // public Result editUserForm(Http.Request request) {
    //     Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
    //     if (entry.getUser().getUserId() == Long.parseLong(request.session().getOptional("id").orElse("guest"))) {
    //         Form<Entry> entryForm = formFactory.form(Entry.class).fill(entry);
    //         return Results.ok(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
    //     } else {
    //         return Results.redirect(routes.SessionController.login())
    //         .removingFromSession(request, "id").flashing("failure", "不正なアクセスがありました。");
    //     }
    // }
}
