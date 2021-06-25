package me.mednikov.vertx4examples.router;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.dizitart.no2.*;

import java.util.Iterator;

class ProjectDaoImpl implements ProjectDao{

    private final NitriteCollection collection;

    ProjectDaoImpl(Nitrite nitrite){
        this.collection = nitrite.getCollection("projects");
    }

    @Override
    public JsonObject createProject(JsonObject project) {
        Document document = new Document();
        document.put("name", project.getString("name"));
        document.put("color", project.getString("color"));
        WriteResult result = collection.insert(document);
        Iterator<NitriteId> iterator = result.iterator();
        if (iterator.hasNext()){
            NitriteId nitriteId = iterator.next();
            String id = nitriteId.getIdValue().toString();
            project.put("id", id);
        }
        return project;
    }

    @Override
    public JsonObject findOneProject(String id) {
        JsonObject result = new JsonObject();
        Document document = collection.getById(NitriteId.createId(Long.valueOf(id)));
        if (document != null) {
            result.put("id", id);
            result.put("name", document.get("name").toString());
            result.put("color", document.get("color").toString());
        }
        return result;
    }

    @Override
    public JsonArray findAll() {
        JsonArray projects = new JsonArray();
        Cursor cursor = collection.find();
        Iterator<Document> iterator = cursor.iterator();
        while (iterator.hasNext()){
            Document document = iterator.next();
            JsonObject o = new JsonObject();
            o.put("name", document.get("name").toString());
            o.put("color", document.get("color").toString());
            o.put("id", document.getId().getIdValue().toString());
            projects.add(o);
        }
        return projects;
    }

    @Override
    public void remove(String id) {
        Document document = collection.getById(NitriteId.createId(Long.valueOf(id)));
        collection.remove(document);
    }
}
