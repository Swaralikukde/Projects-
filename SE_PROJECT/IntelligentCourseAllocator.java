import java.util.*;

class Instructor {
    private int instructorId;
    private String name;
    private String department;

    public Instructor(int instructorId, String name, String department) {
        this.instructorId = instructorId;
        this.name = name;
        this.department = department;
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
        return "Course ID: " + courseId + ", Course Name: " + courseName + ", Instructor ID: " + instructorId;
    }
}

class Student {
    private int studentId;
    private String name;
    private String registrationId;
    private String rollNo;
    private double CGPA;
    private List<Course> preferences;
    private Course allocatedCourse;

    public Student(int studentId, String name, String registrationId, String rollNo, double CGPA, List<Course> preferences) {
        this.studentId = studentId;
        this.name = name;
        this.registrationId = registrationId;
        this.rollNo = rollNo;
        this.CGPA = CGPA;
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

    public void printDetails() {
        System.out.println("Student ID: " + studentId);
        System.out.println("Student Name: " + name);
        System.out.println("Registration ID: " + registrationId);
        System.out.println("Roll No: " + rollNo);
        System.out.println("CGPA: " + CGPA);
    }

    public void printAllocationDetails() {
        if (allocatedCourse != null) {
            System.out.println("Allocated Course Details: ");
            System.out.println(allocatedCourse.toString());
        } else {
            System.out.println("No course allocated.");
        }
    }
}

class CourseAllocator {
    public void allocateCourses(List<Student> students, List<Course> courses) {
        // Sort students by CGPA in descending order
        students.sort((s1, s2) -> Double.compare(s2.getCGPA(), s1.getCGPA()));

        for (Student student : students) {
            allocateCourseForStudent(student, courses);
        }
    }

    private void allocateCourseForStudent(Student student, List<Course> courses) {
        List<Course> preferences = student.getPreferences();

        for (Course preferredCourse : preferences) {
            // Check if the preferred course exists in the courses list
            for (Course course : courses) {
                if (course.getCourseId() == preferredCourse.getCourseId() && course.isAvailable()) {
                    course.allocateSeat();
                    student.setAllocatedCourse(course);
                    student.printDetails();
                    student.printAllocationDetails();
                    return;
                }
            }
        }
        student.setAllocatedCourse(null);  // No course allocated if all preferences are full
        student.printDetails();
        System.out.println("No course allocated.");
    }
}

public class IntelligentCourseAllocator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        Instructor instructor = null;

        while (true) {
            System.out.println("-------------------------------------------------");
            System.out.print("Are you a Student or Instructor? (Type 'S' for Student and 'I' for Instructor, 'Q' to Quit): ");
            String role = scanner.nextLine();
            System.out.println("-------------------------------------------------");

            if (role.equalsIgnoreCase("I")) {
                // Instructor login
                System.out.print("Enter Instructor ID: ");
                int instructorId = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                System.out.print("Enter Instructor Name: ");
                String instructorName = scanner.nextLine();
                System.out.print("Enter Department: ");
                String department = scanner.nextLine();
                instructor = new Instructor(instructorId, instructorName, department);

                // Display instructor details
                System.out.println("Instructor logged in: " + instructor);
                System.out.println("-------------------------------------------------");

                // Add courses
                System.out.print("Enter number of courses to add: ");
                int courseCount = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                for (int i = 0; i < courseCount; i++) {
                    System.out.println("Enter details for course " + (i + 1));
                    System.out.print("Course ID: ");
                    int courseId = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Course Name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Capacity: ");
                    int capacity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    Course course = new Course(courseId, courseName, capacity, instructorId);
                    courses.add(course);
                    System.out.println("-------------------------------------------------");
                }

            } else if (role.equalsIgnoreCase("S")) {
                // Student login
                System.out.print("Enter Student ID: ");
                int studentId = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                System.out.print("Enter Student Name: ");
                String studentName = scanner.nextLine();
                System.out.print("Enter Registration ID: ");
                String registrationId = scanner.nextLine();
                System.out.print("Enter Roll No: ");
                String rollNo = scanner.nextLine();
                System.out.print("Enter CGPA: ");
                double cgpa = scanner.nextDouble();
                scanner.nextLine();  // Consume newline

                // Add student preferences
                System.out.print("Enter number of preferences: ");
                int prefCount = scanner.nextInt();
                List<Course> preferences = new ArrayList<>();
                for (int i = 0; i < prefCount; i++) {
                    System.out.print("Enter course ID for preference " + (i + 1) + ": ");
                    int courseId = scanner.nextInt();
                    preferences.add(new Course(courseId, "", 0, 0));  // Temporary course for preference
                }

                Student student = new Student(studentId, studentName, registrationId, rollNo, cgpa, preferences);
                students.add(student);

                System.out.println("-------------------------------------------------");
            } else if (role.equalsIgnoreCase("Q")) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'S', 'I', or 'Q'.");
            }
        }

        // Allocate courses to students
        CourseAllocator allocator = new CourseAllocator();
        allocator.allocateCourses(students, courses);

        // Finally, print all student details with allocated courses
        System.out.println("-------------------------------------------------");
        System.out.println("All Students and their Allocated Courses:");
        for (Student student : students) {
            student.printDetails();
            student.printAllocationDetails();
            System.out.println("-------------------------------------------------");
        }

        scanner.close();
    }
}
