package me.mednikov.vertx4examples.futures;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class RecoverTest {

    @Test
    void recoverFuture(Vertx vertx, VertxTestContext context){
        Future<String> future = Future.failedFuture(new RuntimeException());
        future
                .recover(err -> Future.succeededFuture("Hello!"))
                .onSuccess(r -> {
                    context.verify(() -> {
                        Assertions.assertThat(r).isEqualTo("Hello!");
                        context.completeNow();
                    });
                });
    }
}
