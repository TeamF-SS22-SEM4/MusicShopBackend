package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Logged
public class LoggingInterceptor {
    private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);

    public LoggingInterceptor() {
    }

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        logger.info("{}::{} called", context.getTarget().getClass().getName(), context.getMethod().getName());
        return context.proceed();
    }
}
