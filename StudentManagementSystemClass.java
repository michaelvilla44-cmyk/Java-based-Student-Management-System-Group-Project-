import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    private final Map<String, Student> students = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        loadFromFile();
        while (true) {
            showMenu();
            switch (scanner.nextLine()) {
                case "1" -> addStudent();
                case "2" -> removeStudent();
                case "3" -> updateStudent();
                case "4" -> generateReports();
                case "5" -> saveToFile();
                case "6" -> exit();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showMenu() {
        System.out.println("""
            \n--- Student Management System ---
            1. Add Student
            2. Remove Student
            3. Update Student Grades
            4. Generate Reports
            5. Save to File
            6. Exit
            Choose an option: """);
    }

    private void addStudent() {
        String id = prompt("Enter Student ID: ");
        if (students.containsKey(id)) {
            System.out.println("Student already exists.");
            return;
        }
        String name = prompt("Enter Student Name: ");
        students.put(id, new Student(id, name));
        System.out.println("Student added.");
    }

    private void removeStudent() {
        String id = prompt("Enter Student ID to remove: ");
        System.out.println(students.remove(id) != null ? "Student removed." : "Student not found.");
    }

    private void updateStudent() {
        String id = prompt("Enter Student ID: ");
        Student student = students.get(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        String subject = prompt("Enter Subject Name: ");
        try {
            double grade = Double.parseDouble(prompt("Enter Grade (0-100): "));
            student.addOrUpdateSubject(subject, grade);
            System.out.println("Grade updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade.");
        }
    }

    private void generateReports() {
        System.out.println("""
            \n--- Reports ---
            1. Average grade per student
            2. Highest/Lowest grade in a subject
            3. Students sorted by average grade
            Choose report: """);

        switch (scanner.nextLine()) {
            case "1" -> students.values().forEach(
                    s -> System.out.println(s + " | Subjects: " + s.getSubjects())
            );
            case "2" -> reportSubjectExtremes();
            case "3" -> students.values().stream()
                    .sorted(Comparator.comparingDouble(Student::getAverageGrade).reversed())
                    .forEach(System.out::println);
            default -> System.out.println("Invalid option.");
        }
    }

    private void reportSubjectExtremes() {
        String subject = prompt("Enter subject name: ");
        Student top = null, bottom = null;
        double max = -1, min = 101;

        for (Student s : students.values()) {
            for (Subject sub : s.getSubjects()) {
                if (sub.getName().equalsIgnoreCase(subject)) {
                    double grade = sub.getGrade();
                    if (grade > max) { max = grade; top = s; }
                    if (grade < min) { min = grade; bottom = s; }
                }
            }
        }

        if (top != null) {
            System.out.println("Highest: " + top.getName() + " - " + max);
            System.out.println("Lowest: " + bottom.getName() + " - " + min);
        } else {
            System.out.println("No grades found for that subject.");
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter("students.txt")) {
            for (Student s : students.values()) {
                writer.println(s.toFileString());
            }
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File("students.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines()
                  .map(Student::fromFileString)
                  .forEach(s -> students.put(s.getId(), s));
            System.out.println("Data loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private void exit() {
        saveToFile();
        System.out.println("Exiting...");
        System.exit(0);
    }

    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    // Entry point
    public static void main(String[] args) {
        new StudentManagementSystem().start();
    }
}
