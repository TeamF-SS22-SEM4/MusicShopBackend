package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        //TODO remove this since main is never called in a javaEE server
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //TODO figure out how to execute this on startup
    public static void initialize() throws IOException {
        logger.info("Initializing database");
        // (Data 1/2) Prepare data from data.sql
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("data.sql");
        String[] insertStatements = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).split(";");

        // (Data 2/2) Insert data into database
        EntityManagerUtil.beginTransaction();
        Arrays.stream(insertStatements).map(EntityManagerUtil.getEntityManager()::createNativeQuery).forEach(Query::executeUpdate);
        EntityManagerUtil.commit();
        logger.info("Finished initializing database");
    }
}
