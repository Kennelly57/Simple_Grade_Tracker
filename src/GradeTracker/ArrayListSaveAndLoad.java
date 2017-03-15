package GradeTracker;

import GradeTracker.Samples.AtomicAssignment;
import GradeTracker.Samples.CompoundAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 3/12/2017.
 * @author Jack Kennelly
 */
public class ArrayListSaveAndLoad {
    private static String FILENAME = "courseList.txt";

    /**
     * Stores the data that the user inputs into the program as a txt file in the local directory.
     * @param data an array list of string that will be stored in courseList.txt
     */
    public static void storeCourseList(ArrayList<String> data) {
        try {
            FileOutputStream fileStream = new FileOutputStream(FILENAME, false);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Error Writing File:");
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the saved file from the local directory. The program assumes it will have the same name every time,
     * as saving is not handled by the user (beyond clicking a button).
     * @return The array list of string that the file contains
     * @throws FileNotFoundException
     */
    public static ArrayList<String> returnCourseList() throws FileNotFoundException{
        ArrayList<String> sum = new ArrayList<String>();
        boolean temp = true;
        try {
            FileInputStream fileStream = new FileInputStream(FILENAME);
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