package me.mednikov.vertx4examples.router;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

class DataVerticle extends AbstractVerticle {

    private ProjectDao dao;

    @Inject
    DataVerticle(ProjectDao dao){
        this.dao = dao;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().consumer("projects.db.create", r -> {
            JsonObject body = JsonObject.mapFrom(r.body());
            JsonObject result = dao.createProject(body);
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.all", r -> {
            JsonArray projects = dao.findAll();
            JsonObject result = new JsonObject().put("projects", projects);
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.one", r -> {
            JsonObject payload = JsonObject.mapFrom(r.body());
            JsonObject result = dao.findOneProject(payload.getString("id"));
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.remove", r -> {
            JsonObject payload = JsonObject.mapFrom(r.body());
            String id = payload.getString("id");
            dao.remove(id);
            r.reply(new JsonObject().put("status", true));
        });
        startPromise.complete();
    }

}
