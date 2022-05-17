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

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    @inject
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;

    public HomeController (FormFactory formFactory) {
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
        // リロードすると同じデータが作られるので注意！！
        Entry user = new Entry("yoshino", "test", "初testです。");
        DB.save(user);
        Entry user2 = new Entry("yoshino2", "test2", "test二回目です。");
        DB.save(user2);
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        // String name = foundUsers.get(1).message.toString();
        return ok(views.html.index.render(foundEntries));
    }

    public Result create(Http.Request request) {
        Form<Entry> entryForm = formFactory.form(Entry.class);
        return ok(views.html.createForm.render(entryForm, request, messagesApi.preferred(request)));
    }

    public Result save(Http.Request request) {
        Form<Entry> entryForm = formFactory.form(Entry.class).bindFromRequest(request);
        if (entryForm.hasErrors()) {
                // This is the HTTP rendering thread context
                return badRequest(views.html.createForm.render(entryForm, request, messagesApi.preferred(request)));
        }

    }

    public Result test() {
        // リロードすると同じデータが作られるので注意！！
        // Entry user = new Entry("yoshino", "test", "初testです。");
        // DB.save(user);
        // Entry user2 = new Entry("yoshino2", "test2", "test二回目です。");
        // DB.save(user2);
        List<Entry> foundEntries = DB.find(Entry.class).findList();
        String name = foundEntries.get(1).message.toString();
        // String name = "test";
        return ok(views.html.test.render(name));
    }
}
