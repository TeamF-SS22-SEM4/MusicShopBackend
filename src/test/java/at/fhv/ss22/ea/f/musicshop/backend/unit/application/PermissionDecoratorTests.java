package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.communication.dto.SoundCarrierAmountDTO;
import at.fhv.ss22.ea.f.communication.exception.CarrierNotAvailableException;
import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.InstanceProvider;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.SaleApplicationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PermissionDecoratorTests {

    private SaleApplicationService saleApplicationService = InstanceProvider.getMockedBuyingApplicationService();

    private AuthenticationApplicationService authenticationApplicationService = InstanceProvider.getMockedAuthenticationApplicationService();

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

    //TODO more tests
}
