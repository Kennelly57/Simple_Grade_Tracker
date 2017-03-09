package GradeTracker.Panes;

import GradeTracker.Samples.SampleCourse;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Created by goodelld on 3/8/17.
 */
public class CoursesOverviewPane {
    // TODO Make this create and return a BorderPane, w/ Hbox btns and title AND grid

    private GridPane root;

    public CoursesOverviewPane(List<SampleCourse> myTermList) {
        root = generateOverviewPane(myTermList);
    }

    public GridPane getRoot() {
        return root;
    }

    private GridPane generateOverviewPane(List<SampleCourse> myTermList) {
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

        // Generate Table of Course Values
        for (int i = 0; i < myTermList.size(); i++) {
            final String idStr = myTermList.get(i).getId();
            Label tempID = new Label(idStr);

            tempID.setOnMouseClicked((MouseEvent) -> {
                // send itself to controller

            });

            dataGrid.add(tempID, 0, i + 1);

            Label tempName = new Label(myTermList.get(i).getName());
            dataGrid.add(tempName, 1, i + 1);

            Label tempGrade = new Label(myTermList.get(i).getGrade());
            dataGrid.add(tempGrade, 2, i + 1);
        }

        return dataGrid;

    }

}
