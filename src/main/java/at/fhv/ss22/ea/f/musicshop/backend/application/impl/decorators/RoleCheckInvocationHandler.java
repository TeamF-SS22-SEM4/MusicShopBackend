package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RoleCheckInvocationHandler implements InvocationHandler {

    private Object target;
    private AuthenticationApplicationService authenticationApplicationService;

    public RoleCheckInvocationHandler(Object target, AuthenticationApplicationService authenticationApplicationService) {
        this.target = target;
        this.authenticationApplicationService = authenticationApplicationService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method implementedMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (implementedMethod.isAnnotationPresent(RequiresRole.class)) {
            UserRole role = implementedMethod.getAnnotation(RequiresRole.class).value();

            int sessionKeyIndex = getSessionKeyIndex(implementedMethod);
            if (sessionKeyIndex == -1) {
                throw new IllegalStateException("Unable to find SessionKey Parameter");
            }
            String sessionId = (String) args[sessionKeyIndex];
            if (!authenticationApplicationService.hasRole(new SessionId(sessionId), role)) {
                throw new NoPermissionForOperation();
            }
        }
        return implementedMethod.invoke(target, args);
    }

    private int getSessionKeyIndex(Method method) {
        Annotation[][] params = method.getParameterAnnotations();
        for (int i = 0; i < params.length; i+=1) {
            Annotation[] annotations = params[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(SessionKey.class)) {
                    return i;
                }
            }

        }
        return -1;
    }
}
