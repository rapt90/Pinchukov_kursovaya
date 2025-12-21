package pin.company.service;

import pin.company.model.AppState;
import pin.company.model.Employee;
import pin.company.ui.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeService {
    private static final int MAX_EMPLOYEES = 100;
    private static final int MAX_FULLNAME_LENGTH = 50;
    private static final int MAX_POST_LENGTH = 50;
    private static final int MAX_DEPARTMENT_LENGTH = 50;
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private final ValidationService validationService;
    private final FileService fileService;
    private final SimpleDateFormat dateFormat;

    public EmployeeService(ValidationService validationService, FileService fileService) {
        this.validationService = validationService;
        this.fileService = fileService;
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.dateFormat.setLenient(false);
    }

    public void addEmployee(AppState state, Scanner scanner) {
        if (state.employees.size() >= MAX_EMPLOYEES) {
            System.out.println("Достигнуто максимальное количество сотрудников (" + MAX_EMPLOYEES + ")");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        Employee newEmployee = new Employee();
        ConsoleHelper.clearInputBuffer(scanner);

        newEmployee.fullNameEmployee = ConsoleHelper.getLimitedInput(scanner, "ФИО сотрудника", MAX_FULLNAME_LENGTH);
        newEmployee.post = ConsoleHelper.getLimitedInput(scanner, "Должность", MAX_POST_LENGTH);
        newEmployee.departmentName = ConsoleHelper.getLimitedInput(scanner, "Название отдела", MAX_DEPARTMENT_LENGTH);

        do {
            System.out.print("Дата рождения сотрудника (DD-MM-YYYY): ");
            newEmployee.birthDate = scanner.next();
            if (!validationService.isValidDate(newEmployee.birthDate)) {
                System.out.println("Некорректный формат даты! Используйте DD-MM-YYYY.");
            }
        } while (!validationService.isValidDate(newEmployee.birthDate));

        do {
            System.out.print("Дата начала работы сотрудника (DD-MM-YYYY): ");
            newEmployee.startDate = scanner.next();
            if (!validationService.isValidDate(newEmployee.startDate)) {
                System.out.println("Некорректный формат даты! Используйте DD-MM-YYYY.");
            }
        } while (!validationService.isValidDate(newEmployee.startDate));

        state.employees.add(newEmployee);

        if (fileService.saveEmployees(state)) {
            System.out.println("Сотрудник успешно добавлен");
        } else {
            System.out.println("Не получилось сохранить информацию о сотруднике");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void editEmployee(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Сотрудников не найдено");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.println("Список сотрудников:");
        for (int i = 0; i < state.employees.size(); i++) {
            Employee employee = state.employees.get(i);
            System.out.println((i + 1) + ". " + employee.fullNameEmployee);
            System.out.println("   Должность: " + employee.post);
            System.out.println("   Название отдела: " + employee.departmentName);
            System.out.println("   Дата рождения сотрудника: " + employee.birthDate);
            System.out.println("   Дата начала работы сотрудника: " + employee.startDate + "\n");
        }

        System.out.print("\nВыберите сотрудника для редактирования (0 чтобы выйти): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > state.employees.size()) {
            if (choice != 0) {
                System.out.println("Некорректный выбор");
                ConsoleHelper.pressAnyKeyToContinue(scanner);
            }
            return;
        }

        Employee employee = state.employees.get(choice - 1);
        int editChoice;

        do {
            System.out.println("\nРедактировать данные сотрудника: " + employee.fullNameEmployee);
            System.out.println("Должность: " + employee.post);
            System.out.println("Название отдела: " + employee.departmentName);
            System.out.println("Дата рождения сотрудника (DD-MM-YYYY): " + employee.birthDate);
            System.out.println("Дата начала работы сотрудника (DD-MM-YYYY): " + employee.startDate + "\n");

            System.out.println("1. Редактировать ФИО сотрудника");
            System.out.println("2. Редактировать должность");
            System.out.println("3. Редактировать название отдела");
            System.out.println("4. Редактировать дату рождения сотрудника");
            System.out.println("5. Редактировать дату начала работы сотрудника");
            System.out.println("0. Завершить редактирование");
            System.out.print("Выбор: ");
            editChoice = scanner.nextInt();

            ConsoleHelper.clearInputBuffer(scanner);
            switch (editChoice) {
                case 1:
                    System.out.print("Новое ФИО: ");
                    employee.fullNameEmployee = scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Новая должность: ");
                    employee.post = scanner.nextLine();
                    break;
                case 3:
                    System.out.print("Новое название отдела: ");
                    employee.departmentName = scanner.nextLine();
                    break;
                case 4:
                    do {
                        System.out.print("Новая дата рождения сотрудника (DD-MM-YYYY): ");
                        employee.birthDate = scanner.next();
                        if (!validationService.isValidDate(employee.birthDate)) {
                            System.out.println("Некорректный формат даты (use DD-MM-YYYY)");
                        }
                    } while (!validationService.isValidDate(employee.birthDate));
                    break;
                case 5:
                    do {
                        System.out.print("Новая дата начала работы сотрудника (DD-MM-YYYY): ");
                        employee.startDate = scanner.next();
                        if (!validationService.isValidDate(employee.startDate)) {
                            System.out.println("Некорректный формат даты (use DD-MM-YYYY)");
                        }
                    } while (!validationService.isValidDate(employee.startDate));
                    break;
            }

            if (editChoice != 0) {
                ConsoleHelper.pressAnyKeyToContinue(scanner);
            }
        } while (editChoice != 0);

        if (fileService.saveEmployees(state)) {
            System.out.println("Изменения успешно сохранены");
        } else {
            System.out.println("Не удалось сохранить изменения");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void deleteEmployee(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Сотрудников не найдено");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.println("Список сотрудников:");
        for (int i = 0; i < state.employees.size(); i++) {
            Employee employee = state.employees.get(i);
            System.out.println((i + 1) + ". " + employee.fullNameEmployee + " (Должность: " + employee.post + ")");
        }

        System.out.print("\nВыберите сотрудника для удаления (0 чтобы выйти): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // очистка '\n' после nextInt()

        if (choice < 1 || choice > state.employees.size()) {
            if (choice != 0) {
                System.out.println("Некорректный выбор");
            }
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        int selectedIndex = choice - 1;
        Employee employee = state.employees.get(selectedIndex);

        System.out.print("Вы действительно хотите удалить сотрудника " + employee.fullNameEmployee + "? (1 - Да, 0 - Нет): ");
        int confirm = scanner.nextInt();
        scanner.nextLine(); // очистка '\n'

        if (confirm == 1) {
            state.employees.remove(selectedIndex);

            if (fileService.saveEmployees(state)) {
                System.out.println("Сотрудник успешно удалён");
            } else {
                System.out.println("Не удалось сохранить изменения");
            }
        }
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void displayEmployees(AppState state, Scanner scanner) {
        displayEmployees(state, state.employees, scanner);
    }

    public void displayEmployees(AppState state, List<Employee> list, Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("Сотрудников не найдено");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.println("=== Список сотрудников ===\n");
        for (int i = 0; i < list.size(); i++) {
            Employee employee = list.get(i);
            System.out.println((i + 1) + ". " + employee.fullNameEmployee);
            System.out.println("   Должность: " + employee.post);
            System.out.println("   Название отдела: " + employee.departmentName);
            System.out.println("   Дата рождения сотрудника: " + employee.birthDate);
            System.out.println("   Дата начала работы сотрудника: " + employee.startDate + "\n");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void sortEmployees(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Нет сотрудников для сортирвоки");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        System.out.println("Сортировка по:");
        System.out.println("1. ФИО");
        System.out.println("2. Должности");
        System.out.println("3. Названию отдела");

        System.out.print("Выбор: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                state.employees.sort(Comparator.comparing(d -> d.fullNameEmployee));
                System.out.println("Сотрудники отсортированы по ФИО");
                break;
            case 2:
                state.employees.sort(Comparator.comparing(d -> d.post));
                System.out.println("Сотрудники отсортированы по должности");
                break;
            case 3:
                state.employees.sort(Comparator.comparing(d -> d.departmentName));
                System.out.println("Сотрудники отсортированы по названию отдела");
                break;
            default:
                System.out.println("Неверный выбор");
                break;
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void searchEmployees(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Нет сотрудников для поиска");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        while (true) {
            System.out.println("\n=== Поиск сотрудников ===");
            System.out.println("1. Поиск по ФИО");
            System.out.println("2. Поиск по должности");
            System.out.println("3. Поиск по названию отдела");
            System.out.println("0. Выход из меню поиска");
            System.out.print("Выбор: ");

            scanner.nextLine();
            String choiceStr = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: нужно ввести число от 0 до 3");
                continue;
            }

            if (choice == 0) {
                System.out.println("Выход из меню поиска...");
                break;
            }

            List<Employee> results = new ArrayList<>();

            switch (choice) {
                case 1:
                    System.out.print("Введите ФИО сотрудника для поиска: ");
                    String nameQuery = scanner.nextLine().trim().toLowerCase();
                    if (!nameQuery.isEmpty()) {
                        for (Employee s : state.employees) {
                            if (s.fullNameEmployee != null && s.fullNameEmployee.toLowerCase().contains(nameQuery)) {
                                results.add(s);
                            }
                        }
                    }
                    break;

                case 2:
                    System.out.print("Введите должность сотрудника для поиска: ");
                    String brandQuery = scanner.nextLine().trim().toLowerCase();
                    if (!brandQuery.isEmpty()) {
                        for (Employee s : state.employees) {
                            if (s.post != null && s.post.toLowerCase().contains(brandQuery)) {
                                results.add(s);
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.print("Введите название отдела для поиска: ");
                    String statusQuery = scanner.nextLine().trim().toLowerCase();
                    if (!statusQuery.isEmpty()) {
                        for (Employee s : state.employees) {
                            if (s.departmentName != null && s.departmentName.toLowerCase().contains(statusQuery)) {
                                results.add(s);
                            }
                        }
                    }
                    break;

                default:
                    System.out.println("Неверный выбор. Введите число от 0 до 3");
                    break;
            }

            if (results.isEmpty()) {
                System.out.println("Сотрудники не найдены");
            } else {
                System.out.println("\n=== РЕЗУЛЬТАТЫ ПОИСКА ===");
                System.out.println("Найдено сотрудников: " + results.size());
                displayEmployees(state, results, scanner);
            }

            ConsoleHelper.pressAnyKeyToContinue(scanner);
        }
    }

    public void displayEmployeeExperience(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Нет сотрудников для расчёта стажа");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        try {
            Date referenceDate = validationService.parseDate("15-12-2025");

            List<Employee> sortedEmployees = getEmployees(state, referenceDate);

            System.out.println("\n=== Список сотрудников по стажу (на 15-12-2025) ===\n");

            for (int i = 0; i < sortedEmployees.size(); i++) {
                Employee employee = sortedEmployees.get(i);
                Date startDate = validationService.parseDate(employee.startDate);

                System.out.println((i + 1) + ". " + employee.fullNameEmployee);
                System.out.println("   Должность: " + employee.post);
                System.out.println("   Отдел: " + employee.departmentName);
                System.out.println("   Дата начала работы: " + employee.startDate);

                if (startDate != null) {
                    long experienceDays = (referenceDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                    long experienceYears = experienceDays / 365;
                    long remainingDays = experienceDays % 365;
                    System.out.printf("   Стаж: %d лет, %d дней\n", experienceYears, remainingDays);
                } else {
                    System.out.println("   Стаж: дата начала работы не указана");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Ошибка при обработке дат");
        }
        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    public void displayPensioners(AppState state, Scanner scanner) {
        if (state.employees.isEmpty()) {
            System.out.println("Нет сотрудников для проверки");
            ConsoleHelper.pressAnyKeyToContinue(scanner);
            return;
        }

        try {
            Calendar nowCal = Calendar.getInstance(); // Текущая дата
            Date currentDate = nowCal.getTime();

            System.out.println("\n=== Список сотрудников пенсионного возраста (65+) ===\n");
            System.out.println("Дата проверки: " + dateFormat.format(currentDate));
            System.out.println();

            boolean hasPensioners = false;
            int pensionerCount = 0;

            for (int i = 0; i < state.employees.size(); i++) {
                Employee employee = state.employees.get(i);
                Date birthDate = validationService.parseDate(employee.birthDate);

                if (birthDate != null) {
                    int ageInYears = calculateExactAge(birthDate, currentDate);

                    if (ageInYears >= 65) {
                        pensionerCount++;
                        hasPensioners = true;

                        Calendar retirementCal = Calendar.getInstance();
                        retirementCal.setTime(birthDate);
                        retirementCal.add(Calendar.YEAR, 65);
                        Date retirementDate = retirementCal.getTime();

                        String pensionStatus = retirementDate.before(currentDate)
                                ? "Уже на пенсии"
                                : "Выйдет на пенсию: " + dateFormat.format(retirementDate);

                        System.out.println(pensionerCount + ". " + employee.fullNameEmployee);
                        System.out.println("   Должность: " + employee.post);
                        System.out.println("   Отдел: " + employee.departmentName);
                        System.out.println("   Дата рождения: " + employee.birthDate);
                        System.out.println("   Возраст: " + ageInYears + " лет");
                        System.out.println("   Статус: " + pensionStatus);

                        if (retirementDate.before(currentDate)) {
                            int pensionYears = calculateExactAge(retirementDate, currentDate);
                            System.out.println("   На пенсии: " + pensionYears + " лет");
                        }

                        System.out.println("   Дата начала работы: " + employee.startDate);
                        System.out.println();
                    }
                }
            }

            if (!hasPensioners) {
                System.out.println("Сотрудников пенсионного возраста не найдено");
            } else {
                System.out.println("Всего сотрудников пенсионного возраста: " + pensionerCount);

                System.out.println("\n=== Статистика ===");
                System.out.println("Всего сотрудников в системе: " + state.employees.size());
                System.out.println("Процент пенсионеров: " +
                        String.format("%.1f", (pensionerCount * 100.0 / state.employees.size())) + "%");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при обработке данных: " + e.getMessage());
        }

        scanner.nextLine();
        ConsoleHelper.pressAnyKeyToContinue(scanner);
    }

    // Вспомогательные методы
    private List<Employee> getEmployees(AppState state, Date referenceDate) {
        List<Employee> sortedEmployees = new ArrayList<>(state.employees);

        sortedEmployees.sort((e1, e2) -> {
            Date start1 = validationService.parseDate(e1.startDate);
            Date start2 = validationService.parseDate(e2.startDate);

            if (start1 == null && start2 == null) return 0;
            if (start1 == null) return 1;
            if (start2 == null) return -1;

            long exp1 = referenceDate.getTime() - start1.getTime();
            long exp2 = referenceDate.getTime() - start2.getTime();

            return Long.compare(exp2, exp1);
        });
        return sortedEmployees;
    }

    private int calculateExactAge(Date birthDate, Date currentDate) {
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(currentDate);

        int age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

        if (nowCal.get(Calendar.MONTH) < birthCal.get(Calendar.MONTH) ||
                (nowCal.get(Calendar.MONTH) == birthCal.get(Calendar.MONTH) &&
                        nowCal.get(Calendar.DAY_OF_MONTH) < birthCal.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
}