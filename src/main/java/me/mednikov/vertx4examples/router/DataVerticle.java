package me.mednikov.vertx4examples.router;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.dizitart.no2.*;

import java.util.Iterator;

class DataVerticle extends AbstractVerticle {

//    private final NitriteCollection collection;
    private ProjectDao dao;

    @Inject
    DataVerticle(ProjectDao dao){
        this.dao = dao;
//        this.collection = nitrite.getCollection("projects");
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().consumer("projects.db.create", r -> {
            JsonObject body = JsonObject.mapFrom(r.body());
//            Document document = new Document();
//            document.put("name", body.getString("name"));
//            document.put("color", body.getString("color"));
//            WriteResult result = collection.insert(document);
//            Iterator<NitriteId> iterator = result.iterator();
//            if (iterator.hasNext()){
//                NitriteId nitriteId = iterator.next();
//                String id = nitriteId.getIdValue().toString();
//                body.put("id", id);
//                r.reply(body);
//            }
            JsonObject result = dao.createProject(body);
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.all", r -> {
//            JsonArray projects = new JsonArray();
//            Cursor cursor = collection.find();
//            Iterator<Document> iterator = cursor.iterator();
//            while (iterator.hasNext()){
//                Document document = iterator.next();
//                JsonObject o = new JsonObject();
//                o.put("name", document.get("name").toString());
//                o.put("color", document.get("color").toString());
//                o.put("id", document.getId().getIdValue().toString());
//                projects.add(o);
//            }
//            JsonObject result = new JsonObject();
//            result.put("projects", projects);
//            r.reply(result);
            JsonArray projects = dao.findAll();
            JsonObject result = new JsonObject().put("projects", projects);
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.one", r -> {
            JsonObject payload = JsonObject.mapFrom(r.body());
//            JsonObject result = new JsonObject();
//            String id = payload.getString("id");
//            Document document = collection.getById(NitriteId.createId(Long.valueOf(id)));
//            if (document != null) {
//                result.put("id", id);
//                result.put("name", document.get("name").toString());
//                result.put("color", document.get("color").toString());
//            }
//            r.reply(result);
            JsonObject result = dao.findOneProject(payload.getString("id"));
            r.reply(result);
        });
        vertx.eventBus().consumer("projects.db.remove", r -> {
            JsonObject payload = JsonObject.mapFrom(r.body());
            String id = payload.getString("id");
//            Document document = collection.getById(NitriteId.createId(Long.valueOf(id)));
//            collection.remove(document);
            dao.remove(id);
            r.reply(new JsonObject().put("status", true));
        });
        startPromise.complete();
    }

}
