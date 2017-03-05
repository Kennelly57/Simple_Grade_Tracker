package GradeTracker;

/**
 * Created by robertsk2 on 3/4/17.
 */
public interface Assignment {

    public void setName(String newName);
    public String getName();

    public String getGrade();

    public float getPointsPossible();

    public float getPointsScore();

    public float getPercentageScore();

    public boolean completed();
}
