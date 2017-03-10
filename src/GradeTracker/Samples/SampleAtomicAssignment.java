package GradeTracker.Samples;

import GradeTracker.Assignment;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleAtomicAssignment implements Assignment, Cloneable {
    private String name;
    private String grade;
    private double pointsPossible;
    private double pointsScore;
    private double percentageScore;
    private boolean completed;

    public SampleAtomicAssignment(String assignmentName){
        completed = false;
        this.name = assignmentName;
    }

    @Override
    public SampleAtomicAssignment clone(){
        SampleAtomicAssignment clone = new SampleAtomicAssignment(this.name);
        clone.setScore(this.name, this.pointsScore);
        clone.setGrade(this.grade);
        clone.setPointsPossible(this.pointsPossible);
        if(this.completed){
            clone.completed();
        }
        return clone;
    }

    public boolean contains(String assignmentName){
        return assignmentName.equalsIgnoreCase(this.name);
    }

    public boolean setScore(String assignmentName, double score){
        if(assignmentName.equalsIgnoreCase(this.name)){
            this.setPointsScore(score);
            return true;
        }
        return false;
    }

    public void markAsCompleted(){
        this.completed = true;
    }

    public void markAsIncomplete(){
        this.completed = false;
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

    public Assignment getAssignment(String targetName){
        if (targetName.equalsIgnoreCase(this.name)){
            return this;
        } else {
            return null;
        }
    }

    public boolean setPointsPossible(String targetName, double newPointsPossible){
        if (this.name.equalsIgnoreCase(targetName)){
            setPointsPossible(newPointsPossible);
            return true;
        } else {
            return false;
        }
    }

    public void setPointsPossible(double newPointsPossible){
        this.pointsPossible = newPointsPossible;
    }
    public double getPointsPossible(){
        return this.pointsPossible;
    }

    public void setPointsScore(double newPointsScore){
        this.pointsScore = newPointsScore;
        this.completed = true;
    }
    public double getPointsScore(){
        return this.pointsScore;
    }

    public void setPercentageScore(double newPercentageScore){
        this.percentageScore = newPercentageScore;
    }
    public void calculatePercentageScore() { setPercentageScore(getPointsScore() / getPointsPossible()); }
    public double getPercentageScore(){
        calculatePercentageScore();
        return this.percentageScore;
    }


    public boolean completed(){
        return this.completed;
    }
}
