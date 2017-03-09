package GradeTracker.Panes;

import GradeTracker.Overviews.AssignmentsOverviewDisplay;
import GradeTracker.Overviews.CoursesOverviewDisplay;
import GradeTracker.Samples.SampleCourse;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Created by goodelld on 3/8/17.
 */
public class CoursesOverviewPane {

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

            tempID.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    AssignmentsOverviewDisplay newDisplay = new AssignmentsOverviewDisplay();
                    newDisplay.start(CoursesOverviewDisplay.getUnivPrimaryStage());
                }
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
