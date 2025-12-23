package pin.company.service;

import pin.company.model.Employee; // модель сотрудника
import pin.company.utils.DateUtils; // утилита для работы с датами

import java.util.Calendar; // класс для работы с календарными датами
import java.util.Date;     // класс для представления даты
import java.util.List;

public class PensionService {
    private static final int RETIREMENT_AGE = 65;
    private final ValidationService validationService; // сервис для проверки и парсинга дат

    // Конструктор, принимает сервис валидации
    public PensionService(ValidationService validationService) {
        this.validationService = validationService;
    }

    // Метод для отображения сотрудников пенсионного возраста
    public void displayPensioners(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Нет сотрудников для проверки");
            return;
        }

        try {
            Calendar nowCal = Calendar.getInstance(); // текущая дата/время
            Date currentDate = nowCal.getTime();      // преобразуем в объект Date

            System.out.println("\n=== Список сотрудников пенсионного возраста (65+) ===\n");
            System.out.println("Дата проверки: " + DateUtils.formatDate(currentDate)); // выводим дату проверки
            System.out.println();

            boolean hasPensioners = false; // флаг, есть ли пенсионеры
            int pensionerCount = 0;        // счётчик пенсионеров

            // перебираем всех сотрудников
            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                Date birthDate = validationService.parseDate(employee.birthDate); // парсим дату рождения

                if (birthDate != null) {
                    int ageInYears = calculateExactAge(birthDate, currentDate); // считаем возраст

                    // если возраст >= пенсионного
                    if (ageInYears >= RETIREMENT_AGE) {
                        pensionerCount++;
                        hasPensioners = true;

                        // рассчитываем дату выхода на пенсию (65 лет от даты рождения)
                        Calendar retirementCal = Calendar.getInstance();
                        retirementCal.setTime(birthDate);
                        retirementCal.add(Calendar.YEAR, RETIREMENT_AGE);
                        Date retirementDate = retirementCal.getTime();

                        // определяем статус: уже на пенсии или выйдет позже
                        String pensionStatus = retirementDate.before(currentDate)
                                ? "Уже на пенсии"
                                : "Выйдет на пенсию: " + DateUtils.formatDate(retirementDate);


                        System.out.println(pensionerCount + ". " + employee.fullNameEmployee);
                        System.out.println("   Должность: " + employee.post);
                        System.out.println("   Отдел: " + employee.departmentName);
                        System.out.println("   Дата рождения: " + employee.birthDate);
                        System.out.println("   Возраст: " + ageInYears + " лет");
                        System.out.println("   Статус: " + pensionStatus);

                        // если уже на пенсии — считаем сколько лет на пенсии
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
                System.out.println("Всего сотрудников в системе: " + employees.size());
                System.out.println("Процент пенсионеров: " +
                        String.format("%.1f", (pensionerCount * 100.0 / employees.size())) + "%");
            }

        } catch (Exception e) {
            // обработка ошибок
            System.out.println("Ошибка при обработке данных: " + e.getMessage());
        }
    }

    // Вспомогательный метод: точный расчёт возраста через Calendar
    private int calculateExactAge(Date birthDate, Date currentDate) {
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate); // устанавливаем дату рождения
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(currentDate); // устанавливаем текущую дату

        int age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR); // разница по годам

        // если день рождения ещё не наступил в этом году — уменьшаем возраст на 1
        if (nowCal.get(Calendar.MONTH) < birthCal.get(Calendar.MONTH) ||
                (nowCal.get(Calendar.MONTH) == birthCal.get(Calendar.MONTH) &&
                        nowCal.get(Calendar.DAY_OF_MONTH) < birthCal.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age; // возвращаем рассчитанный возраст
    }
}
