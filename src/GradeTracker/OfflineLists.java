package GradeTracker;

import GradeTracker.Samples.AtomicAssignment;
import GradeTracker.Samples.CompoundAssignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jack on 3/12/2017.
 */


public class OfflineLists {

    public void storeCourseList(ArrayList<String> data) {
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt", true);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> returnCourseList() {
        ArrayList<String> sum = new ArrayList<String>();
        boolean temp = true;
        try {
            FileInputStream fileStream = new FileInputStream("courseList.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);
            try {
                sum = (ArrayList<String>) inputStream.readObject();
                } catch (IOException e) {
                System.out.println("Error reading file");
                } catch (ClassNotFoundException e) {
                System.out.println("Error reading file");
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

    /* Generates an array of all the information used to run the program.
    *  Will be stored as an object in a text file. */
    public ArrayList<String> dataGenerator(Map<String, ModelCourse> coursesMap){
        ArrayList<String> dataList = new ArrayList<>();
        int cursor = 1;
        for (ModelCourse course : coursesMap.values()){
            dataList.add("<course" + Integer.toString(cursor) + ">");
            dataList.add(course.getName());
            dataList.add(course.getID());
            dataList.add(intArrayConverter(course.getGradingScale()));
            int atomicCatCursor = 1;
            for (AtomicAssignment atomicCat : course.getAtomicAssignmentCategories().values()){
                dataList.add("<atomicCategory" + Integer.toString(atomicCatCursor) + ">");
                dataList.add(atomicCat.getName());
                dataList.add(Double.toString(atomicCat.getPointsPossible()));
                dataList.add(Double.toString(atomicCat.getPointsScore()));
                dataList.add(Boolean.toString(atomicCat.completed()));
                dataList.add(Integer.toString(course.getCategoryWeights().get(atomicCat.getName())));
                dataList.add("</atomicCategory" + Integer.toString(atomicCatCursor) + ">");
                atomicCatCursor++;
            }
            int compCatCursor = 1;
            for (CompoundAssignment compCat: course.getCompoundAssignmentCategories().values()) {
                dataList.add("<compoundCategory" + Integer.toString(compCatCursor) + ">");
                dataList.add(compCat.getName());
                dataList.add(Double.toString(compCat.getPointsPossible()));
                dataList.add(Double.toString(compCat.getPointsScore()));
                dataList.add(Boolean.toString(compCat.completed()));
                dataList.add(Integer.toString(course.getCategoryWeights().get(compCat.getName())));
                int subCatCursor = 1;
                for (AtomicAssignment subCat : compCat.getAtomicSubAssignmentMap().values()){
                    dataList.add("<subCategory" + Integer.toString(subCatCursor) + ">");
                    dataList.add(subCat.getName());
                    dataList.add(Double.toString(subCat.getPointsPossible()));
                    dataList.add(Double.toString(subCat.getPointsScore()));
                    dataList.add(Boolean.toString(subCat.completed()));
                    dataList.add(Integer.toString(course.getCategoryWeights().get(subCat.getName())));
                    dataList.add("</subCategory" + Integer.toString(subCatCursor) + ">");
                    subCatCursor++;
                }

                dataList.add("</compoundCategory" + Integer.toString(compCatCursor) + ">");
                compCatCursor ++;
            }

            dataList.add("</course" + Integer.toString(cursor) + ">");
            cursor++;
        }
        System.out.println(dataList);
        return dataList;
    }

    public static void main(String args[]) {
        OfflineLists test = new OfflineLists();
        GTModel upload = new GTModel();
        ArrayList<String> data = test.dataGenerator(upload.getLatestCourses());
        test.storeCourseList(data);
        System.out.print(test.returnCourseList());

    }
}