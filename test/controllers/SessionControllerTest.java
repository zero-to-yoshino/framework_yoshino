package controllers;

import org.junit.jupiter.api.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class SessionControllerTest {
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void ログインページがGETできるか() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/login");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
}
