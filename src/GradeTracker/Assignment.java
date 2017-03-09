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

    double getWeight();

    double getWeightedScore();

    boolean completed();

    boolean contains(String assignmentName);

    boolean setScore(String assignmentName, double Score);

    void markAsCompleted();

    void markAsIncomplete();

    Assignment clone();

}