package GradeTracker.AssignmentClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertsk2 on 3/4/17.
 * @author Kilian Roberts
 */
public class CompoundAssignment implements Assignment {
    private String name;
    private boolean completed;
    private int[] gradingScale;

    private Map<String, AtomicAssignment> atomicSubAssignmentMap;
    private Map<String, CompoundAssignment> compoundSubAssignmentMap;

    /**
     * @param name A string representing this assignment's name
     * @param newGradingScale an integer array containing the cutoff points for the grades A+ through D-
     */
    public CompoundAssignment(String name, int[] newGradingScale){
        this.completed = false;
        this.name = name;
        this.gradingScale = newGradingScale;//CAN WE TEST TO VERIFY THIS IS CORRECTLY FORMATED
        this.compoundSubAssignmentMap = new HashMap<String, CompoundAssignment>();
        this.atomicSubAssignmentMap = new HashMap<String, AtomicAssignment>();
    }

    @Override
    public CompoundAssignment clone(){
        CompoundAssignment clone = new CompoundAssignment(this.name, this.gradingScale);
        if (this.completed()){
            clone.markAsCompleted();
        } else {clone.markAsIncomplete();}
        for (AtomicAssignment assignment : atomicSubAssignmentMap.values()) {
            clone.addAtomicAssignment(this.getName(), assignment.clone());
        }
        for (CompoundAssignment assignment : compoundSubAssignmentMap.values()) {
            clone.addCompoundAssignment(this.getName(), assignment.clone());
        }

        return clone;
    }

    /**
     * Sets the name of this assignment to newName
     * @param newName the new name of the assignment
     */
    public void setName(String newName){
        this.name = newName;
    }

    /**
     * @return the name of this assignment
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return a dictionary containing all of the compound subassignments associated with this compound assignment
     */
    public Map<String, CompoundAssignment> getCompoundSubAssignmentMap(){
        return this.compoundSubAssignmentMap;
    }

    /**
     * @return a dictionary containing all of the atomic subassignments associated with this compound assignment
     */
    public Map<String, AtomicAssignment> getAtomicSubAssignmentMap(){
        return this.atomicSubAssignmentMap;
    }


    /**
     * @return the grade associated with this assignment, as determined by the grading scale associated with this assignment.
     */
    public String getGrade(){ // We could make this log(n), but I don't feel like it at the moment.
        double percentageScore = this.getPercentageScore();
        String[] grades = new String[] {"A+", "A", "A-",
                                        "B+", "B", "B-",
                                        "C+", "C", "C-",
                                        "D+", "D", "D-",};
        for (int i = 0; i < grades.length; i++) {
            if(percentageScore >= this.gradingScale[i]){
                return grades[i];
            }
        }
        return "F";
    }

    /**
     * @return the sum of the values of the sub-assignments of this compound assignment
     */
    public double getPointsPossible(){
        double pointsPossibleSum = 0;
        for (AtomicAssignment subAssignment : atomicSubAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsPossibleSum += subAssignment.getPointsPossible();
            }
        }
        for (CompoundAssignment subAssignment : compoundSubAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsPossibleSum += subAssignment.getPointsPossible();
            }
        }
        return pointsPossibleSum;
    }

    /**
     * @return the sum of the scores of the sub-assignments of this compound assignment
     */
    public double getPointsScore(){
        double pointsAchievedSum = 0;
        for (AtomicAssignment subAssignment : atomicSubAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsAchievedSum += subAssignment.getPointsScore();
            }
        }
        for (CompoundAssignment subAssignment : compoundSubAssignmentMap.values()) {
            if(subAssignment.completed()){
                pointsAchievedSum += subAssignment.getPointsScore();
            }
        }
        return pointsAchievedSum;
    }

    /**
     * @return the score that the user has achieved on this assignment so far.
     */
    public double getPercentageScore() {
        double pointsPossibleSum = 0;
        double pointsAchievedSum = 0;
        for (AtomicAssignment subAssignment : atomicSubAssignmentMap.values()) {
            if (subAssignment.completed()) {
                pointsPossibleSum += subAssignment.getPointsPossible();
                pointsAchievedSum += subAssignment.getPointsScore();
            }
        }
        for (CompoundAssignment subAssignment : compoundSubAssignmentMap.values()) {
            if (subAssignment.completed()) {
                pointsPossibleSum += subAssignment.getPointsPossible();
                pointsAchievedSum += subAssignment.getPointsScore();
            }
        }
        if (pointsPossibleSum == 0) {
            return Double.NaN;
        } else {
            return pointsAchievedSum / pointsPossibleSum;
        }
    }

    /**
     * Adds an atomic assignment as a subAssignment, if compoundToAddTo == this.name
     * @param compoundToAddTo The compound assignment that the calling program would like to add the atomic assignment to.
     * @param atomicAssignment An atomic assignment
     * @return a boolean denoting whether this add was a success
     */
    public boolean addAtomicAssignment(String compoundToAddTo, AtomicAssignment atomicAssignment){
        if (!this.containsAtomic(atomicAssignment.getName())) {
            if (this.getName().equalsIgnoreCase(compoundToAddTo)){
                this.atomicSubAssignmentMap.put(atomicAssignment.getName(), atomicAssignment);
                int i = 0;
                return true;
            } else {
                for (CompoundAssignment compoundAssignment: this.compoundSubAssignmentMap.values()){
                    if (compoundAssignment.addAtomicAssignment(compoundToAddTo, atomicAssignment)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds a compound assignment as a subAssignment, if compoundToAddTo == this.name
     * @param compoundToAddTo The compound assignment that the calling program would like to add the compound assignment to.
     * @param compoundAssignment An atomic assignment
     * @return a boolean denoting whether this add was a success
     */
    public boolean addCompoundAssignment(String compoundToAddTo, CompoundAssignment compoundAssignment){
        if (!this.containsAtomic(compoundAssignment.getName())) {
            if (this.getName().equalsIgnoreCase(compoundToAddTo)){
                this.compoundSubAssignmentMap.put(compoundAssignment.getName(), compoundAssignment);
                return true;
            } else {
                for (CompoundAssignment compoundSubAssignment: this.compoundSubAssignmentMap.values()){
                    if (compoundSubAssignment.addCompoundAssignment(compoundToAddTo, compoundAssignment)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets a subAssignment from the compoundassignment
     * @param targetName
     * @return the assignment targetName, or null if that assignment is not part of this compound assignment
     */
    public Assignment getAssignment(String targetName){
        if (this.atomicSubAssignmentMap.containsKey(targetName)) {
            return this.atomicSubAssignmentMap.get(targetName);
        } else {
            for (CompoundAssignment subAssignment : getCompoundSubAssignmentMap().values()) {
                if (subAssignment.getName().equalsIgnoreCase(targetName)){
                    return subAssignment;
                } else if (subAssignment.containsAtomic(targetName)){
                    return subAssignment.getAssignment(targetName);
                }
            }
        }
        return null;
    }

    /**
     * Sets the score of a subassignment of this compound assignment
     * @param assignmentName a string representing the name of the assignment whose score we want to set
     * @param score a double representing the score
     * @return a boolean denoting whether the operation was a success
     */
    public boolean setScore(String assignmentName, double score) {
        if (this.atomicSubAssignmentMap.containsKey(assignmentName)) {
            this.atomicSubAssignmentMap.get(assignmentName).setPointsScore(score);
            return true;
        } else {
            for (CompoundAssignment subAssignment : getCompoundSubAssignmentMap().values()) {
                if (subAssignment.containsAtomic(assignmentName)){
                    return subAssignment.setScore(assignmentName, score);
                }
            }
        }
        return false;
    }

    /**
     * Sets the value of a subassignment of this compound assignment
     * @param assignmentName a string representing the name of the assignment whose value we want to set
     * @param value a double representing the value
     * @return a boolean denoting whether the operation was a success
     */
    public boolean setPointsPossible(String assignmentName, double value) {
        if (this.atomicSubAssignmentMap.containsKey(assignmentName)) {
            this.atomicSubAssignmentMap.get(assignmentName).setPointsPossible(value);
            return true;
        } else {
            for (CompoundAssignment subAssignment : getCompoundSubAssignmentMap().values()) {
                if (subAssignment.containsAtomic(assignmentName)){
                    return subAssignment.setPointsPossible(assignmentName, value);
                }
            }
        }
        return false;
    }

    /**
     * @param targetName the name of the sub-assignment we would like to remove.
     * @return a boolean denoting whether the removal was successful
     */
    public boolean removeAssignment(String targetName){
        if (this.atomicSubAssignmentMap.containsKey(targetName)){
            this.atomicSubAssignmentMap.remove(targetName);
            return true;
        } else {
            for (CompoundAssignment compoundAssignment: this.compoundSubAssignmentMap.values()) {
                if (compoundAssignment.containsAtomic(targetName)){
                    removeAssignment(targetName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return true if some subassignment as been completed and false otherwise.
     */
    public boolean completed(){
        this.completed = false;
        for (AtomicAssignment assignment: atomicSubAssignmentMap.values()) {
            if (assignment.completed()){
                this.completed = true;
                return true;
            }
        }
        for (CompoundAssignment assignment: compoundSubAssignmentMap.values()) {
            if (assignment.completed()){
                this.completed = true;
                return true;
            }
        }
        return false;
    }

    /**
     * @param assignmentName The string of an assignment name
     * @return true if this compound assignment has the assignment with name assignmentName as a sub-assignment, and false otherwise.
     */
    public boolean contains(String assignmentName){
        return (this.containsCompound(assignmentName)||this.containsAtomic(assignmentName));
    }

    /**
     *
     * @param assignmentName The string of an assignment name
     * @return true if this compound assignment has the a compound assignment with name assignmentName as a sub-assignment, and false otherwise.
     */
    public boolean containsCompound(String assignmentName) {
        if (this.getName().equalsIgnoreCase(assignmentName)){
            return true;
        }
        for (CompoundAssignment subAssignment: compoundSubAssignmentMap.values()) {
            if(subAssignment.containsCompound(assignmentName)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param assignmentName The string of an assignment name
     * @return true if this compound assignment has the an atomic assignment with name assignmentName as a sub-assignment, and false otherwise.
     */
    public boolean containsAtomic(String assignmentName){
        for (AtomicAssignment subAssignment: atomicSubAssignmentMap.values()) {
            if(subAssignment.getName().equalsIgnoreCase(assignmentName)){
                return true;
            }
        }
        for (CompoundAssignment subAssignment: compoundSubAssignmentMap.values()) {
            if(subAssignment.containsAtomic(assignmentName)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return an integer array representing the cutoff points for the grades A+ through D-
     */
    public int[] getGradingScale(){
        return this.gradingScale;
    }

    /**
     * @param newGScale an integer array representing the cutoff points for the grades A+ through D-
     */
    public void setGradingScale(int[] newGScale){
        this.gradingScale = newGScale;
    }

    /**
     * Marks this assignment as completed. Incomplete assignments will be ignored when calculating grades.
     */
    public void markAsCompleted(){
        this.completed = true;
    }

    /**
     * Marks this assignment as completed. Incomplete assignments will be ignored when calculating grades.
     */
    public void markAsIncomplete(){
        this.completed = false;
    }
}
