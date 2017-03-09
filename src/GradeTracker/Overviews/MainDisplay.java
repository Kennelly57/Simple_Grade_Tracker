package GradeTracker.Overviews;

import GradeTracker.Assignment;
import GradeTracker.GTModel;
import GradeTracker.GTObserver;
import GradeTracker.ModelCourse;
import GradeTracker.Panes.CoursesOverviewPane;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCourse;
import GradeTracker.Samples.SampleTerm;
import GradeTracker.Panes.CategoriesOverviewPane;
import GradeTracker.Setups.AssignmentSetupWindow;
import GradeTracker.Setups.CourseSetupWindow;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by michelsd on 3/8/17.
 */
public class MainDisplay extends Application implements GTObserver {
    public static Stage univPrimaryStage;
    private double numberOfCourses = 3;
    private GTModel model;
    private Map<String, ModelCourse> latestCourses;
    private boolean upToDate;

    @Override
    public void start(Stage primaryStage) {
        univPrimaryStage = primaryStage;
        model = new GTModel();
        model.registerObserver(this);
        this.updateCourses();

        showCourses();
    }



    private void formatCoursesGridPane(GridPane dataPane, double numberOfCourses) {
        // TODO Refactor this into Courses Pane
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n : dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 3) {
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
        oneThird.setPercentWidth(100 / 3.0);
        oneThird.setHalignment(HPos.CENTER);
        for (double i = 0.0; i < 3.0; i++){
            dataPane.getColumnConstraints().addAll(oneThird);
        }
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100 / numberOfCourses);
        oneHalf.setValignment(VPos.CENTER);
        for (double i = 0.0; i <= numberOfCourses; i++){
            dataPane.getRowConstraints().addAll(oneHalf);
        }
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
        this.updateCourses();

        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        String title = "Courses for Winter 2017";
        Text setupTitle = new Text(title);
        GridPane dataPane = new CoursesOverviewPane(myTermList, this).getRoot();
        formatCoursesGridPane(dataPane, numberOfCourses);
        HBox controlBtns = createCoursesBtnPane();

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle, Pos.CENTER);

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
        this.updateCourses();

        List<Assignment> myAssignments = new LinkedList<Assignment>();
        for (Assignment assignmentCat: latestCourses.get("TEST 101").getAtomicAssignmentCategories().values()) {
            myAssignments.add(assignmentCat);
        }
        System.out.println("List Complete");

        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        //List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Categories");
        GridPane dataPane = new CategoriesOverviewPane(myAssignments, this).getRoot();
//        formatAssignmentGridPane(dataPane);

        Button btnBack = new Button();
        btnBack.setText("<--");
        btnBack.setOnAction((ActionEvent) -> {
            this.showCourses();
        });

        HBox controlBtns = createAssBtnPane(btnBack);

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle, Pos.CENTER);

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

    public void printDiagnostic(){
        System.out.println("Diagnostic");
        System.out.flush();
    }

    public void showAssignments() {
        this.updateCourses();

        // Temp, demo term
        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();
        List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Assignments");
        GridPane dataPane = new CategoriesOverviewPane(myAssignments).getRoot();
//        formatAssignmentGridPane(dataPane);

        Button btnBack = new Button();
        btnBack.setText("<--");
        btnBack.setOnAction((ActionEvent) -> {
            this.showCategories();
        });


        HBox controlBtns = createAssBtnPane(btnBack);

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle, Pos.CENTER);

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

        String courseID = "TEST 101";
        System.out.println("ASSEMBLED COURSE");
        int[] gScale = {96, 93, 90, 86, 83, 80, 76, 66, 63, 60};

        this.model.addCourse(courseID, "Test Course", gScale);

        SampleAtomicAssignment midtermExams = new SampleAtomicAssignment("Midterm Exams");

        this.model.addAtomicAssignmentCategory(courseID, "Midterm Exams", 42);
        this.model.setAssignmentPointsPossible(courseID, "Midterm Exams", 300);
        this.model.setAssignmentScore(courseID, "Midterm Exams", 210);

        System.out.println("ASSEMBLED Assignment");

        midtermExams.setPointsPossible(300);
        midtermExams.setPercentageScore(.695);
        midtermExams.setWeight(.4286);
        midtermExams.calculateWeightedScore();

        SampleAtomicAssignment problemSets = new SampleAtomicAssignment("Problem Sets");

        this.model.addAtomicAssignmentCategory(courseID, "Problem Sets", 22);
        this.model.setAssignmentPointsPossible(courseID, "Problem Sets", 160);
        this.model.setAssignmentScore(courseID, "Problem Sets", 125);

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

        this.model.addAtomicAssignmentCategory(courseID, "Article Discussion", 31);
        this.model.setAssignmentPointsPossible(courseID, "Article Discussion", 50);
        this.model.setAssignmentScore(courseID, "Article Discussion", 50);


        SampleAtomicAssignment participation = new SampleAtomicAssignment("Participation");
        participation.setPointsPossible(50);
        participation.setPointsScore(50);
        participation.calculatePercentageScore();
        participation.setWeight(.714);
        participation.calculateWeightedScore();

        this.model.addAtomicAssignmentCategory(courseID, "Participation", 25);
        this.model.setAssignmentPointsPossible(courseID, "Participation", 40);
        this.model.setAssignmentScore(courseID, "Participation", 40);


        SampleAtomicAssignment finalExam = new SampleAtomicAssignment("Final Exam");
        finalExam.setPointsPossible(40);
        finalExam.setPointsScore(40);
        finalExam.calculatePercentageScore();
        finalExam.setWeight(.571);
        finalExam.calculateWeightedScore();

        this.model.addAtomicAssignmentCategory(courseID, "Final Exam", 22);
        this.model.setAssignmentPointsPossible(courseID, "Final Exam", 40);
        this.model.setAssignmentScore(courseID, "Final Exam", 40);

        this.updateCourses();
        System.out.println("ASSIGNMENTS ENTERED AND UPDATED");

        System.out.println(latestCourses.get(courseID).getName());


        for (Assignment assignmentCat: latestCourses.get(courseID).getAtomicAssignmentCategories().values()) {
            System.out.println(assignmentCat.getName());
        }


        myAssignments.add(midtermExams);
        myAssignments.add(problemSets);
        myAssignments.add(articleDiscussion);
        myAssignments.add(participation);
        myAssignments.add(finalExam);
        return myAssignments;
    }

    public void setNumberOfCourses(){
        numberOfCourses = numberOfCourses + 1.0;
    }

    public void notifyOfChange() {
        this.upToDate = false;
        System.out.println("Notified of change");
    }

    private void updateCourses(){
        if (! this.upToDate) {
            this.latestCourses = this.model.getLatestCourses();
            this.upToDate = true;
            System.out.println("Updated Courses");
        }
    }
}
