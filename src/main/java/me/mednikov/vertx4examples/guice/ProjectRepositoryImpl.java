package me.mednikov.vertx4examples.guice;

class ProjectRepositoryImpl implements ProjectRepository {
    @Override
    public String getName() {
        return "ProjectRepositoryImpl";
    }
}
