package me.mednikov.vertx4examples.router;

import org.assertj.core.api.Assertions;
import org.dizitart.no2.*;
import org.dizitart.no2.filters.Filters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

class NitriteTest {

    private Logger logger = Logger.getLogger("NitriteTest");

    private Nitrite nitrite;

    @BeforeEach
    void setup(){
        nitrite = Nitrite.builder().openOrCreate();
    }

    @AfterEach
    void close(){
        nitrite.close();
    }

    @Test
    void writeTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        WriteResult result = collection.insert(document);
        Iterator<NitriteId> iterator = result.iterator();
        if (iterator.hasNext()){
            NitriteId id = iterator.next();
            Assertions.assertThat(id).isNotNull();
            logger.info(id.getIdValue().toString());
        } else {
            Assertions.fail("No ids");
        }
    }

    @Test
    void updateTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        WriteResult ir = collection.insert(document);
        long iid = ir.iterator().next().getIdValue();

        Document update = new Document();
        update.put("name", "Not John Doe anymore");
        WriteResult ur = collection.update(Filters.eq("email", "john.doe@email.com"), update);
        long uid = ur.iterator().next().getIdValue();

        Assertions.assertThat(iid).isEqualTo(uid);
    }

    @Test
    void getAllTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        WriteResult write = collection.insert(document);

        Cursor cursor = collection.find();
        List<Document> list = cursor.toList();

        Assertions.assertThat(list).hasSize(1);

        Document d = list.get(0);
        Assertions.assertThat(d.get("name").toString()).isEqualTo("John Doe");
        Assertions.assertThat(d.get("email").toString()).isEqualTo("john.doe@email.com");
        Assertions.assertThat(d.getId().toString()).isNotNull().isNotEmpty();
    }

    @Test
    void selectTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        collection.insert(document);

        Cursor cursor = collection.find(Filters.eq("email", "john.doe@email.com"));
        List<Document> list = cursor.toList();
        Assertions.assertThat(list).hasSize(1);
        Document d = list.get(0);
        Assertions.assertThat(d.get("name").toString()).isEqualTo("John Doe");
        Assertions.assertThat(d.get("email").toString()).isEqualTo("john.doe@email.com");
        Assertions.assertThat(d.getId().toString()).isNotNull().isNotEmpty();
    }

    @Test
    void findOneTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        WriteResult ir = collection.insert(document);
        NitriteId iid = ir.iterator().next();

        Document result = collection.getById(iid);
        Assertions.assertThat(result.get("email").toString()).isEqualTo("john.doe@email.com");
    }

    @Test
    void removeTest(){
        NitriteCollection collection = nitrite.getCollection("my-collection");
        Document document = new Document();
        document.put("name", "John Doe");
        document.put("email", "john.doe@email.com");
        WriteResult result = collection.insert(document);
        Iterator<NitriteId> iterator = result.iterator();
        if (iterator.hasNext()){
            NitriteId id = iterator.next();
            Assertions.assertThat(id).isNotNull();
            collection.remove(Filters.eq("email", "john.doe@email.com"));
            Cursor cursor = collection.find();
            List<Document> list = cursor.toList();
            Assertions.assertThat(list).isEmpty();
        } else {
            Assertions.fail("No ids");
        }
    }
}
