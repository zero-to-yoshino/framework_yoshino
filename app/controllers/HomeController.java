package controllers;

import models.Entry;
import models.User;
import objects.Search;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Result index(Http.Request request) {
        List<Entry> Entries = DB.find(Entry.class).findList();
        Form<Entry> entryForm = formFactory.form(Entry.class);
        return Results.ok(views.html.index.render(Entries, entryForm, request, messagesApi.preferred(request)));
    }

    @Security.Authenticated(Secured.class)
    public Result save(Http.Request request) {
        List<Entry> Entries = DB.find(Entry.class).findList();
        Form<Entry> entryForm = formFactory.form(Entry.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.index.render(Entries, entryForm, request, messagesApi.preferred(request)));
        } else {
            Entry entry = entryForm.get();
            // タイムスタンプ外挿
            entry.setCreateDate(new Timestamp(System.currentTimeMillis()));
            Long userId = Long.parseLong(request.session().getOptional("id").orElse("guest"));
            User user = DB.find(User.class).where().eq("id", userId).findOne();
            entry.setUser(user);
            DB.save(entry);
            return Results.redirect(routes.HomeController.index()).flashing("success", "投稿しました！");
        }
    }

    // 投稿更新
    @Security.Authenticated(Secured.class)
    public Result edit(Http.Request request, Long id) {
        Entry entry = DB.find(Entry.class, id);
        if (entry.getUser().getUserId() == Long.parseLong(request.session().getOptional("id").orElse("guest"))) {
            Form<Entry> entryForm = formFactory.form(Entry.class).fill(entry);
            return Results.ok(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
        } else {
            return Results.redirect(routes.SessionController.login())
            .removingFromSession(request, "id").flashing("failure", "不正なアクセスがありました。");
        }
    }

    @Security.Authenticated(Secured.class)
    public Result update(Http.Request request, Long id) {
        Entry savedEntry = DB.find(Entry.class, id);
        Form<Entry> entryForm = formFactory.form(Entry.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
        } else {
            Entry entry = entryForm.get();
            entry.setId(savedEntry.getId());
            DB.update(entry);
            return Results.redirect(routes.HomeController.index()).flashing("success", "編集しました！");
        }
    }

    // 投稿削除
    @Security.Authenticated(Secured.class)
    public Result delete(Long id) {
        DB.delete(Entry.class, id);
        return Results.redirect(routes.HomeController.index());
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
