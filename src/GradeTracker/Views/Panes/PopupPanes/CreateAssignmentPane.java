package GradeTracker.Views.Panes.PopupPanes;

import GradeTracker.ModelComponents.GTModel;
import GradeTracker.Views.PopupStages.AssignmentSetupWindow;
import com.sun.istack.internal.NotNull;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateAssignmentPane {

    private BorderPane root;
    private GTModel model;
    private String courseID;

    // Creates a pane prompting the user for an assignment name and weight,
    // and asks user to choose if the assignment has subitems.
    public CreateAssignmentPane(String setupType, GTModel theModel, String theCourseID) {
        this.model = theModel;
        this.courseID = theCourseID;

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
        Label subItemsLabel = new Label("Contains Sub-assignments:");

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
}
