package GradeTracker.AssignmentClasses;

/**
 * Created by robertsk2 on 3/4/17.
 */
public class AtomicAssignment implements Assignment, Cloneable {
    private String name;
    private double pointsPossible;
    private double pointsScore;
    private boolean completed;

    public AtomicAssignment(String assignmentName){
        completed = false;
        this.name = assignmentName;
    }

    @Override
    public AtomicAssignment clone(){
        AtomicAssignment clone = new AtomicAssignment(this.name);
        clone.setScore(this.name, this.pointsScore);
        clone.setPointsPossible(this.pointsPossible);
        if(this.completed){
            clone.completed();
        } else {clone.markAsIncomplete();}
        return clone;
    }

    /**
     *
     * @param assignmentName The name of an assignment
     * @return True if assignmentName is equal to this.name, and false otherwise.
     */
    public boolean containsAtomic(String assignmentName){
        return assignmentName.equalsIgnoreCase(this.name);
    }

    /**
     * If this assignment is assignmentName, its score is set to score
     * @param assignmentName a string
     * @param score a double
     * @return True if assignmentName is equal to this.name, and false otherwise.
     */
    public boolean setScore(String assignmentName, double score){
        if(assignmentName.equalsIgnoreCase(this.name)){
            this.setPointsScore(score);
            return true;
        }
        return false;
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
     * Returns this assignment object if targetName is the name of this assignment
     * @param targetName a name which may be equal to the name of this assignment
     * @return this assignment or null
     */
    public Assignment getAssignment(String targetName){
        if (targetName.equalsIgnoreCase(this.name)){
            return this;
        } else {
            return null;
        }
    }

    /**
     * If this assignment's name is targetName, sets the value of this assignment to newPoints Possible
     * @param targetName The name of an assignment that a courses object wants changed
     * @param newPointsPossible a double representing the number of points possible
     * @return true if targetName == this.name, false otherwise
     */
    public boolean setPointsPossible(String targetName, double newPointsPossible){
        if (this.name.equalsIgnoreCase(targetName)){
            this.setPointsPossible(newPointsPossible);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets this.pointsPossible to newPointsPossible
     * @param newPointsPossible a double representing the number of points this assignment is worth
     */
    public void setPointsPossible(double newPointsPossible){
        this.pointsPossible = newPointsPossible;
    }
    public double getPointsPossible(){
        return this.pointsPossible;
    }

    /**
     * Sets this assignments score to newPointsScore
     * @param newPointsScore a double that represents the score the user received on this assignment
     */
    public void setPointsScore(double newPointsScore){
        this.pointsScore = newPointsScore;
        this.completed = true;
    }

    /**
     * @return the current recorded score that the user got on the assignment
     */
    public double getPointsScore(){
        return this.pointsScore;
    }

    public double getPercentageScore(){
        return this.getPointsScore()/this.getPointsPossible();
    }


    /**
     * Marks this assignment as completed. Incomplete assignments are ignored when calculating grades.
     */
    public void markAsCompleted(){
        this.completed = true;
    }

    /**
     * Marks this assignment as completed. Incomplete assignments are ignored when calculating grades.
     */
    public void markAsIncomplete(){
        this.completed = false;
    }

    /**
     * @return true if this assignment has been completed, and false otherwise
     */
    public boolean completed(){
        return this.completed;
    }
}
