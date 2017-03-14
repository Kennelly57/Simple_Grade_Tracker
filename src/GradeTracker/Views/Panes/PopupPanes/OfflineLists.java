package GradeTracker.Views.Panes.PopupPanes;

import GradeTracker.ModelCourse;
import GradeTracker.Samples.SampleAtomicAssignment;
import GradeTracker.Samples.SampleCompoundAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

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

    private String intArrayConverter(int[] gradeArray){
        String gradeScale = "";
        String temp = "";
        for (int item : gradeArray){
            temp = Integer.toString(item);
            gradeScale = gradeScale + "," + temp;
        }
        return gradeScale;
    }

    private ArrayList<String> writeCompCat(ArrayList<String> dataList, SampleCompoundAssignment compCat, int cursor){
        //Map<String, Assignment> assignments = compCat.getSubAssignmentMap();


        return dataList;
    }

    public ArrayList<String> dataGenerator(Map<String, ModelCourse> coursesMap){
        ArrayList<String> dataList = new ArrayList<>();
        int cursor = 1;
        for (ModelCourse course : coursesMap.values()){
            dataList.add("<course" + Integer.toString(cursor) + ">");
            dataList.add(course.getName());
            dataList.add(course.getID());
            dataList.add(intArrayConverter(course.getGradingScale()));
            int atomicCatCursor = 1;
            for (SampleAtomicAssignment atomicCat : course.getAtomicAssignmentCategories().values()){
                dataList.add("<atomicCategory" + Integer.toString(atomicCatCursor) + ">");
                dataList.add(atomicCat.getName());
                dataList.add(Double.toString(atomicCat.getPointsPossible()));
                dataList.add(Double.toString(atomicCat.getPointsScore()));
                dataList.add(Boolean.toString(atomicCat.completed()));
                dataList.add(Integer.toString(course.getCategoryWeights().get(atomicCat.getName())));
                dataList.add("</atomicCategory" + Integer.toString(atomicCatCursor) + ">");
            }
            int compCatCursor = 1;
            for (SampleCompoundAssignment compCat: course.getCompoundAssignmentCategories().values()) {
                dataList = writeCompCat(dataList, compCat, compCatCursor);
                compCatCursor ++;
            }

            dataList.add("</course" + Integer.toString(cursor) + ">");
        }
        return dataList;
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

