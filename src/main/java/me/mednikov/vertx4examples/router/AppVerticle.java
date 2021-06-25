package me.mednikov.vertx4examples.router;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.logging.Logger;

class AppVerticle extends AbstractVerticle {

    private final static Logger logger = Logger.getLogger("AppVerticle");

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Injector injector = Guice.createInjector(new AppModule());
        DataVerticle dataVerticle = injector.getInstance(DataVerticle.class);
        Future<String> dvr = vertx.deployVerticle(dataVerticle);
        Future<String> rvr = dvr.compose(r -> vertx.deployVerticle(new RouterVerticle()));
        rvr.onSuccess(r -> startPromise.complete()).onFailure(e -> startPromise.fail(e));
    }

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AppVerticle())
                .onSuccess(r -> logger.info("App deployed"))
                .onFailure(e -> {
                    logger.warning("App failed");
                    logger.warning(e.getMessage());
                    vertx.close();
                });
    }
}
