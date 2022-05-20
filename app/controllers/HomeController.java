package controllers;

import models.Entry;
import objects.Search;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public Result index(Http.Request request) {
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        Form<Entry> entryForm = formFactory.form(Entry.class);
        return ok(views.html.index.render(foundEntries, entryForm, request, messagesApi.preferred(request)));
    }

    public Result save(Http.Request request) {
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        Form<Entry> entryForm = formFactory.form(Entry.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.index.render(foundEntries, entryForm, request, messagesApi.preferred(request)));
        } else {
            Entry entry = entryForm.get();
            // タイムスタンプ外挿
            entry.setCreateDate(new Timestamp(System.currentTimeMillis()));
            DB.save(entry);
            return Results.redirect(routes.HomeController.index());
        }
    }

    // 投稿更新
    public Result edit(Http.Request request, Long id) {
        Entry entry = DB.find(Entry.class, id);
        Form<Entry> entryForm = formFactory.form(Entry.class).fill(entry);
        return ok(views.html.edit.render(id, entryForm, request, messagesApi.preferred(request)));
    }

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
            return Results.redirect(routes.HomeController.index());
        }
    }

    // 投稿削除
    public Result delete(Long id) {
        DB.delete(Entry.class, id);
        return Results.redirect(routes.HomeController.index());
    }

    // 検索機能
    // foundEntriesはこの後検索結果によって数を減らす
    public Result search(Http.Request request){
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        Form<Search> searchForm = formFactory.form(Search.class);
        List<String> searchList = new ArrayList<String>();
        // searchList.add("aaa");
        // searchList.add("bbb");
        // for (int i = 0; i < searchList.size(); i++) {
        //     if (i == 0){
        //         searchListQuery += searchList.get(i);
        //     } else {
        //         searchListQuery += "&" + searchList.get(i);
        //     }
        // }
        return ok(views.html.search.render(searchForm, request, messagesApi.preferred(request)));
    }

    // public Result searchResult(Http.Request request, List<String> searchList){
    //     List<Entry> foundEntries = DB.find(Entry.class).findList();
    //     Form<Search> searchForm = formFactory.form(Search.class);
    //     searchList = new ArrayList<String>();
    //     if (entryForm.hasErrors()) {
    //         // This is the HTTP rendering thread context
    //         return badRequest(views.html.search.render(foundEntries, searchForm, searchList, request, messagesApi.preferred(request)));
    //     } else {
    //         Entry entry = entryForm.get();
    //         entry.setId(savedEntry.getId());
    //         DB.update(entry);
    //         return Results.redirect(routes.HomeController.index());
    //     }
    // }


    // 任意のテスト

    public Result test(){
        // Search search = new Search();
        // search.setSearchInput("searchInput");
        // return ok(views.html.test.render(search.getSearchInput()));
        return ok(views.html.test.render());
    }
}
