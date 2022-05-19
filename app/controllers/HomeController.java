package controllers;

import models.Entry;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.sql.Timestamp;
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
            entry.setCreateDate(new Timestamp(System.currentTimeMillis()));
            DB.save(entry);
            return Results.redirect(routes.HomeController.index());
        }
    }

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

    public Result delete(Long id) {
        DB.delete(Entry.class, id);
        return Results.redirect(routes.HomeController.index());
    }
}
