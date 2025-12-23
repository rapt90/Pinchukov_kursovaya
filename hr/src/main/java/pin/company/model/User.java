package pin.company.model;

import java.io.Serializable;

public class User implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public String login;
    public String password;
    public UserRole role;

    public User(String login, String password, UserRole role) {
        this.login = login; //присваивает знаение аргумента полю объекта
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", login, role == UserRole.ROLE_ADMIN ? "Admin" : "User");
    }
}