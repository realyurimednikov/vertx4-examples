package me.mednikov.vertx4examples.guiceconfig;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

class ProjectVerticleModule extends AbstractModule {

    ProjectVerticleModule(Vertx vertx, JsonObject config){

    }

    @Override
    protected void configure() {
        bind(ProjectRepository.class).to(ProjectRepositoryImpl.class);
    }
}
