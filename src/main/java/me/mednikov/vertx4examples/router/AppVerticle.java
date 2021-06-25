package me.mednikov.vertx4examples.router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.dizitart.no2.Nitrite;

class AppVerticle extends AbstractVerticle {

    AppVerticle(){

    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
    }

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new DataVerticle(new ProjectDaoImpl(Nitrite.builder().openOrCreate())));
        vertx.deployVerticle(new RouterVerticle());
    }
}
