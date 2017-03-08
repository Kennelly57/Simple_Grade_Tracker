package GradeTracker;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleAtomicAssignment implements AssignmentViewable {
    private String name;
    private String grade;
    private double pointsPossible;
    private double pointsScore;
    private double percentageScore;
    private double weight;
    private double weightedScore;
    private boolean completed;

    public SampleAtomicAssignment(String assignmentName){
        completed = false;
        this.name = assignmentName;
    }

    public boolean contains(String assignmentName){
        return assignmentName.equalsIgnoreCase(this.name);
    }

    public boolean setScore(String assignmentName, int score){
        if(assignmentName.equalsIgnoreCase(this.name)){
            this.setPointsScore(score);
            return true;
        }
        return false;
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

    public void setPointsPossible(double newPointsPossible){
        this.pointsPossible = newPointsPossible;
    }
    public double getPointsPossible(){
        return this.pointsPossible;
    }

    public void setPointsScore(double newPointsScore){
        this.pointsScore = newPointsScore;
    }
    public double getPointsScore(){
        return this.pointsScore;
    }

    public void setPercentageScore(double newPercentageScore){
        this.percentageScore = newPercentageScore;
    }
    public void calculatePercentageScore() { setPercentageScore(getPointsScore() / getPointsPossible()); }
    public double getPercentageScore(){
        return this.percentageScore;
    }

    public void setWeight(double newWeight) { this.weight = newWeight; }
    public double getWeight() {return this.weight; }

    public void setWeightedScore(double newWeightedScore) { this.weightedScore = newWeightedScore; }
    public void calculateWeightedScore() { setWeightedScore(getPercentageScore() * getWeight()); }
    public double getWeightedScore() {
        calculateWeightedScore();
        return this.weightedScore;
    }

    public boolean completed(){
        return this.completed;
    }
}
