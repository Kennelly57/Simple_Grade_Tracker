package GradeTracker.Panes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NameIDGridPane {

    private GridPane root;

    public NameIDGridPane() {
        root = generateGrid();
    }

    public GridPane getRoot() {
        return root;
    }

    private GridPane generateGrid() {

        GridPane inputGrid = new GridPane();
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

        return inputGrid;
    }


}