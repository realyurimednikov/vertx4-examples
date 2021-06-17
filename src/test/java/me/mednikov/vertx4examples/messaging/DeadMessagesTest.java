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
class DeadMessagesTest {
    
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
}
