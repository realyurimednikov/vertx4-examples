package me.mednikov.vertx4examples.guice;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

class ProjectVerticle extends AbstractVerticle {

    @Inject private ProjectClient client;
    @Inject private ProjectRepository repository;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
    }

    String getProjectClientName(){
        return this.client.getName();
    }

    String getProjectRepositoryName(){
        return this.repository.getName();
    }
}
