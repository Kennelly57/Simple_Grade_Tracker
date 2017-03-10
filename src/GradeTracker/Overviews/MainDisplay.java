package GradeTracker.Overviews;

import GradeTracker.Assignment;
import GradeTracker.GTModel;
import GradeTracker.GTObserver;
import GradeTracker.ModelCourse;
import GradeTracker.Panes.CoursesOverviewPane;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;
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


import java.util.Map;

/**
 * Created by michelsd on 3/8/17.
 */
public class MainDisplay extends Application implements GTObserver {
    public static Stage univPrimaryStage;
    private GTModel model;
    private Map<String, ModelCourse> latestCourses;
    private boolean upToDate;

    @Override
    public void start(Stage primaryStage) {
        univPrimaryStage = primaryStage;
        model = new GTModel();
        model.registerObserver(this);
        this.updateCourses();
        makeDemoAssignmentList();
        showCourses();
    }

    private void formatCoursesGridPane(GridPane dataPane) {
        int numberOfCourses = getNumberOfCourses();
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
        System.out.print("Showing ");
        this.updateCourses();
        System.out.println("courses");
        System.out.flush();


        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        String title = "Courses for Winter 2017";
        Text setupTitle = new Text(title);
        GridPane dataPane = new CoursesOverviewPane(this.latestCourses, this).getRoot();
        formatCoursesGridPane(dataPane);
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
        return btnHbox;
    }

    public void showCategories(ModelCourse course) {
        this.updateCourses();
        System.out.println("Showing Categories");

        Map<String, SampleAtomicAssignment> atomicAsssignmentCategories = course.getAtomicAssignmentCategories();
        Map<String, SampleCompoundAssignment> compoundAsssignmentCategories = course.getCompoundAssignmentCategories();

//        List<Assignment> myAssignments = new LinkedList<Assignment>();
//        for (Assignment assignmentCat: latestCourses.get("TEST 101").getAtomicAssignmentCategories().values()) {
//            myAssignments.add(assignmentCat);
//        }
//        System.out.println("List Complete");

        //List<Assignment> myAssignments = makeDemoAssignmentList();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text setupTitle = new Text("Categories");
        GridPane dataPane = new CategoriesOverviewPane(atomicAsssignmentCategories, compoundAsssignmentCategories, this).getRoot();
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


//    public void showAssignments() {
//        this.updateCourses();
//
//        // Temp, demo term
//        SampleTerm myTerm = new SampleTerm("WI2017");
//        List<SampleCourse> myTermList = myTerm.getCourses();
//        List<Assignment> myAssignments = makeDemoAssignmentList();
//
//        // Borderpane "root" will hold other panes
//        BorderPane root = new BorderPane();
//
//        // Create instances of subpanes
//        Text setupTitle = new Text("Assignments");
//        GridPane dataPane = new CategoriesOverviewPane(myAssignments).getRoot();
////        formatAssignmentGridPane(dataPane);
//
//        Button btnBack = new Button();
//        btnBack.setText("<--");
//        btnBack.setOnAction((ActionEvent) -> {
//            this.showCategories();
//        });
//
//
//        HBox controlBtns = createAssBtnPane(btnBack);
//
//        // Place subpanes in "root" pane
//        root.setTop(setupTitle);
//        root.setAlignment(setupTitle, Pos.CENTER);
//
//        root.setCenter(dataPane);
//        root.setAlignment(dataPane, Pos.CENTER);
//
//        root.setBottom(controlBtns);
//        root.setAlignment(dataPane, Pos.CENTER);
//
//        // Create scene
//        Scene scene = new Scene(root, 1020, 730);
//        univPrimaryStage.setTitle("Courses for Winter 2017");
//        univPrimaryStage.setScene(scene);
//        univPrimaryStage.show();
//    }

    private void makeDemoAssignmentList() {

        int[] gScale = {96, 93, 90, 86, 83, 80, 76, 66, 63, 60};

        String courseID_1 = "TEST 101";
        String courseID_2 = "BIOL.362";
        String courseID_3 = "CS.111";

        this.model.addCourse(courseID_1, "Test Course", gScale);
        this.model.addCourse(courseID_2, "Ows Patterns & Colors", gScale);
        this.model.addCourse(courseID_3, "Intro CS", gScale);

        SampleAtomicAssignment midtermExams = new SampleAtomicAssignment("Midterm Exams");

        // demo stuff for test 101
        this.model.addAtomicAssignmentCategory(courseID_1, "Midterm Exams", 42);
        this.model.setAssignmentPointsPossible(courseID_1, "Midterm Exams", 300);
        this.model.setAssignmentScore(courseID_1, "Midterm Exams", 210);

        this.model.addAtomicAssignmentCategory(courseID_1, "Problem Sets", 22);
        this.model.setAssignmentPointsPossible(courseID_1, "Problem Sets", 160);
        this.model.setAssignmentScore(courseID_1, "Problem Sets", 125);

        this.model.addAtomicAssignmentCategory(courseID_1, "Article Discussion", 31);
        this.model.setAssignmentPointsPossible(courseID_1, "Article Discussion", 50);
        this.model.setAssignmentScore(courseID_1, "Article Discussion", 50);

        this.model.addAtomicAssignmentCategory(courseID_1, "Participation", 25);
        this.model.setAssignmentPointsPossible(courseID_1, "Participation", 40);
        this.model.setAssignmentScore(courseID_1, "Participation", 40);

        this.model.addAtomicAssignmentCategory(courseID_1, "Final Exam", 22);
        this.model.setAssignmentPointsPossible(courseID_1, "Final Exam", 40);
        this.model.setAssignmentScore(courseID_1, "Final Exam", 40);


        // demo stuff for test biol
        this.model.addAtomicAssignmentCategory(courseID_2, "Owl Stuff", 22);
        this.model.setAssignmentPointsPossible(courseID_2, "Owl Stuff", 160);
        this.model.setAssignmentScore(courseID_2, "Owl Stuff", 125);

        this.model.addAtomicAssignmentCategory(courseID_2, "More Owl Stuff", 22);
        this.model.setAssignmentPointsPossible(courseID_2, "More Owl Stuff", 160);
        this.model.setAssignmentScore(courseID_2, "More Owl Stuff", 125);


        // demo stuff for test cs
        this.model.addAtomicAssignmentCategory(courseID_3, "CS Stuff", 22);
        this.model.setAssignmentPointsPossible(courseID_3, "CS Stuff", 160);
        this.model.setAssignmentScore(courseID_3, "CS Stuff", 125);

        this.model.addAtomicAssignmentCategory(courseID_3, "More CS Stuff", 22);
        this.model.setAssignmentPointsPossible(courseID_3, "More CS Stuff", 160);
        this.model.setAssignmentScore(courseID_3, "More CS Stuff", 125);




        this.updateCourses();

//        System.out.println("ASSIGNMENTS ENTERED AND UPDATED");
//        System.out.println(latestCourses.get(courseID_1).getName());
//
//        for (Assignment assignmentCat: latestCourses.get(courseID_1).getAtomicAssignmentCategories().values()) {
//            System.out.println(assignmentCat.getName());
//        }
    }

    public int getNumberOfCourses(){
        this.updateCourses();
        int counter = 0;
        for (ModelCourse course: latestCourses.values()){
            counter++;
        }
        return counter;
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
