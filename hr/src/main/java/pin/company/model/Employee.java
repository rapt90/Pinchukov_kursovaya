package pin.company.model;

import java.io.Serializable;

public class Employee implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public String fullNameEmployee;
    public String post;
    public String departmentName;
    public String birthDate;
    public String startDate;

    public Employee() {}

    @Override
    public String toString() {
        return String.format(" ФИО сотрудника: %s | Должность: %s | Название отдела: %s | Дата рождения: %s | Дата начала работы: %s",
                fullNameEmployee, post, departmentName, birthDate, startDate);
    }
}