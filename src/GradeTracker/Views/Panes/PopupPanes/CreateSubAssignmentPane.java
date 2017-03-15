package GradeTracker.Views.Panes.PopupPanes;

import GradeTracker.ModelComponents.GTModel;
import GradeTracker.Views.PopupStages.AssignmentSetupWindow;
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

    // Creates a pane prompting the user for an assignment name
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

        TextField assignmentNameTF = new TextField();
        Label assignmentNameLabel = new Label("Assignment Name:");

        dataGrid.add(assignmentNameLabel, 0, 0);
        dataGrid.add(assignmentNameTF, 1, 0);

        //---------------------------------------------------------------------------------------

        root.setCenter(dataGrid);

        //--------------------------------------CREATING BUTTON----------------------------------
        Button btnCreate = new Button();
        btnCreate.setText("Create");
        btnCreate.setDefaultButton(true);
        btnCreate.setOnAction(event -> {
            System.out.println("CreatingAssignment");
            String assignmentNameString = assignmentNameTF.getText();

            model.addAtomicAssignmentToCompoundCategory(courseID, categoryName, assignmentNameString);

//            model.setAssignmentPointsPossible(courseID_2, "test 1", 160);

            Stage stage = AssignmentSetupWindow.stage;
            stage.hide();
        });
        //---------------------------------------------------------------------------------------

        root.setBottom(btnCreate);
        BorderPane.setAlignment(btnCreate, Pos.BOTTOM_RIGHT);

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
