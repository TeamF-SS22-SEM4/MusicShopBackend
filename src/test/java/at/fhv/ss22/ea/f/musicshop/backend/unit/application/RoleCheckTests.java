package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RoleCheckInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleCheckTests {
    private RoleCheckInterceptor roleCheckInterceptor;
    private AuthenticationApplicationService authService;

    @BeforeAll
    void setup() {
        authService = mock(AuthenticationApplicationService.class);
        roleCheckInterceptor = new RoleCheckInterceptor(authService);
    }

    @Test
    void call_method_with_valid_permissions() throws Exception {
        //given
        InvocationContext context = mock(InvocationContext.class);
        when(authService.hasRole(any(), any())).thenReturn(true);
        Method method = TestingInterfaceValid.class.getMethod("testing", String.class, Integer.class);
        when(context.getParameters()).thenReturn(new Object[]{"some-session-id,", 2});

        when(context.getMethod()).thenReturn(method);
        when(context.proceed()).thenReturn(true);

        //whhen
        roleCheckInterceptor.intercept(context);

        //then
        verify(context, times(1)).proceed();
        verify(authService, times(1)).hasRole(any(), any());
    }

    @Test
    void when_called_on_class_no_session_key_then_exception() throws Exception {
        //given
        TestingInterfaceMissingSessionKey object = (sessionId, anotherParam) -> false;
        InvocationContext context = mock(InvocationContext.class);
        Method method = TestingInterfaceMissingSessionKey.class.getMethod("testing", String.class, int.class);
        when(context.getMethod()).thenReturn(method);
        when(context.proceed()).thenReturn(false);

        //when - then
        assertThrows(IllegalStateException.class, () -> roleCheckInterceptor.intercept(context));


    }


}
