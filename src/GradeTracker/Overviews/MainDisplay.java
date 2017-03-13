package GradeTracker.Overviews;

import GradeTracker.GTModel;
import GradeTracker.GTObserver;
import GradeTracker.ModelCourse;
import GradeTracker.Samples.SampleCompoundAssignment;
import GradeTracker.Panes.CoursesOverviewPane;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Panes.CategoriesOverviewPane;
import GradeTracker.Setups.AssignmentSetupWindow;
import GradeTracker.Setups.CourseSetupWindow;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
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
    private DropShadow dropShadow;

    private int layer;
    private ModelCourse courseShowing;
    private SampleCompoundAssignment categoryShowing;

    @Override
    public void start(Stage primaryStage) {
        univPrimaryStage = primaryStage;
        model = new GTModel();
        model.registerObserver(this);
        this.updateCourses();
        makeDemoAssignmentList();
        makeDropshadow();

        // get screen size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        univPrimaryStage.setX(primaryScreenBounds.getMinX());
        univPrimaryStage.setY(primaryScreenBounds.getMinY());
        univPrimaryStage.setWidth(primaryScreenBounds.getWidth());
        univPrimaryStage.setHeight(primaryScreenBounds.getHeight());

        showCourses();
    }

    private void makeDropshadow() {
        dropShadow = new DropShadow();
        dropShadow.setRadius(20.0);
    }


    public HBox createCoursesBtnPane() {
        HBox btnHbox = new HBox();

        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setId("labelButton");
        btnAdd.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(univPrimaryStage);
            new CourseSetupWindow().start(dialog, this.model);
        });
        addDropShadow(btnAdd);

        btnHbox.getChildren().add(btnAdd);
        btnHbox.setAlignment(Pos.BOTTOM_RIGHT);

        return btnHbox;
    }

    public void showCourses() {
        this.updateCourses();

        this.layer = 0;

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        String title = "Courses for Winter 2017";
        Text setupTitle = new Text(title);
        setupTitle.setId("fancytext");
        GridPane dataPane = new CoursesOverviewPane(this.latestCourses, this).getRoot();
        double numberOfRows = model.getLatestCourses().size();
        formatGridPane(dataPane, 3.0, numberOfRows);
        HBox controlBtns = createCoursesBtnPane();

        // Place subpanes in "root" pane
        root.setTop(setupTitle);
        root.setAlignment(setupTitle, Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setMargin(controlBtns, new Insets(15, 15, 15, 15));

        // Create scene
        Scene scene = new Scene(root, 1020, 730);
        scene.getStylesheets().add("resources/basicStyle.css");
        univPrimaryStage.setTitle("Courses");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    public void showCategories(ModelCourse course) {
        this.layer = 1;
        this.courseShowing = course;

        this.updateCourses();

        //todo THIS IS JUST A HACKED-TOGETHER THING. REPLACE IT WITH SOMETHING BETTER.
        course = this.latestCourses.get(course.getID());

        Map<String, SampleAtomicAssignment> tMap = this.latestCourses.get(course.getID()).getAtomicAssignmentCategories();


        BorderPane root = new CategoriesOverviewPane(course, this, this.model).getRoot();

        // Create scene
        int length = model.getLatestCourses().size();
        int height = 100 * length;
        Scene scene = new Scene(root, 1423, height);
        scene.getStylesheets().add("resources/basicStyle.css");
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    public void showAssignments(SampleCompoundAssignment category) {
        this.layer = 2;
        this.categoryShowing = category;

//        this.updateCourses();
//        course = this.latestCourses.get(course.getID());

//        Map<String, SampleAtomicAssignment> tMap = this.latestCourses.get(course.getID()).getAtomicAssignmentCategories();


        BorderPane root= new AssignmentsOverviewPane(course, this, this.model).getRoot();

        // Create scene
        int length = model.getLatestCourses().size();
        int height = 100 * length;
        Scene scene = new Scene(root, 1423, height);
        scene.getStylesheets().add("resources/basicStyle.css");
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    public void printDiagnostic() {
        System.out.println("Diagnostic");
        System.out.flush();
    }

    public int getNumberOfCourses() {
        this.updateCourses();
        int counter = 0;
        for (ModelCourse course : latestCourses.values()) {
            counter++;
        }
        return counter;
    }

    public void notifyOfChange() {
        this.upToDate = false;
        if (this.layer == 0) {
            this.showCourses();
        } else if (this.layer == 1) {
            this.showCategories(this.courseShowing);
        }
    }

    private void updateCourses() {
        if (!this.upToDate) {
            this.latestCourses = this.model.getLatestCourses();
            this.upToDate = true;

        }
    }

    // Add proper IDs to grid elements for CSS styling and sets grid size
    private void formatGridPane(GridPane dataPane, Double numberOfColumns, Double numberOfRows) {
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n : dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 3) {
                    control.setId("categories");
                }
                if ((columnCounter >= 3) && (columnCounter % 3 == 0)) {
                    control.setId("labelButton");
                }
                columnCounter++;
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }
        }

        addColumnConstraints(dataPane, numberOfColumns);
        addRowConstraints(dataPane, numberOfRows);
    }

    private void addColumnConstraints(GridPane dataPane, Double numberOfColumns) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100 / numberOfColumns);
        columnConstraints.setHalignment(HPos.CENTER);
        for (double i = 0.0; i < numberOfColumns; i++) {
            dataPane.getColumnConstraints().addAll(columnConstraints);
        }
    }

    private void addRowConstraints(GridPane dataPane, Double numberOfRows) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100 / numberOfRows);
        rowConstraints.setValignment(VPos.CENTER);
        for (double i = 0.0; i <= numberOfRows; i++) {
            dataPane.getRowConstraints().addAll(rowConstraints);
        }
    }

    private void addDropShadow(final Button btn) {
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setEffect(dropShadow);
            }
        });
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btn.setEffect(null);
            }
        });
    }

    private void makeDemoAssignmentList() {

        int[] gScale = {96, 93, 90, 86, 83, 80, 76, 66, 63, 60};

        String courseID_1 = "TEST 101";
        String courseID_2 = "BIOL.362";
        String courseID_3 = "CS.111";

        this.model.addCourse(courseID_1, "Test Course", gScale);
        this.model.addCourse(courseID_2, "Owls Patterns & Colors", gScale);
        this.model.addCourse(courseID_3, "Intro CS", gScale);

        SampleAtomicAssignment midtermExams = new SampleAtomicAssignment("Midterm Exams");

        // demo stuff for test 101
        this.model.addAtomicAssignmentCategory(courseID_1, "Midterm Exams", 30);
        this.model.setAssignmentPointsPossible(courseID_1, "Midterm Exams", 300);
        this.model.setAssignmentScore(courseID_1, "Midterm Exams", 210);

        this.model.addAtomicAssignmentCategory(courseID_1, "Problem Sets", 20);
        this.model.setAssignmentPointsPossible(courseID_1, "Problem Sets", 160);
        this.model.setAssignmentScore(courseID_1, "Problem Sets", 125);

        this.model.addAtomicAssignmentCategory(courseID_1, "Article Discussion", 20);
        this.model.setAssignmentPointsPossible(courseID_1, "Article Discussion", 50);
        this.model.setAssignmentScore(courseID_1, "Article Discussion", 50);

        this.model.addAtomicAssignmentCategory(courseID_1, "Participation", 15);
        this.model.setAssignmentPointsPossible(courseID_1, "Participation", 40);
        this.model.setAssignmentScore(courseID_1, "Participation", 40);

        this.model.addAtomicAssignmentCategory(courseID_1, "Final Exam", 15);
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
    }

}