package GradeTracker;

/**
 * Created by Jack on 3/9/2017.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class handleCSV {

    private LinkedList<String> tempData = new LinkedList<String>();

    public void generateCSVFile() throws FileNotFoundException{
        PrintWriter printWriter = new PrintWriter(new File("myCourses.csv"));
        StringBuilder stringBuilder = new StringBuilder();

        //Adds basic index to the CSV file
        for (int i = 0; i <= 14; i++) {
            String temp = Integer.toString(i);
            stringBuilder.append(temp);
            stringBuilder.append(',');
        }

        stringBuilder.append('\n');

        //Formats the columns which will hold data on the grade distribution
        stringBuilder.append("Course ID");
        stringBuilder.append(',');
        stringBuilder.append("Name");
        stringBuilder.append(",");
        stringBuilder.append("Grade");
        stringBuilder.append(",");

        //Formats the Columns which will hold data on the grade distribution
        stringBuilder.append("A+");
        stringBuilder.append(',');
        stringBuilder.append("A");
        stringBuilder.append(",");
        stringBuilder.append("A-");
        stringBuilder.append(",");
        stringBuilder.append("B+");
        stringBuilder.append(',');
        stringBuilder.append("B");
        stringBuilder.append(",");
        stringBuilder.append("B-");
        stringBuilder.append(",");
        stringBuilder.append("C+");
        stringBuilder.append(',');
        stringBuilder.append("C");
        stringBuilder.append(",");
        stringBuilder.append("C-");
        stringBuilder.append(",");
        stringBuilder.append("D+");
        stringBuilder.append(',');
        stringBuilder.append("D");
        stringBuilder.append(",");
        stringBuilder.append("D-");





        printWriter.write(stringBuilder.toString());
        printWriter.close();
    }

    public void dataCollector(String input){
        tempData.add(input);
    }

    public void addCourse() throws FileNotFoundException{
        File csv = new File("myCourses.csv");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(csv, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        for (String item : tempData){
            stringBuilder.append(item);
            stringBuilder.append(",");
        }

        try {
            fileWriter.append(stringBuilder.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        handleCSV test = new handleCSV();
        try {
            test.generateCSVFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
