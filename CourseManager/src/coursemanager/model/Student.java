package coursemanager.model;

public class Student {
    private String scode;  // Student Code
    private String name;
    private int byear;  // Birth Year

    private boolean isValidByear(int byear) {
        return byear >= 18;
    }

    public Student() {}

    public Student(String scode, String name, int byear) {
        this.scode = scode;
        this.name = name;

        if (!isValidByear(byear)) {
            throw new IllegalArgumentException();
        }
        this.byear = byear;
    }

    public Student(Student student) {
        this(student.scode, student.name, student.byear);
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getByear() {
        return byear;
    }

    public void setByear(int byear) {
        if (!isValidByear(byear)) {
            throw new IllegalArgumentException();
        }
        this.byear = byear;
    }

    @Override
    public String toString() {
        return "Student{" +
                "scode='" + scode + '\'' +
                ", name='" + name + '\'' +
                ", byear=" + byear +
                '}';
    }
}
