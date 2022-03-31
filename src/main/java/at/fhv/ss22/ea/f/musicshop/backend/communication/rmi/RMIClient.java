package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi;

public class RMIClient {
    private static RMIClient rmiClient;
    // private CustomerSearchService customerSearchService;
    private static int PORT = 1099;
    private static String PROTOCOL = "rmi://";
    private static String HOST = "10.0.40.171";
    private static String STUB = "/CustomerSearch";

    private RMIClient() {
        /*
        try {
            customerSearchService = (CustomerSearchService) Naming.lookup(PROTOCOL + HOST + ":" + PORT + STUB);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
         */
    }

    public static RMIClient getRmiClient() {
        if(rmiClient == null) {
            rmiClient = new RMIClient();
        }

        return rmiClient;
    }

    /*
    public CustomerSearchService getCustomerSearchService() {
        return customerSearchService;
    }
     */
}
