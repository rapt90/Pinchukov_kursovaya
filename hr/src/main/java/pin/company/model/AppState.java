package pin.company.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppState implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public List<Employee> employees = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public int currentUserId = -1;
    public UserRole currentUserRole = UserRole.ROLE_USER;
}