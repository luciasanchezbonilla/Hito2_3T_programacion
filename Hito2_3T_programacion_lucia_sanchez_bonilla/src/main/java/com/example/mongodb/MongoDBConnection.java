package com.example.mongodb;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://administrador:Abc123456@cluster0.wuuuu2p.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE_NAME = "hito_2";
    private static final String COLLECTION_NAME = "usuarios";

    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBConnection() {
        connect();
    }

    private void connect() {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}
