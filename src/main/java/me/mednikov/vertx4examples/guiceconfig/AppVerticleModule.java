package me.mednikov.vertx4examples.guiceconfig;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;

class AppVerticleModule extends AbstractModule {

    private ConfigRetriever configRetriever;

    AppVerticleModule(Vertx vertx){
        configRetriever = ConfigRetriever.create(vertx);
    }

    @Override
    protected void configure() {
        bind(ConfigRetriever.class).annotatedWith(Names.named("ConfigRetriever")).toInstance(configRetriever);
    }
}
