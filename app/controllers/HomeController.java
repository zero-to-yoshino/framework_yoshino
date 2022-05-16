package controllers;

import models.Entry;
import play.mvc.*;
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
    FormFactory formFactory;

    public HomeController (FormFactory formFactory) {
        this.formFactory = formFactory;
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

    public Result create() {
        Form<Entry> entryForm = formFactory.form(Entry.class);
        return ok(views.html.createForm.render(entryForm));
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
