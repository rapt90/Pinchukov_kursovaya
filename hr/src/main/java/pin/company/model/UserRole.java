package pin.company.model;

public enum UserRole {
    ROLE_ADMIN(1),
    ROLE_USER(2);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public static UserRole fromValue(int value) {
        for (UserRole role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Недопустимое значение роли: " + value);
    }
}