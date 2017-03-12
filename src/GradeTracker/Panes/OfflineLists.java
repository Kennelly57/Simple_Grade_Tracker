package GradeTracker.Panes;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Jack on 3/12/2017.
 */


public class OfflineLists {

    public void storeCourseList(ArrayList<String> data){
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> returnCourseList(){
        ArrayList<String> data;
        try{
            FileInputStream fileStream = new FileInputStream("courseList.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);

            data = (ArrayList<String>) inputStream.readObject();

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
        ArrayList<String> testy = new ArrayList<String>();
        testy.add("OWLS");
        testy.add("CATS");
        test.storeCourseList(testy);
        testy = null;
        testy = test.returnCourseList();
        for(String item: testy){
            System.out.println(item);
        }
    }

}
