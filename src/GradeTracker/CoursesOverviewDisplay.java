package GradeTracker;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CoursesOverviewDisplay extends Application {
    @Override
    public void start(Stage primaryStage){
        start(primaryStage, "WI2017");
    }

    public void start(Stage primaryStage, String termName) {

        BorderPane overviewBorderPane = new BorderPane();
        overviewBorderPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Your courses for ".concat(termName).concat(":"));
        overviewBorderPane.setTop(setupTitle);
        overviewBorderPane.setAlignment(setupTitle, Pos.CENTER);

        GridPane dataPane = generateAssignmentCreationPane();
        overviewBorderPane.setCenter(dataPane);

        //------------------------------CREATE_ADD_BUTTON-----------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("+");
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding");
            }
        });

        overviewBorderPane.setBottom(btnFinish);
        overviewBorderPane.setAlignment(btnFinish, Pos.BOTTOM_RIGHT);
        //---------------------------------------------------------------------------------------

        Scene overviewScene = new Scene(overviewBorderPane, 500, 350);
        primaryStage.setScene(overviewScene);
        primaryStage.show();
    }

    public GridPane generateAssignmentCreationPane(){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

        TextField courseIdTF = new TextField();
        TextField courseNameTF = new TextField();
        TextField courseGradeTF = new TextField();

        Label courseIdLabel = new Label("Course Id:");
        Label courseNameLabel = new Label("Name:");
        Label courseGradeLabel = new Label("Predicted Grade:");

        dataGrid.add(courseIdLabel, 0, 0);
        dataGrid.add(courseIdTF, 0, 1);
        dataGrid.add(courseNameLabel, 1, 0);
        dataGrid.add(courseNameTF, 1, 1);
        dataGrid.add(courseGradeLabel, 2, 0);
        dataGrid.add(courseGradeTF, 2, 1);

        return dataGrid;
    }

    public static void main(String[] args) {launch(args);}
}