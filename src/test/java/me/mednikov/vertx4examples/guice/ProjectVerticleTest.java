package me.mednikov.vertx4examples.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class ProjectVerticleTest {

    @Test
    void injectTest(Vertx vertx, VertxTestContext context){
        ProjectVerticleModule module = new ProjectVerticleModule();
        Injector injector = Guice.createInjector(module);
        ProjectVerticle verticle = injector.getInstance(ProjectVerticle.class);
        Future<String> deploy = vertx.deployVerticle(verticle);
        deploy.onComplete(id -> {
            context.verify(() -> {
                String client = verticle.getProjectClientName();
                String repository = verticle.getProjectRepositoryName();

                Assertions.assertThat(client).isEqualTo("ProjectClientImpl");
                Assertions.assertThat(repository).isEqualTo("ProjectRepositoryImpl");

                context.completeNow();
            });
        }).onFailure(err -> context.failNow(err));
    }
}
