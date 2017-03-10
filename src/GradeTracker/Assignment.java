package GradeTracker;

/**
 * Created by robertsk2 on 3/4/17.
 */
public interface Assignment extends Cloneable {

    String getName();

    String getGrade();

    double getPointsPossible();

    double getPointsScore();

    double getPercentageScore();

    boolean completed();

    boolean contains(String assignmentName);

    boolean setScore(String assignmentName, double Score);

    boolean setPointsPossible(String assignmentName, double PointsPossible);

    Assignment getAssignment(String assignmentName);

    void markAsCompleted();

    void markAsIncomplete();

    Assignment clone();

}
