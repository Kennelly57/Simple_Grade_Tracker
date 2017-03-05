//DO NOT USE: this doesn't work
package GradeTracker;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class SampleFlexibleAssignment implements Assignment {
    private String name;
    private String grade;
    private float pointsPossible;
    private float pointsScore;
    private float percentageScore;
    private boolean completed;

    //Question: How do we deal with points based vs percentage based weighting
    private Boolean compound;
    private Map<String, Assignment> subAssignmentMap;

    public SampleFlexibleAssignment(){
        completed = false;
        compound = false;
        subAssignmentMap = new HashMap<String, Assignment>();
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
        if(this.compound){
            //THIS SHOULD THROW AN EXCEPTION OR THE ASSIGNMENT NEEDS A GRADING SCHEME
            return "ERROR";
        } else {
            return this.grade;
        }
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
        if(this.compound){
            float pointsPossibleSum = 0;
            float pointsAchievedSum = 0;

            for (Assignment subAssignment : subAssignmentMap.values()) {
                if(subAssignment.completed()){
                    pointsPossibleSum += subAssignment.getPointsPossible();
                    pointsAchievedSum += subAssignment.getPointsScore();
                }
            }

            return pointsAchievedSum/pointsPossibleSum;

        } else {
            return this.percentageScore;
        }
    }

    public boolean completed(){
        return this.completed;
    }


}
