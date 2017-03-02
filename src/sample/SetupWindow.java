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
    BorderPane crsIDandNamePane;

    @Override
    public void start(Stage primaryStage) {
        crsIDandNamePane = new BorderPane();

        crsIDandNameScene = new Scene(crsIDandNamePane, 300, 250);
        primaryStage.setScene(crsIDandNameScene);
        primaryStage.setTitle("Course Setup");

        GridPane courseNameGrid = generateCourseNameGrid();


        Button btnNext = new Button();
        btnNext.setText("Next");
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    System.out.println("Works");
            }
        });

        crsIDandNamePane.setCenter(courseNameGrid);
        crsIDandNamePane.setBottom(btnNext);
        crsIDandNamePane.setAlignment(btnNext, Pos.BOTTOM_RIGHT);
        crsIDandNamePane.setMargin(btnNext, new Insets(0,15,15,0));


        primaryStage.show();






    }

    private GridPane generateCourseNameGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);

        Text gridTitle = new Text("Set up a new Course:");
        grid.add(gridTitle, 0, 0);

        Label identificationLabel = new Label("Course ID:");
        Label crsNameLabel = new Label("Course Name:");

        TextField identificationTextField = new TextField();
        TextField crsNameTextField = new TextField();

        grid.add(identificationLabel, 0, 2);
        grid.add(identificationTextField, 1, 2);
        grid.add(crsNameLabel, 0, 3);
        grid.add(crsNameTextField, 1, 3);

        return grid;
    }

    public static void main(String[] args) {launch(args);}
}
