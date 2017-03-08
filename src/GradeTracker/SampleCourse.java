package GradeTracker;

import java.util.List;

/**
 * Created by michelsd on 3/4/17.
 */
public class SampleCourse {
    private String id;
    private String name;
    private String grade;
    private float[] gradingScale;
    private List<AssignmentViewable> assignmentViewables;

    public SampleCourse(String courseId, String courseName, String courseGrade) {
        this.id = courseId;
        this.name = courseName;
        this.grade = courseGrade;
        this.gradingScale = new float[12];
    }

    public SampleCourse(String courseId, String courseName, String courseGrade, float[] gScale, List<AssignmentViewable> assignmentViewableList) {
        this.id = courseId;
        this.name = courseName;
        this.grade = courseGrade;

        //Is there a way to check that grading scale returns what we think it should?
        this.gradingScale = gScale;

        this.assignmentViewables = assignmentViewableList;
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
