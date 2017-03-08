package GradeTracker;

import java.util.*;

/**
 * Created by michelsd on 3/4/17.
 */
public class ModelCourse {
    private String id;
    private String name;
    private String grade;
    private int[] gradingScale;

    private Map<String, SampleAtomicAssignment> atomicAsssignmentCategories;
    private Map<String, SampleCompoundAssignment> compoundAsssignmentCategories;
    //percentage as an integer
    private Map<String, Integer> assignmentCategoryWeights;
    private int totalWeight;

    private Integer maxAssignmentCategoryNumber;


    public ModelCourse(String courseId, String courseName, int[] gScale) {
        this.id = courseId;
        this.name = courseName;

        //Is there a way to check that grading scale returns what we think it should?
        this.gradingScale = gScale;

        this.atomicAsssignmentCategories = new TreeMap<String, SampleAtomicAssignment>();
        this.compoundAsssignmentCategories = new TreeMap<String, SampleCompoundAssignment>();
        this.assignmentCategoryWeights = new TreeMap<String, Integer>();
    }


    public String getId() {
        return this.id;
    }

    public boolean weightBalanced(){
        return this.totalWeight == 100;
    }

    public void computeTotalWeight(){
        int runningTotal = 0;
        for (Integer weight : assignmentCategoryWeights.values()) {
            runningTotal += weight;
        }
        this.totalWeight = runningTotal;
    }

    public String getName() {
        return this.name;
    }

    public String getGrade() {
        return this.grade;
    }


    //WE NEED TO AVOID DUPLICATE NAMES
    public boolean addAtomicAssignmentCategory(String assignmentCategoryName, Integer weight) {
        totalWeight = totalWeight + weight;
        atomicAsssignmentCategories.put(assignmentCategoryName, new SampleAtomicAssignment(assignmentCategoryName));
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
        return this.totalWeight == 100;
    }

    public boolean addCompoundAssignmentCategory(String assignmentCategoryName, Integer weight){
        return addCompoundAssignmentCategory(assignmentCategoryName, weight, this.gradingScale);
    }

    public boolean addCompoundAssignmentCategory(String assignmentCategoryName, Integer weight, int[] gScale) {
        totalWeight = totalWeight + weight;
        compoundAsssignmentCategories.put(assignmentCategoryName, new SampleCompoundAssignment(assignmentCategoryName, gScale));
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
        return this.weightBalanced();
    }

    public boolean setAssignmentCategoryWeight(String assignmentCategoryName, int weight){
        if (assignmentCategoryWeights.containsKey(assignmentCategoryName)){
            assignmentCategoryWeights.replace(assignmentCategoryName, weight);
            computeTotalWeight();
            return this.weightBalanced();
        }

        return this.weightBalanced();
    }

    public boolean removeAssignmentCategory(String assignmentCategoryName) {
        int weight = assignmentCategoryWeights.get(assignmentCategoryName);
        assignmentCategoryWeights.remove(assignmentCategoryName);
        atomicAsssignmentCategories.remove(assignmentCategoryName);
        compoundAsssignmentCategories.remove(assignmentCategoryName);
        totalWeight = totalWeight - weight;
        return this.weightBalanced();
    }

    public void setAssignmentScore(String assignmentName, int score) {
        if (atomicAsssignmentCategories.containsKey(assignmentName)) {
            SampleAtomicAssignment assignment = atomicAsssignmentCategories.get(assignmentName);
            assignment.setPointsScore(score);
        } else {//ADD ERROR CHECKING!!!
            for (SampleCompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.contains(assignmentName)){
                    AssignmentViewable assignment = assignmentCategory.getAssignment(assignmentName);
                }
            }
        }
    }
}