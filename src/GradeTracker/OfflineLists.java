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


public class OfflineLists {

    public void storeCourseList(ArrayList<String> data) {
        try {
            FileOutputStream fileStream = new FileOutputStream("courseList.txt", false); // This will now overwrite the existing file.
            ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Error Writing File:");
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
            //dataList.add("<course" + Integer.toString(cursor) + ">");
            dataList.add("<course>");
            dataList.add(course.getName());
            dataList.add(course.getID());
            dataList.add(intArrayConverter(course.getGradingScale()));
            int atomicCatCursor = 1;
            for (AtomicAssignment atomicCat : course.getAtomicAssignmentCategories().values()){
                //dataList.add("<atomicCategory" + Integer.toString(atomicCatCursor) + ">");
                dataList.add("<atomicCategory>");
                dataList.add(atomicCat.getName());
                dataList.add(Double.toString(atomicCat.getPointsPossible()));
                dataList.add(Double.toString(atomicCat.getPointsScore()));
                dataList.add(Boolean.toString(atomicCat.completed()));
                dataList.add(Integer.toString(course.getCategoryWeights().get(atomicCat.getName())));
                //dataList.add("</atomicCategory" + Integer.toString(atomicCatCursor) + ">");
                dataList.add("</atomicCategory>");
                atomicCatCursor++;
            }
            int compCatCursor = 1;
            for (CompoundAssignment compCat: course.getCompoundAssignmentCategories().values()) {
                //dataList.add("<compoundCategory" + Integer.toString(compCatCursor) + ">");
                dataList.add("<compoundCategory>");
                dataList.add(compCat.getName());
                dataList.add(this.intArrayConverter(compCat.getGradingScale()));
                dataList.add(Boolean.toString(compCat.completed()));
                dataList.add(Integer.toString(course.getCategoryWeights().get(compCat.getName())));
                int subCatCursor = 1;
                for (AtomicAssignment subCat : compCat.getAtomicSubAssignmentMap().values()){
                    dataList.add("<subCategory" + Integer.toString(subCatCursor) + ">");
                    dataList.add("<subCategory>");
                    dataList.add(subCat.getName());
                    dataList.add(Double.toString(subCat.getPointsPossible()));
                    dataList.add(Double.toString(subCat.getPointsScore()));
                    dataList.add(Boolean.toString(subCat.completed()));
                    //dataList.add("</subCategory" + Integer.toString(subCatCursor) + ">");
                    dataList.add("</subCategory>");
                    subCatCursor++;
                }

                //dataList.add("</compoundCategory" + Integer.toString(compCatCursor) + ">");
                dataList.add("</compoundCategory>");
                compCatCursor ++;
            }

            //dataList.add("</course" + Integer.toString(cursor) + ">");
            dataList.add("</course>");
            cursor++;
        }
        System.out.println(dataList);
        System.out.flush();
        return dataList;
    }

    public void buildModel(GTModel model, List<String> dataList){

    }

    private int addCourse(GTModel model, List<String> dataList, int cursor){
        int i = cursor;

        return i;
    }

    private int addAtomiCat(GTModel model, List<String> dataList, String couseName, int cursor){
        int i = cursor;
        if( i >= 0 && i<dataList.size() &&
                dataList.get(i).equalsIgnoreCase("<atomicCategory>") &&
                dataList.get(i+6).equalsIgnoreCase("</atomicCategory>")){
            String atomiCatName = dataList.get(i+1);
            //String
        } else{
            i = -1;
        }
        return i;
    }

    private int addComCat(GTModel model, List<String> dataList, String couseName, int cursor){
        int i = cursor;

        return i;
    }

    private int addSubCat(GTModel model, List<String> dataList, String couseName, String comCatName, int cursor){
        int i = cursor;

        return i;
    }

    public static void main(String args[]) {
        OfflineLists test = new OfflineLists();
        GTModel upload = new GTModel();

        int[] gScale = {96, 93, 90, 87, 83, 80, 77, 73, 70, 67, 63, 60};

        String courseID_1 = "TEST 101";
        String courseID_2 = "BIOL.362";
        String courseID_3 = "CS.111";

        upload.addCourse(courseID_1, "Test Course", gScale);
        upload.addCourse(courseID_2, "Owls Patterns & Colors", gScale);
        upload.addCourse(courseID_3, "Intro CS", gScale);

        AtomicAssignment midtermExams = new AtomicAssignment("Midterm Exams");

        // demo stuff for test 101
        upload.addAtomicAssignmentCategory(courseID_1, "Midterm Exams", 30);
        upload.setAssignmentPointsPossible(courseID_1, "Midterm Exams", 300);
        upload.setAssignmentScore(courseID_1, "Midterm Exams", 210);

        upload.addAtomicAssignmentCategory(courseID_1, "Problem Sets", 20);
        upload.setAssignmentPointsPossible(courseID_1, "Problem Sets", 160);
        upload.setAssignmentScore(courseID_1, "Problem Sets", 125);

        upload.addAtomicAssignmentCategory(courseID_1, "Article Discussion", 20);
        upload.setAssignmentPointsPossible(courseID_1, "Article Discussion", 50);
        upload.setAssignmentScore(courseID_1, "Article Discussion", 50);

        upload.addAtomicAssignmentCategory(courseID_1, "Participation", 15);
        upload.setAssignmentPointsPossible(courseID_1, "Participation", 40);
        upload.setAssignmentScore(courseID_1, "Participation", 40);

        upload.addAtomicAssignmentCategory(courseID_1, "Final Exam", 15);
        upload.setAssignmentPointsPossible(courseID_1, "Final Exam", 40);
        upload.setAssignmentScore(courseID_1, "Final Exam", 40);

        // demo stuff for test biol
        upload.addAtomicAssignmentCategory(courseID_2, "Owl Stuff", 22);
        upload.setAssignmentPointsPossible(courseID_2, "Owl Stuff", 160);
        upload.setAssignmentScore(courseID_2, "Owl Stuff", 125);

        upload.addCompoundAssignmentCategory(courseID_2, "More Compound Stuff", 22);

        String atomInComCatName = "Pellet Analysis";
        upload.addAtomicAssignmentToCompoundCategory(courseID_2, "More Compound Stuff", atomInComCatName);
        upload.setAssignmentPointsPossible(courseID_2, atomInComCatName, 160);
        upload.setAssignmentScore(courseID_2, atomInComCatName, 125);

        // demo stuff for test cs
        upload.addAtomicAssignmentCategory(courseID_3, "CS Stuff", 22);
        upload.setAssignmentPointsPossible(courseID_3, "CS Stuff", 160);
        upload.setAssignmentScore(courseID_3, "CS Stuff", 125);

        upload.addAtomicAssignmentCategory(courseID_3, "More CS Stuff", 22);
        upload.setAssignmentPointsPossible(courseID_3, "More CS Stuff", 160);
        upload.setAssignmentScore(courseID_3, "More CS Stuff", 125);

        ArrayList<String> data = test.dataGenerator(upload.getLatestCourses());
        System.out.println(data);
        test.storeCourseList(data);
        System.out.println(test.returnCourseList());
        System.out.flush();

    }
}