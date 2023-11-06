package org.example;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import com.mongodb.client.MongoClient; //getDatabase
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;

import java.util.Map;

public class DatabaseHandler {
    private MongoClient mongoClient = null;
    private String dbName = null;
    public DatabaseHandler(String uri, String databaseName) {
        this.dbName = databaseName;
        mongoClient = MongoClients.create(uri);

    }

    public InsertOneResult update(JSONObject rowData, String collectionName) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(String.valueOf(collectionName)); //"vehicle-id"
        Map<String, Object> data = rowData.toMap();
        Document document = new Document(data);
        return collection.insertOne(document);
    }

    public void close() {
        this.mongoClient.close();
    }


}


