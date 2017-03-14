package GradeTracker.Views.Panes;


import GradeTracker.GTModel;
import GradeTracker.ModelCourse;
import GradeTracker.Views.MainDisplay;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Map;

//import com.sun.tools.internal.ws.processor.model.Model;

public class AssignmentsOverviewPane {

    private GridPane root;
    private DropShadow shadow;
    private MainDisplay mainDisplay;
    private ModelCourse course;
    private SampleCompoundAssignment category;
    private GTModel model;

    public AssignmentsOverviewPane(ModelCourse myCourse, SampleCompoundAssignment myCategory, MainDisplay newMainDisplay, GTModel gtModel) {
        this.model = gtModel;
        this.course = myCourse;
        this.category = myCategory;
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

        Map<String, SampleAtomicAssignment> subAssignmentMap = this.category.getAtomicSubAssignmentMap();

        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label nameHeader = new Label("Assignment");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Points Earned");
        Label scorePercentHeader = new Label("Score (%)");

        dataGrid.add(nameHeader, 0, 0);
        dataGrid.add(pointsPosHeader, 1, 0);
        dataGrid.add(scorePtsHeader, 2, 0);
        dataGrid.add(scorePercentHeader, 3, 0);

        // Generate Table of Course Values

        int i = 0;
        ModelCourse theCourse = this.course;
        GTModel theModel = this.model;
        for (SampleAtomicAssignment atomAss: subAssignmentMap.values()) {

            Label tempName = new Label(atomAss.getName());

            dataGrid.add(tempName, 0, i + 1);

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

            dataGrid.add(pointsPos, 1, i + 1);

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

            dataGrid.add(pointsEarned, 2, i + 1);

            Label tempPercentScore = new Label(Double.toString(100*atomAss.getPercentageScore()));
            dataGrid.add(tempPercentScore, 3, i + 1);

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
