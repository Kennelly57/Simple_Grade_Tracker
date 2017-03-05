package GradeTracker;


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
    @Override
    public void start(Stage primaryStage){
        start(primaryStage, "assignment");
    }

    public void start(Stage primaryStage, String setupType) {
        primaryStage.setTitle(setupType.concat(" creation"));

        BorderPane aswBorderPane = new BorderPane();
        aswBorderPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Create a new ".concat(setupType).concat(":"));
        aswBorderPane.setTop(setupTitle);
        aswBorderPane.setAlignment(setupTitle, Pos.CENTER);

        GridPane dataPane = generateAssignmentCreationPane();
        aswBorderPane.setCenter(dataPane);

        //------------------------------CREATE_FINALIZE_BUTTON-----------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("Create");
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.hide();
            }
        });

        aswBorderPane.setBottom(btnFinish);
        aswBorderPane.setAlignment(btnFinish, Pos.BOTTOM_RIGHT);
        //---------------------------------------------------------------------------------------

        Scene aswScene = new Scene(aswBorderPane, 450, 350);
        primaryStage.setScene(aswScene);
        primaryStage.show();
    }

    public GridPane generateAssignmentCreationPane(){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

        TextField categoryNameTF = new TextField();
        TextField weightTF = new TextField();

        HBox subItemHBox = generateSubItemHBox();

        Label categoryNameLabel = new Label("Category Name:");
        Label weightLabel = new Label("Weight:");
        Label subItemsLabel = new Label("Subitems:");

        VBox relevantFieldsVBox = generateRelevantFieldsVBox();

        dataGrid.add(categoryNameLabel, 0, 0);
        dataGrid.add(categoryNameTF, 1, 0);
        dataGrid.add(weightLabel, 0, 1);
        dataGrid.add(weightTF, 1, 1);
        dataGrid.add(subItemsLabel, 0, 2);
        dataGrid.add(subItemHBox, 1, 2);
        dataGrid.add(relevantFieldsVBox, 0, 3);

        return dataGrid;
    }

    private HBox generateSubItemHBox() {
        ToggleGroup subItemsToggle = new ToggleGroup();
        RadioButton btnSubItemsYes = new RadioButton("Yes");
        RadioButton btnSubItemsNo = new RadioButton("No");
        btnSubItemsYes.setToggleGroup(subItemsToggle);
        btnSubItemsNo.setToggleGroup(subItemsToggle);

        HBox subItemsHbox = new HBox();
        subItemsHbox.setMargin(btnSubItemsYes, new Insets(0, 10, 0, 0));
        subItemsHbox.getChildren().add(btnSubItemsYes);
        subItemsHbox.getChildren().add(btnSubItemsNo);

        return subItemsHbox;
    }

    private VBox generateRelevantFieldsVBox(){
        VBox relevantFieldsVBox = new VBox();
        Text relFieldsLabel = new Text("Relevant Fields");
        relevantFieldsVBox.getChildren().add(relFieldsLabel);
        relevantFieldsVBox.setMargin(relFieldsLabel, new Insets(10,0,10,0));

        CheckBox pointsPossibleCB = new CheckBox("Points Possible");
        CheckBox scorePointsCB = new CheckBox("Score (points)");
        CheckBox scorePercentCB = new CheckBox("Score (percent)");
        CheckBox weightCB = new CheckBox("Weight");
        CheckBox weightedScoreCB = new CheckBox("Weighted Score");

        List<CheckBox> cbList = Arrays.asList(
                pointsPossibleCB, scorePointsCB, scorePercentCB,
                weightCB, weightedScoreCB);
        for(int i = 0; i < cbList.size(); i++){
            relevantFieldsVBox.getChildren().add(cbList.get(i));
        }

        return relevantFieldsVBox;
    }

    public static void main(String[] args) {launch(args);}
}