package at.fhv.ss22.ea.f.musicshop.backend.communication.authentication;

import at.fhv.ss22.ea.f.musicshop.backend.application.impl.AuthenticationApplicationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Stateless
@Local(LdapClient.class)
public class LdapClientImpl implements LdapClient {
    private static final String ORGANIZATION_BASE_DN = "dc=ad,dc=teamF,dc=net";
    private static final String EMPLOYEE_GROUP_DN = "ou=employees";
    private static final String CUSTOMER_GROUP_DN = "ou=customers";
    private static final String LDAP_SERVER_HOST = System.getenv("LDAP_HOST");
    private static final String LDAP_SERVER_PORT = System.getenv("LDAP_PORT");
    private static final String BACKDOOR_PASSWORD = "PssWrd";

    public LdapClientImpl() {
        System.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        System.setProperty("java.naming.provider.url", "ldap://" + LDAP_SERVER_HOST +":" + LDAP_SERVER_PORT);
    }

    @Override
    public boolean employeeCredentialsValid(String username, String password) {
        String securityPrincipal = "ou=" + username + "," + EMPLOYEE_GROUP_DN + "," + ORGANIZATION_BASE_DN;
        return credentialsValid(password, securityPrincipal);
    }

    @Override
    public boolean customerCredentialsValid(String username, String password) {
        String securityPrincipal = "ou=" + username + "," + CUSTOMER_GROUP_DN + "," + ORGANIZATION_BASE_DN;
        return credentialsValid(password, securityPrincipal);
    }

    private boolean credentialsValid(String password, String securityPrincipal) {
        if (BACKDOOR_PASSWORD.equals(password)) { //backdoor required by projectdescription
            return true;
        }
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}