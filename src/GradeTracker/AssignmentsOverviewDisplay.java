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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
import java.util.Observable;

/**
 * Created by michelsd on 3/5/17.
 */
public class AssignmentsOverviewDisplay extends Application {
    private Stage univPrimaryStage;
    @Override
    public void start(Stage primaryStage){

        univPrimaryStage = primaryStage;
        start(primaryStage, "Owl Colors and Patterns");
    }

    public void start(Stage primaryStage, String courseName) {

        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();

        List<Assignment> myAssignments = new ArrayList<Assignment>();

        SampleAtomicAssignment midtermExams = new SampleAtomicAssignment("Midterm Exams");
        midtermExams.setPointsPossible(300);
        midtermExams.setPercentageScore(.695);
        midtermExams.setWeight(.4286);
        midtermExams.calculateWeightedScore();

        SampleAtomicAssignment problemSets = new SampleAtomicAssignment("Problem Sets");
        problemSets.setPointsPossible(160);
        problemSets.setPercentageScore(.818);
        problemSets.setWeight(.2286);
        problemSets.calculateWeightedScore();

        SampleAtomicAssignment articleDiscussion = new SampleAtomicAssignment("Article Discussion");
        articleDiscussion.setPointsPossible(40);
        articleDiscussion.setPointsScore(40);
        articleDiscussion.calculatePercentageScore();
        articleDiscussion.setWeight(.571);
        articleDiscussion.calculateWeightedScore();

        SampleAtomicAssignment participation = new SampleAtomicAssignment("Participation");
        participation.setPointsPossible(50);
        participation.setPointsScore(50);
        participation.calculatePercentageScore();
        participation.setWeight(.714);
        participation.calculateWeightedScore();

        SampleAtomicAssignment finalExam = new SampleAtomicAssignment("Final Exam");
        finalExam.setPointsPossible(40);
        finalExam.setPointsScore(40);
        finalExam.calculatePercentageScore();
        finalExam.setWeight(.571);
        finalExam.calculateWeightedScore();

        myAssignments.add(midtermExams);
        myAssignments.add(problemSets);
        myAssignments.add(articleDiscussion);
        myAssignments.add(participation);
        myAssignments.add(finalExam);

        BorderPane overviewBorderPane = new BorderPane();
        overviewBorderPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Courses --> ".concat(courseName).concat(":"));
        overviewBorderPane.setTop(setupTitle);
        overviewBorderPane.setAlignment(setupTitle, Pos.CENTER);

        GridPane dataPane = generateOverviewPane(myAssignments);
        overviewBorderPane.setCenter(dataPane);
        overviewBorderPane.setAlignment(dataPane, Pos.CENTER_LEFT);

        //------------------------------CREATE_ADD_BUTTON-----------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("+");
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(univPrimaryStage);
                new CourseSetupWindow().start(dialog);
            }
        });

        overviewBorderPane.setBottom(btnFinish);
        overviewBorderPane.setAlignment(btnFinish, Pos.BOTTOM_RIGHT);
        //---------------------------------------------------------------------------------------
        Scene overviewScene = new Scene(overviewBorderPane, 500, 350);
        primaryStage.setScene(overviewScene);
        primaryStage.show();
    }


    public GridPane generateOverviewPane(List<Assignment> myAssignments){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label nameHeader = new Label("Category");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Score (pts)");
        Label scorePercentHeader = new Label("Score (%)");
        Label weightHeader = new Label("Weight");
        Label weightedScore = new Label("Weighted Score");


        dataGrid.add(nameHeader, 0, 0);
        dataGrid.add(pointsPosHeader, 1, 0);
        dataGrid.add(scorePtsHeader, 2, 0);
        dataGrid.add(scorePercentHeader, 3, 0);
        dataGrid.add(weightHeader, 4, 0);
        // Generate Table of Course Values
        for (int i=0; i<myAssignments.size(); i++) {
            Label tempName = new Label(myAssignments.get(i).getName());
            dataGrid.add(tempName, 0, i+1);

            Label tempPointsPos = new Label(Double.toString(myAssignments.get(i).getPointsPossible()));
            dataGrid.add(tempPointsPos, 1, i+1);

            Label tempPointsScore = new Label(Double.toString(myAssignments.get(i).getPointsScore()));
            dataGrid.add(tempPointsScore, 2, i+1);

            Label tempPercentScore = new Label(Double.toString(myAssignments.get(i).getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i+1);

            Label tempWeight = new Label(Double.toString(myAssignments.get(i).getWeight()));
            dataGrid.add(tempWeight, 4, i+1);

            Label tempWeightedScore = new Label(Double.toString(myAssignments.get(i).getWeightedScore()));
            dataGrid.add(tempWeightedScore, 5, i+1);
        }

        return dataGrid;
    }

    public static void main(String[] args) {launch(args);}

}
