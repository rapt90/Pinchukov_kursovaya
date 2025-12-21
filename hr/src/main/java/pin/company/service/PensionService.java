package pin.company.service;

import pin.company.model.Employee;
import pin.company.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PensionService {
    private static final int RETIREMENT_AGE = 65;
    private final ValidationService validationService;

    public PensionService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public void displayPensioners(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Нет сотрудников для проверки");
            return;
        }

        try {
            Calendar nowCal = Calendar.getInstance();
            Date currentDate = nowCal.getTime();

            System.out.println("\n=== Список сотрудников пенсионного возраста (65+) ===\n");
            System.out.println("Дата проверки: " + DateUtils.formatDate(currentDate));
            System.out.println();

            boolean hasPensioners = false;
            int pensionerCount = 0;

            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                Date birthDate = validationService.parseDate(employee.birthDate);

                if (birthDate != null) {
                    int ageInYears = calculateExactAge(birthDate, currentDate);

                    if (ageInYears >= RETIREMENT_AGE) {
                        pensionerCount++;
                        hasPensioners = true;

                        // Рассчитываем дату выхода на пенсию (65 лет)
                        Calendar retirementCal = Calendar.getInstance();
                        retirementCal.setTime(birthDate);
                        retirementCal.add(Calendar.YEAR, RETIREMENT_AGE);
                        Date retirementDate = retirementCal.getTime();

                        // Проверяем, уже на пенсии или скоро выйдет
                        String pensionStatus = retirementDate.before(currentDate)
                                ? "Уже на пенсии"
                                : "Выйдет на пенсию: " + DateUtils.formatDate(retirementDate);

                        System.out.println(pensionerCount + ". " + employee.fullNameEmployee);
                        System.out.println("   Должность: " + employee.post);
                        System.out.println("   Отдел: " + employee.departmentName);
                        System.out.println("   Дата рождения: " + employee.birthDate);
                        System.out.println("   Возраст: " + ageInYears + " лет");
                        System.out.println("   Статус: " + pensionStatus);

                        // Если уже на пенсии, показываем сколько лет на пенсии
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

                // Дополнительная статистика
                System.out.println("\n=== Статистика ===");
                System.out.println("Всего сотрудников в системе: " + employees.size());
                System.out.println("Процент пенсионеров: " +
                        String.format("%.1f", (pensionerCount * 100.0 / employees.size())) + "%");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при обработке данных: " + e.getMessage());
        }
    }

    // Точный расчёт возраста через Calendar
    private int calculateExactAge(Date birthDate, Date currentDate) {
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(currentDate);

        int age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

        // Если день рождения ещё не наступил в этом году, уменьшаем возраст
        if (nowCal.get(Calendar.MONTH) < birthCal.get(Calendar.MONTH) ||
                (nowCal.get(Calendar.MONTH) == birthCal.get(Calendar.MONTH) &&
                        nowCal.get(Calendar.DAY_OF_MONTH) < birthCal.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
}