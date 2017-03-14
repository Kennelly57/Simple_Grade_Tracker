package GradeTracker.Views.PopupStages;


import GradeTracker.GTModel;
import GradeTracker.Views.Panes.PopupPanes.CreateAssignmentPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SubAssignmentSetupWindow extends Application {

    public static Stage stage;
    private GTModel model;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("DO NOT USE THE OVERRIDDEN START METHOD");
    }//Can we use this first and give the model later?

    public void start(Stage primaryStage, GTModel theModel, String currentCourseID){
        this.model = theModel;
        start(primaryStage, "Assignment", theModel, currentCourseID);
    }

    public void start(Stage primaryStage, String setupType, GTModel theModel, String currentCourseID) {
        stage = primaryStage;
        stage.setTitle(setupType.concat(" creation"));
        BorderPane aswBorderPane = new CreateAssignmentPane(setupType, this.model, currentCourseID).getRoot();
        Scene aswScene = new Scene(aswBorderPane, 450, 350);
        stage.setScene(aswScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}