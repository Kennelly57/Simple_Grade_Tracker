package GradeTracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleCompoundAssignment implements Assignment {
    private String name;
    private boolean completed;
    private int[] gradingScale;

    //Question: How do we deal with points based vs percentage based weighting
    private Boolean compound;
    private Map<String, Assignment> subAssignmentMap;

    public SampleCompoundAssignment(int[] newGradingScale){
        completed = false;
        this.gradingScale = newGradingScale;//CAN WE TEST TO VERIFY THIS IS CORRECTLY FORMATED
        subAssignmentMap = new HashMap<String, Assignment>();
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
        for (Assignment subAssignment : subAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsPossibleSum += subAssignment.getPointsPossible();
            }
        }
        return pointsPossibleSum;
    }


    public double getPointsScore(){
        double pointsAchievedSum = 0;
        for (Assignment subAssignment : subAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsAchievedSum += subAssignment.getPointsScore();
            }
        }
        return pointsAchievedSum;
    }


    public double getPercentageScore(){
        double pointsPossibleSum = 0;
        double pointsAchievedSum = 0;
        for (Assignment subAssignment : subAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsPossibleSum += subAssignment.getPointsPossible();
                pointsAchievedSum += subAssignment.getPointsScore();
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

    public void addAssignment(Assignment newAssignment){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF DUPLICATE NAMES!
        subAssignmentMap.put(newAssignment.getName(), newAssignment);
    }

    public Assignment getAssignment(String targetName){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF A WRONG NAME!
        Assignment target = subAssignmentMap.get(targetName);
        return target;
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
