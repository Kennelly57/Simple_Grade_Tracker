package GradeTracker.Overviews;

import GradeTracker.Setups.CourseSetupWindow;
import GradeTracker.Samples.SampleCourse;
import GradeTracker.Samples.SampleTerm;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CoursesOverviewDisplay extends Application {
    private Stage univPrimaryStage;
    @Override
    public void start(Stage primaryStage){

        univPrimaryStage = primaryStage;
        start(primaryStage, "WI2017");
    }

    public void start(Stage primaryStage, String termName) {

        SampleTerm myTerm = new SampleTerm("WI2017");
        List<SampleCourse> myTermList = myTerm.getCourses();

        BorderPane overviewBorderPane = new BorderPane();
        overviewBorderPane.setPadding(new Insets(15, 15, 15, 25));
        Text setupTitle = new Text("Your courses for ".concat(termName).concat(":"));
        overviewBorderPane.setTop(setupTitle);
        BorderPane.setAlignment(setupTitle, Pos.CENTER);

        GridPane dataPane = generateOverviewPane(myTermList);
        overviewBorderPane.setCenter(dataPane);
        dataPane.setId("dataPane");
        int columnCounter = 0;
        for (Node n: dataPane.getChildren()) {
            if (n instanceof Control) {
                Control control = (Control) n;
                control.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                control.setId("gridNodes");
                if (columnCounter < 3){
                    control.setId("categories");
                    columnCounter++;
                }
            }
            if (n instanceof Pane) {
                Pane pane = (Pane) n;
                pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                pane.setId("gridNodes");
            }
        }

        ColumnConstraints oneThird = new ColumnConstraints();
        oneThird.setPercentWidth(100/3.0);
        oneThird.setHalignment(HPos.CENTER);
        dataPane.getColumnConstraints().addAll(oneThird, oneThird, oneThird);
        RowConstraints oneHalf = new RowConstraints();
        oneHalf.setPercentHeight(100/2.0);
        oneHalf.setValignment(VPos.CENTER);
        dataPane.getRowConstraints().addAll(oneHalf, oneHalf, oneHalf, oneHalf);

        //------------------------------CREATE_ADD_BUTTON-----------------------------------
        Button btnFinish = new Button();
        btnFinish.setText("+");
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(univPrimaryStage);
                new CourseSetupWindow().start(dialog);
            }
        });

        overviewBorderPane.setBottom(btnFinish);
        BorderPane.setAlignment(btnFinish, Pos.BOTTOM_RIGHT);
        //---------------------------------------------------------------------------------------

        Scene overviewScene = new Scene(overviewBorderPane, 1000, 500);

        String css = this.getClass().getResource("basicStyle.css").toExternalForm();
        dataPane.getStylesheets().add(css);

        primaryStage.setScene(overviewScene);
        primaryStage.show();

    }


    public GridPane generateOverviewPane(List<SampleCourse> myTermList){
        GridPane dataGrid = new GridPane();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);
        dataGrid.setPadding(new Insets(15, 0, 0, 0));
        dataGrid.setGridLinesVisible(true);

        Label courseIdLabel = new Label("Course Id");
        Label courseNameLabel = new Label("Name");
        Label courseGradeLabel = new Label("Grade");

        dataGrid.add(courseIdLabel, 0, 0);
        dataGrid.add(courseNameLabel, 1, 0);
        dataGrid.add(courseGradeLabel, 2, 0);

        // Generate Table of Course Values
        for (int i=0; i<myTermList.size(); i++) {
            final String idStr = myTermList.get(i).getId();
            Label tempID = new Label(idStr);

            tempID.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    AssignmentsOverviewDisplay newDisplay = new AssignmentsOverviewDisplay();
                    newDisplay.start(univPrimaryStage);
                }
            });

            dataGrid.add(tempID, 0, i+1);

            Label tempName = new Label(myTermList.get(i).getName());
            dataGrid.add(tempName, 1, i+1);

            Label tempGrade = new Label(myTermList.get(i).getGrade());
            dataGrid.add(tempGrade, 2, i+1);
        }

        return dataGrid;
    }

    public static void main(String[] args) {launch(args);}
}