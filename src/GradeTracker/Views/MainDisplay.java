package GradeTracker.Views;

import GradeTracker.GTModel;
import GradeTracker.GTObserver;
import GradeTracker.ModelCourse;
import GradeTracker.Samples.AtomicAssignment;
import GradeTracker.Views.Panes.AssignmentsOverviewPane;
import GradeTracker.Samples.CompoundAssignment;
import GradeTracker.Views.Panes.CoursesOverviewPane;
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
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
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
    private Button btnAdd;

    private int layer;
    private ModelCourse courseShowing;
    private CompoundAssignment categoryShowing;

    @Override
    public void start(Stage primaryStage) {

        univPrimaryStage = primaryStage;
        model = new GTModel();
        model.registerObserver(this);
        this.updateCourses();
        makeDropshadow();

        model = new GTModel();
        model.registerObserver(this);
        try {
            model.loadCourses();
        } catch (FileNotFoundException e) {
            System.out.println("No file found, generating a new one.");
            System.out.flush();
        }
        makeDemoAssignmentList();

        // get screen size & set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        univPrimaryStage.setX(primaryScreenBounds.getMinX());
        univPrimaryStage.setY(primaryScreenBounds.getMinY());
        univPrimaryStage.setWidth(primaryScreenBounds.getWidth());
        univPrimaryStage.setHeight(primaryScreenBounds.getHeight());

        // the initial view is the courses screen
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
        GridPane dataPane = new CoursesOverviewPane(this.latestCourses, this, this.model).getRoot();
        HBox titleBar = generateTitleAndSaveBtn(screenTitle);
        titleBar.setId("Titlebar");

        // Format GridPane
        double numberOfColumns = 4.0;
        double numberOfRows = model.getLatestCourses().size();
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, dataPane, controlBtns, titleBar);

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
        HBox titleBar = generateTitleAndSaveBtn(screenTitle);
        titleBar.setId("TitleBar");

        Text gradeMsg = getGradeMessage(course);

        HBox controlBtns = generateControlBtnPane_WithBackBtn(btnAdd, btnBack, gradeMsg);
        GridPane dataPane = new CategoriesOverviewPane(course, this, this.model).getRoot();

        // Format GridPane
        double numberOfColumns = 7.0;
        double numberOfRows = course.getAtomicAssignmentCategories().size() + course.getCompoundAssignmentCategories().size();
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, dataPane, controlBtns, titleBar);

        // Set stage to scene
        createScene(root);
    }

    public void showAssignments(ModelCourse course, CompoundAssignment category) {
        this.layer = 2;
        this.updateCourses();
        course = this.latestCourses.get(course.getID()); //todo THIS IS JUST A HACKED-TOGETHER THING. REPLACE IT WITH SOMETHING BETTER.
        category = course.getCompoundAssignmentCategories().get(category.getName());
        this.courseShowing = course;
        this.categoryShowing = category;

        // Borderpane "root" will hold other panes
        BorderPane root = new BorderPane();

        // Create instances of subpanes
        Text screenTitle = generateSetupTitle(layer, course.getName(), category.getName());
        Button btnAdd = generateBtnAdd(layer, course.getID(), category.getName());
        Button btnBack = generateBtnBack(layer, course);
        HBox titleBar = generateTitleAndSaveBtn(screenTitle);
        titleBar.setId("TitleBar");

        Text gradeMsg = getGradeMessage(course);

        HBox controlBtns = generateControlBtnPane_WithBackBtn(btnAdd, btnBack, gradeMsg);
        GridPane dataPane = new AssignmentsOverviewPane(course, category, this, this.model).getRoot();

        // Format GridPane
        double numberOfColumns = 5.0;
        double numberOfRows = 5.0;
        formatGridPane(dataPane, numberOfColumns, numberOfRows);

        // Place subpanes in "root" pane
        addPanesToRoot(root, dataPane, controlBtns, titleBar);

        // Set stage to scene
        createScene(root);
    }

    // ------------------------------------------------------------------------
    // Auxulary functions: used by makeCourses(), makeCategories, makeAssignments()
    // to format and generate content
    // ------------------------------------------------------------------------

    /**
     * generateSetupAdd
     * Makes correct title, returns Text object with CSS id applied
     * Takes into account which layer we are at, where 0=courses(ie Biology), 1=categories(ie Tests) 2=assignments(ie Test #1)
     * -----------
     * At level 0, need no arguments, title just equals "Courses"
     * At level 1, need course name, ie "Courses / Owl Biology"
     * At level 2, need course name & category name, ie "Courses / Owl Biology / Tests"
     */
    private Text generateSetupTitle(int layer, String... info) {
        String title = "";

        if (layer == 0) {
            title = "Courses";
        } else if (layer == 1) {
            String courseTrimmed = shortenString(info[0], 25);
            title = String.format("Courses / %s", courseTrimmed);
        } else if (layer == 2) {
            String courseTrimmed = shortenString(info[0], 15);
            String categoryTrimmed = shortenString(info[1], 15);
            title = String.format("Courses / %s / %s", courseTrimmed, categoryTrimmed);
        }

        Text setupTitle = new Text(title);
        setupTitle.setId("fancytext");
        return setupTitle;
    }

    // Generates Text displaying course grade
    private Text getGradeMessage(ModelCourse course) {
        String gradeMsgStr = "Calculated Course Grade: " + course.getGrade();
        Text gradeMsg = new Text(gradeMsgStr);
        gradeMsg.setId("gradeMsg");
        return gradeMsg;
    }

    /**
     * Helper fcn to generateSetupTitle(),
     * Truncates title of screen to fit dimensions
     */
    private String shortenString(String string, int trimLength) {
        String stringTrimmed = string.substring(0, Math.min(string.length(), trimLength));
        if (!string.equals(stringTrimmed)) {
            stringTrimmed += "...";
        }
        return stringTrimmed;
    }

    /**
     * generateBtnAdd
     * generates "+" that will open appropriate popup
     * Takes into account which layer we are at, where 0=courses(ie Biology), 1=categories(ie Tests) 2=assignments(ie Test #1)
     * -------------------
     * At level 0, need no arguments, just open CourseSetupWindow
     * At level 1, need courseId to generate appropriate AssignmentSetupWindow
     * At level 2, need courseId & category name to generate appropriate AssignmentSetupWindow
     */
    private Button generateBtnAdd(int layer, String... info) {
        btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setId("labelButton");
        btnAdd.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(univPrimaryStage);
            if (layer == 0) {
                new CourseSetupWindow().start(dialog, this.model);
            } else if (layer == 1) {
                new AssignmentSetupWindow().start(dialog, this.model, info[0], false); // passing courseID
            } else if (layer == 2) {
                new AssignmentSetupWindow().start(dialog, this.model, info[0], true, info[1]); // passing courseID & category name
            }
        });
        addDropShadow(btnAdd);
        return btnAdd;
    }

    /**
     * generateBtnBack
     * generates "←" that will reload appropriate screen
     * Takes into account which layer we are at, where 0=courses(ie Biology), 1=categories(ie Tests) 2=assignments(ie Test #1)
     * <p>
     * At level 0, don't make a back button
     * At level 1, don't need parameters, just reload showCourses()
     * At level 2, need a course object so we can reload showCategories(), passing the correct Course object
     */
    private Button generateBtnBack(int layer, ModelCourse... course) {
        Button btnBack = new Button();
        btnBack.setText("←");
        btnBack.setId("labelButton");
        btnBack.setOnAction((ActionEvent) -> {
            if (layer == 1) {
                showCourses();
            }
            if (layer == 2) {
                showCategories(course[0]);
            }
        });
        addDropShadow(btnBack);
        return btnBack;
    }

    /**
     * generateControlBtnPane_NoBackBtn
     * Called by showCourses()
     *
     * @return hBox with "+" button
     */
    private HBox generateControlBtnPane_NoBackBtn(Button btnAdd) {
        HBox btnHbox = new HBox();
        btnHbox.getChildren().add(btnAdd);
        btnHbox.setAlignment(Pos.BOTTOM_RIGHT);
        return btnHbox;
    }

    /**
     * generateControlBtnPane_WithBackBtn
     * Called by showCategories() and showAssignments()
     *
     * @return hBox with "+" button & "←" button
     */
    private HBox generateControlBtnPane_WithBackBtn(Button btnAdd, Button btnBack, Text grade) {
        HBox btnHbox = new HBox();
        HBox spacerLeft = new HBox();
        HBox spacerRight = new HBox();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);
        btnHbox.getChildren().addAll(btnBack, spacerLeft, grade, spacerRight, btnAdd);
        return btnHbox;
    }

    /**
     * Adds title, grid full of content, and control buttons to the root BorderPane for a scene
     */
    private void addPanesToRoot(BorderPane root, GridPane dataPane, HBox controlBtns, HBox saveButtonAndTitle) {

        root.setTop(saveButtonAndTitle);
        root.setAlignment(saveButtonAndTitle, Pos.CENTER);

        root.setCenter(dataPane);
        root.setAlignment(dataPane, Pos.CENTER);

        root.setBottom(controlBtns);
        root.setAlignment(controlBtns, Pos.CENTER);

        root.setMargin(controlBtns, new Insets(15, 15, 15, 15));
    }

    /**
     * Generates a scene using "root" BorderPane and configures CSS
     * btnAdd.requestFocus() keeps the cursor off of editable areas.
     */
    private void createScene(BorderPane root) {
        Scene scene = new Scene(root);
        btnAdd.requestFocus();
        scene.getStylesheets().add("resources/basicStyle.css");
        univPrimaryStage.setTitle("Courses for Winter 2017");
        univPrimaryStage.setScene(scene);
        univPrimaryStage.show();
    }

    private void makeDropshadow() {
        dropShadow = new DropShadow();
        dropShadow.setRadius(20.0);
    }

//    public void printDiagnostic() {
//        System.out.println("Diagnostic");
//        System.out.flush();
//    }

//    public int getNumberOfCourses() {
//        this.updateCourses();
//        int counter = 0;
//        for (ModelCourse course : latestCourses.values()) {
//            counter++;
//        }
//        return counter;
//    }

    public void notifyOfChange() {
        this.upToDate = false;
        if (this.layer == 0) {
            this.showCourses();
        } else if (this.layer == 1) {
            this.showCategories(this.courseShowing);
        } else if (this.layer == 2) {
            this.showAssignments(this.courseShowing, this.categoryShowing);
        }
    }

    private void updateCourses() {
        if (!this.upToDate) {
            this.latestCourses = this.model.getLatestCourses();
            this.upToDate = true;
        }
    }

    /**
     * Called after generating a GridPane to ensure proper styling is applied
     */
    private void formatGridPane(GridPane dataPane, Double numberOfColumns, Double numberOfRows) {
        dataPane.setId("dataPane");
        int columnCounter = 0;
        int children = dataPane.getChildren().size();
        for (Node n : dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");

                boolean isFirstRow = columnCounter <= numberOfColumns;
                if (isFirstRow) {
                    control.setId("categories");
                } else {

                    boolean isBtnCol = columnCounter % numberOfColumns == 2;
                    if (isBtnCol) {

                        // todo quick fix
                        if (n instanceof Label) {

                            styleBtnCol((Label) n, control);
                        }

                    }
                }
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }

            columnCounter++;
        }

        addColumnConstraints(dataPane, numberOfColumns);
        addRowConstraints(dataPane, numberOfRows);
    }

    // Helper fcn for styling elements of grids that act as buttons.
    private void styleBtnCol(Label label, Control control) {
        String key = label.getText();
        boolean isCourse = model.getLatestCourses().containsKey(key);
        boolean isCompoundAssignment = false;

        // Check if view is on the assignmentCategories layer.
        // If it is, then courseShowing is instantiated.
        if (layer == 1) {
            isCompoundAssignment = courseShowing.getCompoundAssignmentCategories().containsKey(key);
        }

        // Check to see if cell refers to a course or compound assignment, i.e. if it should look clickable.
        if (isCourse || isCompoundAssignment) {
            control.setId("labelButton");
            addDropShadow(label);
        }
    }

    /**
     * Helper fcn for formatGridPane
     */
    private void addColumnConstraints(GridPane dataPane, Double numberOfColumns) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100 / numberOfColumns);
        columnConstraints.setHalignment(HPos.CENTER);
        for (double i = 0.0; i < numberOfColumns; i++) {
            dataPane.getColumnConstraints().addAll(columnConstraints);
        }
    }

    /**
     * Helper fcn for formatGridPane
     */
    private void addRowConstraints(GridPane dataPane, Double numberOfRows) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100 / numberOfRows);
        rowConstraints.setValignment(VPos.CENTER);
        for (double i = 0.0; i <= numberOfRows; i++) {
            dataPane.getRowConstraints().addAll(rowConstraints);
        }
    }

    /**
     * Used to generate drop shadow effect for clickable buttons
     */
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

    /**
     * Used to generate drop shadow effect for clickable buttons
     */
    private void addDropShadow(final Label label) {
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                label.setEffect(dropShadow);
            }
        });
        label.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                label.setEffect(null);
            }
        });
    }

    private HBox generateTitleAndSaveBtn(Text screenTitle) {
        HBox saveButtonAndScreenTitle = new HBox();
        Button save = new Button();
        this.addDropShadow(save);
        save.setPadding(new Insets(5, 5, 5, 5));
        save.setId("Save");
        save.setText("Save");
        save.setOnAction(ActionEvent -> {
            model.saveCouses();
        });

        HBox titleBar = new HBox(screenTitle);
        titleBar.setId("Title");
        VBox titleAndSave = new VBox(save, titleBar);
        titleAndSave.setId("TitleAndSave");

        saveButtonAndScreenTitle.getChildren().addAll(titleAndSave);

        return saveButtonAndScreenTitle;
    }

    private void makeDemoAssignmentList() {

        int[] gScale = {96, 93, 90, 87, 83, 80, 77, 73, 70, 67, 63, 60};

        String courseID_1 = "TEST 101";
        String courseID_2 = "BIOL.362";
        String courseID_3 = "CS.111";

        this.model.addCourse(courseID_1, "Test Course", gScale);
        this.model.addCourse(courseID_2, "Owls Patterns & Colors", gScale);
        this.model.addCourse(courseID_3, "Intro CS", gScale);

        AtomicAssignment midtermExams = new AtomicAssignment("Midterm Exams");

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

        this.model.addCompoundAssignmentCategory(courseID_2, "More Compound Stuff", 22);

        this.model.addAtomicAssignmentToCompoundCategory(courseID_2, "More Compound Stuff", "test 1");
        this.model.setAssignmentPointsPossible(courseID_2, "test 1", 160);
        this.model.setAssignmentScore(courseID_2, "test 1", 125);

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