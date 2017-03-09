package GradeTracker.Panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class NameAndId {

    private Scene crsIDandNameScene;
    private GridPane inputGrid;

    public GridPane getGridPane(){
        return inputGrid;
    }

    private Scene generateCrsIDandName() {
        BorderPane crsIDandNamePane = new BorderPane();
        crsIDandNamePane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Set up a new Course:");
        crsIDandNamePane.setTop(setupTitle);
        BorderPane.setAlignment(setupTitle, Pos.CENTER);

        crsIDandNameScene = new Scene(crsIDandNamePane, 350, 200);


        //------------------------------CREATE_INPUT_GRID---------------------------------------
        inputGrid = new GridPane();
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
//        Button btnNext = new Button();
//        btnNext.setText("Next");
//        btnNext.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                univPrimaryStage.setScene(gradeDistributionScene);
//                univPrimaryStage.show();
//            }
//        });
//
// todo possibly put this in the setup window class since it's connected to the button
//        crsIDandNamePane.setBottom(btnNext);
//        BorderPane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        //crsIDandNamePane.setMargin(btnNext, new Insets(0, 15, 15, 0));
        //---------------------------------------------------------------------------------------

        return crsIDandNameScene;
    }

}
