package GradeTracker.Views.Panes;

import GradeTracker.GTModel;
import GradeTracker.ModelCourse;
import GradeTracker.Views.MainDisplay;
import GradeTracker.Views.PopupStages.AssignmentSetupWindow;
import GradeTracker.Views.PopupStages.CourseSetupWindow;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

public class CoursesOverviewPane {

    private GTModel model;
    private GridPane root;
    private MainDisplay mainDisplay;
    private DropShadow shadow;

    public CoursesOverviewPane(Map<String, ModelCourse> courseMap, MainDisplay newMainDisplay, GTModel gtModel) {
        makeDropShadow();
        this.model = gtModel;
        root = generateOverviewPane(courseMap);
        this.mainDisplay = newMainDisplay;
    }

    public GridPane getRoot() {
        return root;
    }


    // --------------------------------------------------
    // Core fcn: Make gridpane
    // --------------------------------------------------

    private GridPane generateOverviewPane(Map<String, ModelCourse> courseMap) {
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label editDelLabel = new Label("Edit / Delete");
        Label courseIdLabel = new Label("Course Id");
        Label courseNameLabel = new Label("Name");
        Label courseGradeLabel = new Label("Grade");

        dataGrid.add(editDelLabel, 0, 0);
        dataGrid.add(courseIdLabel, 1, 0);
        dataGrid.add(courseNameLabel, 2, 0);
        dataGrid.add(courseGradeLabel, 3, 0);

        // --------------------------------------------------
        // --> Fill in table values, making appropriate fields editable / clickable
        // --------------------------------------------------

        int i = 0; // keeps track of row we're adding to
        for (ModelCourse course: courseMap.values()) {

            // Fill EDIT/DELETE column
            Button btnDel = generateDelBtn(course);
            Button btnEdit = generateEditBtn(course);
            HBox hBoxEditDel = generateEditAndDelBtnPane(btnEdit, btnDel);

            // Fill ID Column; clicking label calls showCategories(), passing relevant course
            final String idStr = course.getID();
            Label tempID = new Label(idStr);
            addDropShadow(tempID);

            tempID.setOnMouseClicked((MouseEvent) -> {
                // Send to assignment overview scene
                this.mainDisplay.showCategories(course);
            });

            // Fill NAME Column
            Label tempName = new Label(course.getName());

            // Fill GRADE column
            Label tempGrade = new Label(course.getGrade());

            // Add columns to Grid
            dataGrid.add(hBoxEditDel, 0, i + 1);
            dataGrid.add(tempID, 1, i + 1);
            dataGrid.add(tempName, 2, i + 1);
            dataGrid.add(tempGrade, 3, i + 1);
            i++;
        }

        return dataGrid;
    }

    // --------------------------------------------------
    // Auxillary functions
    // --------------------------------------------------

    /**
     * generateDelBtn
     */
    private Button generateDelBtn(ModelCourse course) {
        Button btnDel = new Button();
        btnDel.setText("✘");
        btnDel.setId("labelButton");
        btnDel.setOnAction(event -> {
            String courseID = course.getID();
            this.model.removeCourse(courseID);
            this.mainDisplay.showCourses();
        });
        addDropShadow(btnDel);
        return btnDel;
    }

    /**
     * generateEditBtn
     */
    private Button generateEditBtn(ModelCourse course) {
        Button btnBack = new Button();
        btnBack.setText("✐");
        btnBack.setId("labelButton");
        btnBack.setOnAction((ActionEvent) -> {
            ;
        });
        addDropShadow(btnBack);
        return btnBack;
    }


    /**
     * generateEditAndDelBtnPane
     * @return hBox with "edit" button & "delete" button
     */
    private HBox generateEditAndDelBtnPane(Button btnEdit, Button btnDel) {
        HBox btnHbox = new HBox();
        btnHbox.getChildren().addAll(btnEdit, btnDel);
        btnHbox.setSpacing(30.0);
        return btnHbox;
    }

    private void makeDropShadow() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
    }

    private void addDropShadow(final Label label) {
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                label.setEffect(shadow);
            }
        });

        label.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                label.setEffect(null);
            }
        });
    }

    private void addDropShadow(final Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(shadow);
            }
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(null);
            }
        });
    }

}