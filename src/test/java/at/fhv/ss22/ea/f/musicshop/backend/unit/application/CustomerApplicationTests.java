package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.CustomerDTO;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.communication.internal.CustomerInternalService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.CustomerApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.CustomerApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerApplicationTests {

    private CustomerApplicationService customerService;

    private CustomerRMIClient rmiClient = mock(CustomerRMIClient.class);

    private AuthenticationApplicationService authenticationService = mock(AuthenticationApplicationService.class);

    private CustomerInternalService customerInternalService;

    @BeforeAll
    void setup() throws SessionExpired {
        this.customerService = new CustomerApplicationServiceImpl(rmiClient);
        this.customerInternalService = mock(CustomerInternalService.class);
        when(authenticationService.hasRole(any(SessionId.class), any(UserRole.class))).thenReturn(true);
        when(rmiClient.getCustomerInternalService()).thenReturn(this.customerInternalService);
    }

    @Test
    void get_customer_by_id() throws RemoteException, SessionExpired, NoPermissionForOperation {
        //given
        UUID id = UUID.randomUUID();
        CustomerDTO customer = CustomerDTO.builder()
                .id(id)
                .city("dornbirn")
                .familyName("mustermann")
                .givenName("max")
                .build();
        when(customerInternalService.customerById(id)).thenReturn(customer);

        //when
        CustomerDTO customerAct = customerService.customerById("some", id);

        //then
        assertEquals(customer.getFamilyName(), customerAct.getFamilyName());
    }

    @Test
    void get_customer_by_id_list() throws RemoteException, SessionExpired, NoPermissionForOperation {
        //given
        UUID id1 = UUID.randomUUID();
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(id1)
                .city("dornbirng")
                .familyName("mustermann")
                .givenName("max")
                .build();
        UUID id2 = UUID.randomUUID();
        CustomerDTO customer2 = CustomerDTO.builder()
                .id(id2)
                .city("bregenz")
                .familyName("muster")
                .givenName("maxine")
                .build();
        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .city("bregenz")
                .familyName("muster")
                .givenName("maxine")
                .build();
        when(customerInternalService.customerListByIds(any(List.class))).thenReturn(List.of(customer1, customer2));

        //when
        List<CustomerDTO> customers = customerService.customerListByIds("any", List.of(id1, id2));

        //then
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
        assertFalse(customers.contains(customer3));
    }

    @Test
    void search() throws SessionExpired, NoPermissionForOperation, RemoteException {
        //given
        UUID id = UUID.randomUUID();
        CustomerDTO customer = CustomerDTO.builder()
                .id(id)
                .city("dornbirn")
                .familyName("mustermann")
                .givenName("max")
                .build();
        when(customerInternalService.search(anyString())).thenReturn(List.of(customer));

        //when
        List<CustomerDTO> customers = customerService.search("some", "dorn");

        //then
        assertTrue(customers.contains(customer));
    }
}
