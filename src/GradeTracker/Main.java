package GradeTracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//This is a sample class created by IntelliJ
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        LoginView login = new LoginView();
        Parent scene = login.displayLogin();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(scene, 600, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
