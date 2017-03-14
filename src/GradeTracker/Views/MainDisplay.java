package GradeTracker.Views;

import GradeTracker.GTModel;
import GradeTracker.GTObserver;
import GradeTracker.ModelCourse;
import GradeTracker.Views.Panes.AssignmentsOverviewPane;
import GradeTracker.Samples.SampleCompoundAssignment;
import GradeTracker.Views.Panes.CoursesOverviewPane;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Views.Panes.CategoriesOverviewPane;
import GradeTracker.Views.PopupStages.AssignmentSetupWindow;
import GradeTracker.Views.PopupStages.CourseSetupWindow;
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
 * MainDisplay.java
 * Generates all necessary views.
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

        // get screen size & set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        univPrimaryStage.setX(primaryScreenBounds.getMinX());
        univPrimaryStage.setY(primaryScreenBounds.getMinY());
        univPrimaryStage.setWidth(primaryScreenBounds.getWidth());
        univPrimaryStage.setHeight(primaryScreenBounds.getHeight());

        // the initial view
        showCourses();
    }

    // ------------------------------------------------------------------------
    // Core functions: makeCourses(), makeCategories, makeAssignments()
    // Each creates a scene and sets the stage to that scene
    // ------------------------------------------------------------------------

    public void showCourses() {
        this.layer = 0;
        this.updateCourses();

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text screenTitle = generateSetupTitle(layer);
        Button btnAdd = generateBtnAdd(layer);
        HBox controlBtns = generateControlBtnPane_NoBackBtn(btnAdd);
        GridPane dataPane = new CoursesOverviewPane(this.latestCourses, this).getRoot();

        // Format GridPane
        double numberOfColumns = 3.0;
        double numberOfRows = model.getLatestCourses().size();
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, screenTitle, dataPane, controlBtns);

        // Set stage to scene
        createScene(root);
    }

    public void showCategories(ModelCourse course) {
        this.layer = 1;
        this.updateCourses();
        course = this.latestCourses.get(course.getID()); //todo THIS IS JUST A HACKED-TOGETHER THING. REPLACE IT WITH SOMETHING BETTER.
        this.courseShowing = course;

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text screenTitle = generateSetupTitle(layer, course.getName());
        Button btnAdd = generateBtnAdd(layer, course.getID());
        Button btnBack = generateBtnBack(layer);
        HBox controlBtns = generateControlBtnPane_WithBackBtn(btnAdd, btnBack);
        GridPane dataPane = new CategoriesOverviewPane(course, this, this.model).getRoot();

        // Format GridPane
        double numberOfColumns = 6.0;
        double numberOfRows = course.getAtomicAssignmentCategories().size() + course.getCompoundAssignmentCategories().size();
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, screenTitle, dataPane, controlBtns);

        // Set stage to scene
        createScene(root);
    }


    public void showAssignments(ModelCourse course, SampleCompoundAssignment category) {
        this.layer= 2;
        this.updateCourses();
        course = this.latestCourses.get(course.getID()); //todo THIS IS JUST A HACKED-TOGETHER THING. REPLACE IT WITH SOMETHING BETTER.
        this.courseShowing = course;

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text screenTitle = generateSetupTitle(layer, course.getName());
        Button btnAdd = generateBtnAdd(layer, course.getID());
        Button btnBack = generateBtnBack(layer);
        HBox controlBtns = generateControlBtnPane_WithBackBtn(btnAdd, btnBack);
        GridPane dataPane = new AssignmentsOverviewPane(course, category, this, this.model).getRoot();

        // Format GridPane
        double numberOfColumns = 6.0;
        double numberOfRows = course.getAtomicAssignmentCategories().size() + course.getCompoundAssignmentCategories().size();
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, screenTitle, dataPane, controlBtns);

        // Set stage to scene
        createScene(root);
    }

    // ------------------------------------------------------------------------
    // Auxulary functions: used by makeCourses(), makeCategories, makeAssignments()
    // to format and generate content
    // ------------------------------------------------------------------------

    private Text generateSetupTitle(int layer, String...info) {
        String title = "";

        // Generate appropriate title text depending on view
        // ie "Courses / Owl Biology" or "Courses / Owl Biology / Tests"
        if(layer == 0) {
            title = "Courses";
        }
        else if (layer == 1) {
            title = String.format("Courses / %s", info[0]);
        }
        else if (layer == 2) {
            title = String.format("Courses / %s / %s", info[0], info[1]);
        }

        Text setupTitle = new Text(title);
        setupTitle.setId("fancytext");
        return setupTitle;
    }

    private Button generateBtnAdd(int layer, String...onPressInfo) {
        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setId("labelButton");
        btnAdd.requestFocus();
        btnAdd.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(univPrimaryStage);
            if (layer==0) {
                new CourseSetupWindow().start(dialog, this.model);
            }
            else if (layer==1){
                new AssignmentSetupWindow().start(dialog, this.model, onPressInfo[0]); // passing courseID
            }
        });
        addDropShadow(btnAdd);
        return btnAdd;
    }

    private Button generateBtnBack(int layer) {
        Button btnBack = new Button();
        btnBack.setText("←");
        btnBack.setId("labelButton");
        btnBack.setOnAction((ActionEvent) -> {
            if (layer==1) {
                showCourses();
            }
//            if (layer==2) {
//                showCategories(course);
//            }
        });
        addDropShadow(btnBack);
        return btnBack;
    }

    private HBox generateControlBtnPane_NoBackBtn(Button btnAdd) {
        HBox btnHbox = new HBox();
        btnHbox.getChildren().add(btnAdd);
        btnHbox.setAlignment(Pos.BOTTOM_RIGHT);
        return btnHbox;
    }

    private HBox generateControlBtnPane_WithBackBtn(Button btnAdd, Button btnBack) {
        HBox btnHbox = new HBox();
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnHbox.getChildren().addAll(btnBack, spacer, btnAdd);
        return btnHbox;
    }

    private void addPanesToRoot(BorderPane root, Text screenTitle, GridPane dataPane, HBox controlBtns) {
        root.setTop(screenTitle);
        root.setAlignment(screenTitle, Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setMargin(controlBtns, new Insets(15, 15, 15, 15));
    }

    private void createScene(BorderPane root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add("resources/basicStyle.css");
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    private void makeDropshadow() {
        dropShadow = new DropShadow();
        dropShadow.setRadius(20.0);
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
                if (columnCounter < numberOfColumns) {
                    control.setId("categories");
                }
                if ((columnCounter >= numberOfColumns) && (columnCounter % numberOfColumns == 0)) {
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

        int[] gScale = {96, 93, 90, 87, 83, 80, 77, 73, 70, 67, 63, 60};

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