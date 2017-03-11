package GradeTracker;

import GradeTracker.Samples.SampleCourse;
//import GradeTracker.Samples.SampleTerm;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

/**
 * Created by michelsd on 3/4/17.
 */
public class _howTableWorks extends Application {

    Stage window;
    TableView<SampleCourse> table;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("thenewboston - JavaFX");

        //Name column
        TableColumn<SampleCourse, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Price column
        TableColumn<SampleCourse, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Quantity column
        TableColumn<SampleCourse, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table = new TableView<>();


        //table.setItems(getTerm());

        table.getColumns().addAll(nameColumn, priceColumn, quantityColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    //Get all of the products
//    public ObservableList<SampleCourse> getTerm() {
//
//        SampleTerm myTerm = new SampleTerm("WI2017");
//        List<SampleCourse> myTermList = myTerm.getCourses();
//
//        ObservableList<SampleCourse> coursesList = FXCollections.observableArrayList(myTermList);
//
//        return coursesList;
//    }
}
