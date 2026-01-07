/* Koden är tagen från:
 * https://github.com/karlstad-business-school/isgb33-examples/blob/main/isgb33-forelasning-6-exempel/isgb33-eclipse-lab-template/src/Stub.java
*/
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;



public class Mongodb {
	private MongoCollection<Document> collection;
	
	// Konstruktor
	public Mongodb() {
        try {
			initMongo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	// Startar mongodb anslutning och hämtar databasen
	private void initMongo() throws Exception {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("connection.properties")) {
            prop.load(input);
        }

        String connString = prop.getProperty("db.connection_string");
        String dbName = prop.getProperty("db.name");

        ConnectionString connectionString = new ConnectionString(connString);

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase(dbName);

       collection = database.getCollection("movies");
        

	}
	
	//Metod som söker efter filmer baserad på genre
    public List<Document> findMoviesByGenre(String genre) {
        return collection.aggregate(Arrays.asList(
                Aggregates.project(
                        Projections.fields(
                                Projections.include("title", "year", "genres"),
                                Projections.excludeId()
                        )
                ),
                Aggregates.match(Filters.regex("genres", "^" + genre + "$", "i")),
                Aggregates.sort(Sorts.descending("title")),
                Aggregates.limit(10)
        )).into(new java.util.ArrayList<>());
    }
 
}


