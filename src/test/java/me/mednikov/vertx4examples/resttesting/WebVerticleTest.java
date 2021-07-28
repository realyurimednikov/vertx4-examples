package me.mednikov.vertx4examples.resttesting;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import me.mednikov.vertx4examples.commons.PostModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class WebVerticleTest {

    @BeforeEach
    void deploy(Vertx vertx, VertxTestContext context){
        DeploymentOptions options = new DeploymentOptions();
        options.setConfig(new JsonObject().put("port", 4000));
        vertx.deployVerticle(new WebVerticle(), context.succeedingThenComplete());
    }

    @Test
    void postTest(Vertx vertx, VertxTestContext context){
        PostModel payload = new PostModel(1, "Post title", "Post body");
        WebClient client = WebClient.create(vertx);
        client.postAbs("http://localhost:8000/posts")
                .expect(ResponsePredicate.SC_CREATED)
                .expect(ResponsePredicate.JSON)
                .sendJson(payload)
                .onSuccess(result -> {
                    JsonObject body = result.bodyAsJsonObject();
                    Assertions.assertThat(body.getInteger("id")).isEqualTo(1);
                    Assertions.assertThat(body.getString("title")).isEqualTo(payload.getTitle());
                    Assertions.assertThat(body.getString("body")).isEqualTo(payload.getBody());
                    Assertions.assertThat(body.getInteger("userId")).isEqualTo(payload.getUserId());
                    context.completeNow();
                })
                .onFailure(context::failNow);
    }

    @Test
    void getAllTest(Vertx vertx, VertxTestContext context){
        WebClient client = WebClient.create(vertx);
        client.getAbs("http://localhost:8000/posts")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.contentType("application/json"))
                .send()
                .onSuccess(response -> context.completeNow())
                .onFailure(context::failNow);
    }

    @Test
    void getOneTest(Vertx vertx, VertxTestContext context){
        WebClient client = WebClient.create(vertx);
        client.getAbs("http://localhost:8000/post/10")
                .expect(ResponsePredicate.SC_NOT_FOUND)
                .send()
                .onSuccess(response -> context.completeNow())
                .onFailure(context::failNow);
    }

}
