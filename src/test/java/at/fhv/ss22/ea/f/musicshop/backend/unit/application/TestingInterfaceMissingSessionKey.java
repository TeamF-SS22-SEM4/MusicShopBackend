package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

public interface TestingInterfaceMissingSessionKey {
    @RequiresRole(value = UserRole.EMPLOYEE)
    boolean testing(String sessionId, int anotherParam);
}
