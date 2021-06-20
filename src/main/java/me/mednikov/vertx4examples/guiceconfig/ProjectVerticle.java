package me.mednikov.vertx4examples.guiceconfig;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

class ProjectVerticle extends AbstractVerticle {

    @Inject private ProjectRepository repository;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        startPromise.complete();
    }
}
