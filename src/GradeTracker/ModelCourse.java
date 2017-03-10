package GradeTracker;

import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;

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

    @Override
    public ModelCourse clone(){
        int weight;
        ModelCourse modelClone = new ModelCourse(this.id, this.name, this.gradingScale);

        for (SampleCompoundAssignment compoundAssignmentCategory : compoundAsssignmentCategories.values()) {
            SampleCompoundAssignment compoundAssignmentClone = compoundAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(compoundAssignmentClone.getName());
            modelClone.addCompoundAssignmentCategory(compoundAssignmentClone, weight); //FIX THIS AFTER ADDING A WAY TO ADD ASSIGNMENTS
        }
        for (SampleAtomicAssignment atomicAssignmentCategory : atomicAsssignmentCategories.values()) {
            SampleAtomicAssignment atomicAssignmentClone = atomicAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(atomicAssignmentClone.getName());
            modelClone.addAtomicAssignmentCategory(atomicAssignmentClone, weight);
        }

        return modelClone;
    }

    public String getID() {
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

    public Map<String, SampleAtomicAssignment> getAtomicAssignmentCategories(){
        return atomicAsssignmentCategories;
    }
    public Map<String, SampleCompoundAssignment> getCompoundAssignmentCategories(){
        return compoundAsssignmentCategories;
    }
    public Map<String, Integer> getCategoryWeights(){
        return assignmentCategoryWeights;
    }
    public String getGrade() { //THIS DOESN'T ACCOUNT FOR INCOMPLETE ASSIGNMENTS
        double currentPerecentage = 0;
        double currentPossible = 0;
        double percentageScore;
        double weightedScore;
        if (this.weightBalanced()){
            for (SampleAtomicAssignment atomicAssignment : atomicAsssignmentCategories.values()) {
                if (atomicAssignment.completed()) {
                    percentageScore = atomicAssignment.getPercentageScore();
                    weightedScore = percentageScore * assignmentCategoryWeights.get(atomicAssignment.getName());
                    currentPerecentage += weightedScore;
                }
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
            //System.out.println(this.totalWeight);
            return "ERROR";
        }

    }


    //WE NEED TO AVOID DUPLICATE NAMES
    public boolean addAtomicAssignmentCategory(String assignmentCategoryName, Integer weight) {
        return addAtomicAssignmentCategory(new SampleAtomicAssignment(assignmentCategoryName), weight);
    }

    protected boolean addAtomicAssignmentCategory(SampleAtomicAssignment atomicAssignment, Integer weight) {
        totalWeight = totalWeight + weight;
        atomicAsssignmentCategories.put(atomicAssignment.getName(), atomicAssignment);
        assignmentCategoryWeights.put(atomicAssignment.getName(), weight);
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

    protected void addCompoundAssignmentCategory(SampleCompoundAssignment compoundAssignment, int weight){
        String assignmentCategoryName = compoundAssignment.getName();
        totalWeight = totalWeight + weight;
        compoundAsssignmentCategories.put(assignmentCategoryName, compoundAssignment);
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
    }

    public boolean setAssignmentCategoryWeight(String assignmentCategoryName, int weight){
        if (assignmentCategoryWeights.containsKey(assignmentCategoryName)){
            assignmentCategoryWeights.put(assignmentCategoryName, weight);
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

    public boolean addAtomicAssignmentToCompoundCategory(String categoryName, String assignmentName){
        return addAtomicAssignmentToCompoundCategory(categoryName, new SampleAtomicAssignment(assignmentName));
    }

    public boolean addAtomicAssignmentToCompoundCategory(String categoryName, SampleAtomicAssignment atomicAssignment){
        if (compoundAsssignmentCategories.containsKey(categoryName) &&
                ! compoundAsssignmentCategories.get(categoryName).contains(atomicAssignment.getName())){
            compoundAsssignmentCategories.get(categoryName).addAssignment(atomicAssignment);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAssignmentFromCompoundCategory(String categoryName, String assignmentName){
        if (compoundAsssignmentCategories.containsKey(categoryName) &&
                ! compoundAsssignmentCategories.get(categoryName).contains(assignmentName)){
            compoundAsssignmentCategories.get(categoryName).removeAssignment(assignmentName);
            return true;
        } else {
            return false;
        }
    }

    public boolean setAssignmentScore(String assignmentName, double score) {
        if (atomicAsssignmentCategories.containsKey(assignmentName)) {
            SampleAtomicAssignment assignment = atomicAsssignmentCategories.get(assignmentName);
            return assignment.setScore(assignmentName, score);
        } else {
            for (SampleCompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.contains(assignmentName)){
                    Assignment assignment = assignmentCategory.getAssignment(assignmentName);
                    return assignment.setScore(assignmentName, score);
                }
            }
            return false;
        }
    }

    public boolean setAssignmentPointsPossible(String assignmentName, double pointsPossible){
        if (atomicAsssignmentCategories.containsKey(assignmentName)) {
            SampleAtomicAssignment assignment = atomicAsssignmentCategories.get(assignmentName);
            assignment.setPointsPossible(pointsPossible);
            return true;
        } else {
            for (SampleCompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.contains(assignmentName)){
                    Assignment assignment = assignmentCategory.getAssignment(assignmentName);
                    assignment.setPointsPossible(assignmentName, pointsPossible);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean existsAssignmentInCompoundCategories(String assignmentName){
        for (Assignment assignmentCategory : compoundAsssignmentCategories.values()) {
            if (assignmentCategory.contains(assignmentName)){
                return true;
            }
        }
        return false;
    }
}