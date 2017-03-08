package GradeTracker;

/**
 * Created by robertsk2 on 3/4/17.
 */
public interface AssignmentViewable {

    public void setName(String newName);
    public String getName();

    public String getGrade();

    public double getPointsPossible();

    public double getPointsScore();

    public double getPercentageScore();

    public double getWeight();

    public double getWeightedScore();

    public boolean completed();

    public boolean contains(String assignmentName);

    public boolean setScore(String assignmentName, int Score);

}
