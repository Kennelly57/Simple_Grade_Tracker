package GradeTracker;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleAtomicAssignment implements Assignment {
    private String name;
    private String grade;
    private float pointsPossible;
    private float pointsScore;
    private float percentageScore;
    private boolean completed;

    public SampleAtomicAssignment(String assignmentName){
        completed = false;
        this.name = assignmentName;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public void setGrade(String newGrade){
        this.grade = newGrade;
    }

    public String getGrade(){
        return this.grade;
    }


    public void setPointsPossible(float newPointsPossible){
        this.pointsPossible = newPointsPossible;
    }

    public float getPointsPossible(){
        return this.pointsPossible;
    }

    public void setPointsScore(Float newPointsScore){
        this.pointsScore = newPointsScore;
    }

    public float getPointsScore(){
        return this.pointsScore;
    }

    public void setPercentageScore(Float newPercentageScore){
        this.percentageScore = newPercentageScore;
    }

    public float getPercentageScore(){
        return this.percentageScore;
    }

    public boolean completed(){
        return this.completed;
    }
}
