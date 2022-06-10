package controllers;

import io.ebean.DB;
import org.junit.*;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import models.*;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.route;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EntrySaveTest extends WithApplication {

    @Test
    public void テスト前にログイン() {
        User user = new User("name", "a@b", "pass");
        DB.save(user);
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyForm(ImmutableMap.of("email", "a@b", "password", "pass"))
                .uri(controllers.routes.SessionController.login().url());
        Result result = route(app, request);
        assertEquals(SEE_OTHER, result.status());
        assertEquals(result.redirectLocation().get(), "/");
        assertEquals(result.flash().get("success").get(), "ログインしました！");
    }

    @Test
    public void フォームに正しい項目があるか() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .session("id", "2")
                .uri("/");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

}
