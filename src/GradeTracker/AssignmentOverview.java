package GradeTracker;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

//QUESTION: Should we change the other classes to be scene generators?

/**
 * Created by robertsk2 on 3/4/17.
 */
public class AssignmentOverview {
    public static Scene generateAtomicAssignmentOverview(){
        BorderPane borderPane = new BorderPane();
        return new Scene(borderPane, 300, 400);
    }
}
