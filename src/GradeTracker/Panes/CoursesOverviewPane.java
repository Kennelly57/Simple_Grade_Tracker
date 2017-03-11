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
    // TODO Make this create and return a BorderPane, w/ Hbox btns and title AND grid

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


            tempID.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e) {
                    tempID.setEffect(shadow);
                }
            });

            tempID.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e) {
                    tempID.setEffect(null);
                }
            });


            tempID.setOnMouseClicked((MouseEvent) -> {
                // Send to assignment overview scene
//                System.out.println("PRINTED");
//                System.out.flush();
//                this.mainDisplay.printDiagnostic();
                this.mainDisplay.showCategories(course);
            });

            dataGrid.add(tempID, 0, i + 1);

            Label tempName = new Label(course.getName());
            dataGrid.add(tempName, 1, i + 1);

            Label tempGrade = new Label(course.getGrade());
            dataGrid.add(tempGrade, 2, i + 1);
            i++;
        }

        return dataGrid;

    }

}
