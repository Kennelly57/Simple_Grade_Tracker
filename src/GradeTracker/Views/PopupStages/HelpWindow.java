package GradeTracker.Views.PopupStages;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * HelpWindow
 * Creates popup when "help" button is pressed.
 */
public class HelpWindow extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Grade Tracker Help");

        BorderPane borderPane = new BorderPane();
        borderPane.setId("HelpMsg");
        String introMsg = "\nWelcome to our program!\n\n";
        String helpMsg = "Click on the plus icons to add new courses or new assingments for those courses.\n\n" +
                "Use the save button to save your couses to the hard drive before exiting the program \n\n" +
                "Blue text denotes a clickable button.";
        Text introText = new Text(introMsg);
        Font font = new Font("Tahoma", 20);
        introText.setFont(font);

        Label helpText = new Label(helpMsg);
        helpText.setTextAlignment(TextAlignment.LEFT);
        helpText.setWrapText(true);
        introText.setId("HelpMsg");
        introText.setTextAlignment(TextAlignment.CENTER);


        Button btnOkay = new Button();
        btnOkay.setText("Okay");
        btnOkay.setOnAction(ActionEvent -> {
            primaryStage.hide();
        });

        borderPane.setTop(introText);
        borderPane.setAlignment(introText, Pos.TOP_CENTER);
        borderPane.setCenter(helpText);
        borderPane.setAlignment(helpText, Pos.BASELINE_LEFT);
        borderPane.setMargin(helpText, new Insets(20,20,20,20));
        borderPane.setBottom(btnOkay);
        borderPane.setAlignment(btnOkay, Pos.BOTTOM_RIGHT);
        borderPane.setMargin(btnOkay, new Insets(20,20,20,20));

        Scene scene = new Scene(borderPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
