package GradeTracker.Setups;


import GradeTracker.GTModel;
import GradeTracker.Panes.CreateAssignmentPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class AssignmentSetupWindow extends Application {

    public static Stage stage;
    private GTModel model;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("DO NOT USE THE OVERRIDDEN START METHOD");
    }//Can we use this first and give the model later?

    public void start(Stage primaryStage, GTModel theModel, String currentCourseID){
        this.model = theModel;
        start(primaryStage, "Assignment", theModel, currentCourseID);
    }

    public void start(Stage primaryStage, String setupType, GTModel theModel, String currentCourseID) {
        stage = primaryStage;
        stage.setTitle(setupType.concat(" creation"));
        BorderPane aswBorderPane = new CreateAssignmentPane(setupType, this.model, currentCourseID).getRoot();
        Scene aswScene = new Scene(aswBorderPane, 450, 350);
        stage.setScene(aswScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//------------------------------PREVIOUSLY IN START--------------------------------------------
//        BorderPane aswBorderPane = new BorderPane();
//        aswBorderPane.setPadding(new Insets(15, 15, 15, 25));
//        Text setupTitle = new Text("Create a new ".concat(setupType).concat(":"));
//        aswBorderPane.setTop(setupTitle);
//        BorderPane.setAlignment(setupTitle, Pos.CENTER);
//
//        GridPane dataPane = new CreateAssignmentPane().getRoot();
//        aswBorderPane.setCenter(dataPane);
//
//        //------------------------------CREATE_FINALIZE_BUTTON------------------------
//        Button btnFinish = new Button();
//        btnFinish.setText("Create");
//        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                primaryStage.hide();
//            }
//        });
//
//        aswBorderPane.setBottom(btnFinish);
//        BorderPane.setAlignment(btnFinish, Pos.BOTTOM_RIGHT);
//        //----------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------


//    public GridPane generateAssignmentCreationPane(){
//        GridPane dataGrid = new CreateAssignmentPane().getRoot();
//        return dataGrid;
//    }

//    private HBox generateSubItemHBox() {
//        ToggleGroup subItemsToggle = new ToggleGroup();
//        RadioButton btnSubItemsYes = new RadioButton("Yes");
//        RadioButton btnSubItemsNo = new RadioButton("No");
//        btnSubItemsYes.setToggleGroup(subItemsToggle);
//        btnSubItemsNo.setToggleGroup(subItemsToggle);
//
//        HBox subItemsHbox = new HBox();
//        HBox.setMargin(btnSubItemsYes, new Insets(0, 10, 0, 0));
//        subItemsHbox.getChildren().add(btnSubItemsYes);
//        subItemsHbox.getChildren().add(btnSubItemsNo);
//
//        return subItemsHbox;
//    }
//
//    private VBox generateRelevantFieldsVBox(){
//        VBox relevantFieldsVBox = new VBox();
//        Text relFieldsLabel = new Text("Relevant Fields");
//        relevantFieldsVBox.getChildren().add(relFieldsLabel);
//        VBox.setMargin(relFieldsLabel, new Insets(10,0,10,0));
//
//        CheckBox pointsPossibleCB = new CheckBox("Points Possible");
//        CheckBox scorePointsCB = new CheckBox("Score (points)");
//        CheckBox scorePercentCB = new CheckBox("Score (percent)");
//        CheckBox weightCB = new CheckBox("Weight");
//        CheckBox weightedScoreCB = new CheckBox("Weighted Score");
//
//        List<CheckBox> cbList = Arrays.asList(
//                pointsPossibleCB, scorePointsCB, scorePercentCB,
//                weightCB, weightedScoreCB);
//        for(int i = 0; i < cbList.size(); i++){
//            relevantFieldsVBox.getChildren().add(cbList.get(i));
//        }
//
//        return relevantFieldsVBox;
//    }

