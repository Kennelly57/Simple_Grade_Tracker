package GradeTracker.Overviews;

import GradeTracker.Assignment;
import GradeTracker.Controller.ControllerInterface;
import GradeTracker.Panes.CoursesOverviewPane;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCourse;
import GradeTracker.Samples.SampleTerm;
import GradeTracker.Panes.AssignmentsOverviewPane;
import GradeTracker.Setups.AssignmentSetupWindow;
import GradeTracker.Setups.CourseSetupWindow;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by michelsd on 3/8/17.
 */
public class MainDisplay extends Application {
    private Stage univPrimaryStage;
    @Override
    public void start(Stage primaryStage) {
        univPrimaryStage = primaryStage;
        showCourses();
    }

    private void formatAssignmentGridPane(GridPane dataPane) {
        // TODO refactor this into assignments pane
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n : dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 6) {
                    control.setId("categories");
                    columnCounter++;
                }
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }
        }
        ColumnConstraints oneSixth = new ColumnConstraints();
        oneSixth.setPercentWidth(100/6.0);
        oneSixth.setHalignment(HPos.CENTER);
        dataPane.getColumnConstraints().addAll(oneSixth, oneSixth, oneSixth, oneSixth, oneSixth, oneSixth);
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100/2.0);
        oneHalf.setValignment(VPos.CENTER);
        dataPane.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf);
        BorderPane.setAlignment(dataPane, Pos.CENTER_LEFT);
        String css = this.getClass().getResource("basicStyle.css").toExternalForm();
        dataPane.getStylesheets().add(css);
    }

    private void formatCoursesGridPane(GridPane dataPane) {
        // TODO Refactor this into Courses Pane
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n: dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 3){
                    control.setId("categories");
                    columnCounter++;
                }
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }
        }
        ColumnConstraints oneThird = new ColumnConstraints();
        oneThird.setPercentWidth(100/3.0);
        oneThird.setHalignment(HPos.CENTER);
        dataPane.getColumnConstraints().addAll(oneThird, oneThird, oneThird);
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100/2.0);
        oneHalf.setValignment(VPos.CENTER);
        dataPane.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf);
        String css = this.getClass().getResource("basicStyle.css").toExternalForm();
        dataPane.getStylesheets().add(css);
    }

    public HBox createCoursesBtnPane() {
        // TODO Refactor this into Courses Pane
        HBox btnHbox = new HBox();

        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(univPrimaryStage);
                new CourseSetupWindow().start(dialog);
            }
        });

        btnHbox.getChildren().add(btnAdd);
        btnHbox.setAlignment(Pos.BOTTOM_RIGHT);
        return btnHbox;
    }

    public void showCourses() {

        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Courses for Winter 2017");
        GridPane dataPane = new CoursesOverviewPane(myTermList).getRoot();
        formatCoursesGridPane(dataPane);
        HBox controlBtns = createCoursesBtnPane();

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle,Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(dataPane, Pos.CENTER);

        // Create scene
        Scene scene = new Scene(root, 1020, 730);
        univPrimaryStage.setTitle("Courses");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    public HBox createAssBtnPane(Button btnBack) {
        // TODO refactor this into assignments pane
        HBox btnHbox = new HBox();

        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(univPrimaryStage);
                new AssignmentSetupWindow().start(dialog);
            }
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnHbox.getChildren().addAll(btnBack, spacer, btnAdd);
        //btnHbox.setAlignment(Pos.BOTTOM_RIGHT);
        return btnHbox;
    }

    public void showCategories() {
        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Categories");
        GridPane dataPane = new AssignmentsOverviewPane(myAssignments).getRoot();
        formatAssignmentGridPane(dataPane);

        Button btnBack = new Button();
        btnBack.setText("<--");
        btnBack.setOnAction((ActionEvent) -> {
            this.showCourses();
        });

        HBox controlBtns = createAssBtnPane(btnBack);

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle,Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(dataPane, Pos.CENTER);

        // Create scene
        Scene scene = new Scene(root, 1020, 730);
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }


    public void showAssignments() {
        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Assignments");
        GridPane dataPane = new AssignmentsOverviewPane(myAssignments).getRoot();
        formatAssignmentGridPane(dataPane);

        Button btnBack = new Button();
        btnBack.setText("<--");
        btnBack.setOnAction((ActionEvent) -> {
            this.showCategories();
        });


        HBox controlBtns = createAssBtnPane(btnBack);

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle,Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(dataPane, Pos.CENTER);

        // Create scene
        Scene scene = new Scene(root, 1020, 730);
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    private List<Assignment> makeDemoAssignmentList() {
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
        return myAssignments;
    }
}
