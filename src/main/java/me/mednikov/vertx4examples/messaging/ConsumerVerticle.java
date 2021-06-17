package me.mednikov.vertx4examples.messaging;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

public class ConsumerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("my-consumer", reply -> reply.fail(400, "Invalid message"));
        startPromise.complete();
    }
    
    
    
}
