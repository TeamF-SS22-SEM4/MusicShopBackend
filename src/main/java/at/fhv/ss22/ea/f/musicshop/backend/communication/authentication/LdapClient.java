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
    private static final String ORGANIZATION_BASE_DN = "dc=musicshop,dc=at";
    private static final String EMPLOYEE_GROUP_DN = "ou=users";
    private static final String ROLE_GROUP_DN = "ou=roles";
    private static final String ROLE_MEMBER_FIELD_NAME = "roleOccupant";

    public LdapClient() {
        System.setProperty("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        System.setProperty("java.naming.provider.url", "ldap://localhost:10389"); //TODO get from .env
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

    //below are buried the wasted efforts of being motivated but not reading requirements
    public List<UserRole> rolesOfUser(String username) throws IllegalStateException {
        List<UserRole> userRoles = new LinkedList<>();

        List<String> roles = allRoles();
        for (String role : roles) {
            List<String> employeeDistinguishedNames = distinguishedNameOfRoleMembers(role);

            for (String employeeDN: employeeDistinguishedNames) {
                String foundUsername = employeeDN.split(",")[0].split("=")[1];
                if (foundUsername.equals(username)) {
                    userRoles.add(UserRole.fromName(role));
                }
            }
        }
        return userRoles;
    }

    private List<String> distinguishedNameOfRoleMembers(String roleName) {
        List<String> names = new LinkedList<>();
        try {
            InitialDirContext ctx = new InitialDirContext();
            Attributes attrs = ctx.getAttributes("ou="+roleName+"," + ROLE_GROUP_DN +","+ ORGANIZATION_BASE_DN);
            NamingEnumeration<String> namingEnum = attrs.getIDs();

            while (namingEnum.hasMore()) {
                String name = namingEnum.next();
                if (ROLE_MEMBER_FIELD_NAME.equals(name)) {
                    Attribute attr = attrs.get(name);
                    for (int i = 0; i < attr.size(); i+=1) {
                        names.add((String) attr.get(i));
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return names;
    }

    private List<String> allRoles() {
        List<String> roles = new LinkedList<>();
        try {
            InitialDirContext ctx = new InitialDirContext();
            NamingEnumeration<Binding> rolesBindings = ctx.listBindings(ROLE_GROUP_DN + "," + ORGANIZATION_BASE_DN);
            while (rolesBindings.hasMore()) {
                Binding b = rolesBindings.next();
                roles.add(b.getName().split("=")[1]);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return roles;
    }

}
