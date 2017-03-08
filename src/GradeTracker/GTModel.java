package GradeTracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kilian on 3/7/2017.
 *
 * TO DO list:
 * Verify grading scale length and order
 * Figure out how to handle errors
 * Verify that no assignment categories have the same name
 */
public class GTModel {
    private Map<String, ModelCourse> courseMap;
    private List<GTObserver> observerList;
    private Map<String, ModelCourse>  latestCourses;

    //---------------------------------------CONSTRUCTOR AND MODEL METHODS-----------------------------------------
    public GTModel(){
        this.courseMap = new TreeMap<String, ModelCourse>();
        this.observerList = new LinkedList<GTObserver>();
        this.latestCourses = new TreeMap<String, ModelCourse>();
    }

    public boolean addCourse(String courseID, String courseName, int[] gScale) {
        if (! courseMap.containsKey(courseID)) {
            ModelCourse courseToAdd = new ModelCourse(courseID, courseName, gScale);
            courseMap.put(courseID, courseToAdd);
            return true;
        }
        return false;
    }

    public boolean removeCourse(String courseID){
        if (courseMap.containsKey(courseID)){
            courseMap.remove(courseID);
            return true;
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------------------

    //-------------------------------------COURSE PASS-THROUGH METHODS---------------------------------------------
    public boolean addAtomicAssignmentCategory(String courseID, String categoryName, Integer weight){
        if (courseMap.containsKey(courseID)){
            return courseMap.get(courseID).addAtomicAssignmentCategory(categoryName, weight);
        } else {
            return false;
        }
    }

    public boolean addCompoundAssignmentCategory(String courseID, String categoryName, Integer weight){
        if (courseMap.containsKey(courseID)){
            return courseMap.get(courseID).addAtomicAssignmentCategory(categoryName, weight);
        } else {
            return false;
        }
    }

    public void setGradingScale(String courseID, int[] newGradingScale){
        if(this.courseMap.containsKey(courseID)){
            this.courseMap.get(courseID).setGradingScale(newGradingScale);
        }
    }

    public boolean setAssignmentCategoryWeight(String courseID, String assignmentCategoryName, int weight){
        if(this.courseMap.containsKey(courseID)){
            return this.courseMap.get(courseID).setAssignmentCategoryWeight(assignmentCategoryName, weight);
        }
        return false;
    }

    public boolean removeAssignmentCategory(String courseID, String assignmentCategoryName) {
        if(this.courseMap.containsKey(courseID)){
            return this.courseMap.get(courseID).removeAssignmentCategory(assignmentCategoryName);
        }
        return false;
    }

    public void setAssignmentScore(String courseID, String assignmentName, int score) {
        if(this.courseMap.containsKey(courseID)){
            this.courseMap.get(courseID).setAssignmentScore(assignmentName, score);
        }
    }
    //-------------------------------------------------------------------------------------------------------------

    //----------------------------------------OBSERVER PATTERN METHODS---------------------------------------------
    public void registerObserver(GTObserver newObserver){
        if (! observerList.contains(newObserver)){
            observerList.add(newObserver);
        }
    }

    public void unregisterObserver(GTObserver observer){
        observerList.remove(observer);
    }

    private void notifyObserversOfChange() {
        for (GTObserver observer: observerList) {
            observer.notifyOfChange();
        }
    }

    public Map<String, ModelCourse> getLatestCourses(){ return latestCourses; }

    private void updateAllCourses(){ //THIS MAY SPEND TOO MUCH TIME TALKING TO OBSERVERS
        for (ModelCourse course : courseMap.values()) {
            updateCourse(course.getId());
        }
    }

    private boolean updateCourse(String courseID){
        if (courseMap.containsKey(courseID)){
            ModelCourse courseClone = courseMap.get(courseID).clone();
            latestCourses.put(courseID, courseClone);
            this.notifyObserversOfChange();
            return true;
        }
        return false;
    }


}
