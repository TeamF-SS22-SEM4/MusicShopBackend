package at.fhv.ss22.ea.f.musicshop.backend.communication.ejb;

import at.fhv.ss22.ea.f.communication.api.AuthenticationService;
import at.fhv.ss22.ea.f.communication.dto.LoginResultDTO;
import at.fhv.ss22.ea.f.communication.exception.AuthenticationFailed;
import at.fhv.ss22.ea.f.musicshop.backend.application.api.AuthenticationApplicationService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Remote(AuthenticationService.class)
@Stateless
public class AuthenticationServiceImpl implements AuthenticationService {

    @EJB
    private AuthenticationApplicationService authenticationApplicationService;

    @Override
    public LoginResultDTO login(String username, String password) throws AuthenticationFailed {
        return this.authenticationApplicationService.employeeLogin(username, password);
    }
}
