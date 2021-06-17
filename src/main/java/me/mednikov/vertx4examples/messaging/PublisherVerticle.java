package me.mednikov.vertx4examples.messaging;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;

public class PublisherVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        EventBus eventBus = vertx.eventBus();
        
        
        JsonObject message = new JsonObject();
        message.put("Message", "Hello world!");
        
//        DeliveryOptions deliveryOptions = new DeliveryOptions();
//        deliveryOptions.setSendTimeout(1);
        
//        eventBus.request("dead-consumer", message, reply -> {
//            if (reply.failed()) {
//                Throwable error = reply.cause();
//                System.out.println(error.getClass());
//                System.out.println(error.getLocalizedMessage());
//                if (error instanceof ReplyException) {
//                    System.out.println("Message cannot be delivered. Recepient is not implemented");
//                    ReplyException exception = (ReplyException) error;
//                    System.out.println(exception.failureCode());
//                    System.out.println(exception.failureType());
//                    
//                    ReplyFailure failure = exception.failureType();
//                    if (failure == ReplyFailure.NO_HANDLERS) {
//                        System.out.println("This is the dead letter");
//                    }
//                    
//                    if (failure == ReplyFailure.TIMEOUT) {
//                        System.out.println("Timeout expired");
//                    }
//                }
//            }
//            startPromise.complete();
//        });


        eventBus.request("my-consumer", message, reply -> {
            Throwable error = reply.cause();
            if (error instanceof ReplyException) {
                ReplyException ex = (ReplyException) error;
                
                System.out.println(ex.failureCode());
                ReplyFailure failure = ex.failureType();
                System.out.println(failure);
                System.out.println(ex.getMessage());
            }
            
            startPromise.complete();
        });
    }
    
    
    
}
