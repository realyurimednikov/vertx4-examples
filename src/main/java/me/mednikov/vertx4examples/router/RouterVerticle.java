package me.mednikov.vertx4examples.router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

class RouterVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setPort(8080);

        HttpServer server = vertx.createHttpServer(serverOptions);

        Router router = Router.router(vertx);

        router.route("/*").handler(BodyHandler.create());

        router.post("/projects").consumes("application/json").produces("application/json").handler(this::createProject);
        router.get("/projects").handler(this::getAllProjects);
        router.get("/project/:id").handler(this::getProject);
        router.delete("/project/:id").handler(this::deleteProject);

        server.requestHandler(router);

        Future<HttpServer> result = server.listen();
        result.onSuccess(r -> startPromise.complete())
                .onFailure(e -> startPromise.fail(e));
    }

    void createProject (RoutingContext context){
        JsonObject body = context.getBodyAsJson();
        Future<Message<JsonObject>> message = vertx.eventBus().request("projects.db.create", body);
        message.compose(r -> context.response().setStatusCode(201).end(r.body().encodePrettily()));
    }

    void getAllProjects (RoutingContext context){
        Future<Message<JsonObject>> message = vertx.eventBus().request("projects.db.all", new JsonObject());
        message.compose(r -> context.response().setStatusCode(201).end(r.body().encodePrettily()));
    }

    void getProject(RoutingContext context){
        JsonObject payload = new JsonObject();
        payload.put("id", context.pathParam("id"));
        Future<Message<JsonObject>> message = vertx.eventBus().request("projects.db.one", payload);
        message.compose(r -> context.response().setStatusCode(201).end(r.body().encodePrettily()));
    }

    void deleteProject(RoutingContext context){
        JsonObject payload = new JsonObject();
        payload.put("id", context.pathParam("id"));
        Future<Message<JsonObject>> message = vertx.eventBus().request("projects.db.remove", payload);
        message.compose(r -> context.response().setStatusCode(201).end(r.body().encodePrettily()));
    }


}
