package me.mednikov.vertx4examples.messaging;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Logger;

@ExtendWith(VertxExtension.class)
class DeadMessagesTest {

    private static Logger logger = Logger.getLogger("DeadMessagesTest");

    // callbacks (vertx 3 + vertx4)
    @Test
    void sendDeadMessage(Vertx vertx, VertxTestContext context){
        vertx.eventBus().request("dead-consumer", new JsonObject(), reply -> {
            context.verify(() -> {
                Assertions.assertThat(reply.failed()).isTrue();
                Assertions.assertThat(reply.cause()).isInstanceOf(ReplyException.class);
                
                ReplyException exception = (ReplyException) reply.cause();
                ReplyFailure failure = exception.failureType();

                Assertions.assertThat(failure).isEqualTo(ReplyFailure.NO_HANDLERS);

                System.out.println(exception.getMessage());
                System.out.println(failure.name());
                System.out.println(exception.failureCode());
                
                context.completeNow();
               
            });
        });
    }

    // same example, but it uses Futures (vertx 4)
    @Test
    void sendDeadMessageFuture(Vertx vertx, VertxTestContext context){
        Future<Message<Object>> sr = vertx.eventBus().request("dead-consumer", new JsonObject());
        sr.onSuccess(message -> context.failNow("Should not be successful!"))
                .onFailure(err -> {
                    context.verify(() -> {
                        Assertions.assertThat(err).isInstanceOf(ReplyException.class);
                        ReplyException exception = (ReplyException) err;
                        ReplyFailure failure = exception.failureType();

                        Assertions.assertThat(failure).isEqualTo(ReplyFailure.NO_HANDLERS);

                        logger.info(exception.getMessage());
                        logger.info(failure.name());
                        logger.info(Integer.toString(exception.failureCode()));

                        context.completeNow();
                    });
                });
    }
}
