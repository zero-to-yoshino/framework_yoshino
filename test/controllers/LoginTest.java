package controllers;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

public class LoginTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void セッションがない状態でユーザリソースにアクセスするとログイン画面にリダイレクト() {
        String[] resourceList = {"", "edit/1", "search"};
        for (String resource : resourceList) {
            Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/" + resource);
            Result result = route(app, request);
            assertEquals(SEE_OTHER, result.status());
            assertEquals(result.redirectLocation().get(), "/login");
            assertEquals(result.flash().get("danger").get(), "先にログインする必要があります。");
        }
    }

    @Test
    public void 新規登録のビューが表示されるか() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/login/new");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void 新規登録が正しくできるか() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyForm(ImmutableMap.of("name", "kosuke", "email", "", "password", ""))
                .uri(controllers.routes.UserController.newUser().url());
        request = addCSRFToken(request);
        Result result = route(app, request);
        assertEquals(SEE_OTHER, result.status());
        assertEquals(result.redirectLocation().get(), "/login");
        assertEquals(result.flash().get("success").get(), "新規登録しました！");
    }

    // @Test
    // public void 新規登録フォームに何も記入してない項目がある場合() {

    // }

    @Test
    // 編集はユーザidと関連させる必要があるのでここでは実施しない
    public void セッション情報を持っていると編集以外のユーザリソースへのアクセスが可能(){
        String[] resourceList = {"", "search"};
        for (String resource : resourceList) {
            Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .session("id", "1")
                .uri("/" + resource);
            Result result = route(app, request);
            assertEquals(OK, result.status());
        }
    }

    @Test
    public void ログアウト後ユーザページに移動できない() {
        Result result = route(app, controllers.routes.SessionController.logout());
        assertEquals(SEE_OTHER, result.status());
        assertEquals(result.redirectLocation().get(), "/login");
        セッションがない状態でユーザリソースにアクセスするとログイン画面にリダイレクト();
    }

    @Test
    public void 別ユーザでログインしたらログイン画面にリダイレクト() {
        Result result = route(app, controllers.routes.SessionController.login());
        assertThat(contentAsString(result), is(containsString("ログイン")));
    }
}