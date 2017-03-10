package GradeTracker.Panes;


import GradeTracker.Assignment;

import GradeTracker.ModelCourse;
import GradeTracker.Overviews.MainDisplay;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;
import GradeTracker.ModelCourse;
import java.util.Map;

//import com.sun.tools.internal.ws.processor.model.Model;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;

public class CategoriesOverviewPane {

    private GridPane root;
    private MainDisplay mainDisplay;
    private ModelCourse course;

    public CategoriesOverviewPane(ModelCourse myCourse, MainDisplay newMainDisplay) {

        Map<String, SampleAtomicAssignment> atomicAsssignmentCategories = myCourse.getAtomicAssignmentCategories();
        Map<String, SampleCompoundAssignment> compoundAsssignmentCategories = myCourse.getCompoundAssignmentCategories();
        Map<String, Integer> weightMap = myCourse.getCategoryWeights();

        root = generateGridPane(atomicAsssignmentCategories, compoundAsssignmentCategories, weightMap);
        this.mainDisplay = newMainDisplay;
        this.course = myCourse;
    }

    public GridPane getRoot() {
        return root;
    }

    private GridPane generateGridPane(Map<String, SampleAtomicAssignment> atomicAsssignmentCategories,
                                      Map<String, SampleCompoundAssignment> compoundAsssignmentCategories,
                                      Map<String, Integer> weightMap) {
        System.out.println("GENERATING GRID PANE");

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

        int i = 0;
        for (SampleAtomicAssignment atomAss: atomicAsssignmentCategories.values()) {
            Label tempName = new Label(atomAss.getName());
            dataGrid.add(tempName, 0, i + 1);

            Label tempPointsPos = new Label(Double.toString(atomAss.getPointsPossible()));
            dataGrid.add(tempPointsPos, 1, i + 1);

            TextField pointsScore = new TextField();
            String currPointsScore = Double.toString(atomAss.getPointsScore());
            pointsScore.setPromptText(currPointsScore);

//            pointsScore.setOnAction(e -> {
//                if (ke.getCode().equals(KeyCode.ENTER)) {
//                    double updateVal = Double.parseDouble(pointsScore.getText());
//                    System.out.println(updateVal);
//                    atomAss.setPointsScore(updateVal);
//                }
//            });

            pointsScore.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        double updateVal = Double.parseDouble(pointsScore.getText());
                        System.out.println(updateVal);
                        atomAss.setPointsScore(updateVal);
                        refreshPane();
                    }
                }
            });

            dataGrid.add(pointsScore, 2, i + 1);

            Label tempPercentScore = new Label(Double.toString(atomAss.getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i + 1);

            Label tempWeight = new Label(Double.toString(weightMap.get(atomAss.getName())));
            dataGrid.add(tempWeight, 4, i + 1);

            double weightedScore = atomAss.getPercentageScore() * weightMap.get(atomAss.getName());
            Label tempWeightedScore = new Label(Double.toString(weightedScore));
            dataGrid.add(tempWeightedScore, 5, i + 1);

            i++;
        }

        for (SampleCompoundAssignment compAss: compoundAsssignmentCategories.values()) {
            Label tempName = new Label(compAss.getName());
            dataGrid.add(tempName, 0, i + 1);

            Label tempPointsPos = new Label(Double.toString(compAss.getPointsPossible()));
            dataGrid.add(tempPointsPos, 1, i + 1);

            Label tempPointsScore = new Label(Double.toString(compAss.getPointsScore()));
            dataGrid.add(tempPointsScore, 2, i + 1);

            Label tempPercentScore = new Label(Double.toString(compAss.getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i + 1);

            Label tempWeight = new Label(Double.toString(compAss.getWeight()));
            dataGrid.add(tempWeight, 4, i + 1);

            Label tempWeightedScore = new Label(Double.toString(compAss.getWeightedScore()));
            dataGrid.add(tempWeightedScore, 5, i + 1);

            i++;
        }

        formatAssignmentGridPane(dataGrid);
        return dataGrid;
    }

    private void refreshPane() {
        this.mainDisplay.showCategories(course);
    }


    private void formatAssignmentGridPane(GridPane dataPane) {
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

    private int getNumCategories(Map<String, SampleAtomicAssignment> atomicAsssignmentCategories, Map<String, SampleCompoundAssignment> compoundAsssignmentCategories){
        int counter = 0;

        for (SampleAtomicAssignment atomCat : atomicAsssignmentCategories.values()){
            counter++;
        }

        for (SampleCompoundAssignment comCat : compoundAsssignmentCategories.values()){
            counter++;
        }

        return counter;
    }

}
