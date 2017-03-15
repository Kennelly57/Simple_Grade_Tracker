package GradeTracker.ModelComponents;

import GradeTracker.AssignmentClasses.Assignment;
import GradeTracker.AssignmentClasses.AtomicAssignment;
import GradeTracker.AssignmentClasses.CompoundAssignment;

import java.util.*;

/**
 * This object stores all information on a single course
 * @author Kilian Roberts
 */
public class ModelCourse implements Cloneable {

    private String id;
    private String name;
    private int[] gradingScale;

    private String[] grades;
    private Map<String, AtomicAssignment> atomicAsssignmentCategories;
    private Map<String, CompoundAssignment> compoundAsssignmentCategories;

    /*
    Assignment categories are stored separately so that each assignment doesn't need to know
    whether it is a category in and of itself or if it is contained within a compound assignment
     */
    private Map<String, Integer> assignmentCategoryWeights;
    private int totalWeight;

    private Integer maxAssignmentCategoryNumber;

    /**
     * Creates a model course object
     * @param courseId
     * @param courseName
     * @param gScale An integer array representing the cutoff points of the grades A+ through D-
     */
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
    // The model creates copies of modelCourse objects to give to observers to prevent them from changing the model's data
    public ModelCourse clone(){
        int weight;
        ModelCourse modelClone = new ModelCourse(this.id, this.name, this.gradingScale);

        for (CompoundAssignment compoundAssignmentCategory : compoundAsssignmentCategories.values()) {
            CompoundAssignment compoundAssignmentClone = compoundAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(compoundAssignmentClone.getName());
            modelClone.addCompoundAssignmentCategory(compoundAssignmentClone, weight);
        }
        for (AtomicAssignment atomicAssignmentCategory : atomicAsssignmentCategories.values()) {
            AtomicAssignment atomicAssignmentClone = atomicAssignmentCategory.clone();
            weight = assignmentCategoryWeights.get(atomicAssignmentClone.getName());
            modelClone.addAtomicAssignmentCategory(atomicAssignmentClone, weight);
        }

        return modelClone;
    }

    /**
     * @return this course's ID
     */
    public String getID() {
        return this.id;
    }

    /**
     * Changes this course's ID
     * @param newID
     */
    public void setId(String newID) { this.id = newID; }


    /**
     * @return this course's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes this course's name
     * @param newName
     */
    public void setName(String newName) { this.name = newName; }

    /**
     * @return this course's grading scale
     */
    public int[] getGradingScale() {
        return gradingScale;
    }

    /**
     * Changes this course's grading scale
     * @param newGradingScale
     */
    public void setGradingScale(int[] newGradingScale){
        this.gradingScale = newGradingScale;
    }

    /**
     * @return the dictionary containing this course's atomic assignment categories
     */
    public Map<String, AtomicAssignment> getAtomicAssignmentCategories(){
        return atomicAsssignmentCategories;
    }

    /**
     * @return the dictionary containing this course's atomic compound categories
     */
    public Map<String, CompoundAssignment> getCompoundAssignmentCategories(){
        return compoundAsssignmentCategories;
    }

    /**
     * @return the dictionary containing this course's category weights
     */
    public Map<String, Integer> getCategoryWeights(){
        return this.assignmentCategoryWeights;
    }

    /**
     * @return the user's current grade in this course
     */
    public String getGrade() {
        double currentPerecentage = 0;
        double weightSum = 0;
        int weight = 0;
        double percentageScore;
        double weightedScore;

        for (String assignment: this.assignmentCategoryWeights.keySet()) {
            if ((this.atomicAsssignmentCategories.containsKey(assignment) && this.atomicAsssignmentCategories.get(assignment).completed()) ||
                    (this.compoundAsssignmentCategories.containsKey(assignment) && this.compoundAsssignmentCategories.get(assignment).completed())) {
                weight = this.assignmentCategoryWeights.get(assignment);
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


    /**
     * Adds an atomic assignment category with the given weight and name.
     * Repeated names are not allowed
     * @param assignmentCategoryName
     * @param weight
     * @return a boolean that describes whether the assignment was successfully added
     */
    public boolean addAtomicAssignmentCategory(String assignmentCategoryName, Integer weight) {
        return addAtomicAssignmentCategory(new AtomicAssignment(assignmentCategoryName), weight);
    }

    private boolean addAtomicAssignmentCategory(AtomicAssignment atomicAssignment, Integer weight) {
        if (!this.containsAtomic(atomicAssignment.getName())) {
            totalWeight = totalWeight + weight;
            atomicAsssignmentCategories.put(atomicAssignment.getName(), atomicAssignment);
            assignmentCategoryWeights.put(atomicAssignment.getName(), weight);
            return true;
        }
        return false;
    }

    /**
     * Adds a compound assignment category with the given weight and name.
     * Repeated names are not allowed
     * @param assignmentCategoryName
     * @param weight
     * @return a boolean that describes whether the assignment was successfully added
     */
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

    private void addCompoundAssignmentCategory(CompoundAssignment compoundAssignment, int weight){
        String assignmentCategoryName = compoundAssignment.getName();
        totalWeight = totalWeight + weight;
        compoundAsssignmentCategories.put(assignmentCategoryName, compoundAssignment);
        assignmentCategoryWeights.put(assignmentCategoryName, weight);
    }

    /**
     * Sets the weight of the given assignment category to the given value
     * @param assignmentCategoryName
     * @param weight
     * @return a boolean describing whether the weight was successfully set
     */
    public boolean setAssignmentCategoryWeight(String assignmentCategoryName, int weight){
        if (assignmentCategoryWeights.containsKey(assignmentCategoryName)){
            assignmentCategoryWeights.put(assignmentCategoryName, weight);
            computeTotalWeight();
            return true;
        }
        return false;
    }

    /**
     * Removes a given assignment category
     * @param assignmentCategoryName
     * @return a boolean describing whether the category was successfully removed
     */
    public boolean removeAssignmentCategory(String assignmentCategoryName) {
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

    /**
     * Adds a new atomic assignment with the given name to the given compound category
     * @param categoryName
     * @param assignmentName
     * @return a boolean describing whether the assignment was successfully added
     */
    public boolean addAtomicAssignmentToCompoundCategory(String categoryName, String assignmentName){
        return addAtomicAssignmentToCompoundCategory(categoryName, new AtomicAssignment(assignmentName));
    }

    private boolean addAtomicAssignmentToCompoundCategory(String categoryName, AtomicAssignment atomicAssignment){
        for (CompoundAssignment compCat: this.compoundAsssignmentCategories.values()) {
            if (compCat.containsCompound(categoryName) &&
                    !compoundAsssignmentCategories.get(categoryName).contains(atomicAssignment.getName())) {
                compoundAsssignmentCategories.get(categoryName).addAtomicAssignment(categoryName, atomicAssignment);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a compound assignment to the given compound category
     * @param categoryName
     * @param compoundAssignment
     * @return a boolean describing whether the assignment was successfully added
     */
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

    /**
     * Removes an assignment from a compound category
     * @param categoryName
     * @param assignmentName
     * @return a boolean describing whether the assignment was successfully removed
     */
    public boolean removeAssignmentFromCompoundCategory(String categoryName, String assignmentName){
        if (compoundAsssignmentCategories.containsKey(categoryName) &&
                compoundAsssignmentCategories.get(categoryName).contains(assignmentName)){
            compoundAsssignmentCategories.get(categoryName).removeAssignment(assignmentName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the score of the given assignment to the given value
     * @param assignmentName
     * @param score
     * @return a boolean describing whether the score was successfully set
     */
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

    /**
     * Sets the value of the given assignment to the given number
     * @param atomicAssignmentName
     * @param pointsPossible
     * @return a boolean describing whether the value was successfully set
     */
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

    /**
     * Marks an atomic assignment as having been completed. Incomplete assignments are ignored when calculating grades.
     * @param atomicAssignmentName
     * @return a boolean describing whether the assignment was successfully marked as completed
     */
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

    /**
     * Marks an atomic assignment as not having been completed. Incomplete assignments are ignored when calculating grades.
     * @param atomicAssignmentName
     * @return a boolean describing whether the assignment was successfully marked as completed
     */
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

    /**
     * @param assignmentName
     * @return whether or not the course contains any assignment with the given name
     */
    public boolean contains(String assignmentName){
        return (this.containsCompound(assignmentName)||this.containsAtomic(assignmentName));
    }

    /**
     * @param assignmentName
     * @return whether or not the course contains an atomic assignment with the given name
     */
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

    /**
     * @param assignmentName
     * @return whether or not the course contains a compound assignment with the given name
     */
    public boolean containsCompound(String assignmentName){
        for (CompoundAssignment assignmentCategory : compoundAsssignmentCategories.values()) {
            if (assignmentCategory.containsCompound(assignmentName)){
                return true;
            }
        }
        return false;
    }

    // Computes the total weight of all the assignment categories
    private void computeTotalWeight(){
        int runningTotal = 0;
        for (Integer weight : assignmentCategoryWeights.values()) {
            runningTotal += weight;
        }
        this.totalWeight = runningTotal;
    }
}