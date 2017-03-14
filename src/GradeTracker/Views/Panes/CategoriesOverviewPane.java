package GradeTracker.Views.Panes;


import GradeTracker.GTModel;
import GradeTracker.ModelCourse;
import GradeTracker.Views.MainDisplay;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;

import java.util.Map;

//import com.sun.tools.internal.ws.processor.model.Model;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class CategoriesOverviewPane {

    private GridPane root;
    private DropShadow shadow;
    private MainDisplay mainDisplay;
    private ModelCourse course;
    private GTModel model;

    public CategoriesOverviewPane(ModelCourse myCourse, MainDisplay newMainDisplay, GTModel gtModel) {
        this.model = gtModel;
        this.course = myCourse;
        makeDropShadow();
        root = generateGridPane();
        this.mainDisplay = newMainDisplay;
    }

    public GridPane getRoot() {
        return root;
    }

    private void makeDropShadow() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
    }

    private GridPane generateGridPane() {

        Map<String, SampleAtomicAssignment> atomicAsssignmentCategories = this.course.getAtomicAssignmentCategories();
        Map<String, SampleCompoundAssignment> compoundAsssignmentCategories = this.course.getCompoundAssignmentCategories();
        Map<String, Integer> weightMap = this.course.getCategoryWeights();

        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label nameHeader = new Label("Category");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Points Earned");
        Label scorePercentHeader = new Label("Score (%)");
        Label weightHeader = new Label("Weight");
        Label weightedHeader = new Label("Weighted Score");

        dataGrid.add(nameHeader, 0, 0);
        dataGrid.add(pointsPosHeader, 1, 0);
        dataGrid.add(scorePtsHeader, 2, 0);
        dataGrid.add(scorePercentHeader, 3, 0);
        dataGrid.add(weightHeader, 4, 0);
        dataGrid.add(weightedHeader, 5, 0);


        // --------------------------------------------------
        // Fill in table values using dictionaries
        // --------------------------------------------------

        int i = 0;
        ModelCourse theCourse = this.course;
        GTModel theModel = this.model;

        // --------------------------------------------------
        // ---> Pt 1: Add Compound Assignments (AKA categories, ie "Tests")
        // --------------------------------------------------
        for (SampleCompoundAssignment compAss: compoundAsssignmentCategories.values()) {

            // Fill NAME Column
            Label tempName = new Label(compAss.getName());
            tempName.setOnMouseClicked((MouseEvent) -> {
                // Call showAssignments; go to subitem overview scene
                this.mainDisplay.showAssignments(course, compAss);
            });

            // Fill POINTS POSSIBLE Column
            Label tempPointsPos = new Label(Double.toString(compAss.getPointsPossible()));

            // Fill POINTS EARNED Column
            Label tempPointsScore = new Label(Double.toString(compAss.getPointsScore()));

            // Fill PERCENT SCORE Column
            Label tempPercentScore = new Label(Double.toString(compAss.getPercentageScore()));

            // Fill WEIGHT Column
            Label tempWeight = new Label(Double.toString(weightMap.get(compAss.getName())));

            // Fill WEIGHTED SCORE Column
            double weightedScore = compAss.getPercentageScore() * weightMap.get(compAss.getName());
            Label tempWeightedScore = new Label(Double.toString(weightedScore));

            // Add elements to Grid
            dataGrid.add(tempName, 0, i + 1);
            dataGrid.add(tempPointsPos, 1, i + 1);
            dataGrid.add(tempPointsScore, 2, i + 1);
            dataGrid.add(tempPercentScore, 3, i + 1);
            dataGrid.add(tempWeight, 4, i + 1);
            dataGrid.add(tempWeightedScore, 5, i + 1);

            i++;
        }

        // --------------------------------------------------
        // ---> Pt 2: Add Atomic Assignments (Directly editable, ie "Participation")
        // --------------------------------------------------
        for (SampleAtomicAssignment atomAss: atomicAsssignmentCategories.values()) {

            // Fill NAME Column
            Label tempName = new Label(atomAss.getName());

            // Fill POINTS POSSIBLE Column
            TextField pointsPos = new TextField();
            String currPointsPos = Double.toString(atomAss.getPointsPossible());
            pointsPos.setPromptText(currPointsPos);

            pointsPos.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (inputOkay(pointsPos)) {
                            double updateVal = Double.parseDouble(pointsPos.getText());
                            theModel.setAssignmentPointsPossible(course.getID(), atomAss.getName(), updateVal);
                            refreshPane();
                        } else{
                            refreshPane();
                        }
                    }
                }
            });

            // Fill POINTS EARNED Column
            TextField pointsEarned = new TextField();
            String currPointsScore = Double.toString(atomAss.getPointsScore());
            pointsEarned.setPromptText(currPointsScore);
            pointsEarned.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (inputOkay(pointsEarned)) {
                            double updateVal = Double.parseDouble(pointsEarned.getText());
                            theModel.setAssignmentScore(course.getID(), atomAss.getName(), updateVal);
                            refreshPane();
                        } else{
                            refreshPane();
                        }
                    }
                }
            });

            // Fill PERCENT SCORE Column
            Label tempPercentScore = new Label(Double.toString(100*atomAss.getPercentageScore()));

            // Fill WEIGHT Column
            Label tempWeight = new Label(Double.toString(weightMap.get(atomAss.getName())));

            // Fill WEIGHTED SCORE Column
            double weightedScore = atomAss.getPercentageScore() * weightMap.get(atomAss.getName());
            Label tempWeightedScore = new Label(Double.toString(weightedScore));

            // Add elements to Grid
            dataGrid.add(tempName, 0, i + 1);
            dataGrid.add(pointsPos, 1, i + 1);
            dataGrid.add(pointsEarned, 2, i + 1);
            dataGrid.add(tempPercentScore, 3, i + 1);
            dataGrid.add(tempWeight, 4, i + 1);
            dataGrid.add(tempWeightedScore, 5, i + 1);

            i++;
        }

        return dataGrid;
    }

    private boolean inputOkay(TextField pointsEarned) {
        boolean matches = false;
        if (pointsEarned.getText().matches("[0-9]*\\.[0-9]+|[0-9]+")){
            matches = true;
        }
        return matches;
    }

    private void refreshPane() {
        this.mainDisplay.showCategories(course);
    }

    private void addColumnConstraints(GridPane dataPane, double numberOfColumns) {
        ColumnConstraints oneSixth = new ColumnConstraints();
        oneSixth.setPercentWidth(100 / numberOfColumns);
        oneSixth.setHalignment(HPos.CENTER);
        dataPane.getColumnConstraints().addAll(oneSixth, oneSixth, oneSixth, oneSixth, oneSixth, oneSixth);
    }

    private void addRowConstraints(GridPane dataPane, double numberOfRows) {
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100 / numberOfRows);
        oneHalf.setValignment(VPos.CENTER);
        dataPane.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf, oneHalf, oneHalf);
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

    private void addDropShadow(final Button btn) {
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DropShadow dropShadow = new DropShadow();
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
}
