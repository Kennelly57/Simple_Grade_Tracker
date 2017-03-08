package GradeTracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kilian on 3/7/2017.
 */
public class GTModel {
    private Map<String, ModelCourse> courseMap;

    public GTModel(){
        this.courseMap = new TreeMap<String, ModelCourse>();
    }

    public void addCourse(String courseID, String courseName, int[] gScale){
        ModelCourse courseToAdd = new ModelCourse(courseID, courseName, gScale);
    }

    public void addAtomicAssignmentCategory(String courseID, String categoryName, Integer weight){

    }

    public void addCompoundAssignmentCategory(String courseID, String categoryName, Integer weight){

    }

    public void setGradingScale(String courseName, int[] newGradingScale){
        if(this.courseMap.containsKey(courseName)){
            this.courseMap.get(courseName).setGradingScale(newGradingScale);
        }
    }

    public boolean setAssignmentCategoryWeight(String courseName, String assignmentCategoryName, int weight){
        if(this.courseMap.containsKey(courseName)){
            return this.courseMap.get(courseName).setAssignmentCategoryWeight(assignmentCategoryName, weight);
        }
        return false; // THIS IS BAD. FIX IT.
    }

    public boolean removeAssignmentCategory(String courseName, String assignmentCategoryName) {
        if(this.courseMap.containsKey(courseName)){
            return this.courseMap.get(courseName).removeAssignmentCategory(assignmentCategoryName);
        }
        return false;
    }

    public void setAssignmentScore(String courseName, String assignmentName, int score) {
        if(this.courseMap.containsKey(courseName)){
            this.courseMap.get(courseName).setAssignmentScore(assignmentName, score);
        }
    }


}
