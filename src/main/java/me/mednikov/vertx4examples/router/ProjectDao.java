package me.mednikov.vertx4examples.router;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ProjectDao {

    JsonObject createProject (JsonObject project);

    JsonObject findOneProject (String id);

    JsonArray findAll ();

    void remove (String id);
}
