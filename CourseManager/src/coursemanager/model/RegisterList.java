package coursemanager.model;

import coursemanager.io.DataManager;
import coursemanager.io.DataParser;
import coursemanager.util.Validation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterList extends CommonList<Register> {
    private final DataManager dataManager;

    public RegisterList() {
        super();
        this.dataManager = DataManager.getInstance();
    }

    // Add new register at the beginning (if we care about recent entries)
    public void addFirst(Register register) {
        super.addFirst(register);
    }

    // Add new register at the end
    public void addLast(Register register) {
        super.addLast(register);
    }
    
    public void load() throws IOException {
        DataParser<Register> dataParser = new DataParser<>() {
            @Override
            public Register parse(String data) {
                String[] properties = data.split(",");
                if (properties.length != 5) {
                    return null; 
                }

                String ccode = properties[0].trim(); 
                String scode = properties[1].trim();
                
                Date bdate;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    bdate = dateFormat.parse(properties[2].trim()); 
                } catch (Exception e) {
                    System.out.println("Invalid date format: " + properties[2]);
                    return null;
                }

                double mark;
                try {
                    mark = Double.parseDouble(properties[3].trim());
                    if (mark < 0 || mark > 10) {
                        System.out.println("Invalid mark: " + properties[3]);
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid mark format: " + properties[3]);
                    return null;
                }
                
                int state;
                try {
                    state = Integer.parseInt(properties[4].trim());
                    if (!Validation.isBooleanInt(state)) {
                        System.out.println("Invalid state: " + properties[4]);
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid state format: " + properties[4]);
                    return null;
                }

               
                return new Register(ccode, scode, bdate, mark, state);
            }
        };

        File file = new File(DataManager.REGISTER_SAVE_FILE);

     
        this.readFile(file, dataParser); 
    }
    
    public void saveData(File file) throws IOException{
    	saveFile(file);
    }


    // Method to register a course for a student
    public void registerCourse(String ccode, String scode) {
        final Node<Course> courseNode = dataManager.getCourseList().searchByCcode(ccode); // Search for the course by course code
        final Node<Student> studentNode = dataManager.getStudentList().searchByScode(scode); // Search for the student by student code (ID)

        if (courseNode == null) {
            System.out.println("Course does not exist.");
            return;
        }

        if (studentNode == null) {
            System.out.println("Student does not exist.");
            return;
        }

        Course course = courseNode.data;
        Student student = studentNode.data;

        // Check if the course has available seats
        if (course.getSeats() <= 0) {
            System.out.println("No seats available for the course.");
            return;
        }

        Date today = new Date();
        Register newRegistration = new Register(ccode, scode, today, 0, 0);
        addFirst(newRegistration);

        course.updateSeatAndRegister(-1, 1);

        System.out.println("Course successfully registered for student: " + scode);
    }

    // Update the mark of a registration
    public void updateMark(String scode, String ccode, double newmark) {
        Node<Register> current = head;
        while (current != null) {
            if (scode.equals(current.data.getScode()) && ccode.equals(current.data.getCcode())) {
                current.data.setMark(newmark);
                return;
            }
            current = current.next;
        }
        System.out.println("Registration not found. Update mark failed!");
    }

    @Override
    public CommonList<Register> sort() {
        if (head == null) {
            return null;
        }

        for (Node<Register> i = head; i != null; i = i.next) {
            Node<Register> minNode = i;

            for (Node<Register> j = i.next; j != null; j = j.next) {
                if (shouldSwap(minNode, j)) {
                    minNode = j;
                }
            }

            if (minNode != i) {
                swap(i, minNode);
            }
        }

        return this;
    }

    private boolean shouldSwap(Node<Register> a, Node<Register> b) {
        int ccodeComparison = a.data.getCcode().compareTo(b.data.getCcode());
        if (ccodeComparison > 0) {
            return true;
        }
        if (ccodeComparison == 0) {
            return a.data.getScode().compareTo(b.data.getScode()) > 0;
        }
        return false;
    }

    @Override
    public void display() {
        if (head == null) {
            System.out.println("No registrations available.");
            return;
        }

        Node<Register> current = head;
        while (current != null) {
            current.data.displayRegistrationInfo();
            System.out.println("---------------------");
            current = current.next;
        }
    }
}
