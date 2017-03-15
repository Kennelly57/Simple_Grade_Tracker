package GradeTracker;

import GradeTracker.Samples.AtomicAssignment;
import GradeTracker.Samples.CompoundAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 3/12/2017.
 */


public class ArrayListSaveAndLoad {


    /* Stores the data that the user inputs into the program as a txt file in the local directory. */
    public static void storeCourseList(ArrayList<String> data) {
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt", false);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Error Writing File:");
            e.printStackTrace();
        }
    }


    /* Retrieves the saved file from the local directory. The program assumes it will have the same name every time,
    * as saving is not handled by the user (beyond clicking a button). */
    public static ArrayList<String> returnCourseList() throws FileNotFoundException{
        ArrayList<String> sum = new ArrayList<String>();
        boolean temp = true;
        try {
            FileInputStream fileStream = new FileInputStream("courseList.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);
            try {
                sum = (ArrayList<String>) inputStream.readObject();
                } catch (IOException e) {
                System.out.println("Error reading file: IOException");
                } catch (ClassNotFoundException e) {
                System.out.println("Error reading file: ClassNotFoundException");
                }

            inputStream.close();
            return sum;
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return null;
    }
}