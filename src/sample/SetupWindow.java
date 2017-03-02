package sample;


import javafx.application.Application;
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
import javafx.stage.Stage;

public class SetupWindow extends Application {
    Scene crsIDandNameScene;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Course Setup");
        crsIDandNameScene = generateCrsIDandName();
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
                System.out.println("Works");
            }
        });

        crsIDandNamePane.setBottom(btnNext);
        crsIDandNamePane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        crsIDandNamePane.setMargin(btnNext, new Insets(0, 15, 15, 0));
        //---------------------------------------------------------------------------------------

        return crsIDandNameScene;
        }

    public static void main(String[] args) {launch(args);}
}
