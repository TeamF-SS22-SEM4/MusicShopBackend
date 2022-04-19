package at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface SessionKey { //not named SessionId because naming conflict with domain-object
}
