package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

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
        //TODO figure out how to check if annotation is present in Impl-class -> annotations could be removed from interface
        if (method.isAnnotationPresent(RequiresRole.class)) {
            UserRole role = method.getAnnotation(RequiresRole.class).role();

            String sessionId = "invalid";
            if (args.length > 0 && args[0] instanceof String) {
                //TODO better way to get sessionId, currently requires sessionId to be the first argument of all servicemethods
                // maybe create annotation for parameterType
                sessionId = (String) args[0];
            }
            if (!authenticationApplicationService.hasRole(new SessionId(sessionId), role)) {
                throw new NoPermissionForOperation();
            }
        }
        return method.invoke(target, args);
    }
}
