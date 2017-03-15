package GradeTracker.Views.PopupStages;


import GradeTracker.GTModel;
import GradeTracker.Views.Panes.PopupPanes.CreateAssignmentPane;
import GradeTracker.Views.Panes.PopupPanes.CreateSubAssignmentPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AssignmentSetupWindow extends Application {

    public static Stage stage;
    private GTModel model;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("DO NOT USE THE OVERRIDDEN START METHOD");
    }//Can we use this first and give the model later?

    public void start(Stage primaryStage, GTModel theModel, String currentCourseID, boolean creatingSubAssignment, String...categoryName){
        System.out.println("here");
        this.model = theModel;
        if (creatingSubAssignment== false) {
            start(primaryStage, "Assignment", theModel, currentCourseID);
        } else if (creatingSubAssignment== true) {
            start(primaryStage, "Sub-assignment", theModel, currentCourseID, categoryName[0]);
        }
    }

    public void start(Stage primaryStage, String setupType, GTModel theModel, String currentCourseID) {
        stage = primaryStage;
        stage.setTitle(setupType.concat(" creation"));
        BorderPane aswBorderPane = new CreateAssignmentPane(setupType, this.model, currentCourseID).getRoot();
        Scene aswScene = new Scene(aswBorderPane, 450, 250);
        stage.setScene(aswScene);
        stage.show();
    }

    public void start(Stage primaryStage, String setupType, GTModel theModel, String currentCourseID, String categoryName) {
        stage = primaryStage;
        stage.setTitle(setupType.concat(" creation"));
        BorderPane aswBorderPane = new CreateSubAssignmentPane(setupType, this.model, currentCourseID, categoryName).getRoot();
        Scene aswScene = new Scene(aswBorderPane, 450, 250);
        stage.setScene(aswScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}