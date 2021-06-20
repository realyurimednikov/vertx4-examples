package me.mednikov.vertx4examples.guice;

import com.google.inject.AbstractModule;

class ProjectVerticleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectClient.class).to(ProjectClientImpl.class);
        bind(ProjectRepository.class).to(ProjectRepositoryImpl.class);
    }
}
