package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.communication.exception.NoPermissionForOperation;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@RequiresRole
public class RoleCheckInterceptor {

    @EJB private AuthenticationApplicationService authenticationApplicationService;

    public RoleCheckInterceptor() {}

    public RoleCheckInterceptor(AuthenticationApplicationService authenticationApplicationService) {
        this.authenticationApplicationService = authenticationApplicationService;
    }

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        Method method = context.getMethod();
        UserRole[] roles = method.getAnnotation(RequiresRole.class).value();

        int sessionKeyIndex = getSessionKeyIndex(method);
        if (sessionKeyIndex == -1) {
            throw new IllegalStateException("Unable to find SessionKey Parameter");
        }
        String sessionId = (String) context.getParameters()[sessionKeyIndex];

        if (Arrays.stream(roles).noneMatch(requiredRole -> {
            try {
                return authenticationApplicationService.hasRole(new SessionId(sessionId), requiredRole);
            } catch (SessionExpired e) {
                //TODO fix this mess by reconsidering if multiple roles are required
                return false;
            }
        })) {
            throw new NoPermissionForOperation();
        }

        return context.proceed();
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
