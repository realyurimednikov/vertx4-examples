package me.mednikov.vertx4examples.resttesting;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import me.mednikov.vertx4examples.commons.PostModel;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;

import java.util.Optional;

class WebVerticle extends AbstractVerticle {

    private MutableList<PostModel> posts;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        if (posts == null) {
            posts = Lists.mutable.empty();
        }

        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setPort(config().getInteger("port", 8000));

        HttpServer server = vertx.createHttpServer(serverOptions);

        Router router = Router.router(vertx);

        router.route("/*").handler(BodyHandler.create());

        router.post("/posts").consumes("application/json").produces("application/json").handler(ctx -> {
            PostModel post = Json.decodeValue(ctx.getBody(), PostModel.class);
            int id = posts.size() + 1;
            post.setId(id);
            posts.add(post);
            ctx.response().setStatusCode(201).end(Json.encodeToBuffer(post));
        });

        router.get("/posts").produces("application/json").handler(ctx->{
            JsonArray json = new JsonArray();
            posts.forEach(post -> {
                JsonObject jo = JsonObject.mapFrom(post);
                json.add(jo);
            });
            ctx.response().setStatusCode(200).end(json.encode());
        });

        router.get("/post/:id").produces("application/json").handler(ctx->{
            Integer id = Integer.getInteger(ctx.pathParam("id"));
            Optional<PostModel> result = posts.detectOptional(p -> p.getId() == id);
            result.ifPresentOrElse(data -> {
                JsonObject jo = JsonObject.mapFrom(data);
                ctx.response().setStatusCode(200).end(jo.encode());
            }, () -> ctx.response().setStatusCode(404).end());
        });

        router.delete("/post/:id").handler(ctx->{
            Integer id = Integer.getInteger(ctx.pathParam("id"));
            posts.removeIf(post -> post.getId() == id);
            ctx.response().setStatusCode(200).end();
        });

        server.requestHandler(router);
        server.listen().onSuccess(r -> startPromise.complete()).onFailure(startPromise::fail);
    }

}
