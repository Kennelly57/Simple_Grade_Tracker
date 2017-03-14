package GradeTracker.Views.PopupStages;

import GradeTracker.GTModel;
import GradeTracker.Views.HelperPanes.PopupPanes.GradePane;
import GradeTracker.handleCSV;
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

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class CourseSetupWindow extends Application {
    private Scene crsIDandNameScene;
    private Scene gradeDistributionScene;
    public Stage univPrimaryStage;

    private String courseID;
    private String courseName;
    private GTModel model;

    private handleCSV csvHandler = new handleCSV();

    public void start(final Stage primaryStage, GTModel theModel) {
        this.model = theModel;
        univPrimaryStage = primaryStage;
        primaryStage.setTitle("Course Setup");
        crsIDandNameScene = generateCrsIDandName();
        gradeDistributionScene = generateGradeDistroSetup();
        primaryStage.setScene(crsIDandNameScene);
        primaryStage.show();
    }


    @Override
    public void start(final Stage primaryStage) {
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
        BorderPane.setAlignment(setupTitle, Pos.CENTER);

        crsIDandNameScene = new Scene(crsIDandNamePane, 350, 200);


        //------------------------------CREATE_INPUT_GRID---------------------------------------
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(15, 25, 25, 25));
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


        //GridPane inputGrid = new NameAndId().getGridPane();
        crsIDandNamePane.setCenter(inputGrid);
        //---------------------------------------------------------------------------------------

        //------------------------------CREATE_NEXT_BUTTON---------------------------------------
        Button btnNext = new Button();
        btnNext.setText("Next");
        btnNext.setDefaultButton(true);
        btnNext.setOnAction(event -> {

            csvHandler.dataCollector(identificationTextField.getText());
            csvHandler.dataCollector(crsNameTextField.getText());

            this.courseID = identificationTextField.getText();
            this.courseName = crsNameTextField.getText();

            univPrimaryStage.setScene(gradeDistributionScene);
            univPrimaryStage.show();

        });

        crsIDandNamePane.setBottom(btnNext);
        BorderPane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        //crsIDandNamePane.setMargin(btnNext, new Insets(0, 15, 15, 0));
        //---------------------------------------------------------------------------------------

        return crsIDandNameScene;
        }


    public Scene generateGradeDistroSetup() {
        BorderPane setupGradesPane = new BorderPane();
        setupGradesPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Set up a new Course:");
        setupGradesPane.setTop(setupTitle);
        BorderPane.setAlignment(setupTitle, Pos.CENTER);

        gradeDistributionScene = new Scene(setupGradesPane, 400, 650);
        GradePane setupPane = new GradePane();
        VBox gradePane = setupPane.getRoot();

//        gradePane.setPadding(new Insets(15,0, 5, 0));
//        Text title = new Text("Configure the grade distribution:");
//        GridPane gradeGrid = generateGradingCurvePane();
//        gradePane.getChildren().add(title);
//        gradePane.getChildren().add(gradeGrid);

        setupGradesPane.setCenter(gradePane);

        //------------------------------CREATE_BUTTONS---------------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("Finish");
        btnFinish.setDefaultButton(true);
        btnFinish.setOnAction(event -> {
            LinkedList<String> gradeStringList = setupPane.getTextFields();
            int[] gradeIntArray = new int[12];
            boolean allInts = true;
            try{
                for (int i = 0; i < 12; i++) {
                    gradeIntArray[i] = Integer.parseInt(gradeStringList.get(i));
                    if (i > 0 && gradeIntArray[i] > gradeIntArray[i-1]){
                        allInts = false;
                    }
                }

            } catch (Exception e){
                System.out.println(e);
               allInts = false;
            }

            for (String item : gradeStringList) {
                csvHandler.dataCollector(item);
            }
            try {
                csvHandler.addCourse();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (allInts && !this.courseID.isEmpty() && !this.courseID.isEmpty()){
                this.model.addCourse(courseID, courseName, gradeIntArray);
            }

            univPrimaryStage.hide();

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

    public static void main(String[] args) {launch(args);}
}
