package at.fhv.ss22.ea.f.musicshop.backend.unit.application;

import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.RequiresRole;
import at.fhv.ss22.ea.f.musicshop.backend.application.impl.decorators.SessionKey;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

interface TestingInterfaceValid {
    @RequiresRole(value = UserRole.EMPLOYEE)
    boolean testing(@SessionKey String sessionId, Integer anotherParam);
}
