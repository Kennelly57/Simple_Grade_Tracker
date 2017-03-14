package GradeTracker;

import GradeTracker.Samples.AtomicAssignment;
import GradeTracker.Samples.CompoundAssignment;
import com.sun.javafx.sg.prism.NGShape;

import java.util.*;

/**
 * Created by michelsd on 3/4/17.
 */
public class ModelCourse implements Cloneable {

    private String id;
    private String name;
    private int[] gradingScale;

    private String[] grades;
    private Map<String, AtomicAssignment> atomicAsssignmentCategories;
    private Map<String, CompoundAssignment> compoundAsssignmentCategories;
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

        this.atomicAsssignmentCategories = new TreeMap<String, AtomicAssignment>();
        this.compoundAsssignmentCategories = new TreeMap<String, CompoundAssignment>();
        this.assignmentCategoryWeights = new TreeMap<String, Integer>();
    }

    @Override
    public ModelCourse clone(){
        int weight;
        ModelCourse modelClone = new ModelCourse(this.id, this.name, this.gradingScale);

        for (CompoundAssignment compoundAssignmentCategory : compoundAsssignmentCategories.values()) {
            CompoundAssignment compoundAssignmentClone = compoundAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(compoundAssignmentClone.getName());
            modelClone.addCompoundAssignmentCategory(compoundAssignmentClone, weight); //FIX THIS AFTER ADDING A WAY TO ADD ASSIGNMENTS
        }
        for (AtomicAssignment atomicAssignmentCategory : atomicAsssignmentCategories.values()) {
            AtomicAssignment atomicAssignmentClone = atomicAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(atomicAssignmentClone.getName());
            modelClone.addAtomicAssignmentCategory(atomicAssignmentClone, weight);
        }

        return modelClone;
    }

    public String getID() {
        return this.id;
    }

    public void setId(String newID) { this.id = newID; }

    public boolean weightIsOneHundred(){
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

    public void setName(String newName) { this.name = newName; }

    public int[] getGradingScale() {
        return gradingScale;
    }

    public void setGradingScale(int[] newGradingScale){
        this.gradingScale = newGradingScale;
    }

    public Map<String, AtomicAssignment> getAtomicAssignmentCategories(){
        return atomicAsssignmentCategories;
    }
    public Map<String, CompoundAssignment> getCompoundAssignmentCategories(){
        return compoundAsssignmentCategories;
    }
    public Map<String, Integer> getCategoryWeights(){
        return this.assignmentCategoryWeights;
    }
    public String getGrade() {
        double currentPerecentage = 0;
        double weightSum = 0;
        int weight = 0;
        double percentageScore;
        double weightedScore;

        for (String asngment: this.assignmentCategoryWeights.keySet()) {
            if ((this.atomicAsssignmentCategories.containsKey(asngment) && this.atomicAsssignmentCategories.get(asngment).completed()) ||
                    (this.compoundAsssignmentCategories.containsKey(asngment) && this.compoundAsssignmentCategories.get(asngment).completed())) {
                weight = this.assignmentCategoryWeights.get(asngment);
                weightSum += weight;
            }
        }

        for (AtomicAssignment atomicAssignment : atomicAsssignmentCategories.values()) {
            if (atomicAssignment.completed()) {
                percentageScore = atomicAssignment.getPercentageScore();
                weightedScore = percentageScore * this.assignmentCategoryWeights.get(atomicAssignment.getName()) * 100/weightSum;
                currentPerecentage += weightedScore;
            }
        }

        for (CompoundAssignment compoundAssignment : compoundAsssignmentCategories.values()) {
            if (compoundAssignment.completed()) {
                percentageScore = compoundAssignment.getPercentageScore();
                weightedScore = percentageScore * this.assignmentCategoryWeights.get(compoundAssignment.getName()) * 100 / weightSum;
                currentPerecentage += weightedScore;
            }
        }

        for (int i = 0; i < this.gradingScale.length; i++) {
            if (currentPerecentage > this.gradingScale[i]){
                return this.grades[i];
            }
        }
        if (this.compoundAsssignmentCategories.isEmpty() && this.atomicAsssignmentCategories.isEmpty()){
            return "NO GRADE";
        } else {
            return "F";
        }

    }


    //WE NEED TO AVOID DUPLICATE NAMES
    public boolean addAtomicAssignmentCategory(String assignmentCategoryName, Integer weight) {
        return addAtomicAssignmentCategory(new AtomicAssignment(assignmentCategoryName), weight);
    }

    protected boolean addAtomicAssignmentCategory(AtomicAssignment atomicAssignment, Integer weight) {
        if (!this.containsAtomic(atomicAssignment.getName())) {
            totalWeight = totalWeight + weight;
            atomicAsssignmentCategories.put(atomicAssignment.getName(), atomicAssignment);
            assignmentCategoryWeights.put(atomicAssignment.getName(), weight);
            return true;
        }
        return false;
    }

    public boolean addCompoundAssignmentCategory(String assignmentCategoryName, Integer weight){
        return addCompoundAssignmentCategory(assignmentCategoryName, weight, this.gradingScale);
    }

    public boolean addCompoundAssignmentCategory(String assignmentCategoryName, Integer weight, int[] gScale) {
        if (!this.contains(assignmentCategoryName)) {
            totalWeight = totalWeight + weight;
            compoundAsssignmentCategories.put(assignmentCategoryName, new CompoundAssignment(assignmentCategoryName, gScale));
            assignmentCategoryWeights.put(assignmentCategoryName, weight);
            return true;
        }
        return false;
    }

    protected void addCompoundAssignmentCategory(CompoundAssignment compoundAssignment, int weight){
        String assignmentCategoryName = compoundAssignment.getName();
        totalWeight = totalWeight + weight;
        compoundAsssignmentCategories.put(assignmentCategoryName, compoundAssignment);
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
    }

    public boolean setAssignmentCategoryWeight(String assignmentCategoryName, int weight){
        if (assignmentCategoryWeights.containsKey(assignmentCategoryName)){
            assignmentCategoryWeights.put(assignmentCategoryName, weight);
            computeTotalWeight();
            return true;
        }

        return false;
    }

    public boolean removeAssignmentCategory(String assignmentCategoryName) { //THIS IS BAD THIS IS BAD THIS IS BAD
        if (this.contains(assignmentCategoryName)) {
            int weight = assignmentCategoryWeights.get(assignmentCategoryName);
            assignmentCategoryWeights.remove(assignmentCategoryName);
            atomicAsssignmentCategories.remove(assignmentCategoryName);
            compoundAsssignmentCategories.remove(assignmentCategoryName);
            totalWeight = totalWeight - weight;
            return true;
        }
        return false;
    }

    public boolean addAtomicAssignmentToCompoundCategory(String categoryName, String assignmentName){
        return addAtomicAssignmentToCompoundCategory(categoryName, new AtomicAssignment(assignmentName));
    }

    public boolean addAtomicAssignmentToCompoundCategory(String categoryName, AtomicAssignment atomicAssignment){
        for (CompoundAssignment compCat: this.compoundAsssignmentCategories.values()) {
            if (compCat.containsCompound(categoryName) &&
                    !compoundAsssignmentCategories.get(categoryName).contains(atomicAssignment.getName())) {
                compoundAsssignmentCategories.get(categoryName).addAtomicAssignment(categoryName, atomicAssignment);
                return true;
            }
        }
        return false;
    }

    public boolean addCompoundAssignmentToCompoundCategory(String categoryName, CompoundAssignment compoundAssignment){
        for (CompoundAssignment compCat: this.compoundAsssignmentCategories.values()) {
            if (compCat.containsCompound(categoryName) &&
                    !compoundAsssignmentCategories.get(categoryName).contains(compoundAssignment.getName())) {
                compoundAsssignmentCategories.get(categoryName).addCompoundAssignment(categoryName, compoundAssignment);
                return true;
            }
        }
        return false;
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
            AtomicAssignment assignment = atomicAsssignmentCategories.get(assignmentName);
            return assignment.setScore(assignmentName, score);
        } else {
            for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.containsAtomic(assignmentName)){
                    Assignment assignment = assignmentCategory.getAssignment(assignmentName);
                    return assignment.setScore(assignmentName, score);
                }
            }
            return false;
        }
    }

    public boolean setAtomicAssignmentPointsPossible(String atomicAssignmentName, double pointsPossible){
        if (atomicAsssignmentCategories.containsKey(atomicAssignmentName)) {
            AtomicAssignment assignment = atomicAsssignmentCategories.get(atomicAssignmentName);
            assignment.setPointsPossible(pointsPossible);
            return true;
        } else {
            for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.containsAtomic(atomicAssignmentName)){
                    Assignment assignment = assignmentCategory.getAssignment(atomicAssignmentName);
                    assignment.setPointsPossible(atomicAssignmentName, pointsPossible);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean markAtomicAssignmentComplete(String atomicAssignmentName){
        if (atomicAsssignmentCategories.containsKey(atomicAssignmentName)) {
            AtomicAssignment assignment = atomicAsssignmentCategories.get(atomicAssignmentName);
            assignment.markAsCompleted();
            return true;
        } else {
            for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.containsAtomic(atomicAssignmentName)) {
                    Assignment assignment = assignmentCategory.getAssignment(atomicAssignmentName);
                    assignment.markAsCompleted();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean markAtomicAssignmentIncomplete(String atomicAssignmentName){
        if (atomicAsssignmentCategories.containsKey(atomicAssignmentName)) {
            AtomicAssignment assignment = atomicAsssignmentCategories.get(atomicAssignmentName);
            assignment.markAsIncomplete();
            return true;
        } else {
            for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
                if (assignmentCategory.containsAtomic(atomicAssignmentName)) {
                    Assignment assignment = assignmentCategory.getAssignment(atomicAssignmentName);
                    assignment.markAsIncomplete();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(String assignmentName){
        return (this.containsCompound(assignmentName)||this.containsAtomic(assignmentName));
    }

    public boolean containsAtomic(String assignmentName){
        for (CompoundAssignment compoundCat : compoundAsssignmentCategories.values()) {
            if (compoundCat.containsAtomic(assignmentName)){
                return true;
            }
        }
        String atomiCatName;
        for (AtomicAssignment atomiCat : atomicAsssignmentCategories.values()) {
            atomiCatName = atomiCat.getName();
            if (atomiCatName.equalsIgnoreCase(assignmentName)){
                return true;
            }
        }
        return false;
    }
    public boolean containsCompound(String assignmentName){
        for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
            if (assignmentCategory.containsCompound(assignmentName)){
                return true;
            }
        }
        return false;
    }




}