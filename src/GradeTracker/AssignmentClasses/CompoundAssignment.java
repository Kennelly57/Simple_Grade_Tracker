package GradeTracker.AssignmentClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class CompoundAssignment implements Assignment {
    private String name;
    private boolean completed;
    private int[] gradingScale;

    //Question: How do we deal with points based vs percentage based weighting
    private Map<String, AtomicAssignment> atomicSubAssignmentMap;
    private Map<String, CompoundAssignment> compoundSubAssignmentMap;

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

    public boolean contains(String assignmentName){
        return (this.containsCompound(assignmentName)||this.containsAtomic(assignmentName));
    }

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
    public boolean containsAtomic(String assignmentName){

//        if (assignmentName.equalsIgnoreCase("test")){
//            //System.out.println("Finding Test");
//        }

        for (AtomicAssignment subAssignment: atomicSubAssignmentMap.values()) {
            if(subAssignment.getName().equalsIgnoreCase(assignmentName)){

//                if (assignmentName.equalsIgnoreCase("test")){
//                    System.out.println("Finding in Atomic True");
//                }

                return true;
            }
        }
        for (CompoundAssignment subAssignment: compoundSubAssignmentMap.values()) {
            if(subAssignment.containsAtomic(assignmentName)){

//                if (assignmentName.equalsIgnoreCase("test")){
//                    System.out.println("Finding Test In Compound");
//                }

                return true;
            }
        }

//        if (assignmentName.equalsIgnoreCase("test")){
//            System.out.println("Finding Test False");
//        }

        return false;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, CompoundAssignment> getCompoundSubAssignmentMap(){
        return this.compoundSubAssignmentMap;
    }

    public Map<String, AtomicAssignment> getAtomicSubAssignmentMap(){
        return this.atomicSubAssignmentMap;
    }


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
            return 1;
        } else {//THIS IS BAD BEHAVIOR. DO NOT USE IT
            return pointsAchievedSum / pointsPossibleSum;
        }
    }

    public boolean addAtomicAssignment(String compoundToAddTo, AtomicAssignment atomicAssignment){
        if (!this.containsAtomic(atomicAssignment.getName())) {
            if (this.getName().equalsIgnoreCase(compoundToAddTo)){
                this.atomicSubAssignmentMap.put(atomicAssignment.getName(), atomicAssignment);
                int i = 0;
//                for (AtomicAssignment a:this.atomicSubAssignmentMap.values()) {
//                    System.out.print("ASSIGNMENT ");
//                    System.out.print(atomicAssignment.getName());
//                    System.out.print(" IS IN " + Integer.toString(i) + " ");
//                    System.out.println(this.getName());
//                }

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


    public Assignment getAssignment(String targetName){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF A WRONG NAME!
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

    public boolean setPointsPossible(String assignmentName, double score) {
        if (this.atomicSubAssignmentMap.containsKey(assignmentName)) {
            this.atomicSubAssignmentMap.get(assignmentName).setPointsPossible(score);
            return true;
        } else {
            for (CompoundAssignment subAssignment : getCompoundSubAssignmentMap().values()) {
                if (subAssignment.containsAtomic(assignmentName)){
                    return subAssignment.setPointsPossible(assignmentName, score);
                }
            }
        }
        return false;
    }

    public void removeAssignment(String targetName){ //THIS NEEDS TO THROW AN ERROR IN THE CASE OF A WRONG NAME!
        if (this.atomicSubAssignmentMap.containsKey(targetName)){
            this.atomicSubAssignmentMap.remove(targetName);
        } else {
            for (CompoundAssignment compoundAssignment: this.compoundSubAssignmentMap.values()) {
                if (compoundAssignment.containsAtomic(targetName)){
                    removeAssignment(targetName);
                }
            }
        }
    }

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

    public int[] getGradingScale(){
        return this.gradingScale;
    }

    public void setGradingScale(int[] newGScale){
        this.gradingScale = newGScale;
    }


    public void markAsCompleted(){
        this.completed = true;
    }
    public void markAsIncomplete(){
        this.completed = false;
    }
}
