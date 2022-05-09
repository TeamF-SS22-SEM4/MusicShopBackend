package at.fhv.ss22.ea.f.musicshop.backend.communication.authentication;

public interface LdapClient {
    boolean employeeCredentialsValid(String username, String password);
    boolean customerCredentialsValid(String username, String password);
}
