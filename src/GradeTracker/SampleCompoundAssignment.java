package GradeTracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleCompoundAssignment implements AssignmentViewable {
    private String name;
    private boolean completed;
    private int[] gradingScale;

    //Question: How do we deal with points based vs percentage based weighting
    private Boolean compound;
    private Map<String, AssignmentViewable> subAssignmentMap;

    public SampleCompoundAssignment(String name, int[] newGradingScale){
        completed = false;
        this.name = name;
        this.gradingScale = newGradingScale;//CAN WE TEST TO VERIFY THIS IS CORRECTLY FORMATED
        subAssignmentMap = new HashMap<String, AssignmentViewable>();
    }

    public boolean contains(String assignmentName){
        for (AssignmentViewable subAssignment: subAssignmentMap.values()) {
            if(subAssignment.contains(assignmentName)){
                return true;
            }
        }
        return false;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public String getGrade(){ // We could make this log(n), but I don't feel like it at the moment.
        double percentageScore = this.getPercentageScore();
        String[] grades = new String[] {"A+", "A", "A-",
                                        "B+", "B", "B-",
                                        "C+", "C", "C-",
                                        "D+", "D", "D-",};
        for (int i = 0; i < grades.length; i++) {
            if(percentageScore >= this.gradingScale[i]){
                return grades[i];
            }
        }
        return "F";
    }

    public double getPointsPossible(){
        double pointsPossibleSum = 0;
        for (AssignmentViewable subAssignmentViewable : subAssignmentMap.values()) {
            if(subAssignmentViewable.completed()){
                pointsPossibleSum += subAssignmentViewable.getPointsPossible();
            }
        }
        return pointsPossibleSum;
    }


    public double getPointsScore(){
        double pointsAchievedSum = 0;
        for (AssignmentViewable subAssignmentViewable : subAssignmentMap.values()) {
            if(subAssignmentViewable.completed()){
                pointsAchievedSum += subAssignmentViewable.getPointsScore();
            }
        }
        return pointsAchievedSum;
    }


    public double getPercentageScore(){
        double pointsPossibleSum = 0;
        double pointsAchievedSum = 0;
        for (AssignmentViewable subAssignmentViewable : subAssignmentMap.values()) {
            if(subAssignmentViewable.completed()){
                pointsPossibleSum += subAssignmentViewable.getPointsPossible();
                pointsAchievedSum += subAssignmentViewable.getPointsScore();
            }
        }
        return pointsAchievedSum/pointsPossibleSum;
    }

    // NOT CUREENTLY IN USE
    public double getWeight(){
        return -1;
    }

    // NOT CUREENTLY IN USE
    public double getWeightedScore(){
        return -1;
    }

    public void addAssignment(AssignmentViewable newAssignmentViewable){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF DUPLICATE NAMES!
        subAssignmentMap.put(newAssignmentViewable.getName(), newAssignmentViewable);
    }


    public AssignmentViewable getAssignment(String targetName){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF A WRONG NAME!
        AssignmentViewable target = subAssignmentMap.get(targetName);
        return target;
    }

    public boolean setScore(String assignmentName, int score) {
        if (this.contains(assignmentName)){
            for (AssignmentViewable subAssignment : subAssignmentMap.values()) {
                if (subAssignment.contains(assignmentName)){
                    subAssignment.setScore(assignmentName, score);
                    return true;
                }
            }
        }
        return false;
    }

    public void removeAssignment(String targetName){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF A WRONG NAME!
        subAssignmentMap.remove(targetName);
    }

    public boolean completed(){
        return this.completed;
    }

    public void markAsCompleted(){
        this.completed = true;
    }
    public void markAsIncomplete(){
        this.completed = false;
    }
}
