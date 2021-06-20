package me.mednikov.vertx4examples.guiceconfig;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.logging.Logger;

class AppVerticle extends AbstractVerticle {

    private static final Logger logger = Logger.getLogger("AppVerticle");

    @Inject @Named("ConfigRetriever")
    private ConfigRetriever configRetriever;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        configRetriever.getConfig(config -> {
            if (config.succeeded()){
                logger.info("Configuration obtained...");
                JsonObject conf = config.result();

                Injector injector = Guice.createInjector(new ProjectVerticleModule(vertx, conf));
                ProjectVerticle projectVerticle = injector.getInstance(ProjectVerticle.class);

                vertx.deployVerticle(projectVerticle, result -> {
                    if (result.succeeded()) {
                        logger.info("ProjectVerticle deployed...");
                        startPromise.complete();
                    } else {
                        logger.warning("Unable to deploy ProjectVerticle");
                        startPromise.fail(result.cause());
                    }
                });
            } else {
                logger.warning("Unable to obtain configuration...");
                logger.warning(config.cause().getMessage());
                startPromise.fail(config.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Injector injector = Guice.createInjector(new AppVerticleModule(vertx));
        AppVerticle appVerticle = injector.getInstance(AppVerticle.class);
        vertx.deployVerticle(appVerticle, result -> {
            if (result.succeeded()){
                logger.info("AppVerticle started...");
                vertx.close(result2 -> {
                    if (result2.succeeded()) {
                        logger.info("Vertx stopped");
                    } else {
                        logger.warning("Unable to close Vertx");
                        logger.warning(result2.cause().getMessage());
                    }
                });
            } else {
                logger.warning("Unable to deploy AppVerticle");
                logger.warning(result.cause().getMessage());
            }
        });

    }

}
