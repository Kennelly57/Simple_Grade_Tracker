package GradeTracker.Views.Panes;

import GradeTracker.ModelComponents.GTModel;
import GradeTracker.ModelComponents.ModelCourse;
import GradeTracker.AssignmentClasses.AtomicAssignment;
import GradeTracker.AssignmentClasses.CompoundAssignment;
import GradeTracker.Views.MainDisplay;

import java.text.DecimalFormat;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.geometry.Insets;

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

    // --------------------------------------------------
    // Core fcn: Make gridpane
    // --------------------------------------------------

    private GridPane generateGridPane() {
        DecimalFormat decimalFormatter = new DecimalFormat("#0.##");

        Map<String, AtomicAssignment> atomicAsssignmentCategories = this.course.getAtomicAssignmentCategories();
        Map<String, CompoundAssignment> compoundAsssignmentCategories = this.course.getCompoundAssignmentCategories();
        Map<String, Integer> weightMap = this.course.getCategoryWeights();

        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label delHeader = new Label("Delete");
        Label nameHeader = new Label("Category");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Points Earned");
        Label scorePercentHeader = new Label("Score (%)");
        Label weightHeader = new Label("Weight");
        Label weightedHeader = new Label("Weighted Score");

        dataGrid.add(delHeader, 0, 0);
        dataGrid.add(nameHeader, 1, 0);
        dataGrid.add(pointsPosHeader, 2, 0);
        dataGrid.add(scorePtsHeader, 3, 0);
        dataGrid.add(scorePercentHeader, 4, 0);
        dataGrid.add(weightHeader, 5, 0);
        dataGrid.add(weightedHeader, 6, 0);


        // --------------------------------------------------
        // ---> Fill in table values using dictionaries, making appropriate fields editable / clickable
        // --------------------------------------------------

        int i = 0; // keeps track of row we're adding to
        GTModel theModel = this.model;

        // --------------------------------------------------
        // ------> Pt 1: Add Compound Assignments (AKA categories, ie "Tests")
        // --------------------------------------------------
        for (CompoundAssignment compAss: compoundAsssignmentCategories.values()) {

            // Fill DELETE Column
            Button btnDel = generateDelBtn(course.getID(), compAss.getName());
            HBox hBoxEditDel = generateDelBtnPane(btnDel);

            // Fill NAME Column; clicking label calls showAssignments(), passing relevant category
            Label tempName = new Label(compAss.getName());
            tempName.setOnMouseClicked((MouseEvent) -> {
                this.mainDisplay.showAssignments(course.getID(), compAss.getName());
            });

            // Fill POINTS POSSIBLE Column
            Label tempPointsPos = new Label(this.formatDouble(compAss.getPointsPossible()));

            // Fill POINTS EARNED Column
            Label tempPointsScore = new Label(this.formatDouble(compAss.getPointsScore()));

            // Fill PERCENT SCORE Column
            Label tempPercentScore = new Label(this.formatDouble(100*compAss.getPercentageScore()));

            // Fill WEIGHT Column
            TextField weightField = new TextField();
            String curWeight = this.formatDouble(course.getCategoryWeights().get(compAss.getName()));
            weightField.setPromptText(curWeight);

            weightField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (stringIsInt(weightField)) {
                            int updateVal = Integer.parseInt(weightField.getText());
                            theModel.setAssignmentCategoryWeight(course.getID(), compAss.getName(), updateVal);
                        }
                    }
                }
            });

            // Fill WEIGHTED SCORE Column
            double weightedScore = compAss.getPercentageScore() * weightMap.get(compAss.getName());
            Label tempWeightedScore = new Label(this.formatDouble(weightedScore));

            // Add columns to Grid
            dataGrid.add(hBoxEditDel, 0, i+1);
            dataGrid.add(tempName, 1, i + 1);
            dataGrid.add(tempPointsPos, 2, i + 1);
            dataGrid.add(tempPointsScore, 3, i + 1);
            dataGrid.add(tempPercentScore, 4, i + 1);
            dataGrid.add(weightField, 5, i + 1);
            dataGrid.add(tempWeightedScore, 6, i + 1);

            i++;
        }

        // --------------------------------------------------
        // ------> Pt 2: Add Atomic Assignments (Directly editable, ie "Participation")
        // --------------------------------------------------
        for (AtomicAssignment atomAss: atomicAsssignmentCategories.values()) {

            // Fill DELETE Column
            Button btnDel = generateDelBtn(course.getID(), atomAss.getName());
            HBox hBoxEditDel = generateDelBtnPane(btnDel);

            // Fill NAME Column
            Label tempName = new Label(atomAss.getName());

            // Fill POINTS POSSIBLE Column; pressing "enter" sends new value to model
            TextField pointsPos = new TextField();
            String currPointsPos = this.formatDouble(atomAss.getPointsPossible());
            pointsPos.setPromptText(currPointsPos);

            pointsPos.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (stringIsDouble(pointsPos)) {
                            double updateVal = Double.parseDouble(pointsPos.getText());
                            theModel.setAssignmentPointsPossible(course.getID(), atomAss.getName(), updateVal);
                        }
                    }
                }
            });

            // Fill POINTS EARNED Column; pressing "enter" sends new value to model
            TextField pointsEarned = new TextField();
            String currPointsScore = this.formatDouble(atomAss.getPointsScore());
            pointsEarned.setPromptText(currPointsScore);

            pointsEarned.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (stringIsDouble(pointsEarned)) {
                            double updateVal = Double.parseDouble(pointsEarned.getText());
                            theModel.setAssignmentScore(course.getID(), atomAss.getName(), updateVal);
                        }
                    }
                }
            });

            // Fill PERCENT SCORE Column
            Label tempPercentScore = new Label(this.formatDouble(100*atomAss.getPercentageScore()));

            // Fill WEIGHT Column
            TextField weightField = new TextField();
            String curWeight = this.formatDouble(course.getCategoryWeights().get(atomAss.getName()));
            weightField.setPromptText(curWeight);

            weightField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        if (stringIsInt(weightField)) {
                            int updateVal = Integer.parseInt(weightField.getText());
                            theModel.setAssignmentCategoryWeight(course.getID(), atomAss.getName(), updateVal);
                        }
                    }
                }
            });

            // Fill WEIGHTED SCORE Column
            double weightedScore = atomAss.getPercentageScore() * weightMap.get(atomAss.getName());
            Label tempWeightedScore = new Label(this.formatDouble(weightedScore));

            // Add columns to Grid
            dataGrid.add(hBoxEditDel, 0, i+1);
            dataGrid.add(tempName, 1, i + 1);
            dataGrid.add(pointsPos, 2, i + 1);
            dataGrid.add(pointsEarned, 3, i + 1);
            dataGrid.add(tempPercentScore, 4, i + 1);
            dataGrid.add(weightField, 5, i + 1);
            dataGrid.add(tempWeightedScore, 6, i + 1);

            i++;
        }

        return dataGrid;
    }

    private String formatDouble(double number){
        if (Double.isNaN(number)){
            return "NaN";
        }else {
            DecimalFormat decimalFormatter = new DecimalFormat("#0.##");
            return decimalFormatter.format(number);
        }
    }

    // --------------------------------------------------
    // Auxiliary functions
    // --------------------------------------------------

    public GridPane getRoot() {
        return root;
    }

    /**
     * generateDelBtn
     */
    private Button generateDelBtn(String courseID, String assignmentCategoryName) {
        Button btnDel = new Button();
        btnDel.setText("✘");
        btnDel.setId("labelButton");
        btnDel.setOnAction(event -> {
            model.removeAssignmentCategory(courseID, assignmentCategoryName);
        });
        addDropShadow(btnDel);
        return btnDel;
    }

    /**
     * generateDelBtnPane
     * @return hBox with "delete" button
     */
    private HBox generateDelBtnPane(Button btnDel) {
        HBox btnHbox = new HBox();
        btnHbox.getChildren().addAll(btnDel);
        btnHbox.setSpacing(30.0);
        return btnHbox;
    }

    private void makeDropShadow() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
    }

    // Makes sure user only inputs a double
    private boolean stringIsDouble(TextField pointsEarned) {
        boolean matches = false;
        if (pointsEarned.getText().matches("[0-9]*\\.[0-9]+|[0-9]+")){
            matches = true;
        }
        return matches;
    }

    private boolean stringIsInt(TextField pointsEarned) {
        boolean matches = false;
        if (pointsEarned.getText().matches("[0-9]+")){
            matches = true;
        }
        return matches;
    }

    private void addDropShadow(final Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(shadow);
            }
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(null);
            }
        });
    }
}
