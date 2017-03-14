package GradeTracker.Views.Panes.PopupPanes;

import GradeTracker.GTModel;
import GradeTracker.Views.PopupStages.AssignmentSetupWindow;
import com.sun.istack.internal.NotNull;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CreateSubAssignmentPane {

    private BorderPane root;
    private GTModel model;
    private String courseID;
    private String categoryName;

    // Creates a pane prompting the user for an assignment name and weight,
    // and asks user to choose if the assignment has subitems.
    public CreateSubAssignmentPane(String setupType, GTModel theModel, String theCourseID, String theCategoryName) {
        this.model = theModel;
        this.courseID = theCourseID;
        this.categoryName = theCategoryName;

        root = new BorderPane();
        root.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Create a new ".concat(setupType).concat(":"));
        root.setTop(setupTitle);
        BorderPane.setAlignment(setupTitle, Pos.CENTER);

        //-----------------------------------GENERATING DATA GRID---------------------------------
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));

        TextField categoryNameTF = new TextField();

        Spinner weightSpinner = generateSpinner();

        ToggleGroup subItemsToggle = new ToggleGroup();
        RadioButton btnSubItemsYes = new RadioButton("Yes");
        RadioButton btnSubItemsNo = new RadioButton("No");
        btnSubItemsYes.setToggleGroup(subItemsToggle);
        btnSubItemsNo.setToggleGroup(subItemsToggle);

        HBox subItemsHBox = new HBox();
        HBox.setMargin(btnSubItemsYes, new Insets(0, 10, 0, 0));
        subItemsHBox.getChildren().add(btnSubItemsYes);
        subItemsHBox.getChildren().add(btnSubItemsNo);

        Label categoryNameLabel = new Label("Category Name:");
        Label weightLabel = new Label("Weight:");
        Label subItemsLabel = new Label("Subitems:");

        //VBox relevantFieldsVBox = generateRelevantFieldsVBox();

        dataGrid.add(categoryNameLabel, 0, 0);
        dataGrid.add(categoryNameTF, 1, 0);
        dataGrid.add(weightLabel, 0, 1);
        dataGrid.add(weightSpinner, 1, 1);
        dataGrid.add(subItemsLabel, 0, 2);
        dataGrid.add(subItemsHBox, 1, 2);
        //---------------------------------------------------------------------------------------

        root.setCenter(dataGrid);

        //--------------------------------------CREATING BUTTON----------------------------------
        Button btnCreate = new Button();
        btnCreate.setText("Create");
        btnCreate.setDefaultButton(true);
        btnCreate.setOnAction(event -> {
            System.out.println("CreatingAssignment");
            String catNameString = categoryNameTF.getText();

            int weightInt = (Integer) weightSpinner.getValue();;
            boolean properWeight = true;

            // todo Don't think we need this try-catch anymore because the spinner does error checking!
            // todo If the input isn't a number between 0 and 100, it uses the default 20.
            // todo used this to fix some bugs:
            // http://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user
//            try {
//                weightInt = (Integer) weightSpinner.getValue();
//            } catch (Exception e) {
//
//                properWeight = false;
//            }
            if (properWeight && !catNameString.isEmpty()) {
                if (subItemsToggle.getSelectedToggle() == btnSubItemsYes) {
                    System.out.println();
                    System.out.println(subItemsToggle.getSelectedToggle());
                    model.addCompoundAssignmentCategory(this.courseID, catNameString, weightInt);
                    System.out.println("COMPOUND LOOP");
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println(subItemsToggle.getSelectedToggle());
                    model.addAtomicAssignmentCategory(this.courseID, catNameString, weightInt);
                    System.out.println("ATOMIC LOOP");
                    System.out.println();
                }
            }

            Stage stage = AssignmentSetupWindow.stage;
            stage.hide();
        });
        //---------------------------------------------------------------------------------------

        root.setBottom(btnCreate);
        BorderPane.setAlignment(btnCreate, Pos.BOTTOM_RIGHT);

    }

    // Creates a spinner for the assignment weight
    @NotNull
    private Spinner generateSpinner() {
        Spinner weightSpinner = new Spinner();
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 20);
        valueFactory.increment(1);
        valueFactory.decrement(1);

        weightSpinner.setValueFactory(valueFactory);
        weightSpinner.setEditable(true);

        //---------------Below ensures that typed input is recorded with user needing to press enter----------------
        // The following three lines from:
        // http://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user
        TextFormatter formatter = new TextFormatter(valueFactory.getConverter(), valueFactory.getValue());
        weightSpinner.getEditor().setTextFormatter(formatter);
        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
        return weightSpinner;
    }

    private Button generateButton() {
        Button btnCreate = new Button();
        btnCreate.setText("Create");
        btnCreate.setDefaultButton(true);
        btnCreate.setOnAction(event -> {

            Stage stage = AssignmentSetupWindow.stage;
            stage.hide();
        });
        return btnCreate;
    }

    public BorderPane getRoot() {
        return root;
    }

    private GridPane generateGridPane() {

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

        //VBox relevantFieldsVBox = generateRelevantFieldsVBox();

        dataGrid.add(categoryNameLabel, 0, 0);
        dataGrid.add(categoryNameTF, 1, 0);
        dataGrid.add(weightLabel, 0, 1);
        dataGrid.add(weightTF, 1, 1);
        dataGrid.add(subItemsLabel, 0, 2);
        dataGrid.add(subItemHBox, 1, 2);
        //dataGrid.add(relevantFieldsVBox, 0, 3);

        return dataGrid;
    }

    private HBox generateSubItemHBox() {
        ToggleGroup subItemsToggle = new ToggleGroup();
        RadioButton btnSubItemsYes = new RadioButton("Yes");
        RadioButton btnSubItemsNo = new RadioButton("No");
        btnSubItemsYes.setToggleGroup(subItemsToggle);
        btnSubItemsNo.setToggleGroup(subItemsToggle);

        HBox subItemsHbox = new HBox();
        HBox.setMargin(btnSubItemsYes, new Insets(0, 10, 0, 0));
        subItemsHbox.getChildren().add(btnSubItemsYes);
        subItemsHbox.getChildren().add(btnSubItemsNo);

        return subItemsHbox;
    }

    private VBox generateRelevantFieldsVBox() {
        VBox relevantFieldsVBox = new VBox();
        Text relFieldsLabel = new Text("Relevant Fields");
        relevantFieldsVBox.getChildren().add(relFieldsLabel);
        VBox.setMargin(relFieldsLabel, new Insets(10, 0, 10, 0));

        CheckBox pointsPossibleCB = new CheckBox("Points Possible");
        CheckBox scorePointsCB = new CheckBox("Score (points)");
        CheckBox scorePercentCB = new CheckBox("Score (percent)");
        CheckBox weightCB = new CheckBox("Weight");
        CheckBox weightedScoreCB = new CheckBox("Weighted Score");

        List<CheckBox> cbList = Arrays.asList(
                pointsPossibleCB, scorePointsCB, scorePercentCB,
                weightCB, weightedScoreCB);
        for (CheckBox box : cbList) {
            relevantFieldsVBox.getChildren().add(box);
        }

        return relevantFieldsVBox;
    }

}
