package me.mednikov.vertx4examples.guiceconfig;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.logging.Logger;

class AppVerticle extends AbstractVerticle {

    private static final Logger logger = Logger.getLogger("AppVerticle");

    private ConfigRetriever configRetriever;

    @Inject
    AppVerticle(@Named("ConfigRetriever") ConfigRetriever configRetriever){
        this.configRetriever = configRetriever;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Future<JsonObject> cr = configRetriever.getConfig();
        Future<ProjectVerticle> vert = cr.map(c -> {
            logger.info("Configuration obtained successfully");
            Injector injector = Guice.createInjector(new ProjectVerticleModule(vertx, c));
            ProjectVerticle verticle = injector.getInstance(ProjectVerticle.class);
            return verticle;
        }).onFailure(err -> {
            logger.warning("Unable to obtain configuration");
            startPromise.fail(err);
        });
        Future<String> dr = vert.compose(v -> vertx.deployVerticle(v));
        dr.onSuccess(id -> {
            logger.info("ProjectVerticle deployed");
            startPromise.complete();
        }).onFailure(err -> {
            logger.warning("Unable to deploy ProjectVerticle");
            logger.warning(err.getMessage());
            startPromise.fail(err);
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Injector injector = Guice.createInjector(new AppVerticleModule(vertx));
        AppVerticle appVerticle = injector.getInstance(AppVerticle.class);
        Future<String> dr = vertx.deployVerticle(appVerticle);
        dr.onSuccess(id -> logger.info("AppVerticle started..."))
        .onFailure(err -> {
            logger.warning("Unable to start AppVerticle");
            logger.warning(err.getMessage());
        })
        .onComplete(r -> {
            vertx.close();
            logger.info("Vertx closed");
        });

    }

}
