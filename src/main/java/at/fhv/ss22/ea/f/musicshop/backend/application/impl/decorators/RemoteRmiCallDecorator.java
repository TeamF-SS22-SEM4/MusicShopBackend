package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.musicshop.backend.communication.internal.CustomerRMIClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;

//TODO change to interceptors
public class RemoteRmiCallDecorator implements InvocationHandler {

    private Object target;

    private CustomerRMIClient rmiClient;

    public RemoteRmiCallDecorator(Object target, CustomerRMIClient rmiClient) {
        this.target = target;
        this.rmiClient = rmiClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //TODO annotation based checking whether or not this handler should add additional behaviour
        // currently not needed because all the remote-rmi-calls are all in a single class and the instance-provider
        // only decorates the customerApplicationService, with this handler
        Object result = null;
        try {
            result = method.invoke(target, args);
        } catch (InvocationTargetException e) {
            //differentiationg types of exception like this, not in catch clause, because compiler yell's the exception is never
            // thrown in the try-block, and found no other way to handle just a single exception type
            if (e.getTargetException() instanceof NoSuchObjectException) {
                //this error can occur when the customer-db-service is restarted, while the backend is still running and thus invalidates the remote-object reference
                // retry once with a fresh remote-reference
                rmiClient.reconnect();
                result = method.invoke(target, args);
            } else {
                throw e.getTargetException();
            }

        }
        return result;
    }
}
