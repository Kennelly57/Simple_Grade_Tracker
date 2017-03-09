package GradeTracker.Panes;


import GradeTracker.Assignment;

import GradeTracker.Overviews.MainDisplay;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;

public class CategoriesOverviewPane {
    // TODO Make this create and return a BorderPane, w/ Hbox btns and title AND grid

    private GridPane root;
    private MainDisplay mainDisplay;

    public CategoriesOverviewPane(List<Assignment> myAssignments, MainDisplay newMainDisplay) {
        root = generateGridPane(myAssignments);
        this.mainDisplay = newMainDisplay;
    }

    public CategoriesOverviewPane(List<Assignment> myAssignments) {
        root = generateGridPane(myAssignments);

    }

    public GridPane getRoot() {
        return root;
    }

    private GridPane generateGridPane(List<Assignment> myAssignments) {
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
        for (int i = 0; i < myAssignments.size(); i++) {
            Label tempName = new Label(myAssignments.get(i).getName());
            dataGrid.add(tempName, 0, i + 1);

            Label tempPointsPos = new Label(Double.toString(myAssignments.get(i).getPointsPossible()));
            dataGrid.add(tempPointsPos, 1, i + 1);

            Label tempPointsScore = new Label(Double.toString(myAssignments.get(i).getPointsScore()));
            dataGrid.add(tempPointsScore, 2, i + 1);

            Label tempPercentScore = new Label(Double.toString(myAssignments.get(i).getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i + 1);

            Label tempWeight = new Label(Double.toString(myAssignments.get(i).getWeight()));
            dataGrid.add(tempWeight, 4, i + 1);

            Label tempWeightedScore = new Label(Double.toString(myAssignments.get(i).getWeightedScore()));
            dataGrid.add(tempWeightedScore, 5, i + 1);
        }


        formatAssignmentGridPane(dataGrid);
        return dataGrid;
    }

    private void formatAssignmentGridPane(GridPane dataPane) {
        // TODO refactor this into assignments pane
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n : dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 6) {
                    control.setId("categories");
                    columnCounter++;
                }
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }
        }
        ColumnConstraints oneSixth = new ColumnConstraints();
        oneSixth.setPercentWidth(100 / 6.0);
        oneSixth.setHalignment(HPos.CENTER);
        dataPane.getColumnConstraints().addAll(oneSixth, oneSixth, oneSixth, oneSixth, oneSixth, oneSixth);
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100 / 2.0);
        oneHalf.setValignment(VPos.CENTER);
        dataPane.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf);
        BorderPane.setAlignment(dataPane, Pos.CENTER_LEFT);
        String css = this.getClass().getResource("basicStyle.css").toExternalForm();
        dataPane.getStylesheets().add(css);
    }

}
