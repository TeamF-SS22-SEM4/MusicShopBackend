package at.fhv.ss22.ea.f.musicshop.backend.domain.model;

public enum UserRole {
    EMPLOYEE("Employee"),
    OPERATOR("Operator"),
    ADMIN("Admin"),

    CUSTOMER("Customer");

    private String niceName;

    public String getNiceName() {
        return this.niceName;
    }

    UserRole(String niceName) {
        this.niceName = niceName;
    }

    public static UserRole fromName(String roleName) {
        switch (roleName) {
            case "employee":
                return EMPLOYEE;
            case "operator":
                return OPERATOR;
            case "admin":
                return ADMIN;
            case "customer":
                return CUSTOMER;
            default:
                throw new IllegalStateException("Unknown rolename");
        }
    }
}
