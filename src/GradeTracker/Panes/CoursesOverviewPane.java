package GradeTracker.Panes;

import GradeTracker.ModelCourse;
import GradeTracker.Overviews.MainDisplay;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Map;

public class CoursesOverviewPane {

    private GridPane root;
    private MainDisplay mainDisplay;
    private DropShadow shadow;

    public CoursesOverviewPane(Map<String, ModelCourse> courseMap, MainDisplay newMainDisplay) {
        makeDropShadow();
        root = generateOverviewPane(courseMap);
        this.mainDisplay = newMainDisplay;
    }

//    public CoursesOverviewPane(Map<String, ModelCourse> courseMap) {
//        makeDropShadow();
//        root = generateOverviewPane(courseMap);
//    }

    public GridPane getRoot() {
        return root;
    }

    private void makeDropShadow() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
    }

    private GridPane generateOverviewPane(Map<String, ModelCourse> courseMap) {
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

        int i = 0;
        // Generate Table of Course Values
        for (ModelCourse course: courseMap.values()) {
            final String idStr = course.getID();
            Label tempID = new Label(idStr);

            addDropshadow(tempID);


            tempID.setOnMouseClicked((MouseEvent) -> {
                // Send to assignment overview scene
                this.mainDisplay.showCategories(course);
            });

            Label tempName = new Label(course.getName());
            Label tempGrade = new Label(course.getGrade());

            dataGrid.add(tempID, 0, i + 1);
            dataGrid.add(tempName, 1, i + 1);
            dataGrid.add(tempGrade, 2, i + 1);
            i++;
        }

        return dataGrid;

    }

    private void addDropshadow(final Label label) {
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

}