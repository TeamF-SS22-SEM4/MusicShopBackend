package at.fhv.ss22.ea.f.musicshop.backend.communication.rmi.servant;

import at.fhv.ss22.ea.f.communication.api.AuthenticationService;
import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;

public class AuthenticationServiceImpl implements AuthenticationService {

    private AuthenticationApplicationService authenticationApplicationService;

    public AuthenticationServiceImpl(AuthenticationApplicationService authenticationApplicationService) {
        this.authenticationApplicationService = authenticationApplicationService;
    }

    @Override
    public LoginResultDTO login(String username, String password) throws AuthenticationFailed {
        return this.authenticationApplicationService.login(username, password);
    }
}
