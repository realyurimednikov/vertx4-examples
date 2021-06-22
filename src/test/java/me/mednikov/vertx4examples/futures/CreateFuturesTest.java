package me.mednikov.vertx4examples.futures;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@ExtendWith(VertxExtension.class)
class CreateFuturesTest {

    @Test
    void createFromJava(Vertx vertx, VertxTestContext context){
        CompletableFuture<BigDecimal> result = CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(10000).multiply(BigDecimal.valueOf(3)));
        Future<BigDecimal> future = Future.fromCompletionStage(result);
        future.onComplete(n-> {
            Assertions.assertThat(n.result()).isEqualByComparingTo(BigDecimal.valueOf(30000));
            context.completeNow();
        });
    }

    @Test
    void createFromValue(Vertx vertx, VertxTestContext context){
        Future<String> successFuture = Future.succeededFuture("Hello world!");
        successFuture.onSuccess(r -> {
            context.verify(() ->{
                Assertions.assertThat(r).isEqualTo("Hello world!");
                context.completeNow();
            });
        });


    }

    @Test
    void createFailed(Vertx vertx, VertxTestContext context){
        Future<Throwable> failedFuture = Future.failedFuture(new RuntimeException());
        failedFuture.onFailure(err -> {
            context.verify(() -> {
                Assertions.assertThat(err).isInstanceOf(RuntimeException.class);
                context.completeNow();
            });
        });
    }

}
