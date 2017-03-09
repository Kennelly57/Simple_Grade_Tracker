package GradeTracker.Panes;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class GradePane {

    private VBox root;

    public GradePane() {
        root = generateGradePane();
    }

    public VBox getRoot(){
        return root;
    }

    private VBox generateGradePane() {
        VBox gradePane = new VBox();
        gradePane.setPadding(new Insets(15, 0, 5, 0));
        Text title = new Text("Configure the grade distribution:");
        GridPane gradeGrid = generateGradingCurvePane();
        gradePane.getChildren().add(title);
        gradePane.getChildren().add(gradeGrid);

        return gradePane;
    }

    private GridPane generateGradingCurvePane() {
        GridPane gradeGrid = new GridPane();
        gradeGrid.setPadding(new Insets(15, 25, 25, 25));
        gradeGrid.setHgap(10);
        gradeGrid.setVgap(10);

        Label gradeAPlus = new Label("A+");
        Label gradeA = new Label("A");
        Label gradeAMinus = new Label("A-");
        Label gradeBPlus = new Label("B+");
        Label gradeB = new Label("B");
        Label gradeBMinus = new Label("B-");
        Label gradeCPlus = new Label("C+");
        Label gradeC = new Label("C");
        Label gradeCMinus = new Label("C-");
        Label gradeDPlus = new Label("D+");
        Label gradeD = new Label("D");
        Label gradeDMinus = new Label("D-");

        List<Label> labelList = Arrays.asList(
                gradeAPlus, gradeA, gradeAMinus,
                gradeBPlus, gradeB, gradeBMinus,
                gradeCPlus, gradeC, gradeCMinus,
                gradeDPlus, gradeD, gradeDMinus);

        TextField tfAPlus = new TextField("96");
        TextField tfA = new TextField("93");
        TextField tfAMinus = new TextField("90");
        TextField tfBPlus = new TextField("86");
        TextField tfB = new TextField("83");
        TextField tfBMinus = new TextField("80");
        TextField tfCPlus = new TextField("76");
        TextField tfC = new TextField("73");
        TextField tfCMinus = new TextField("70");
        TextField tfDPlus = new TextField("66");
        TextField tfD = new TextField("63");
        TextField tfDMinus = new TextField("60");

        List<TextField> textFieldList = Arrays.asList(
                tfAPlus, tfA, tfAMinus,
                tfBPlus, tfB, tfBMinus,
                tfCPlus, tfC, tfCMinus,
                tfDPlus, tfD, tfDMinus);


        for (int i = 0; i < 12; i++) {
            gradeGrid.add(labelList.get(i), 0, i);
            gradeGrid.add(textFieldList.get(i), 1, i);
        }
        return gradeGrid;
    }

}
