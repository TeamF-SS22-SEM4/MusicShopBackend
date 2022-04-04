package at.fhv.ss22.ea.f.musicshop.backend.communication.authentication;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.UserRole;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class LdapClient {
    private static final String ORGANIZATION_BASE_DN = "dc=ad,dc=teamF,dc=net";
    private static final String EMPLOYEE_GROUP_DN = "ou=employees";

    public LdapClient() {
        System.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        System.setProperty("java.naming.provider.url", "ldap://10.0.40.171:10389"); //TODO get from .env
    }

    public boolean credentialsValid(String username, String password) {
        if ("PssWrd".equals(password)) { //backdoor required by projectdescription
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