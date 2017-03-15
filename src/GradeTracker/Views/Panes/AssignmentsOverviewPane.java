package GradeTracker.Views.Panes;


import GradeTracker.GTModel;
import GradeTracker.ModelCourse;
import GradeTracker.Samples.CompoundAssignment;
import GradeTracker.Views.MainDisplay;
import GradeTracker.Samples.AtomicAssignment;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Map;

//import com.sun.tools.internal.ws.processor.model.Model;

public class AssignmentsOverviewPane {

    private GridPane root;
    private DropShadow shadow;
    private MainDisplay mainDisplay;
    private ModelCourse course;
    private CompoundAssignment category;
    private GTModel model;

    public AssignmentsOverviewPane(ModelCourse myCourse, CompoundAssignment myCategory, MainDisplay newMainDisplay, GTModel gtModel) {
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

    // --------------------------------------------------
    // Core fcn: Make gridpane
    // --------------------------------------------------

    private GridPane generateGridPane() {

        Map<String, AtomicAssignment> subAssignmentMap = this.category.getAtomicSubAssignmentMap();

        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label delHeader = new Label("Delete");
        Label nameHeader = new Label("Assignment");
        Label pointsPosHeader = new Label("Points Possible");
        Label scorePtsHeader = new Label("Points Earned");
        Label scorePercentHeader = new Label("Score (%)");

        dataGrid.add(delHeader,0,0);
        dataGrid.add(nameHeader, 1, 0);
        dataGrid.add(pointsPosHeader, 2, 0);
        dataGrid.add(scorePtsHeader, 3, 0);
        dataGrid.add(scorePercentHeader, 4, 0);

        // --------------------------------------------------
        // Fill in table of sub-assignments, making appropriate fields editable / clickable
        // --------------------------------------------------

        int i = 0; // keeps track of row we're adding to
        GTModel theModel = this.model;

        for (AtomicAssignment atomAss: subAssignmentMap.values()) {

            // Fill DELETE Column
            Button btnDel = generateDelBtn(course.getID(), category.getName(), atomAss.getName());
            HBox hBoxEditDel = generateDelBtnPane(btnDel);

            // Fill NAME Column
            Label tempName = new Label(atomAss.getName());

            // Fill POINTS POSSIBLE Column; pressing "enter" sends new value to model
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
                        }
                    }
                }
            });

            // Fill POINTS EARNED Column; pressing "enter" sends new value to model
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
                        }
                    }
                }
            });

            // Fill PERCENT SCORE Column
            Label tempPercentScore = new Label(Double.toString(100*atomAss.getPercentageScore()));

            // Add columns to Grid
            dataGrid.add(hBoxEditDel, 0, i+1);
            dataGrid.add(tempName, 1, i + 1);
            dataGrid.add(pointsPos, 2, i + 1);
            dataGrid.add(pointsEarned, 3, i + 1);
            dataGrid.add(tempPercentScore, 4, i + 1);

            i++;
        }

        return dataGrid;
    }

    // --------------------------------------------------
    // Auxiliary functions
    // --------------------------------------------------

    private void makeDropShadow() {
        shadow = new DropShadow();
        shadow.setRadius(20.0);
    }

    private boolean inputOkay(TextField pointsEarned) {
        boolean matches = false;
        if (pointsEarned.getText().matches("[0-9]*\\.[0-9]+|[0-9]+")){
            matches = true;
        }
        return matches;
    }

    /**
     * generateDelBtn
     */
    private Button generateDelBtn(String courseID, String categoryName, String assignmentName) {
        Button btnDel = new Button();
        btnDel.setText("✘");
        btnDel.setId("labelButton");
        btnDel.setOnAction(event -> {
            System.out.println("Pressed delete Button, for sub-assignment.");
            System.out.println(courseID + " " + categoryName + " " + assignmentName);
            model.removeAssignmentFromCompoundCategory(courseID, categoryName, assignmentName);
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
