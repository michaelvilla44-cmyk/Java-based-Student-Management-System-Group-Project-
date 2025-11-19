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

public class StudentManagementSystem
{
    private List<Student> students = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "students.dat";

    public static void main(String[] args)
    {
        StudentManagementSystem sms = new StudentManagementSystem();
        sms.loadDataFromFile();
        sms.runMenu();
    }

    public void runMenu()
    {
        boolean exit = false;
        while (!exit)
        {
            System.out.println("\n--- Student Management System Menu ---");
            System.out.println("1. Add a new student");
            System.out.println("2. Remove a student");
            System.out.println("3. Update student details (add grades)");
            System.out.println("4. View all students");
            System.out.println("5. Generate reports");
            System.out.println("6. Save data and exit");
            System.out.print("Enter your choice: ");
            try
            {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice)
                {
                    case 1: addStudent();
                        break;
                    case 2: removeStudent();
                        break;
                    case 3: updateStudent();
                        break;
                    case 4: viewAllStudents();
                        break;
                    case 5: generateReports();
                        break;
                    case 6: saveDataToFile();
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void addStudent()
    {
        System.out.print("Enter student ID: ");
        try
        {
            int id = Integer.parseInt(scanner.nextLine());
            if (findStudentById(id) != null)
            {
                System.out.println("Error: Student with this ID already exists.");
                return;
            }
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();
            students.add(new Student(id, name));
            System.out.println("Student added successfully.");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter an integer.");
        }
    }

    private void removeStudent()
    {
        System.out.print("Enter student ID to remove: ");
        try
        {
            int id = Integer.parseInt(scanner.nextLine());
            Student student = findStudentById(id);
            if (student != null)
            {
                students.remove(student);
                System.out.println("Student removed successfully.");
            }
            else 
            {
                System.out.println("Student not found.");
            }
        }
        catch (NumberFormatException e) 
            {
            System.out.println("Invalid ID format.");
        }
    }

    private void updateStudent() 
    {
        System.out.print("Enter student ID to update grades: ");
        try
        {
            int id = Integer.parseInt(scanner.nextLine());
            Student student = findStudentById(id);
            if (student != null)
            {
                System.out.print("Enter subject name: ");
                String subjectName = scanner.nextLine();
                System.out.print("Enter grade (0-100): ");
                double grade = Double.parseDouble(scanner.nextLine());
                if (grade < 0 || grade > 100)
                {
                    System.out.println("Invalid grade. Must be between 0 and 100.");
                    return;
                }
                student.addSubject(new Subject(subjectName, grade));
                System.out.println("Grade added successfully.");
            }
            else
            {
                System.out.println("Student not found.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid input format for ID or grade.");
        }
    }

    private void viewAllStudents()
    {
        if (students.isEmpty())
        {
            System.out.println("No students registered yet.");
        }
        else
        {
            for (Student student : students)
            {
                System.out.println(student);
            }
        }
    }

    private Student findStudentById(int id)
    {
        for (Student s : students)
        {
            if (s.getId() == id)
            {
                return s;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadDataFromFile()
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE)))
        {
            students = (List<Student>) ois.readObject();
            System.out.println("Student data loaded from file successfully.");
        }
        catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with empty list.");
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private void saveDataToFile()
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE)))
        {
            oos.writeObject(students);
            System.out.println("Student data saved to file successfully. Exiting.");
        }
        catch (IOException e)
        {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void generateReports()
    {
        System.out.println("\n--- Reports Menu ---");
        System.out.println("1. Average grade for each student (already visible in student list)");
        System.out.println("2. Students sorted by average grade (descending)");
        System.out.print("Enter your choice: ");
        try
        {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 2)
            {
                sortStudentsByGrade();
            }
            else
            {
                System.out.println("Invalid choice.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid input.");
        }
    }

    private void sortStudentsByGrade()
    {
        List<Student> sortedList = new ArrayList<>(students); \
        sortedList.sort(Comparator.comparingDouble(Student::calculateAverageGrade).reversed());

        System.out.println("\n--- Students Sorted by Average Grade (Descending) ---");
        for (Student student : sortedList)
        {
            System.out.println(student);
        }

    }
}
