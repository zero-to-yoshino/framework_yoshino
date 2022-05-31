package controllers;

import models.Entry;
import models.User;
import forms.EntryForm;
import forms.Search;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {
    private final FormFactory formFactory;
    private MessagesApi messagesApi;

    @Inject
    public HomeController (FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    // 新規投稿
    @Security.Authenticated(Secured.class)
    public Result toppage(Http.Request request) {
        List<Entry> Entries = DB.find(Entry.class).findList();
        Form<EntryForm> entryForm = formFactory.form(EntryForm.class);
        Long userId = Long.parseLong(request.session().get("id").orElse("guest"));
        User user = DB.find(User.class).where().eq("id", userId).findOne();
        return Results.ok(views.html.toppage.render(user, Entries, entryForm, request, messagesApi.preferred(request)));
    }

    @Security.Authenticated(Secured.class)
    public Result save(Http.Request request) {
        List<Entry> Entries = DB.find(Entry.class).findList();
        Form<EntryForm> entryForm = formFactory.form(EntryForm.class).bindFromRequest(request);
        Long userId = Long.parseLong(request.session().get("id").orElse("guest"));
        User user = DB.find(User.class).where().eq("id", userId).findOne();
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.toppage.render(user, Entries, entryForm, request, messagesApi.preferred(request)));
        } else {
            EntryForm inputEntry = entryForm.get();
            // データベース操作処理
            Date currentTime = new Timestamp(System.currentTimeMillis());
            Entry entry = new Entry(inputEntry.getName(), inputEntry.getTitle(), 
                inputEntry.getMessage(), currentTime, user);
            DB.save(entry);
            return Results.redirect(routes.HomeController.toppage()).flashing("success", "投稿しました！");
        }
    }

    // 投稿更新
    @Security.Authenticated(Secured.class)
    public Result edit(Http.Request request, Long id) {
        // セッション情報との称号
        Entry savedEntry = DB.find(Entry.class, id);
        Long userId = Long.parseLong(request.session().get("id").orElse("guest"));
        User user = DB.find(User.class).where().eq("id", userId).findOne();
        if (savedEntry.getUser().getUserId() == user.getUserId() || user.getHasAdmin() == true) {
            Form<EntryForm> entryForm = formFactory.form(EntryForm.class)
                .fill(new EntryForm(savedEntry.getName(), savedEntry.getTitle(), savedEntry.getMessage()));
            return Results.ok(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
        } else {
            return Results.redirect(routes.SessionController.login())
            .removingFromSession(request, "id").flashing("failure", "不正なアクセスがありました。");
        }
    }

    @Security.Authenticated(Secured.class)
    public Result update(Http.Request request, Long id) {
        Form<EntryForm> entryForm = formFactory.form(EntryForm.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
        } else {
            Entry savedEntry = DB.find(Entry.class, id);
            EntryForm inputEntry = entryForm.get();
            savedEntry.setName(inputEntry.getName());
            savedEntry.setTitle(inputEntry.getTitle());
            savedEntry.setMessage(inputEntry.getMessage());
            DB.update(savedEntry);
            return Results.redirect(routes.HomeController.toppage()).flashing("success", "編集しました！");
        }
    }

    // 投稿削除
    @Security.Authenticated(Secured.class)
    public Result delete(Long id) {
        DB.delete(Entry.class, id);
        return Results.redirect(routes.HomeController.toppage());
    }

    // 検索機能
    // foundEntriesはこの後検索結果によって数を減らす
    @Security.Authenticated(Secured.class)
    public Result search(Http.Request request){
        List<Entry> foundEntries =new ArrayList<Entry>();
        Form<Search> searchForm = formFactory.form(Search.class);
        // searchWordによってURIを変更しています
        String searchWord = "default";
        return Results.ok(views.html.search.render(foundEntries, searchForm, searchWord, request, messagesApi.preferred(request)));
    }

    @Security.Authenticated(Secured.class)
    public Result searchDo(Http.Request request, String searchWord){
        List<Entry> Entries = DB.find(Entry.class).findList();
        List<Entry> foundEntries =new ArrayList<Entry>();
        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest(request);
        searchWord = "default";
        if (searchForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.search.render(Entries, searchForm, searchWord, request, messagesApi.preferred(request)));
        } else {
            Search search = searchForm.get();
            searchWord = search.getSearchInput();
            for (Entry entry : Entries) {
                if (entry.getTitle().contains(searchWord) || entry.getMessage().contains(searchWord)) {
                    foundEntries.add(entry);
                } 
            }
            return Results.ok(views.html.search.render(foundEntries, searchForm, searchWord, request, messagesApi.preferred(request)));
        }
    }


    // 任意のテスト

    public Result test(){
        // Search search = new Search();
        // search.setSearchInput("searchInput");
        // return ok(views.html.test.render(search.getSearchInput()));
        return Results.ok(views.html.test.render());
    }
}
