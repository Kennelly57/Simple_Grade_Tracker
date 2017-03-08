package GradeTracker;

import java.util.*;

/**
 * Created by michelsd on 3/4/17.
 */
public class ModelCourse implements Cloneable {
    private String id;
    private String name;
    private int[] gradingScale;
    private String[] grades;

    private Map<String, SampleAtomicAssignment> atomicAsssignmentCategories;
    private Map<String, SampleCompoundAssignment> compoundAsssignmentCategories;
    //percentage as an integer
    private Map<String, Integer> assignmentCategoryWeights;
    private int totalWeight;

    private Integer maxAssignmentCategoryNumber;


    public ModelCourse(String courseId, String courseName, int[] gScale) {
        this.id = courseId;
        this.name = courseName;

        this.grades = new String[] {"A+", "A", "A-",
                "B+", "B", "B-",
                "C+", "C", "C-",
                "D+", "D", "D-",};

        //Is there a way to check that grading scale returns what we think it should?
        this.gradingScale = gScale;

        this.atomicAsssignmentCategories = new TreeMap<String, SampleAtomicAssignment>();
        this.compoundAsssignmentCategories = new TreeMap<String, SampleCompoundAssignment>();
        this.assignmentCategoryWeights = new TreeMap<String, Integer>();
    }

    public ModelCourse clone(){
        int weight;
        ModelCourse modelClone = new ModelCourse(this.id, this.name, this.gradingScale);

        for (SampleCompoundAssignment compoundAssignmentCategory : compoundAsssignmentCategories.values()) {
            SampleCompoundAssignment compoundAssignmentClone = compoundAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(compoundAssignmentClone.getName());
            modelClone.addCompoundAssignmentCategory(compoundAssignmentClone, weight);//FIX THIS AFTER ADDING A WAY TO ADD ASSIGNMENTS
        }
        for (SampleAtomicAssignment atomicAssignmentCategory : atomicAsssignmentCategories.values()) {
            SampleAtomicAssignment atomicAssignmentClone = atomicAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(atomicAssignmentClone.getName());
            modelClone.addAtomicAssignmentCategory(atomicAssignmentClone, weight);
        }

        return modelClone;
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

    public int[] getGradingScale() {
        return gradingScale;
    }

    public void setGradingScale(int[] newGradingScale){
        this.gradingScale = newGradingScale;
    }

    public String getGrade() { //THIS DOESN'T ACCOUNT FOR INCOMPLETE ASSIGNMENTS
        double currentPerecentage = 0;
        double currentPossible = 0;
        double percentageScore;
        double weightedScore;
        if (this.weightBalanced()){
            for (SampleAtomicAssignment atomicAssignment : atomicAsssignmentCategories.values()) {
                percentageScore = atomicAssignment.getPercentageScore();
                weightedScore = percentageScore * assignmentCategoryWeights.get(atomicAssignment.getName());
                currentPerecentage += weightedScore;
            }

            for (SampleCompoundAssignment compoundAssignment : compoundAsssignmentCategories.values()) {
                percentageScore = compoundAssignment.getPercentageScore();
                weightedScore = percentageScore * assignmentCategoryWeights.get(compoundAssignment.getName());
                currentPerecentage += weightedScore;
            }

            for (int i = 0; i < this.grades.length; i++) {
                if (currentPerecentage > this.gradingScale[i]){
                    return this.grades[i];
                }
            }
            return "F";

        } else{
            return "ERROR";
        }

    }


    //WE NEED TO AVOID DUPLICATE NAMES
    public boolean addAtomicAssignmentCategory(String assignmentCategoryName, Integer weight) {
        totalWeight = totalWeight + weight;
        atomicAsssignmentCategories.put(assignmentCategoryName, new SampleAtomicAssignment(assignmentCategoryName));
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
        return this.totalWeight == 100;
    }

    protected void addAtomicAssignmentCategory(SampleAtomicAssignment atomicAssignment, Integer weight) {
        totalWeight = totalWeight + weight;
        atomicAsssignmentCategories.put(atomicAssignment.getName(), atomicAssignment);
        assignmentCategoryWeights.put(atomicAssignment.getName(), weight);
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

    protected void addCompoundAssignmentCategory(SampleCompoundAssignment compoundAssignment, int weight){
        String assignmentCategoryName = compoundAssignment.getName();
        totalWeight = totalWeight + weight;
        compoundAsssignmentCategories.put(assignmentCategoryName, compoundAssignment);
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
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
                    Assignment assignment = assignmentCategory.getAssignment(assignmentName);
                }
            }
        }
    }
}