package GradeTracker;

/**
 * Created by michelsd on 3/4/17.
 */
public class SampleCourse {
    public String id;
    public String name;
    public String grade;

    public SampleCourse(String courseId, String courseName, String courseGrade) {
        this.id = courseId;
        this.name = courseName;
        this.grade = courseGrade;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getGrade() {
        return this.grade;
    }
}
