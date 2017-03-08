package GradeTracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kilian on 3/7/2017.
 */
public class GTModel {
    private Map<String, ModelCourse> courseMap;

    public GTModel(){
        this.courseMap = new TreeMap<String, ModelCourse>();
    }

    public void addCourse(String courseID, String courseName, int[] gScale){
        ModelCourse courseToAdd = new ModelCourse(courseID, courseName, gScale);
    }

    public void addAssignmentCategory(String courseID, String categoryName, Integer weight){

    }

}
