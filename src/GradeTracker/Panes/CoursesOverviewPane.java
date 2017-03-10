package GradeTracker.Panes;

import GradeTracker.Assignment;
import GradeTracker.ModelCourse;
import GradeTracker.Overviews.MainDisplay;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;
import GradeTracker.Samples.SampleCourse;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class CoursesOverviewPane {
    // TODO Make this create and return a BorderPane, w/ Hbox btns and title AND grid

    private GridPane root;
    private MainDisplay mainDisplay;

    public CoursesOverviewPane(Map<String, ModelCourse> courseMap, MainDisplay newMainDisplay) {
        root = generateOverviewPane(courseMap);
        this.mainDisplay = newMainDisplay;
    }

    public CoursesOverviewPane(Map<String, ModelCourse> courseMap) {root = generateOverviewPane(courseMap); }

    public GridPane getRoot() {
        return root;
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
            final String idStr = course.getId();
            Label tempID = new Label(idStr);

            tempID.setOnMouseClicked((MouseEvent) -> {
                // Send to assignment overview scene
                System.out.println("PRINTED");
                System.out.flush();
                this.mainDisplay.printDiagnostic();
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
