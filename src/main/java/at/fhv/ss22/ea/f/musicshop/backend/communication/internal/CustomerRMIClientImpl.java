package at.fhv.ss22.ea.f.musicshop.backend.communication.internal;

import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Stateless
@Local(CustomerRMIClient.class)
public class CustomerRMIClientImpl implements CustomerRMIClient {
    private static final Logger logger = LogManager.getLogger(CustomerRMIClientImpl.class);
    private CustomerInternalService customerService;
    private static String PORT =  System.getenv("CUSTOMER_SERVICE_RMI_PORT");
    private static String PROTOCOL = "rmi://";
    private static String HOST = System.getenv("CUSTOMER_SERVICE_RMI_HOSTNAME");
    private static String OBJECT_NAME = "CustomerInternalService";

    public CustomerRMIClientImpl() {
        this.reconnect();
    }

    @Override
    public void reconnect() {
        try {
            customerService = (CustomerInternalService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + "/" + OBJECT_NAME);
            logger.info("Successfully looked up {} from {}:{}", OBJECT_NAME, HOST, PORT);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
            logger.error("Failed to look {} from {}:{}", OBJECT_NAME, HOST, PORT);
        }
    }

    @Override
    public CustomerInternalService getCustomerInternalService() {
        return customerService;
    }
}
