package GradeTracker.Panes;


import GradeTracker.Assignment;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

public class AssignmentsOverviewPane {

    private GridPane root;

    public AssignmentsOverviewPane(List<Assignment> myAssignments){
        root = generateOverviewPane(myAssignments);
    }

    public GridPane getRoot() {
        return root;
    }

    private GridPane generateOverviewPane(List<Assignment> myAssignments){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label nameHeader = new Label("Category");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Score (pts)");
        Label scorePercentHeader = new Label("Score (%)");
        Label weightHeader = new Label("Weight");
        Label weightedHeader = new Label("Weighted Score");

        dataGrid.add(nameHeader, 0, 0);
        dataGrid.add(pointsPosHeader, 1, 0);
        dataGrid.add(scorePtsHeader, 2, 0);
        dataGrid.add(scorePercentHeader, 3, 0);
        dataGrid.add(weightHeader, 4, 0);
        dataGrid.add(weightedHeader, 5, 0);

        // Generate Table of Course Values
        for (int i = 0; i< myAssignments.size(); i++) {
            Label tempName = new Label(myAssignments.get(i).getName());
            dataGrid.add(tempName, 0, i+1);

            Label tempPointsPos = new Label(Double.toString(myAssignments.get(i).getPointsPossible()));
            dataGrid.add(tempPointsPos, 1, i+1);

            Label tempPointsScore = new Label(Double.toString(myAssignments.get(i).getPointsScore()));
            dataGrid.add(tempPointsScore, 2, i+1);

            Label tempPercentScore = new Label(Double.toString(myAssignments.get(i).getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i+1);

            Label tempWeight = new Label(Double.toString(myAssignments.get(i).getWeight()));
            dataGrid.add(tempWeight, 4, i+1);

            Label tempWeightedScore = new Label(Double.toString(myAssignments.get(i).getWeightedScore()));
            dataGrid.add(tempWeightedScore, 5, i+1);
        }

        return dataGrid;
    }
}
