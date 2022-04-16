package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.musicshop.backend.communication.jms.JMSClient;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;

import javax.jms.JMSException;
import javax.persistence.Query;
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
            JMSClient.getJmsClient().publishMessage("Orders", "Order me some CDs");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize() throws IOException {
        System.out.println("Initializing database");
        // (Data 1/2) Prepare data from data.sql
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("data.sql");
        String[] insertStatements = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).split(";");

        // (Data 2/2) Insert data into database
        EntityManagerUtil.beginTransaction();
        Arrays.stream(insertStatements).map(EntityManagerUtil.getEntityManager()::createNativeQuery).forEach(Query::executeUpdate);
        EntityManagerUtil.commit();
        System.out.println("Finished initializing database");
    }
}
