package controllers;

import models.Entry;
// import play.api.mvc.*;
import play.mvc.*;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import io.ebean.DB;
import java.util.List;
// import java.util.concurrent.CompletionStage;
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
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        // String name = foundUsers.get(1).message.toString();
        return ok(views.html.index.render(foundEntries));
    }

    public Result create(Http.Request request) {
        Form<Entry> entryForm = formFactory.form(Entry.class);
        return ok(views.html.create.render(entryForm, request, messagesApi.preferred(request)));
    }

    public Result save(Http.Request request) {
        Form<Entry> entryForm = formFactory.form(Entry.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
            // This is the HTTP rendering thread context
            return badRequest(views.html.create.render(entryForm, request, messagesApi.preferred(request)));
        } else {
            Entry entry = entryForm.get();
            DB.save(entry);
            return Results.redirect(routes.HomeController.index());
            // return ok(views.html.index.render(foundEntries));
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
            savedEntry.setName(entry.getName());
            savedEntry.setTitle(entry.getTitle());
            savedEntry.setMessage(entry.getMessage());
            DB.update(savedEntry);
            return Results.redirect(routes.HomeController.index());
        }
    }

    public Result delete(Long id) {
        DB.delete(Entry.class, id);
        return Results.redirect(routes.HomeController.index());

    }

    // public Result test() {
    //     List<Entry> foundEntries = DB.find(Entry.class).findList();
    //     String name = foundEntries.get(1).getMessage();
        // String name = "test";
        // return ok(views.html.test.render(name));
//     }
}
