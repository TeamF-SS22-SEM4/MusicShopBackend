package at.fhv.ss22.ea.f.musicshop.backend.communication.authentication;

public interface LdapClient {
    boolean credentialsValid(String username, String password);
}
