package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RemoteRmiCallDecorator;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Proxy;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RmiDecoratorTests {
    private CustomerApplicationService undecoratedService = InstanceProvider.getMockedCustomerApplicationService();

    private CustomerRMIClient customerRMIClient = InstanceProvider.getMockedCustomerRmiClient();

    private CustomerApplicationService decoratedService;

    @BeforeAll
    void setup() {
        this.decoratedService = (CustomerApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                undecoratedService.getClass().getInterfaces(),
                new RemoteRmiCallDecorator(undecoratedService, customerRMIClient));
    }

    @Test
    void when_exception_then_retry_called() throws SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        reset(undecoratedService);
        when(undecoratedService.search(anyString(), anyString())).thenThrow(new NoSuchObjectException(""))
                .thenReturn(List.of(CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .givenName("Max")
                        .familyName("Mustermann")
                        .build()));
        //when
        decoratedService.search("some", "ax");

        verify(undecoratedService, times(2)).search(anyString(), anyString());
        verify(customerRMIClient, times(1)).reconnect();
    }

    @Test
    void when_exception_other_than_no_such_object_then_rethrown() throws SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        reset(undecoratedService);
        when(undecoratedService.search(anyString(), anyString())).thenThrow(new RuntimeException("Testing exception"));

        //when - then
        assertThrows(RuntimeException.class, () -> decoratedService.search("some", "another some"));
    }

}
