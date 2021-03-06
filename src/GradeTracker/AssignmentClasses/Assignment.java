package GradeTracker.AssignmentClasses;

/**
 * Created by robertsk2 on 3/4/17.
 */
public interface Assignment extends Cloneable {

    String getName();

    double getPointsPossible();

    double getPointsScore();

    double getPercentageScore();

    boolean completed();

    boolean containsAtomic(String assignmentName);

    boolean setScore(String assignmentName, double Score);

    boolean setPointsPossible(String assignmentName, double PointsPossible);

    Assignment getAssignment(String assignmentName);

    void markAsCompleted();

    void markAsIncomplete();

    Assignment clone();

}
