package GradeTracker.Panes;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jack on 3/12/2017.
 */


public class OfflineLists {

    public void storeCourseList(ArrayList<ArrayList<String>> data) {
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt", true);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.writeChar('\n');
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> returnCourseList() {
        ArrayList<ArrayList<String>> sum = new ArrayList<ArrayList<String>>();
        boolean temp = true;
        try {
            FileInputStream fileStream = new FileInputStream("courseList.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);

            while (temp) {
                try {
                    //sum.add((ArrayList<String>) inputStream.readObject());
                    sum = (ArrayList<ArrayList<String>>) inputStream.readObject();
                } catch (IOException e) {
                    temp = false;
                    break;
                } catch (ClassNotFoundException e) {
                    temp = false;
                    break;
                }
            }

            inputStream.close();
            return sum;
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return null;
    }

    public static void main(String args[]) {
        ArrayList<ArrayList<String>> subsection = new ArrayList<ArrayList<String>>();
        OfflineLists test = new OfflineLists();
        ArrayList<ArrayList<String>> testSum = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> newTestSum = new ArrayList<ArrayList<String>>();
        ArrayList<String> test1 = new ArrayList<String>();
        ArrayList<String> test2 = new ArrayList<String>();
        test1.add("OWLS");
        test1.add("CATS");
        test2.add("DOGS");
        test2.add("NEANDERTHALS");
        testSum.add(test1);
        testSum.add(test2);
        test.storeCourseList(testSum);
        //testSum = null;
        newTestSum = test.returnCourseList();
        int outerLength = testSum.size();

    }
}

