package GradeTracker.ModelComponents;

import GradeTracker.AssignmentClasses.AtomicAssignment;
import GradeTracker.AssignmentClasses.CompoundAssignment;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kilian on 3/7/2017.
 *
 * The model class that stores data for the gradeTracker program.
 *
 * @author Kilian Roberts
 * @author Jack Kennelly
 * @author Dustin Michels
 */
public class GTModel {
    private Map<String, ModelCourse> courseMap;
    private List<GTObserver> observerList;
    private Map<String, ModelCourse> latestCourses;

    //---------------------------------------CONSTRUCTOR AND COURSE MANAGEMENT METHODS------------------------------
    public GTModel() {
        this.courseMap = new TreeMap<String, ModelCourse>();
        this.observerList = new LinkedList<GTObserver>();
        this.latestCourses = new TreeMap<String, ModelCourse>();
    }

    /**
     * This function adds a course to the model
     *
     * @param courseID A string containing the ID of the course that contains the assignment.
     * @param courseName A string containing the name of the assignment that should be marked incomplete
     * @param gScale An iteger array that describes the cutoff points for each grade
     * @return A boolean that describes whether the assignment was successfully updated
     */
    public boolean addCourse(String courseID, String courseName, int[] gScale) {
        if (!courseMap.containsKey(courseID)) {
            ModelCourse courseToAdd = new ModelCourse(courseID, courseName, gScale);
            courseMap.put(courseID, courseToAdd);
            this.updateCourse(courseID);
            return true;
        }
        return false;
    }

    /**
     * This function changes the name, ID, and grading scale of an existing course
     *
     * @param oldCourseID A string containing the ID of the course that contains the assignment.
     * @param newCourseID A string containing the new course ID
     * @param newCourseName A string that contains the new course name.
     * @param new_gScale An integer array that describes the cutoff points for each grade
     * @return A boolean that describes whether the course was successfully updated
     */
    public boolean changeInfoForCourse(String oldCourseID, String newCourseID, String newCourseName, int[] new_gScale) {
        if (this.courseMap.containsKey(oldCourseID)) {
            ModelCourse course = courseMap.get(oldCourseID);
            this.removeCourse(oldCourseID);
            course.setId(newCourseID);
            course.setName(newCourseName);
            course.setGradingScale(new_gScale);

            courseMap.put(newCourseID, course);
            this.updateCourse(newCourseID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function removes a course from the list of courses.
     *
     * @param courseID The ID of the course that should be removed
     * @return A boolean describing whether or not the course was successfully removed
     */
    public boolean removeCourse(String courseID) {
        if (courseMap.containsKey(courseID)) {
            courseMap.remove(courseID);
            this.updateCourse(courseID);
            return true;
        }
        return false;
    }
    //-------------------------------------------------------------------------------------------------------------

    //-------------------------------------COURSE PASS-THROUGH METHODS---------------------------------------------

    /**
     * This function adds an atomic assignment category to a specific course.
     *
     * @param courseID The ID of the course that should get the new category
     * @param categoryName The name of the new category
     * @param weight The weight, as an integer, of the new category
     * @return A boolean describing whether the category was successfully added
     */
    public boolean addAtomicAssignmentCategory(String courseID, String categoryName, Integer weight) {
        if (courseMap.containsKey(courseID)) {
            boolean successBoolean = courseMap.get(courseID).addAtomicAssignmentCategory(categoryName, weight);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    /**
     * This function adds a compound assignment category to a specific course.
     *
     * @param courseID The ID of the course that should get the new category
     * @param categoryName The name of the new category
     * @param weight The weight, as an integer, of the new category
     * @return A boolean describing whether the category was successfully added
     */
    public boolean addCompoundAssignmentCategory(String courseID, String categoryName, Integer weight) {
        if (courseMap.containsKey(courseID)) {
            boolean successBoolean = courseMap.get(courseID).addCompoundAssignmentCategory(categoryName, weight);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    /**
     * Sets the grading scale of the given course to newGradingScale
     * @param courseID The name of a course
     * @param newGradingScale An integer array containing the cutoff points for the grades from A+ to D-
     */
    public void setGradingScale(String courseID, int[] newGradingScale) {
        if (this.courseMap.containsKey(courseID)) {
            this.courseMap.get(courseID).setGradingScale(newGradingScale);
            this.updateCourse(courseID);
        }
    }

    /**
     * Sets the weight of an assignment category in the given course
     * @param courseID The name of the course
     * @param assignmentCategoryName The name of the assignment category
     * @param weight The new weight of the assignment category
     * @return a boolean describing whether or not the weight was successfully set
     */
    public boolean setAssignmentCategoryWeight(String courseID, String assignmentCategoryName, int weight) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).setAssignmentCategoryWeight(assignmentCategoryName, weight);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    /**
     * This function marks the assignment in the given course with the given name as incomplete.
     * Incomplete assignments are ignored when calculating current course grades.
     *
     * @param courseID A string containing the ID of the course that contains the assignment.
     * @param atomicName A string containing the name of the assignment that should be marked incomplete
     * @return A boolean that describes whether the assignment was successfully updated
     */
    public boolean markAtomicAssignmentIncomplete(String courseID, String atomicName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).markAtomicAssignmentIncomplete(atomicName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    /**
     * This function marks the assignment in the given course with the given name as complete.
     * Incomplete assignments are ignored when calculating current course grades.
     *
     * @param courseID A string containing the ID of the course that contains the assignment.
     * @param atomicName A string containing the name of the assignment that should be marked incomplete
     * @return A boolean that describes whether the assignment was successfully updated
     */
    public boolean markAtomicAssignmentComplete(String courseID, String atomicName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).markAtomicAssignmentComplete(atomicName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }


    /**
     * This function removes the assignment category with the given name in the given course.
     *
     * @param courseID A string containing the ID of the course that contains the assignment.
     * @param assignmentCategoryName A string containing the name of the assignment category that should be removed
     * @return A boolean that describes whether the assignment category was successfully removed
     */
    public boolean removeAssignmentCategory(String courseID, String assignmentCategoryName) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBool = this.courseMap.get(courseID).removeAssignmentCategory(assignmentCategoryName);
            this.updateCourse(courseID);
            return successBool;
        }
        return false;
    }

    /**
     * This function adds an atomic assignment with the given name to the given category in the given course.
     *
     * @param courseID A string containing the ID of the course that contains the assignment
     * @param categoryName A string containing the name of the assignment category that should be removed
     * @param assignmentName A string containing the name of the new item
     * @return A boolean that describes whether the assignment was successfully added to the given compound category
     */
    public boolean addAtomicAssignmentToCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID) && courseMap.get(courseID).containsCompound(categoryName)){
            boolean sucessBool = courseMap.get(courseID).addAtomicAssignmentToCompoundCategory(categoryName, assignmentName);
            this.updateCourse(courseID);
            return sucessBool;
        }
        return false;
    }

    /**
     * This function adds a compound assignment with the given name to the given category in the given course.
     *
     * @param courseID A string containing the ID of the course that contains the assignment
     * @param categoryName A string containing the name of the assignment category that should be added
     * @param assignmentName A string containing the name of the new item
     * @return A boolean that describes whether the assignment was successfully added to the given compound category
     */
    public boolean addCompoundAssignmentToCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID) && courseMap.get(courseID).containsCompound(categoryName)){
            return courseMap.get(courseID).addCompoundAssignmentToCompoundCategory(categoryName, new CompoundAssignment(assignmentName, courseMap.get(courseID).getGradingScale()));
        }
        return false;
    }

    /**
     * This function removes an assignment with the given name to the given category in the given course.
     *
     * @param courseID A string containing the ID of the course that contains the assignment
     * @param categoryName A string containing the name of the assignment category that should be removed
     * @param assignmentName A string containing the name of the new item
     * @return A boolean that describes whether the assignment was successfully removed from the given compound category
     */
    public boolean removeAssignmentFromCompoundCategory(String courseID, String categoryName, String assignmentName){
        if (this.courseMap.containsKey(courseID)){
            boolean sucessBool = courseMap.get(courseID).removeAssignmentFromCompoundCategory(categoryName, assignmentName);
            this.updateCourse(courseID);
            return sucessBool;
        }
        return false;
    }

    /**
     * This function sets the score of the given atomic assignment in the given course to the given value.
     *
     * @param courseID A string containing the ID of the course that contains the assignment
     * @param assignmentName The name of an assignment in the course with ID courseID
     * @param score The score that the user received on the assignment
     * @return A boolean that describes whether the assignment's score was successfully set
     */
    public boolean setAssignmentScore(String courseID, String assignmentName, double score) {
        if (this.courseMap.containsKey(courseID)) {
            boolean successBoolean = this.courseMap.get(courseID).setAssignmentScore(assignmentName, score);
            this.updateCourse(courseID);
            return successBoolean;
        } else {
            return false;
        }
    }

    /**
     * This function sets the value of the given atomic assignment in the given course to the given value.
     *
     * @param courseID A string containing the ID of the course that contains the assignment
     * @param assignmentName The name of an assignment in the course with ID courseID
     * @param pointsPossible The new value of the assignment
     * @return A boolean that describes whether the assignment value was successfully set
     */
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
    /**
     * This function registers observers of the model so that they can receive up-to-date information from the model.
     *
     * @param newObserver A GTObserver object that would like to receive updates from the model
     */
    public void registerObserver(GTObserver newObserver) {
        if (!observerList.contains(newObserver)) {
            observerList.add(newObserver);
        }
    }

    /**
     * This function de-registers observers of the model so that they will not receive information from the model.
     *
     * @param observer A GTObserver object that would like not to receive updates from the model
     */
    public void unregisterObserver(GTObserver observer) {
        observerList.remove(observer);
    }

    // This private function notifies all registered observers that updated information is available
    private void notifyObserversOfChange() {
        for (GTObserver observer : observerList) {
            observer.notifyOfChange();
        }
    }

    /**
     * This function de-registers observers of the model so that they will not receive information from the model.
     *
     * @return A copy of the most recent version of the dictionary containing the model's data
     */
    public Map<String, ModelCourse> getLatestCourses() {
        return latestCourses;
    }


    // This function puts a new copy of the given course into the this.latestCourses dictionary.
    private boolean updateCourse(String courseID) {
        if (this.courseMap.containsKey(courseID)) {
            ModelCourse courseClone = courseMap.get(courseID).clone();
            this.latestCourses.put(courseID, courseClone);

            this.notifyObserversOfChange();
            return true;

        // In this case, the item has been removed from courseMap and should also be removed from latestCourses
        } else if (latestCourses.containsKey(courseID)){
            latestCourses.remove(courseID);
            return true;
        }
        return false;
    }

    //-------------------------------------------------------------------------------------------------------------

    //----------------------------------------SAVE/LOAD METHODS----------------------------------------------------

    /** Calls the saving method. Saves the model as a txt file, which can be re-read when the program
     * is reopened. */
    public void saveCouses(){
        ArrayList<String> saveData = this.dataGenerator(this.getLatestCourses());
        ArrayListSaveAndLoad.storeCourseList(saveData);
    }

    /* Reads the data file and sets sets the variables of the model appropriately.
    * Relies on good data being saved (handled in offlineLists.java)
    * to set up everything properly. */
    public void loadCourses() throws FileNotFoundException {
        ArrayList<String> data = ArrayListSaveAndLoad.returnCourseList();
        if (data == null){
            throw new FileNotFoundException();
        }

        String subCategoryName = null;
        String courseName = null;
        String gradeScale = null;
        String lowestCategoryName = null;
        for (int pointer =0; pointer <= data.size() - 5; pointer++) {
            if (data.get(pointer).equals("<course>")){
                courseName = data.get(pointer + 2);
                gradeScale = data.get(pointer + 3);
                this.addCourse(courseName, data.get(pointer + 1), stringToIntArrayConverter(gradeScale));
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

    /* Packages the data within the model into an array. The array is properly formatted so that
    * it can be re-read by loadCourses at a later point in time.*/
    private static ArrayList<String> dataGenerator(Map<String, ModelCourse> coursesMap){
        ArrayList<String> dataList = new ArrayList<>();

        // Write each of the courses to the array
        for (ModelCourse course : coursesMap.values()){
            dataList.add("<course>");
            dataList.add(course.getName());
            dataList.add(course.getID());
            dataList.add(intArrayToStringConverter(course.getGradingScale()));
            for (AtomicAssignment atomicCat : course.getAtomicAssignmentCategories().values()){
                dataList.add("<atomicCategory>");
                dataList.add(atomicCat.getName());
                dataList.add(Integer.toString(course.getCategoryWeights().get(atomicCat.getName())));
                dataList.add(Double.toString(atomicCat.getPointsPossible()));
                dataList.add(Double.toString(atomicCat.getPointsScore()));
                dataList.add(Boolean.toString(atomicCat.completed()));
                dataList.add("</atomicCategory>");
            }

            // Write each of the course's compound categories to the array
            for (CompoundAssignment compCat: course.getCompoundAssignmentCategories().values()) {
                dataList.add("<compoundCategory>");
                dataList.add(compCat.getName());
                dataList.add(Integer.toString(course.getCategoryWeights().get(compCat.getName())));

                // Write each of the atomic assignments in the compound categories to the array
                for (AtomicAssignment subCat : compCat.getAtomicSubAssignmentMap().values()){
                    dataList.add("<subCategory>");
                    dataList.add(subCat.getName());
                    dataList.add(Double.toString(subCat.getPointsPossible()));
                    dataList.add(Double.toString(subCat.getPointsScore()));
                    dataList.add(Boolean.toString(subCat.completed()));
                    dataList.add("</subCategory>");
                }
                dataList.add("</compoundCategory>");
            }

            dataList.add("</course>");
        }
        return dataList;
    }

    /* Converts the integer array into a String, which allows it to be saved easily with the rest of the data*/
    private static String intArrayToStringConverter(int[] gradeArray){
        String gradeScale = "";
        String temp = "";
        for (int item : gradeArray){
            temp = Integer.toString(item);
            gradeScale = gradeScale + "," + temp;
        }
        return gradeScale;
    }

    /* Recreates the integer array stored as a string by intArrayToStringConverter*/
    private int[] stringToIntArrayConverter(String gradeScale){
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
    //-------------------------------------------------------------------------------------------------------------
}
