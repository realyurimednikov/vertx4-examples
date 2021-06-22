package me.mednikov.vertx4examples.futures;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@ExtendWith(VertxExtension.class)
class CompositionTest {

    /*
    All results are completed
     */
    @Test
    void allCompositionSuccess(Vertx vertx, VertxTestContext context){
        Future<BigDecimal> future1 = Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(10000)));
        Future<BigDecimal> future2 = Future.succeededFuture(new BigDecimal(20000));
        Future<CompositeFuture> composed = CompositeFuture.all(future1, future2);
        composed.onSuccess(r -> {
            /*
            Beware that indexes start from 0!
             */
            BigDecimal value1 = r.resultAt(0);
            BigDecimal value2 = r.resultAt(1);
            context.verify(() -> {
                Assertions.assertThat(value1).isEqualByComparingTo(new BigDecimal((10000)));
                Assertions.assertThat(value2).isEqualByComparingTo(new BigDecimal(20000));
                context.completeNow();
            });
        });
    }

    /*
    At least one is failed
     */
    @Test
    void allCompositionFailed(Vertx vertx, VertxTestContext context){
        Future<BigDecimal> future1 = Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(10000)));
        Future<BigDecimal> future2 = Future.failedFuture(new RuntimeException());
        Future<CompositeFuture> composed = CompositeFuture.all(future1, future2);
        composed
                .onSuccess(r -> context.failNow("That should not be!"))
                .onFailure(err -> context.completeNow());
    }

    @Test
    void anyComposition(Vertx vertx, VertxTestContext context){
        Future<BigDecimal> future1 = Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(10000)));
        Future<BigDecimal> future2 = Future.failedFuture(new RuntimeException());
        Future<CompositeFuture> composed = CompositeFuture.any(future1, future2);
        composed.onSuccess(r -> context.completeNow());
    }

    @Test
    void anyCompositionFail(Vertx vertx, VertxTestContext context){
        Future<BigDecimal> future1 = Future.failedFuture(new RuntimeException());
        Future<BigDecimal> future2 = Future.failedFuture(new RuntimeException());
        Future<CompositeFuture> composed = CompositeFuture.any(future1, future2);
        composed
                .onSuccess(r -> context.failNow("That should not be!"))
                .onFailure(r -> context.completeNow());
    }

    @Test
    void joinFutures(Vertx vertx, VertxTestContext context){
        Future<BigDecimal> future1 = Future.fromCompletionStage(CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(10000)));
        Future<BigDecimal> future2 = Future.succeededFuture(new BigDecimal(20000));
        Future<CompositeFuture> composed = CompositeFuture.join(future1, future2);
        composed.onSuccess(r -> {
            BigDecimal value1 = r.resultAt(0);
            BigDecimal value2 = r.resultAt(1);
            context.verify(() -> {
                BigDecimal sum = value1.add(value2);
                Assertions.assertThat(sum).isEqualByComparingTo(new BigDecimal("30000"));
                context.completeNow();
            });
        });
    }
}
