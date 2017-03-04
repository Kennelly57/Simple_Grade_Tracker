package GradeTracker;

import javafx.application.Application;
import javafx.collections.FXCollections;
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

import java.util.*;
import java.util.List;
import java.util.Observable;

public class CoursesOverviewDisplay_FirstTry extends Application {
    @Override
    public void start(Stage primaryStage){
        start(primaryStage, "WI2017");
    }

    public void start(Stage primaryStage, String termName) {

        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();

        BorderPane overviewBorderPane = new BorderPane();
        overviewBorderPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Your courses for ".concat(termName).concat(":"));
        overviewBorderPane.setTop(setupTitle);
        overviewBorderPane.setAlignment(setupTitle, Pos.CENTER);

        GridPane dataPane = generateOverviewPane(myTermList);
        overviewBorderPane.setCenter(dataPane);
        overviewBorderPane.setAlignment(dataPane, Pos.CENTER_LEFT);

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


    public GridPane generateOverviewPane(List<SampleCourse> myTermList){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label courseIdLabel = new Label("Course Id");
        Label courseNameLabel = new Label("Name");
        Label courseGradeLabel = new Label("Grade");

        dataGrid.add(courseIdLabel, 0, 0);
        dataGrid.add(courseNameLabel, 1, 0);
        dataGrid.add(courseGradeLabel, 2, 0);

        // Generate Table of Course Values
        for (int i=0; i<myTermList.size(); i++) {
            Label tempID = new Label(myTermList.get(i).getId());
            dataGrid.add(tempID, 0, i+1);

            Label tempName = new Label(myTermList.get(i).getName());
            dataGrid.add(tempName, 1, i+1);

            Label tempGrade = new Label(myTermList.get(i).getGrade());
            dataGrid.add(tempGrade, 2, i+1);
        }

        return dataGrid;
    }

    public static void main(String[] args) {launch(args);}
}