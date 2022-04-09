package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting Server....");
        RMIServer.startRMIServer();


        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() throws IOException {
        System.out.println("Initializing database");
        // (Data 1/2) Prepare data from data.sql
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("data.sql");
        String[] lines = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).split("\n");

        // (Data 2/2) Insert data into database
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        entityManager.getTransaction().begin();
        Arrays.stream(lines).forEach(entityManager::createNativeQuery);
        entityManager.getTransaction().commit();
        System.out.println("Finished initializing database");
    }
}
