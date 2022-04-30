package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.SaleApplicationServiceImpl;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RoleCheckInvocationHandler;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PermissionDecoratorTests {

    //Mocking impl, not interface because tests also check for the annotations defined in the implementation
    private SaleApplicationServiceImpl saleApplicationService = mock(SaleApplicationServiceImpl.class);
    private AuthenticationApplicationServiceImpl authenticationApplicationService = mock(AuthenticationApplicationServiceImpl.class);

    @Test
    void call_method_with_valid_permissions() throws SessionExpired, NoPermissionForOperation, CarrierNotAvailableException {
        String sessionId = "someId";
        List<SoundCarrierAmountDTO> carriers = new LinkedList<>();
        String payment = "CASH";
        UUID id = UUID.randomUUID();
        when(saleApplicationService.buy(sessionId, carriers, payment, id)).thenReturn("R000001");
        when(authenticationApplicationService.hasRole(any(SessionId.class), any(UserRole.class))).thenReturn(true);

        SaleApplicationService decoratedService = (SaleApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                SaleApplicationServiceImpl.class.getInterfaces(),
                new RoleCheckInvocationHandler(saleApplicationService, authenticationApplicationService));
        decoratedService.buy(sessionId, carriers, payment, id);

        //not exactly once but at least because apparently counts from other tests arent reset
        verify(saleApplicationService, atLeastOnce()).buy(sessionId, carriers, payment, id);
        verify(authenticationApplicationService, atLeastOnce()).hasRole(any(), any());
    }

    @Test
    void when_calling_method_with_no_permission_then_nothing_happens() {
        TestingInterfaceNoRoleAnnotations mocked = mock(TestingInterfaceNoRoleAnnotations.class);
        when(mocked.testing(anyString(), anyInt())).thenReturn(true);

        TestingInterfaceNoRoleAnnotations decoratedInstance = (TestingInterfaceNoRoleAnnotations) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[] {TestingInterfaceNoRoleAnnotations.class},
                new RoleCheckInvocationHandler(mocked, authenticationApplicationService));

        assertTrue(decoratedInstance.testing("someId", 2));

        verify(mocked, times(1)).testing(anyString(), anyInt());
    }

    @Test
    void when_calling_method_with_no_session_key_then_exception() {
        TestingInterfaceMissingSessionKey mocked = mock(TestingInterfaceMissingSessionKey.class);
        when(mocked.testing(anyString(), anyInt())).thenReturn(true);

        TestingInterfaceMissingSessionKey decoratedInstance = (TestingInterfaceMissingSessionKey) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[] {TestingInterfaceMissingSessionKey.class},
                new RoleCheckInvocationHandler(mocked, authenticationApplicationService));

        assertThrows(IllegalStateException.class, () -> decoratedInstance.testing("someId", 2));

        verify(mocked, times(0)).testing(anyString(), anyInt());
    }

    @Test
    void when_no_role_present_then_exception() throws SessionExpired, NoPermissionForOperation, CarrierNotAvailableException {
        String sessionId = "someId";
        List<SoundCarrierAmountDTO> carriers = new LinkedList<>();
        String payment = "CASH";
        UUID id = UUID.randomUUID();
        when(saleApplicationService.buy(sessionId, carriers, payment, id)).thenReturn("R000001");
        when(authenticationApplicationService.hasRole(any(SessionId.class), any(UserRole.class))).thenReturn(false);

        SaleApplicationService decoratedService = (SaleApplicationService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                SaleApplicationServiceImpl.class.getInterfaces(),
                new RoleCheckInvocationHandler(saleApplicationService, authenticationApplicationService));

        assertThrows(NoPermissionForOperation.class, () -> decoratedService.buy(sessionId, carriers, payment, id));
    }
}
