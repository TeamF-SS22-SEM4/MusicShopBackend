package at.fhv.ss22.ea.f.musicshop.backend;

import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.OrderingApplicationService;
import at.fhv.ss22.ea.f.communication.dto.SoundCarrierOrderDTO;
import at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.RMIServer;
import at.fhv.ss22.ea.f.musicshop.backend.infrastructure.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        RMIServer.startRMIServer();

        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO remove this
        try {
            AuthenticationApplicationService authService = InstanceProvider.getAuthenticationApplicationService();
            String sessionId = authService.login("lka2222", "password").getSessionId();
            OrderingApplicationService service = InstanceProvider.getOrderingApplicationService();
            service.placeOrder(sessionId, SoundCarrierOrderDTO.builder().withOrderId(UUID.randomUUID())
                            .withCarrierId(UUID.fromString("1ce152c6-77f3-4293-98ff-a7ad43653bdd"))
                            .withAmount(2)
                            .build()
                    );
        } catch (SessionExpired e) {
            e.printStackTrace();
        } catch (NoPermissionForOperation e) {
            e.printStackTrace();
        } catch (AuthenticationFailed authenticationFailed) {
            authenticationFailed.printStackTrace();
        }

    }

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
