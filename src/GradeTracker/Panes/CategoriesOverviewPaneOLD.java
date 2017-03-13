package GradeTracker.Panes;


import GradeTracker.GTModel;
import GradeTracker.ModelCourse;
import GradeTracker.Overviews.MainDisplay;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;
import GradeTracker.Setups.AssignmentSetupWindow;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

import static GradeTracker.Overviews.MainDisplay.univPrimaryStage;

//import com.sun.tools.internal.ws.processor.model.Model;

public class CategoriesOverviewPaneOLD {

    private BorderPane root;
    private GridPane grid;
    private MainDisplay mainDisplay;
    private ModelCourse course;
    private GTModel model;

    public CategoriesOverviewPaneOLD(ModelCourse myCourse, MainDisplay newMainDisplay, GTModel gtModel) {
        this.model = gtModel;
        this.course = myCourse;
        grid = generateGridPane();
        this.mainDisplay = newMainDisplay;

        root = new BorderPane();
        Text setupTitle = generateSetupTitle();
        Button btnBack = generateBtnBack();

        HBox controlBtns = createAssBtnPane(btnBack, course.getID());

        root.setTop(setupTitle);
        root.setCenter(grid);
        root.setBottom(controlBtns);

        root.setAlignment(setupTitle, Pos.CENTER);
        root.setAlignment(grid, Pos.CENTER);
        root.setMargin(controlBtns, new Insets(15, 15, 15, 15));

    }

    public BorderPane getRoot() {
        return root;
    }

    private Text generateSetupTitle() {
        String title = String.format("Courses / %s", course.getName());
        Text setupTitle = new Text(title);
        setupTitle.setId("fancytext");
        return setupTitle;
    }

    private Button generateBtnBack() {
        Button btnBack = new Button();
        btnBack.setText("←");
        btnBack.setId("labelButton");
        btnBack.setOnAction((ActionEvent) -> {
            mainDisplay.showCourses();
        });
        addDropShadow(btnBack);
        return btnBack;
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

        // Generate Table of Course Values

        int i = 0;
        ModelCourse theCourse = this.course;
        GTModel theModel = this.model;
        for (SampleAtomicAssignment atomAss: atomicAsssignmentCategories.values()) {

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

            Label tempWeight = new Label(Double.toString(weightMap.get(compAss.getName())));
            dataGrid.add(tempWeight, 4, i + 1);

            double weightedScore = compAss.getPercentageScore() * weightMap.get(compAss.getName());
            Label tempWeightedScore = new Label(Double.toString(weightedScore));
            dataGrid.add(tempWeightedScore, 5, i + 1);

            i++;
        }

        double numberOfColumns = 6.0;
        double numberOfRows = model.getLatestCourses().size();
        formatAssignmentGridPane(dataGrid, numberOfColumns, numberOfRows);
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


    private void formatAssignmentGridPane(GridPane dataPane, double numberOfColumns, double numberOfRows) {
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
        addColumnConstraints(dataPane, numberOfColumns);
        addRowConstraints(dataPane, numberOfRows);
        BorderPane.setAlignment(dataPane, Pos.CENTER_LEFT);
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

    private HBox createAssBtnPane(Button btnBack, String currentCourseID) {
        HBox btnHbox = new HBox();

        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setId("labelButton");
        btnAdd.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(univPrimaryStage);
            new AssignmentSetupWindow().start(dialog, this.model, currentCourseID);
        });
        addDropShadow(btnAdd);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnHbox.getChildren().addAll(btnBack, spacer, btnAdd);
        return btnHbox;
    }

}
