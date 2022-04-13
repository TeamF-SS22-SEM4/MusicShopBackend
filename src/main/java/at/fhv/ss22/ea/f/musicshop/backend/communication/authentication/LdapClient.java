package at.fhv.ss22.ea.f.musicshop.backend.communication.authentication;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class LdapClient {
    private static final String ORGANIZATION_BASE_DN = "dc=ad,dc=teamF,dc=net";
    private static final String EMPLOYEE_GROUP_DN = "ou=employees";
    private static final String LDAP_SERVER_HOST = System.getenv("LDAP_HOST");
    private static final String LDAP_SERVER_PORT = System.getenv("LDAP_PORT");
    private static final String BACKDOOR_PASSWORD = "PssWrd";

    public LdapClient() {
        System.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        System.setProperty("java.naming.provider.url", "ldap://" + LDAP_SERVER_HOST +":" + LDAP_SERVER_PORT);
    }

    public boolean credentialsValid(String username, String password) {
        if (BACKDOOR_PASSWORD.equals(password)) { //backdoor required by projectdescription
            return true;
        }
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "ou=" + username + "," + EMPLOYEE_GROUP_DN + "," + ORGANIZATION_BASE_DN);
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