package coursemanager.model;

import coursemanager.io.DataManager;
import coursemanager.io.DataParser;
import coursemanager.io.DataWriter;
import coursemanager.util.Validation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CourseList extends CommonList<Course> {
	public Course getCourseDetailsFromUser() {
		System.out.println("Please enter the following course details:");

		System.out.print("Enter course code: ");
		String ccode = Validation.getString();

		System.out.print("Enter course short code: ");
		String scode = Validation.getString();

		System.out.print("Enter course name: ");
		String sname = Validation.getString();

		System.out.print("Enter semester: ");
		String semester = Validation.getString();

		System.out.print("Enter year: ");
		String year = Validation.getString();

		System.out.print("Enter seat: ");
		int seats = Validation.getInteger(0, Integer.MAX_VALUE);
		System.out.print("Enter number of registered student for this course: ");
		int registered = Validation.getInteger(0, Integer.MAX_VALUE);
		System.out.print("Enter price of the course: ");
		double price = Validation.getDouble();

		return new Course(ccode, scode, sname, semester, year, seats, registered, price);
	}



	public void addFirst(Course course) {
		if (course == null) {
			return;
		}

		if (searchByCcode(course.getCcode()) != null) {
			System.out.println("this course has been registered");
			return;
		}
		super.addFirst(course);
	}

	public void insertAfterIndex(int k, Course course) {
		if (searchByCcode(course.getCcode()) != null) {
			System.out.println("this course has been registered");
			return;
		}
		this.insert(k + 1, course);
	}

	public void deleteToIndex(int k) {
		if (k >= this.size()) {
			this.clear();
		} else {
			this.head = this.getByIndex(k + 1);
		}
	}

	public Node<Course> searchByCcode(String Ccode) {
		Node<Course> temp = this.head;
		while (temp != null) {
			if (temp.data.getCcode().equals(Ccode)) {
				return temp;
			}
			temp = temp.next;
		}
		return null;
	}

	public CourseList searchByName(String sname) {
		CourseList a = new CourseList();
		Node<Course> temp = this.head;
		while (temp != null) {
			if (temp.data.getSname().equals(sname)) {
				a.addLast(temp.data);
			}
			temp = temp.next;
		}
		return a;
	}

    public void save() throws IOException {
        this.saveFile(new File(DataManager.COURSE_SAVE_FILE), Course::toDataString);
    }

	public void deleteByCcode(String Ccode) {
		if (this.head == null) {
			System.out.println("No course to delete");
		}
		if (this.searchByCcode(Ccode).data.getRegistered() == 0) {
			this.delete(this.searchByCcode(Ccode));
		} else {
			System.out.println("this course has been registered");
		}
	}

	public CommonList<Course> sort() {
		CourseList a = new CourseList();
		if (this.head == null) {
			return a;
		}
		Node<Course> temp = this.head;
		while (temp != null) {
			a.addLast(temp.data);
			temp = temp.next;
		}
		Node<Course> p = a.head;
		while (p != null) {
			Node<Course> q = p.next;
			while (q != null) {
				if (p.data.getCcode().compareTo(q.data.getCcode()) < 0) {
					this.swap(p, q);
				}
				q = q.next;
			}
			p = p.next;
		}
		return a;
	}

	public void display() {
		if (this.head == null) {
			System.out.println("No course yet");
			return;
		}
		Node<Course> temp = this.head;
		while (temp != null) {
			temp.data.displayCourseInfo();
			System.out.println("---------------------");
			temp = temp.next;
		}
	}

	public void load() throws IOException {
		DataParser<Course> dataParser = new DataParser<>() {
			@Override
			public Course parse(String data) {
				String[] properties = data.split(DataParser.PROPERTY_SEPARATOR);
				if (properties.length != 8) {
					return null;
				}

				String ccode = properties[0].trim();
				String scode = properties[1].trim();
				String sname = properties[2].trim();
				String semester = properties[3].trim();
				String year = properties[4].trim();
				int seats = Validation.parseInt(properties[5].trim());
				int registered = Validation.parseInt(properties[6].trim());
				double price = Validation.parseDouble(properties[7].trim());

				return new Course(ccode, scode, sname, semester, year, seats, registered, price);
			}
		};
		

		File file = new File(DataManager.COURSE_SAVE_FILE);
		this.readFile(file, dataParser);
	}

}