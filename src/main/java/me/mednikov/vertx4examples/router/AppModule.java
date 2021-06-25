package me.mednikov.vertx4examples.router;

import com.google.inject.AbstractModule;
import org.dizitart.no2.Nitrite;

class AppModule extends AbstractModule {

    private final ProjectDaoImpl dao;

    AppModule() {
        Nitrite nitrite = Nitrite.builder().openOrCreate();
        dao = new ProjectDaoImpl(nitrite);
    }

    @Override
    protected void configure() {
        bind(ProjectDao.class).toInstance(this.dao);
    }
}
