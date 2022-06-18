package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresRole {
    @Nonbinding
    UserRole[] value() default UserRole.EMPLOYEE;
}
