package me.mednikov.vertx4examples.webclient;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import me.mednikov.vertx4examples.commons.PostModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URL;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(VertxExtension.class)
class WebClientTest {

    private static URL BASE_URL;
    private Logger logger = Logger.getLogger("WebClientTest");

    @BeforeAll
    static void setup() throws Exception{
        BASE_URL = new URL("https://jsonplaceholder.typicode.com/posts");
    }

    @Test
    void createWebClientTest(Vertx vertx, VertxTestContext context){
        WebClient client = WebClient.create(vertx);
        assertNotNull(client);
        context.completeNow();
    }

    @Test
    void urlTest(){
        String result = BASE_URL.toExternalForm();
        assertEquals("https://jsonplaceholder.typicode.com/posts", result);
    }

    @Test
    void getRequestTest(Vertx vertx, VertxTestContext context){
        WebClient client = WebClient.create(vertx);
        String url = BASE_URL.toExternalForm();
        context.verify(() -> {
            Future<HttpResponse<JsonArray>> result = client.getAbs(url).as(BodyCodec.jsonArray()).send();
            result
                    .flatMap(r -> Future.succeededFuture((r.body())))
                    .onSuccess(posts -> {
                        assertEquals(100, posts.size());
                        context.completeNow();
                    }).onFailure(context::failNow);
        });
    }

    @Test
    void getWithQueryParamsTest(Vertx vertx, VertxTestContext context) throws Exception{
        WebClient client = WebClient.create(vertx);
        String url = new URL("https://jsonplaceholder.typicode.com/comments").toExternalForm();
        context.verify(() -> {
            Future<HttpResponse<JsonArray>> result = client.getAbs(url)
                    .as(BodyCodec.jsonArray())
                    .addQueryParam("postId", "1")
                    .send();
            result
                    .flatMap(r -> Future.succeededFuture((r.body())))
                    .onSuccess(posts -> {
                        assertEquals(5, posts.size());
                        context.completeNow();
                    }).onFailure(context::failNow);
        });
    }

    @Test
    void postRequestTest(Vertx vertx, VertxTestContext context){
        JsonObject payload = new JsonObject();
        payload.put("userId", 1);
        payload.put("title", "Nam ullamcorper scelerisque condimentum");
        payload.put("body", "Vivamus accumsan aliquam libero, vitae varius risus dictum sit amet. Quisque vitae maximus orci. Curabitur porta id tellus convallis finibus. Integer semper at augue non bibendum. Quisque eget condimentum mi, quis cursus odio. Mauris mattis mauris vitae mauris eleifend blandit. Ut quis felis ante.");

        String url = BASE_URL.toExternalForm();

        WebClient client = WebClient.create(vertx);
        context.verify(() -> {
            var result = client.postAbs(url).sendJsonObject(payload);
            result.onSuccess(response -> {
                        JsonObject body = response.bodyAsJsonObject();
                        assertEquals(101, body.getInteger("id"));
                        context.completeNow();
                    })
                    .onFailure(context::failNow);
        });
    }

    @Test
    void postWithCustomObjectTest(Vertx vertx, VertxTestContext context){
        String url = BASE_URL.toExternalForm();
        PostModel post = new PostModel(1, "Donec quis augue semper", "Quisque lacus ligula, lacinia congue blandit ac, varius efficitur ante");

        WebClient client = WebClient.create(vertx);
        context.verify(() -> {
            var result = client.postAbs(url).sendJson(post);
            result.onSuccess(response -> {
                        PostModel body = response.bodyAsJson(PostModel.class);
                        assertEquals(101, body.getId());
                        context.completeNow();
                    })
                    .onFailure(context::failNow);
        });
    }

    @Test
    void responsePredicateTest(Vertx vertx, VertxTestContext context){
        WebClient client = WebClient.create(vertx);
        String url = "https://jsonplaceholder.typicode.com/posts/101";

        context.verify(() -> {
            var result = client.getAbs(url)
                    .send();
            result.onSuccess(response -> {
                logger.info("On success handler is executed");
                context.completeNow();
            }).onFailure(err -> {
                logger.info("On failure handler is executed");
                context.failNow(err);
            });
        });
    }

}
