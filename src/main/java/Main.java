import entity.*;
import enums.ActivityType;
import enums.DisciplineName;
import service.DisciplineService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Університетська система вивчення дисциплін ===\n");

        DisciplineService service = new DisciplineService();

        Teacher teacherMalkevish = new Teacher("Малькевич Р.О.");
        Teacher teacherPetrov = new Teacher("Петров А.О.");

        Teacher teacherPechenenko = new Teacher("Печененко В.О.");
        Teacher teacherMelnyk = new Teacher("Мельник М.М.");

        Teacher teacherBax = new Teacher("Бах К.О.");
        Teacher teacherBondar = new Teacher("Бондар Б.Б.");

        List<Student> allStudents = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            allStudents.add(new Student("Студент " + i, true));
        }

        Subgroup subgroup1 = new Subgroup("Підгрупа 1", allStudents.subList(0, 12));
        Subgroup subgroup2 = new Subgroup("Підгрупа 2", allStudents.subList(12, 25));

        Group group1 = new Group("Б-121-24-1-ПІ", 1, allStudents);
        group1.addSubgroup(subgroup1);
        group1.addSubgroup(subgroup2);

        System.out.println("Групу створено: " + group1);
        System.out.println("Підгрупи: " + subgroup1 + ", " + subgroup2);
        System.out.println();

        System.out.println("─── Сценарій 1: Основи програмування ───");

        Discipline basics = service.createDiscipline(DisciplineName.BASICS_OF_PROGRAMMING, group1);
        basics.addListener(event -> System.out.println("  [ПОДІЯ] " + event.getMessage()));

        service.addActivity(basics, ActivityType.LECTURE, 32, teacherMalkevish);
        service.addActivity(basics, ActivityType.LAB_WORK, 16, teacherPetrov);
        service.addActivity(basics, ActivityType.MODULE_CONTROL, 4, teacherMalkevish);
        service.addActivity(basics, ActivityType.EXAM, 4, teacherMalkevish);
        service.addActivity(basics, ActivityType.CREDIT, 8, teacherMalkevish);

        basics.addTeacher(teacherMalkevish);
        basics.addTeacher(teacherPetrov);

        System.out.println("Загальна кількість годин: " + basics.getTotalHours());

        service.startDiscipline(basics);
        System.out.println("Статус дисципліни: " + basics);
        System.out.println();

        System.out.println("Студенти здають роботи...");
        for (Student student : allStudents.subList(0, 20)) {
            service.submitStudentWork(student);
        }
        System.out.println("Роботи здали: 20 студентів\n");

        System.out.println("Проведення активностей:");
        service.conductActivity(basics, ActivityType.LECTURE);
        service.conductActivity(basics, ActivityType.LAB_WORK);
        service.conductActivity(basics, ActivityType.MODULE_CONTROL);
        service.conductActivity(basics, ActivityType.EXAM);
        service.conductActivity(basics, ActivityType.CREDIT);
        System.out.println();

        System.out.println("─── Сценарій 2: ООП ───");

        Discipline oop = service.createDiscipline(DisciplineName.OOP, group1);
        oop.addListener(event -> System.out.println("  [ПОДІЯ] " + event.getMessage()));

        service.addActivity(oop, ActivityType.LECTURE, 32, teacherPechenenko);
        service.addActivity(oop, ActivityType.LAB_WORK, 16, teacherMelnyk);
        service.addActivity(oop, ActivityType.COURSE_WORK, 8, teacherPechenenko);
        service.addActivity(oop, ActivityType.MODULE_CONTROL, 4, teacherPechenenko);
        service.addActivity(oop, ActivityType.EXAM, 4, teacherPechenenko);

        oop.addTeacher(teacherPechenenko);
        oop.addTeacher(teacherMelnyk);

        service.startDiscipline(oop);
        System.out.println("Статус: " + oop);
        System.out.println();

        System.out.println("Зміна викладача ООП (лекції): Сидоров → новий викладач Ткач Т.Т.");
        Teacher teacherTkach = new Teacher("Ткач Т.Т.");
        service.changeTeacher(oop, ActivityType.LECTURE, teacherTkach);
        System.out.println();

        System.out.println("─── Сценарій 3: Перевірка обмежень ───");

        System.out.print("Спроба повторно записати 'Основи програмування': ");
        try {
            Discipline dup = service.createDiscipline(DisciplineName.BASICS_OF_PROGRAMMING, group1);
            service.startDiscipline(dup);
        } catch (IllegalStateException exception) {
            System.out.println("ПОМИЛКА – " + exception.getMessage());
        }

        System.out.print("Спроба вивчити 'Алгоритми та СД' на 1 курсі: ");
        try {
            Discipline algDs = service.createDiscipline(DisciplineName.ALGORITHMS_AND_DATA_STRUCTURES, group1);
            service.addActivity(algDs, ActivityType.LECTURE, 32, teacherBax);
            service.addActivity(algDs, ActivityType.LAB_WORK, 16, teacherBondar);
            service.addActivity(algDs, ActivityType.EXAM, 16, teacherBax);
            algDs.addTeacher(teacherBax);
            service.startDiscipline(algDs);
        } catch (IllegalStateException e) {
            System.out.println("ПОМИЛКА – " + e.getMessage());
        }

        System.out.print("Спроба провести МКР без зданих студентами робіт: ");
        try {
            List<Student> freshStudents = new ArrayList<>();
            for (int i = 1; i <= 25; i++) freshStudents.add(new Student("Новий студент " + i, true));
            Group group2 = new Group("Б-121-24-2-ПІ", 2, freshStudents);
            group2.addSubgroup(new Subgroup("Підгрупа А", freshStudents.subList(0, 12)));
            group2.addSubgroup(new Subgroup("Підгрупа Б", freshStudents.subList(12, 25)));

            Discipline algDs2 = service.createDiscipline(DisciplineName.ALGORITHMS_AND_DATA_STRUCTURES, group2);
            service.addActivity(algDs2, ActivityType.LECTURE, 32, teacherBax);
            service.addActivity(algDs2, ActivityType.LAB_WORK, 16, teacherBondar);
            service.addActivity(algDs2, ActivityType.MODULE_CONTROL, 8, teacherBax);
            service.addActivity(algDs2, ActivityType.EXAM, 8, teacherBax);
            algDs2.addTeacher(teacherBax);
            algDs2.addTeacher(teacherBondar);
            service.startDiscipline(algDs2);
            service.conductActivity(algDs2, ActivityType.MODULE_CONTROL);
        } catch (IllegalStateException e) {
            System.out.println("ПОМИЛКА – " + e.getMessage());
        }

        System.out.print("Спроба створити підгрупу з 5 студентів: ");
        try {
            new Subgroup("Мала підгрупа", allStudents.subList(0, 5));
        } catch (IllegalArgumentException e) {
            System.out.println("ПОМИЛКА – " + e.getMessage());
        }

        System.out.print("Спроба призначити зайнятого викладача до іншої дисципліни: ");
        try {
            List<Student> st3 = new ArrayList<>();
            for (int i = 1; i <= 15; i++) st3.add(new Student("Ст" + i, true));
            Group g3 = new Group("Б-122-24-1-ПІ", 2, st3);
            g3.addSubgroup(new Subgroup("ПІ-1", st3));
            Discipline algDs3 = service.createDiscipline(DisciplineName.ALGORITHMS_AND_DATA_STRUCTURES, g3);
            service.addActivity(algDs3, ActivityType.LECTURE, 40, teacherPechenenko);
            service.addActivity(algDs3, ActivityType.LAB_WORK, 24, teacherMelnyk);
            algDs3.addTeacher(teacherPechenenko);
        } catch (IllegalStateException e) {
            System.out.println("ПОМИЛКА – " + e.getMessage());
        }
    }
}