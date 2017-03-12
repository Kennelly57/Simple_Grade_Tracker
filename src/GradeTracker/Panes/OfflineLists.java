package GradeTracker.Panes;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Jack on 3/12/2017.
 */


public class OfflineLists {

    public void storeCourseList(ArrayList<ArrayList> data){
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList> returnCourseList(){
        ArrayList<ArrayList> data;
        try{
            FileInputStream fileStream = new FileInputStream("courseList.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);

            data = (ArrayList<ArrayList>) inputStream.readObject();

            inputStream.close();
            return data;
        } catch (IOException e){
            System.out.println("Error reading file");
        } catch (ClassNotFoundException e){
            System.out.println("Error reading object; file found.");
        }
        return null;
    }

    public static void main(String args[]){
        OfflineLists test = new OfflineLists();
        ArrayList<ArrayList> testSum = new ArrayList<ArrayList>();
        ArrayList<String> test1 = new ArrayList<String>();
        ArrayList<String> test2 = new ArrayList<String>();
        test1.add("OWLS");
        test1.add("CATS");
        test2.add("DOGS");
        test2.add("NEANDERTHALS");
        testSum.add(test1);
        testSum.add(test2);
        test.storeCourseList(testSum);
        testSum = null;
        testSum = test.returnCourseList();
        for(ArrayList<String> item: testSum){
            for (String item2 : item) {
                System.out.println(item2);
            }
        }
    }

}
