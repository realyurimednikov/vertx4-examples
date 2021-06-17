package me.mednikov.vertx4examples.messaging;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class InvalidMessagesTest {

    private final static int ERROR_CODE = 500;
    private final static String ERROR_MESSAGE = "Invalid message";

    @Test
    void sendInvalidMessage(Vertx vertx, VertxTestContext context){
        vertx.eventBus().consumer("my-consumer", message -> {
            message.fail(ERROR_CODE, ERROR_MESSAGE);
        });

        vertx.eventBus().request("my-consumer", new JsonObject(), reply -> {
            context.verify(() -> {
                Assertions.assertThat(reply.failed()).isTrue();
                Assertions.assertThat(reply.cause()).isInstanceOf(ReplyException.class);

                ReplyException ex = (ReplyException)  reply.cause();

                System.out.println(ex.getMessage());
                System.out.println(ex.failureCode());

                Assertions.assertThat(ex.failureCode()).isEqualTo(ERROR_CODE);
                Assertions.assertThat(ex.getMessage()).isEqualTo(ERROR_MESSAGE);

                ReplyFailure failure = ex.failureType();

                Assertions.assertThat(failure).isEqualTo(ReplyFailure.RECIPIENT_FAILURE);

                context.completeNow();
            });
        });
    }
}
