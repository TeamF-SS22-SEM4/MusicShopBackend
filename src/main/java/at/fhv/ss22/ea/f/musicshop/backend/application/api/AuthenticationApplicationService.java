package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.exceptions.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.exceptions.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

public interface AuthenticationApplicationService {

    /**
     * @param username
     * @param password
     * @return The created Session-Id and a List of the roles the user has.
     * @throws AuthenticationFailed If the login fails for whatever reason.
     */
    LoginResultDTO login(String username, String password) throws AuthenticationFailed;

    boolean hasRole(SessionId sessionId, UserRole userRole) throws SessionExpired;
    //maybe switch to permission based, where each role has certain permissions
}
