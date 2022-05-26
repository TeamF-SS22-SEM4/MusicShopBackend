package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.communication.exception.SessionExpired;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.session.SessionId;

import javax.ejb.Local;

@Local
public interface AuthenticationApplicationService {

    /**
     * @param username
     * @param password
     * @return The created Session-Id and a List of the roles the user has.
     * @throws AuthenticationFailed If the login fails for whatever reason.
     */
    LoginResultDTO employeeLogin(String username, String password) throws AuthenticationFailed;
    LoginResultDTO customerLogin(String username, String password) throws AuthenticationFailed;

    boolean checkValidity(String sessionId);

    /**Do <b>NOT</b> expose to communication!
     *
     * @param sessionId
     * @param userRole
     * @return
     * @throws SessionExpired If no valid session has been found.
     */
    boolean hasRole(SessionId sessionId, UserRole userRole) throws SessionExpired;
    //maybe switch to permission based, where each role has certain permissions
}
