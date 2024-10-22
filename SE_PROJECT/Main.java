import java.util.*;

class Instructor {
    private int instructorId;
    private String name;
    private String department;
    private String password;

    public Instructor(int instructorId, String name, String department, String password) {
        this.instructorId = instructorId;
        this.name = name;
        this.department = department;
        this.password = password;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Instructor ID: " + instructorId + ", Name: " + name + ", Department: " + department;
    }
}

class Course {
    private int courseId;
    private String courseName;
    private int capacity;
    private int filledSeats;
    private int instructorId;

    public Course(int courseId, String courseName, int capacity, int instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.capacity = capacity;
        this.instructorId = instructorId;
        this.filledSeats = 0;
    }

    public boolean isAvailable() {
        return filledSeats < capacity;
    }

    public void allocateSeat() {
        if (isAvailable()) {
            filledSeats++;
        }
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getInstructorId() {
        return instructorId;
    }

    @Override
    public String toString() {
        return "Course ID: " + courseId + ", Course Name: " + courseName + ", Instructor ID: " + instructorId + ", Filled Seats: " + filledSeats + "/" + capacity;
    }
}

class Student {
    private int studentId;
    private String name;
    private String registrationId;
    private String rollNo;
    private double CGPA;
    private String password;
    private List<Course> preferences;
    private Course allocatedCourse;

    public Student(int studentId, String name, String registrationId, String rollNo, double CGPA, String password, List<Course> preferences) {
        this.studentId = studentId;
        this.name = name;
        this.registrationId = registrationId;
        this.rollNo = rollNo;
        this.CGPA = CGPA;
        this.password = password;
        this.preferences = preferences;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public double getCGPA() {
        return CGPA;
    }

    public List<Course> getPreferences() {
        return preferences;
    }

    public void setAllocatedCourse(Course course) {
        this.allocatedCourse = course;
    }

    public Course getAllocatedCourse() {
        return allocatedCourse;
    }

    public String getPassword() {
        return password;
    }

    public void printDetails() {
        System.out.println("Student ID: " + studentId);
        System.out.println("Student Name: " + name);
        System.out.println("Registration ID: " + registrationId);
        System.out.println("Roll No: " + rollNo);
        System.out.println("CGPA: " + CGPA);
    }

    public void printAllocationDetails() {
        if (allocatedCourse != null) {
            System.out.println("Allocated Course: " + allocatedCourse.getCourseName());
        } else {
            System.out.println("No course allocated.");
        }
    }
}

class CourseAllocator {
    public void allocateCourses(List<Student> students, List<Course> courses) {
        students.sort((s1, s2) -> Double.compare(s2.getCGPA(), s1.getCGPA()));

        for (Student student : students) {
            allocateCourseForStudent(student, courses);
        }
    }

    private void allocateCourseForStudent(Student student, List<Course> courses) {
        double cgpa = student.getCGPA();
        List<Course> preferences = student.getPreferences();
        
        System.out.println("\nAllocating course for student with CGPA: " + cgpa);
        if (cgpa >= 9.0 && cgpa <= 9.9) {
            System.out.println("High priority (CGPA 9.0 - 9.9)");
        } else if (cgpa >= 8.0 && cgpa < 9.0) {
            System.out.println("Medium-high priority (CGPA 8.0 - 8.9)");
        } else if (cgpa >= 7.0 && cgpa < 8.0) {
            System.out.println("Medium priority (CGPA 7.0 - 7.9)");
        } else if (cgpa >= 6.0 && cgpa < 7.0) {
            System.out.println("Low priority (CGPA 6.0 - 6.9)");
        } else {
            System.out.println("CGPA below 6.0, no course allocation.");
            student.setAllocatedCourse(null);
            return;
        }

        // Try to allocate a preferred course
        for (Course preferredCourse : preferences) {
            for (Course availableCourse : courses) {
                if (availableCourse.getCourseId() == preferredCourse.getCourseId() && availableCourse.isAvailable()) {
                    availableCourse.allocateSeat();
                    student.setAllocatedCourse(availableCourse);
                    student.printDetails();
                    student.printAllocationDetails();
                    return;
                }
            }
        }

        // If no preferred course is available, allocate any available course
        for (Course availableCourse : courses) {
            if (availableCourse.isAvailable()) {
                availableCourse.allocateSeat();
                student.setAllocatedCourse(availableCourse);
                student.printDetails();
                student.printAllocationDetails();
                return;
            }
        }

        // If no course is available at all
        student.setAllocatedCourse(null);
        student.printDetails();
        System.out.println("No course allocated.");
    }
}

public class Main {
    private static Map<Integer, Instructor> instructors = new HashMap<>();
    private static Map<Integer, Student> students = new HashMap<>();
    private static List<Course> courses = new ArrayList<>();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("-------------------------------------------------");
            System.out.print("Are you a Student or Instructor? (Type 'S' for Student and 'I' for Instructor, 'Q' to Quit): ");
            String role = scanner.nextLine();
            System.out.println("-------------------------------------------------");

            if (role.equalsIgnoreCase("I")) {
                instructorLogin();
            } else if (role.equalsIgnoreCase("S")) {
                studentLogin();
            } else if (role.equalsIgnoreCase("Q")) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'S', 'I', or 'Q'.");
            }
        }

        CourseAllocator allocator = new CourseAllocator();
        allocator.allocateCourses(new ArrayList<>(students.values()), courses);

        System.out.println("-------------------------------------------------");
        System.out.println("Final Allocations:");
        for (Student student : students.values()) {
            student.printDetails();
            student.printAllocationDetails();
        }
    }

    private static void instructorLogin() {
        System.out.print("Enter Instructor ID: ");
        int instructorId = scanner.nextInt();
        scanner.nextLine();

        if (!instructors.containsKey(instructorId)) {
            System.out.print("Enter Instructor Name: ");
            String instructorName = scanner.nextLine();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            String password = enterPassword();
            Instructor instructor = new Instructor(instructorId, instructorName, department, password);
            instructors.put(instructorId, instructor);
            System.out.println("Instructor registered successfully.");
        }

        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();
        if (instructors.get(instructorId).getPassword().equals(inputPassword)) {
            System.out.println("Instructor logged in: " + instructors.get(instructorId));
            addCourses(instructorId);
        } else {
            System.out.println("Incorrect password.");
        }
    }

    private static void studentLogin() {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        if (!students.containsKey(studentId)) {
            System.out.print("Enter Student Name: ");
            String studentName = scanner.nextLine();
            System.out.print("Enter Registration ID: ");
            String registrationId = scanner.nextLine();
            System.out.print("Enter Roll No: ");
            String rollNo = scanner.nextLine();
            System.out.print("Enter CGPA: ");
            double cgpa = scanner.nextDouble();
            scanner.nextLine();
            String password = enterPassword();

            List<Course> preferences = getStudentPreferences();
            Student student = new Student(studentId, studentName, registrationId, rollNo, cgpa, password, preferences);
            students.put(studentId, student);
            System.out.println("Student registered successfully.");
        }

        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();
        if (students.get(studentId).getPassword().equals(inputPassword)) {
            System.out.println("Student logged in: " + students.get(studentId).getName());
        } else {
            System.out.println("Incorrect password.");
        }
    }

    private static String enterPassword() {
        String password;
        while (true) {
            System.out.print("Enter password (min 6 characters): ");
            password = scanner.nextLine();
            if (password.length() >= 6) {
                break;
            } else {
                System.out.println("Password too short, try again.");
            }
        }
        return password;
    }

    private static void addCourses(int instructorId) {
        System.out.print("Enter number of courses to add: ");
        int courseCount = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < courseCount; i++) {
            System.out.println("Enter details for course " + (i + 1));
            System.out.print("Course ID: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Course Name: ");
            String courseName = scanner.nextLine();
            System.out.print("Capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine();
            Course course = new Course(courseId, courseName, capacity, instructorId);
            courses.add(course);
            System.out.println("Course added successfully.");
        }
    }

    private static List<Course> getStudentPreferences() {
        System.out.print("Enter number of preferences: ");
        int prefCount = scanner.nextInt();
        scanner.nextLine();
        List<Course> preferences = new ArrayList<>();

        for (int i = 0; i < prefCount; i++) {
            System.out.print("Enter course ID for preference " + (i + 1) + ": ");
            int courseId = scanner.nextInt();
            scanner.nextLine();
            boolean found = false;
            for (Course course : courses) {
                if (course.getCourseId() == courseId) {
                    preferences.add(course);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Course with ID " + courseId + " not found.");
            }
        }
        return preferences;
    }
}
