package at.fhv.ss22.ea.f.musicshop.backend.communication.internal;

import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;


public interface CustomerRMIClient {
    void reconnect();

    CustomerInternalService getCustomerInternalService();
}
