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
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CourseSetupWindow extends Application {
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
        crsIDandNamePane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Set up a new Course:");
        crsIDandNamePane.setTop(setupTitle);
        crsIDandNamePane.setAlignment(setupTitle, Pos.CENTER);

        crsIDandNameScene = new Scene(crsIDandNamePane, 350, 200);


        //------------------------------CREATE_INPUT_GRID---------------------------------------
        GridPane inputGrid = new GridPane();
        //inputGrid.setPadding(new Insets(15, 25, 25, 25));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

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
        //crsIDandNamePane.setMargin(btnNext, new Insets(0, 15, 15, 0));
        //---------------------------------------------------------------------------------------

        return crsIDandNameScene;
        }


    public Scene generateGradeDistroSetup() {
        BorderPane setupGradesPane = new BorderPane();
        setupGradesPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Set up a new Course:");
        setupGradesPane.setTop(setupTitle);
        setupGradesPane.setAlignment(setupTitle, Pos.CENTER);

        gradeDistributionScene = new Scene(setupGradesPane, 400, 575);

        VBox gradePane = new VBox();
        gradePane.setPadding(new Insets(15,0, 5, 0));
        Text title = new Text("Configure the grade distribution:");
        GridPane gradeGrid = generateGradingCurvePane();
        gradePane.getChildren().add(title);
        gradePane.getChildren().add(gradeGrid);

        setupGradesPane.setCenter(gradePane);

        //------------------------------CREATE_BUTTONS---------------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("Finish");
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                univPrimaryStage.hide();


//                VBox dialogVbox = new VBox(20);
//                dialogVbox.getChildren().add(new Text("This is a Dialog"));
//                Scene dialogScene = new Scene(dialogVbox, 300, 200);
//                dialog.setScene(dialogScene);
//                dialog.show();

            }
        });

        Button btnBack = new Button();
        btnBack.setText("Back");
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                univPrimaryStage.setScene(crsIDandNameScene);
                univPrimaryStage.show();
            }
        });
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox btnHBox = new HBox();
        btnHBox.getChildren().add(btnBack);
        btnHBox.getChildren().add(spacer);
        btnHBox.getChildren().add(btnFinish);
        btnHBox.setAlignment(Pos.BOTTOM_RIGHT);
        setupGradesPane.setBottom(btnHBox);
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

        TextField tfAPlus = new TextField("96");
        TextField tfA = new TextField("93");
        TextField tfAMinus = new TextField("90");
        TextField tfBPlus = new TextField("86");
        TextField tfB = new TextField("83");
        TextField tfBMinus = new TextField("80");
        TextField tfCPlus = new TextField("76");
        TextField tfC = new TextField("73");
        TextField tfCMinus = new TextField("70");
        TextField tfDPlus = new TextField("66");
        TextField tfD = new TextField("63");
        TextField tfDMinus = new TextField("60");

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
