package GradeTracker;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class SetupWindow extends Application {
    private Scene crsIDandNameScene;
    private Scene gradeDistributionScene;
    private Stage univPrimaryStage;

    @Override
    public void start(Stage primaryStage) {
        univPrimaryStage = primaryStage;
        primaryStage.setTitle("Course Setup");
        crsIDandNameScene = generateCrsIDandName();
        gradeDistributionScene = generateGradeDistroSetup();
        primaryStage.setScene(crsIDandNameScene);
        primaryStage.show();

    }

    private Scene generateCrsIDandName() {
        BorderPane crsIDandNamePane = new BorderPane();
        crsIDandNameScene = new Scene(crsIDandNamePane, 300, 200);

        //------------------------------CREATE_INPUT_GRID---------------------------------------
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(15, 25, 25, 25));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        Text gridTitle = new Text("Set up a new Course:");
        inputGrid.add(gridTitle, 0, 0);

        Label identificationLabel = new Label("Course ID:");
        Label crsNameLabel = new Label("Course Name:");

        TextField identificationTextField = new TextField();
        TextField crsNameTextField = new TextField();

        inputGrid.add(identificationLabel, 0, 2);
        inputGrid.add(identificationTextField, 1, 2);
        inputGrid.add(crsNameLabel, 0, 3);
        inputGrid.add(crsNameTextField, 1, 3);

        crsIDandNamePane.setCenter(inputGrid);
        //---------------------------------------------------------------------------------------

        //------------------------------CREATE_NEXT_BUTTON---------------------------------------
        Button btnNext = new Button();
        btnNext.setText("Next");
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                univPrimaryStage.setScene(gradeDistributionScene);
                univPrimaryStage.show();
            }
        });

        crsIDandNamePane.setBottom(btnNext);
        crsIDandNamePane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        crsIDandNamePane.setMargin(btnNext, new Insets(0, 15, 15, 0));
        //---------------------------------------------------------------------------------------

        return crsIDandNameScene;
        }


    private Scene generateGradeDistroSetup() {
        BorderPane setupGradesPane = new BorderPane();
        setupGradesPane.setPadding(new Insets(15, 15, 15, 25));
        gradeDistributionScene = new Scene(setupGradesPane, 400, 525);

        VBox gradePane = new VBox();
        Text title = new Text("Configure the grade distribution:");
        GridPane gradeGrid = generateGradingCurvePane();
        gradePane.getChildren().add(title);
        gradePane.getChildren().add(gradeGrid);

        setupGradesPane.setLeft(gradePane);

        //------------------------------CREATE_NEXT_BUTFON---------------------------------------
        Button btnNext = new Button();
        btnNext.setText("Next");
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                univPrimaryStage.setScene(crsIDandNameScene);
                univPrimaryStage.show();
            }
        });

        setupGradesPane.setBottom(btnNext);
        setupGradesPane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        //---------------------------------------------------------------------------------------

        return gradeDistributionScene;
    }

    private GridPane generateGradingCurvePane() {
        GridPane gradeGrid = new GridPane();
        gradeGrid.setPadding(new Insets(15, 25, 25, 25));
        gradeGrid.setHgap(10);
        gradeGrid.setVgap(10);

        Label gradeAPlus = new Label("A+");
        Label gradeA = new Label("A");
        Label gradeAMinus = new Label("A-");
        Label gradeBPlus = new Label("B+");
        Label gradeB = new Label("B");
        Label gradeBMinus = new Label("B-");
        Label gradeCPlus = new Label("C+");
        Label gradeC = new Label("C");
        Label gradeCMinus = new Label("C-");
        Label gradeDPlus = new Label("D+");
        Label gradeD = new Label("D");
        Label gradeDMinus = new Label("D-");

        List<Label> labelList = Arrays.asList(
                gradeAPlus, gradeA, gradeAMinus,
                gradeBPlus, gradeB, gradeBMinus,
                gradeCPlus, gradeC, gradeCMinus,
                gradeDPlus, gradeD, gradeDMinus);

        TextField tfAPlus = new TextField();
        TextField tfA = new TextField();
        TextField tfAMinus = new TextField();
        TextField tfBPlus = new TextField();
        TextField tfB = new TextField();
        TextField tfBMinus = new TextField();
        TextField tfCPlus = new TextField();
        TextField tfC = new TextField();
        TextField tfCMinus = new TextField();
        TextField tfDPlus = new TextField();
        TextField tfD = new TextField();
        TextField tfDMinus = new TextField();

        List<TextField> textFieldList = Arrays.asList(
                tfAPlus, tfA, tfAMinus,
                tfBPlus, tfB, tfBMinus,
                tfCPlus, tfC, tfCMinus,
                tfDPlus, tfD, tfDMinus);


        for(int i = 0; i < 12; i++) {
            gradeGrid.add(labelList.get(i), 0, i);
            gradeGrid.add(textFieldList.get(i), 1, i);
        }
        return gradeGrid;
    }


    public static void main(String[] args) {launch(args);}
}
