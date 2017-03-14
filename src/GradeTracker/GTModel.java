package GradeTracker;

import java.util.*;

import static GradeTracker.OfflineLists.returnCourseList;

/**
 * Created by Kilian on 3/7/2017.
 * <p>
 * TO DO list:
 * Verify grading scale length and order
 * Figure out how to handle errors
 * Verify that no assignment categories have the same name
 */
public class GTModel {
    private Map<String, ModelCourse> courseMap;
    private List<GTObserver> observerList;
    private Map<String, ModelCourse> latestCourses;

    //---------------------------------------CONSTRUCTOR AND MODEL METHODS-----------------------------------------
    public GTModel() {
        this.courseMap = new TreeMap<String, ModelCourse>();
        this.observerList = new LinkedList<GTObserver>();
        this.latestCourses = new TreeMap<String, ModelCourse>();
    }

    public boolean addCourse(String courseID, String courseName, int[] gScale) {
        if (!courseMap.containsKey(courseID)) {
            ModelCourse courseToAdd = new ModelCourse(courseID, courseName, gScale);
            courseMap.put(courseID, courseToAdd);
            this.updateCourse(courseID);
//            System.out.print("ADDED COURSE: ");
//            System.out.println(courseID);
            return true;
        }
        return false;
    }

    public boolean removeCourse(String courseID) {
        if (courseMap.containsKey(courseID)) {
            courseMap.remove(courseID);
            this.updateCourse(courseID);
            System.out.println("removed!");
            return true;
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------------------

    //-------------------------------------COURSE PASS-THROUGH METHODS---------------------------------------------
    public boolean addAtomicAssignmentCategory(String courseID, String categoryName, Integer weight) {
        if (courseMap.containsKey(courseID)) {
//            System.out.print("Adding category ");
//            System.out.print(categoryName);
//            System.out.print(" to ");
//            System.out.println(courseID);
            boolean successBoolean = courseMap.get(courseID).addAtomicAssignmentCategory(categoryName, weight);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    public boolean addCompoundAssignmentCategory(String courseID, String categoryName, Integer weight) {
        if (courseMap.containsKey(courseID)) {
            boolean successBoolean = courseMap.get(courseID).addCompoundAssignmentCategory(categoryName, weight);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    public void setGradingScale(String courseID, int[] newGradingScale) {
        if (this.courseMap.containsKey(courseID)) {
            this.courseMap.get(courseID).setGradingScale(newGradingScale);
            this.updateCourse(courseID);
        }
    }

    public void changeCourseId(String oldID, String newId) {
        if (this.courseMap.containsKey(oldID)) {

        }
    }

    public boolean setAssignmentCategoryWeight(String courseID, String assignmentCategoryName, int weight) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).setAssignmentCategoryWeight(assignmentCategoryName, weight);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    public boolean markAtomicAssignmentIncomplete(String courseID, String atomicName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).markAtomicAssignmentIncomplete(atomicName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    public boolean markAtomicAssignmentComplete(String courseID, String atomicName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).markAtomicAssignmentComplete(atomicName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }


    public boolean removeAssignmentCategory(String courseID, String assignmentCategoryName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).removeAssignmentCategory(assignmentCategoryName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    public boolean addAtomicAssignmentToCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID) && courseMap.get(courseID).containsCompound(categoryName)){
            boolean sucessBool = courseMap.get(courseID).addAtomicAssignmentToCompoundCategory(categoryName, assignmentName);
            this.updateCourse(courseID);
            return sucessBool;
        }
        return false;
    }

    public boolean addCompoundAssignmentToCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID) && courseMap.get(courseID).containsCompound(categoryName)){
            //return courseMap.get(courseID).addCompoundAssignmentToCompoundCategory(categoryName, );
        }
        return false;
    }

    public boolean removeAssignmentFromCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID)){
            boolean sucessBool = courseMap.get(courseID).removeAssignmentFromCompoundCategory(categoryName, assignmentName);
            this.updateCourse(courseID);
            return sucessBool;
        }
        return false;
    }

    public boolean setAssignmentScore(String courseID, String assignmentName, double score) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBoolean = this.courseMap.get(courseID).setAssignmentScore(assignmentName, score);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    public boolean setAssignmentPointsPossible(String courseID, String assignmentName, double pointsPossible) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBoolean = this.courseMap.get(courseID).setAtomicAssignmentPointsPossible(assignmentName, pointsPossible);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    //-------------------------------------------------------------------------------------------------------------

    //----------------------------------------OBSERVER PATTERN METHODS---------------------------------------------
    public void registerObserver(GTObserver newObserver) {
        if (!observerList.contains(newObserver)) {
            observerList.add(newObserver);
        }
    }

    public void unregisterObserver(GTObserver observer) {
        observerList.remove(observer);
    }

    private void notifyObserversOfChange() {
        for (GTObserver observer : observerList) {
            observer.notifyOfChange();
        }
    }

    public Map<String, ModelCourse> getLatestCourses() {
        return latestCourses;
    }

    private void updateAllCourses() { //THIS MAY SPEND TOO MUCH TIME TALKING TO OBSERVERS
        for (ModelCourse course : courseMap.values()) {
            updateCourse(course.getID());
        }
    }

    private boolean updateCourse(String courseID) {
        if (courseMap.containsKey(courseID)) {
            ModelCourse courseClone = courseMap.get(courseID).clone();
            latestCourses.put(courseID, courseClone);

            this.notifyObserversOfChange();
            return true;
        } else if (latestCourses.containsKey(courseID)){
            latestCourses.remove(courseID);
            return true;
        }
        return false;
    }


    public int[] gradeScaleStringParser(String gradeScale){ //Maybe set to private?
        int[] gScale = new int[12];
        int stringLength = gradeScale.length();
        int currentIndex = 0;
        for (int cursor = 0; cursor < stringLength; cursor++){
            if (gradeScale.charAt(cursor) == ',') {
                String tempString = gradeScale.substring(cursor+1, cursor+3);
                int tempInt = Integer.parseInt(tempString);
                gScale[currentIndex] = tempInt;
                currentIndex++;
            }
        }
        return gScale;
    }

    /* Reads the data file and sets the model appropriately.
    * Relies on good data being saved (handled in offlineLists.java)
    * to set up everything properly. */
    public void dataSetter(){
        ArrayList<String> data = returnCourseList();
        String subCategoryName = null;
        String courseName = null;
        String gradeScale = null;
        String lowestCategoryName = null;
        for (int pointer =0; pointer <= data.size() - 5; pointer++) {
            if (data.get(pointer).equals("<course>")){
                courseName = data.get(pointer + 1);
                gradeScale = data.get(pointer + 3);
                this.addCourse(courseName, data.get(pointer + 2), gradeScaleStringParser(gradeScale));
            } else if (data.get(pointer).equals("<atomicCategory>")) {
                subCategoryName = data.get(pointer + 1);
                this.addAtomicAssignmentCategory(courseName, subCategoryName, Integer.parseInt(data.get(pointer + 2)));
                this.setAssignmentPointsPossible(courseName, subCategoryName, Double.parseDouble(data.get(pointer + 3)));
                this.setAssignmentScore(courseName, subCategoryName, Double.parseDouble(data.get(pointer + 4)));
                if (Boolean.parseBoolean(data.get(pointer + 5))){
                    this.markAtomicAssignmentComplete(courseName, subCategoryName);
                } else if (!Boolean.parseBoolean(data.get(pointer + 5))){
                    this.markAtomicAssignmentIncomplete(courseName, subCategoryName);
                }
            } else if (data.get(pointer).equals("<compoundCategory>")){
                subCategoryName = data.get(pointer + 1);
                this.addCompoundAssignmentCategory(courseName, subCategoryName, Integer.parseInt(data.get(pointer + 2)));
            } else if (data.get(pointer).equals("<subCategory>")) {
                lowestCategoryName = data.get(pointer + 1);
                this.addAtomicAssignmentToCompoundCategory(courseName, subCategoryName, lowestCategoryName);
                this.setAssignmentPointsPossible(courseName, lowestCategoryName, Double.parseDouble(data.get(pointer + 2)));
                this.setAssignmentScore(courseName, lowestCategoryName, Double.parseDouble(data.get(pointer + 3)));
                if (Boolean.parseBoolean(data.get(pointer + 4))){
                    this.markAtomicAssignmentComplete(courseName, subCategoryName);
                } else if (!Boolean.parseBoolean(data.get(pointer + 4))){
                    this.markAtomicAssignmentIncomplete(courseName, subCategoryName);
                }
            }
        }
    }
}
