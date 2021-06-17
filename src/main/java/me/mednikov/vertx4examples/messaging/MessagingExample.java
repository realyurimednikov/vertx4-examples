package me.mednikov.vertx4examples.messaging;

import io.vertx.core.Vertx;

class MessagingExample {
    
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        
//        PublisherVerticle publisher = new PublisherVerticle();
//        vertx.deployVerticle(publisher, result -> {
//            vertx.undeploy(result.result(), result2 -> vertx.close());
//        });
        PublisherVerticle publisher = new PublisherVerticle();
        ConsumerVerticle consumer = new ConsumerVerticle();
        vertx.deployVerticle(consumer, result -> {
            vertx.deployVerticle(publisher, result2 -> {
//                vertx.undeploy(result.result());
//                vertx.undeploy(result2.result());
                vertx.close();
            });
        });
    }
}
