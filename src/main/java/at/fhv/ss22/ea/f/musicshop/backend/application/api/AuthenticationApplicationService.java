package at.fhv.ss22.ea.f.musicshop.backend.application.api;

import at.fhv.ss22.ea.f.musicshop.backend.application.api.exceptions.AuthenticationFailed;

public interface AuthenticationApplicationService {

    /**
     * @param username
     * @param password
     * @return The created Session-Id and a List of the roles the user has.
     * @throws AuthenticationFailed If the login fails for whatever reason.
     */
    LoginResultDTO login(String username, String password) throws AuthenticationFailed;

}
