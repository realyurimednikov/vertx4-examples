package me.mednikov.vertx4examples.guice;

import com.google.inject.AbstractModule;

class ProjectVerticleModule extends AbstractModule {

    @Override
    protected void configure() {
        ProjectClient client = new ProjectClientImpl();
        ProjectRepository repository = new ProjectRepositoryImpl();

        bind(ProjectClient.class).toInstance(client);
        bind(ProjectRepository.class).toInstance(repository);
    }
}
