package me.mednikov.vertx4examples.futures;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class MapTest {

    @Test
    void mapFuture(Vertx vertx, VertxTestContext context){
        Future<String> future = Future.succeededFuture("Hello world");
        future
                .map(s -> s.toUpperCase())
                .onSuccess(result -> context.verify(() -> {
                    Assertions.assertThat(result).isUpperCase();
                    context.completeNow();
        }));
    }

    @Test
    void composeMapFuture(Vertx vertx, VertxTestContext context){
        Future<String> future = Future.succeededFuture("Hello world!");
        future
                .compose(s -> Future.succeededFuture(s.toUpperCase()))
                .onComplete(r -> {
                    if (r.succeeded()) {
                        Assertions.assertThat(r.result()).isUpperCase();
                        context.completeNow();
                    } else {
                        context.failNow(r.cause());
                    }
                });
    }
}
