package GradeTracker;
import java.util.*;

/**
 * Created by michelsd on 3/4/17.
 */
public class SampleTerm {
    public String termName;
    public List<SampleCourse> myCourses = new ArrayList<SampleCourse>();

    public SampleTerm(String name) {
        this.termName = name;

        SampleCourse samp1 = new SampleCourse("BIOL.362", "Owl Patterns and Colors", "C");
        SampleCourse samp2 = new SampleCourse("CS.111", "Intro CS", "A-");
        SampleCourse samp3 = new SampleCourse("RELG.355", "Popes, People, and Pears", "A+");

        this.myCourses.add(samp1);
        this.myCourses.add(samp2);
        this.myCourses.add(samp3);
    }

    public String getName() {
        return this.termName;
    }

    public List<SampleCourse> getCourses() {
        return this.myCourses;
    }

    public static void main(String[] args) {
        SampleTerm sampT = new SampleTerm("WI2017");
        System.out.println(sampT.getName());
        System.out.println();

        List<SampleCourse> temp = sampT.getCourses();

        for (int i=0; i < temp.size(); i++) {
            System.out.println(temp.get(i).getId());
            System.out.println(temp.get(i).getName());
            System.out.println(temp.get(i).getGrade());
            System.out.println();
        }

    }
}




